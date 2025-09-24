import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        mapToJson();
        jsonToMap();
        mapToBean();
        beanToMap();
    }

    public static void mapToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        map.put("height", 1.88);
        System.out.println("mapToJson map = " + map);

        // map 转 json 字符串
        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println("mapToJson json = " + json);
    }

    public static void jsonToMap() {
        String json = "{\"name\":\"张三\",\"age\":18,\"height\":1.88}";
        System.out.println("jsonToMap json = " + json);

        // json 字符串转 map
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("jsonToMap map = " + map);
    }

    public static void mapToBean() {
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("city", "杭州");
        addressMap.put("district", "西湖区");

        Map<String, Object> personMap = new HashMap<>();
        personMap.put("name", "张三");
        personMap.put("age", 18);
        personMap.put("height", 1.88);
        personMap.put("addressBean", addressMap);

        System.out.println("mapToBean personMap = " + personMap);

        // map 转 bean
        Gson gson = new Gson();
        // 得先把 map 转成 json 字符串
        String json = gson.toJson(personMap);
        // 然后再把 json 字符串转成 bean
        PersonBean personBean = gson.fromJson(json, PersonBean.class);
        System.out.println("mapToBean personBean = " + personBean);
    }

    public static void beanToMap() {
        AddressBean addressBean = new AddressBean();
        addressBean.setCity("杭州");
        addressBean.setDistrict("西湖区");

        PersonBean personBean = new PersonBean();
        personBean.setName("张三");
        personBean.setAge(18);
        personBean.setHeight(1.88);
        personBean.setAddressBean(addressBean);

        System.out.println("beanToMap personBean = " + personBean);

        // bean 转 map
        Gson gson = new Gson();
        // 得先把 bean 转成 json 字符串
        String json = gson.toJson(personBean);
        // 然后再把 json 字符串转成 map
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("beanToMap map = " + map);
    }

}