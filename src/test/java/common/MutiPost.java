package common;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Hujf
 * @title: MutiPost
 * @date 2021-04-26 10:49
 * @description: TODO
 */
public class MutiPost {
    private static CloseableHttpClient httpClient;
    private static final String url = "http://127.0.0.1:4242/api/put?async";

    private static int ThreadNum = 40;  //线程数模拟主机数、或者机器数
    private static int CirculNum = 250; //单线程循环次数
    private static int pointNun = 100; //单次post请求点数
    private static CountDownLatch count = new CountDownLatch(ThreadNum);
    private static ThreadPoolExecutor executors = new ThreadPoolExecutor(20,40,1000L, TimeUnit.SECONDS,new LinkedBlockingQueue<>());


    public static void main(String[] args) {
        try {
            httpClient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            for (int i = 0; i < ThreadNum; i++) {
                int finalI = i;
                executors.execute(()->{
                    for (int j = 0; j < CirculNum; j++) {
                        String string = getJson(finalI);
                        sendPost(string);
                        System.out.println("Thread-" + finalI + "-" + j + " is finished");
                    }
                    count.countDown();
                });
            }
            count.await();
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                executors.shutdown();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendPost(String string) {
        final HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(string, "utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJson(int j) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < pointNun; i++) {
            long Starttime = System.currentTimeMillis();
            Map<String, Object> map1 = new HashMap<>();
            Map<String, String> tags = new HashMap<>();

            tags.put("host", "00" + j);
            tags.put("gateMac", "00" + i);
            map1.put("metric", "test");
            map1.put("value", Math.random() * 100);
            map1.put("tags", tags);
            //   System.out.println(i + "条数据");
            map1.put("timestamp", Starttime);
            list.add(map1);
        }
        String string = JSON.toJSONString(list);
        return string;
    }
}
