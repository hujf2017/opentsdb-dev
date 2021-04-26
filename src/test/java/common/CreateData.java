package common;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hujf
 * @title: CreateData
 * @date 2021-04-26 10:27
 * @description: TODO
 */
public class CreateData {
    private static final String PATH = "E:\\java\\workspace\\myself\\opentsdb-dev\\data";
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for(int i=0;i<20;i++) {
            Thread t = new Thread("主机"+i);
            String file = PATH + "\\a"+i+".txt";
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                String s = getJson(i);
                out.write(s.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end -start);
    }

    public static String getJson(int j) {

        List<Object> list = new ArrayList<>();
            for (int i = 0; i <= 100 ; i++) {
                long Starttime = System.currentTimeMillis();
                Map<String, Object> map1 = new HashMap<>();
                Map<String, String> tags = new HashMap<>();
                tags.put("host", "00"+j );
                tags.put("gateMac", "00"+i);
                map1.put("metric", "test");
                map1.put("value", i+"-9527");
                map1.put("tags", tags);
                //   System.out.println(i + "条数据");
                map1.put("timestamp", (Starttime / 1000 + i) * 1000);
                list.add(map1);
            }

        String string = JSON.toJSONString(list);
        return string;
    }
}
