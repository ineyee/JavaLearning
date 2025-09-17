import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        map.put("height", 1.88);

        // map 转 json 字符串
        Gson gson = new Gson();
        String json = gson.toJson(map);

        System.out.println(json);
    }
}
