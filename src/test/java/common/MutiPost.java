package common;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author Hujf
 * @title: MutiPost
 * @date 2021-04-26 10:49
 * @description: TODO
 */
public class MutiPost {
    private static CloseableHttpClient httpClient;
    private static final String url="http://192.168.3.102:4242/api/put?async";
    private static CountDownLatch count = new CountDownLatch(20);
    public static void main(String[] args) throws InterruptedException {
        httpClient = HttpClients.createDefault();
        long start = System.currentTimeMillis();
        for(int i=0;i<20;i++) {
            int finalI = i;
             new Thread(()->{
                 for(int j=0;j<1000;j++) {
                     String string = getJson(finalI);
                     sendPost(string);
                     System.out.println("Thread-"+finalI+"-"+j+" is finished" );
                 }
                 count.countDown();
            }).start();
        }
        count.await();
        long end = System.currentTimeMillis();
        System.out.println(end -start);
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
        for (int i = 0; i <50  ; i++) {
            long Starttime = System.currentTimeMillis();
            Map<String, Object> map1 = new HashMap<>();
            Map<String, String> tags = new HashMap<>();

            tags.put("host", "00"+j );
            tags.put("gateMac", "00"+i);
            map1.put("metric", "test");
            map1.put("value", Math.random()*100);
            map1.put("tags", tags);
            //   System.out.println(i + "条数据");
            map1.put("timestamp", Starttime);
            list.add(map1);
        }
        String string = JSON.toJSONString(list);
        return string;
    }
}
