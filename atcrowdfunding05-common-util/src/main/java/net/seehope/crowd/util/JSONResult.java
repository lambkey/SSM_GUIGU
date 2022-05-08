package net.seehope.crowd.util;

/**
 * @author JoinYang
 * @date 2022/3/13 21:32
 */

public class JSONResult <E>{

    public static final String SUCCESS="SUCCESS";
    public static final String FAILURE="FAILURE";

    //用来封装当前请求的结果是成功还是失败
    private String result;

    //请求处理失败返回的错误信息
    private String message;

    //要返回的数据
    private Object data;

    //请求处理成功且不需要返回信息和数据时
    public static JSONResult successWithoutData(){
        return new JSONResult(SUCCESS,null,null);
    }

    //请求处理成功且不需要返回信息和数据时
    public static JSONResult successNeedData(Object data){
        return new JSONResult(SUCCESS,null,data);
    }

    //请求处理失败时返回错误信息
    public static JSONResult FailureNeedMessage(String message){
        return new JSONResult(FAILURE,message,null);
    }


    public JSONResult() {
    }

    public JSONResult(String result, String message, Object data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
