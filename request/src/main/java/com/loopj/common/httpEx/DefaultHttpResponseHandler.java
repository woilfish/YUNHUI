package com.loopj.common.httpEx;

import com.loopj.common.exception.ServerResultDataException;
import com.loopj.common.exception.TradeException;
import com.loopj.common.util.StringUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Michael on 14-10-15.
 */
public class DefaultHttpResponseHandler extends HttpResponseHandler {
    /** 应答报文必须是JSON字符串格式，这是缺省格式*/
    public static final int RESPONSE_DATA_TYPE_JSON = RESPONSE_DATA_TYPE_DEFAULT;
    /** 应答报文必须是文本*/
    public static final int RESPONSE_DATA_TYPE_TEXT = 1;
    /** 应答报文必须是二进制数据*/
    public static final int RESPONSE_DATA_TYPE_DATA = 2;
    /** 应答数据为文件*/
    public static final int RESPONSE_DATA_TYPE_FILE = 3;

    //当应答数据为文件时候，则将文件对象返回
    private File file;

    /**
     * 获取文件
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * 设置保存数据的文件
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
        setHeaders(headers);
        this.statusCode = statusCode;
        this.responseBody = responseBody;

        setCookie();

        //处理响应报文
        handleResponseBody(statusCode,responseBody);
    }

    @Override
    public String getAccept() {
        switch(getResponseDataType()){
            case RESPONSE_DATA_TYPE_JSON:
                return "application/json";

            case RESPONSE_DATA_TYPE_TEXT:
                return "text/html";

            case RESPONSE_DATA_TYPE_DATA:
            case RESPONSE_DATA_TYPE_FILE:
            default:
                return "image/png,image/*";
        }
    }

    /**
     * 处理响应报文
     * @param statusCode   http 状态码
     * @param responseBody 响应报文
     */
    protected void handleResponseBody(int statusCode,byte[] responseBody){

        switch (responseDataType){
            case RESPONSE_DATA_TYPE_JSON:
                JSONObject responseData = null;
                String JsonErrorMsg = "";
                try {
                    //将响应报文件转换成 JSON
                    responseData = new JSONObject(getResponseString(responseBody, getCharset()));
                } catch (JSONException e) {
                    JsonErrorMsg = e.getMessage();
                }

                //解析 JSON 失败，发送失败事件给 HttpRequest
                if (responseData == null){
                    if (request != null){
                        //尝试将 respons 报文转换成字符串
                        String responseText = getResponseString(responseBody, getCharset());
                        ServerResultDataException exception = new ServerResultDataException(responseText,JsonErrorMsg);
                        request.onFailure(exception);
                    }

                    return;
                }

                this.resultCode = responseData.optString("code","");
                this.resultMessage = responseData.optString("message","");

                this.resultData = responseData;
                if (isSucceed()){
                    //交易成功
                    if (request != null){
                        request.onSuccess();
                    }
                }
                else{
                    //交易失败
                    if (request != null){
                        //生成交易失败异常对象
                        TradeException exception = new TradeException(resultCode,resultMessage);
                        request.onFailure(exception);
                    }
                }

                break;

            case RESPONSE_DATA_TYPE_TEXT:
                this.resultData = getResponseString(responseBody, getCharset());

                if (request != null){
                    request.onSuccess();
                }

                break;

            case RESPONSE_DATA_TYPE_DATA:
                this.resultData = responseBody;

                if (request != null){
                    request.onSuccess();
                }

                break;

            case RESPONSE_DATA_TYPE_FILE:
                this.resultData = responseBody;

                if (request != null){
                    request.onSuccess();
                }

                break;
        }
    }

    @Override
    public byte[] getResponseData(HttpEntity entity) throws IOException {
        byte[] data = super.getResponseData(entity);

        if(responseDataType == RESPONSE_DATA_TYPE_FILE){
            FileOutputStream buffer = new FileOutputStream(getFile());
            buffer.write(data);
            buffer.flush();
            buffer.close();
        }
        return data;
    }

//    /**
//     * 设置相应文件数据
//     *
//     * @param entity
//     * @throws java.io.IOException
//     */
//    protected void setResponseFileData(HttpEntity entity) throws IOException {
//
//        if (entity != null) {
//            InputStream instream = entity.getContent();
//            long contentLength = entity.getContentLength();
//
//            if(getFile() == null){
//                return;
//            }
//
//            FileOutputStream buffer = new FileOutputStream(getFile());
//            if (instream != null) {
//                try {
//                    byte[] tmp = new byte[BUFFER_SIZE];
//                    int l, count = 0;
//                    while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
//                        count += l;
//                        buffer.write(tmp, 0, l);
//                        sendProgressMessage(count, (int) contentLength);
//                    }
//                } finally {
//                    AsyncHttpClient.silentCloseInputStream(instream);
//                    buffer.flush();
//                    AsyncHttpClient.silentCloseOutputStream(buffer);
//                }
//            }
//        }
//    }

    /**
     * 判断交易是否成功
     * @return 成功返回 true
     */
    protected boolean isSucceed(){
        if (statusCode < 200 || statusCode >= 300){
            return false;
        }

        switch (responseDataType){
            case RESPONSE_DATA_TYPE_JSON:
                return StringUtil.isNotEmpty(resultCode) && "0000".equals(resultCode);

            case RESPONSE_DATA_TYPE_TEXT:
                return true;

            case RESPONSE_DATA_TYPE_DATA:
                return true;
        }

        return false;
    }

}
