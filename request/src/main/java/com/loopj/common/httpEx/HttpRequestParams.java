package com.loopj.common.httpEx;

import com.loopj.common.http.RequestParams;
import com.loopj.common.http.ResponseHandlerInterface;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Michael on 14-10-14.
 */
public class HttpRequestParams extends RequestParams {
    public static enum PostDataEncodingType{
        PostDataEncodingTypeURL,
        PostDataEncodingTypeJSON // default
    }
    public final static String APPLICATION_IMAGE = "image/jpeg";

    //POST 数据缺省使用 JSON 编码
    protected PostDataEncodingType postDateEncodingType = PostDataEncodingType.PostDataEncodingTypeJSON;

    public HttpRequestParams() {
    }

    public HttpRequestParams(Map<String, String> source) {
        super(source);
    }

    public HttpRequestParams(String key, String value) {
        super(key, value);
    }

    public HttpRequestParams(Object... keysAndValues){
        super(keysAndValues);
    }

    @Override
    public HttpEntity getEntity(ResponseHandlerInterface progressHandler) throws IOException {
        if(streamParams.isEmpty() && fileParams.isEmpty() &&
                postDateEncodingType == PostDataEncodingType.PostDataEncodingTypeJSON){
            return new JsonStringEntity(urlParams,urlParamsWithObjects, HTTP.UTF_8);
        }
        else {
            return super.getEntity(progressHandler);
        }
    }

    /**
     * 根据key获取请求参数中的value
     *
     * @param key 请求key
     * @return
     */
    public Object get(String key){
        if(urlParams.containsKey(key)){
            return urlParams.get(key);
        }else if(streamParams.containsKey(key)){
            return streamParams.get(key);
        }else if(fileParams.containsKey(key)){
            return fileParams.get(key);
        }else if(urlParamsWithObjects.containsKey(key)){
            return urlParamsWithObjects.get(key);
        }

        return null;
    }

    /**
     * 压入 JSON 对象内所有数据
     * @param json json 对象
     */
    public void put(JSONObject json){
        if (json == null || json.length() == 0){
            return;
        }

        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            Object value = json.opt(key);

            if (value == null)
                continue;

            if (value instanceof Integer){
                put(key, (int) value);
            }
            else if (value instanceof Short){
                put(key, (short) value);
            }
            else if (value instanceof Long){
                put(key, (long) value);
            }
            else if (value instanceof Float){
                put(key, value);
            }
            else if (value instanceof Double){
                put(key, value);
            }
            else if (value instanceof Boolean){
                put(key, value);
            }
            else if (value instanceof String){
                put(key, (String) value);
            }
            else if (value instanceof JSONObject){
                put(key, value);
            }
            else if (value instanceof JSONArray){
                put(key, value);
            }
        }
    }

    public PostDataEncodingType getPostDateEncodingType() {
        return postDateEncodingType;
    }

    public void setPostDateEncodingType(PostDataEncodingType postDateEncodingType) {
        this.postDateEncodingType = postDateEncodingType;
    }
}
