package common;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;

/**
 * @author Hujf
 * @title: sendpPost
 * @date 2021/4/17 0017下午 4:21
 * @description: TODO
 */
public class SendpPost {
    private static CloseableHttpClient httpClient;
    public static void  main(String[] args) throws Exception {
        httpClient = HttpClients.createDefault();
        List<Object> list = new ArrayList<>();
        long sum =0;
        for(int j=0;j<=1000;j++) {
            for (int i = 100*j; i <= 100*j+100; i++) {
                long Starttime = System.currentTimeMillis();
                Map<String, Object> map1 = new HashMap<>();
                Map<String, String> tags = new HashMap<>();
                    tags.put("operationValue", "HDFF88FFF"+j);
                tags.put("gateMac", "00E2690CDFFD"+j);
                map1.put("metric", "hello3");
                map1.put("value", i);
                map1.put("tags", tags);
             //   System.out.println(i + "条数据");
                map1.put("timestamp", (Starttime/1000+i)*1000);
                list.add(map1);
            }
            long start = System.currentTimeMillis();
            sendPost("http://192.168.3.102:4242/api/put?async", list);
            long end = System.currentTimeMillis();
            sum+=end - start;
            list.clear();
        }
        System.out.println(sum);
        httpClient.close();
    }

    private static Object deal(int i) {
        String result = "";        // 保留code的位数
        result = String.format("%05d", i);
        return result;
    }


    public static void sendPost(final String url, List<Object> list) throws IOException {
        final HttpPost httpPost = new HttpPost(url);
        if (! list.isEmpty()) {
            String string = JSON.toJSONString(list);
            System.out.println(string);
            StringEntity entity = new StringEntity(string, "utf-8");
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
             httpClient.execute(httpPost);
        }
    }
}
