package cn.tedu.charging.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 通过Jackson实现的json和对象转换工具
 */
public class JsonUtils {

    //定义对象转换json工具ObjectMapper jackson
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象转json
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * json转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T covertValue(Object object, Class<T> clazz) {
        return mapper.convertValue(object,clazz);
    }
}
