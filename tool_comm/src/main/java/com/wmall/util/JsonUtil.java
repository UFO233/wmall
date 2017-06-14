package com.wmall.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * json相关的工具类
 *
 * @author lichun
 *         注意:ObjectMapper 是一个线程安全的类;
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 说明: twf 2015-05-07 在为空或“” 情况下不序列化对象
        //OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将一个简单对象转化成json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个json转化成一个对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个json转化成一个list
     *
     * @param jsonString
     * @param collectionClass
     * @param elementClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
        try {
            return (T) OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一个json转化成一个map
     *
     * @param jsonString
     * @param mapClass
     * @param keyClass
     * @param valueClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, @SuppressWarnings("rawtypes") Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
        try {
            return (T) OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("11", "aaaaaaaaaaaaaaaaaaaa");
        map.put("12", "aaaaaaaaaaaaaaaaaaaa");
        map.put("13", "aaaaaaaaaaaaaaaaaaaa");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("11", "aaaaaaaaaaaaaaaaaaaa");
        map2.put("12", "aaaaaaaaaaaaaaaaaaaa");
        map2.put("13", "aaaaaaaaaaaaaaaaaaaa");
        List<Map> list = new ArrayList<Map>();
        list.add(map);
        list.add(map2);
        String jsonString = toJson(map);
        String jsonString2 = toJson(list);
        System.out.println(jsonString);
        System.out.println(jsonString2);
        System.out.println(fromJson(jsonString, HashMap.class));
        System.out.println(fromJson(jsonString, HashMap.class, String.class, Object.class));
        System.out.println(fromJson(jsonString2, ArrayList.class, HashMap.class));
    }

}
