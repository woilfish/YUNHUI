package com.loopj.common.httpEx;

import com.loopj.common.http.AsyncHttpClient;
import com.loopj.common.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 14-10-14.
 */
public class JsonStringEntity extends StringEntity {

    //JSON CONTENT TYPE
    private static final Header HEADER_JSON_CONTENT =
            new BasicHeader(
                    AsyncHttpClient.HEADER_CONTENT_TYPE,
                    RequestParams.APPLICATION_JSON);

    public JsonStringEntity(String s, String charset) throws UnsupportedEncodingException {
        super(s, charset);
    }

    public JsonStringEntity(String s) throws UnsupportedEncodingException {
        super(s);
    }

    public JsonStringEntity(ConcurrentHashMap<String, String> urlParams,
                            ConcurrentHashMap<String, Object> objParams,
                            String charset) throws UnsupportedEncodingException {
        this(JsonStringEntity.getJsonStringWithParams(urlParams,objParams), charset);
    }

    @Override
    public Header getContentType() {
        return HEADER_JSON_CONTENT;
    }

    @Override
    public Header getContentEncoding() {
        return super.getContentEncoding();
    }

    /**
     * 获取当前参数->JSON String
     *
     * @return
     */
    private static String getJsonStringWithParams(ConcurrentHashMap<String, String> urlParams,
                                                  ConcurrentHashMap<String, Object> objParams){
        JSONObject json = new JSONObject();

        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {}
        }

        for (ConcurrentHashMap.Entry<String, Object> entry : objParams.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {}
        }

        return json.toString();
    }
}
