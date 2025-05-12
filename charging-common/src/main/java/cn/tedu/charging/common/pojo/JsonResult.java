package cn.tedu.charging.common.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JsonResult<T> {

    /**
     * 状态码
     */
    Integer code;

    /**
     * 提示消息
     */
    String message;

    /**
     * 具体的数据 接口的出参
     */
    T data;


    /**
     * 正常返回 入参 有数据和消息
     * @param data
     * @param msg
     * @return
     */
    public static JsonResult ok(Object data,String msg) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(data);
        jsonResult.setCode(2000);
        jsonResult.setMessage(msg);
        return jsonResult;
    }

    /**
     * 正常返回 入参只有数据
     * @param data
     * @return
     */
    public static JsonResult ok(Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(data);
        jsonResult.setCode(2000);
        return jsonResult;
    }
}
