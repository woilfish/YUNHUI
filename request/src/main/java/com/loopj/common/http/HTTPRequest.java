package com.loopj.common.http;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *	恒易数据请求框架封装
 */
public class HTTPRequest {
    private static Map<String, String> param = null;
    private static Map<String, CacheBody> cache = null;
    private static final int TIME_OUT = 1000 * 5;
    private static int maxCacheCount = 100;//只能缓存100个。
    private static final int HTTP_OK = 200;
    private static HTTPRequest instance = null;
    public static final String POST = "POST";
    public static final String GET = "GET";
    private AsyncRequestListener async = null;
    private String server = null;//可以直接设置请求服务器
    private String url = null;
    private String method = GET;
    private int expire_time = 10;//秒
    private boolean useCache = true;
    private int totalLength = 0;

    //线程安全
    public static synchronized HTTPRequest getInstance() {
        if (instance == null) {
            cache = new HashMap<String, CacheBody>();
            instance = new HTTPRequest();
        }
        return instance;
    }

    public void SendRequest(AsyncRequestListener async) {
        this.async = async;
        AsyncDown as1 = new AsyncDown();
        String http = server + url;
        //如果存在缓存
        if (useCache && cache.containsKey(http)) {
            CacheBody body = cache.get(http);
            long cha = System.currentTimeMillis() - body.getStampTime();
            if (body.getExpireTime() * 1000 > cha) {
                as1.onPostExecute(body.getContent());
            } else {
                cache.remove(http);
                as1.execute(http);
            }

        } else {
            as1.execute(http);
        }

    }

    public Map<String, String> getParamMap() {
        if (param == null) {
            param = new HashMap<String, String>();
        } else {
            param.clear();
        }
        return param;
    }

    public void setParamMap(Map<String, String> p) {
        param = p;
    }

    public void clearCache() {
        cache.clear();
    }

    public void removeCache(String http) {
        cache.remove(http);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setExpireTime(int expire_time) {
        this.expire_time = expire_time;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     *
     */
    public String httpGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        String response = "";
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(TIME_OUT);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestMethod(GET);
            conn.setRequestProperty("accept", "*/*");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HTTP_OK) {
                totalLength = conn.getContentLength();
                inStream = conn.getInputStream();
                response = getResponse(inStream);
                inStream.close();
            } else {
                response = null;
            }
        } catch (Exception e) {
            response = null;
        } finally {
            conn.disconnect();
        }
        return response;
    }

    public String httpPost(String urlStr, String params) {
        byte[] data = params.getBytes();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        String response = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(POST);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HTTP_OK) {
                totalLength = conn.getContentLength();
                inStream = conn.getInputStream();
                response = getResponse(inStream);
                inStream.close();
            } else {
                response = null;
            }
        } catch (Exception e) {
            response = null;
        } finally {
            conn.disconnect();
        }
        return response;
    }

    private String getResponse(InputStream inStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readLength = 0;
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
            readLength += len;
            int progress = (int) (((float) readLength / totalLength) * 100);
            async.UpdateProgress(progress);
        }

        byte[] data = outputStream.toByteArray();
        outputStream.close();
        return new String(data).trim();
    }

    class AsyncDown extends AsyncTask<String, Float, String> {
        @Override
        protected String doInBackground(String... arg0) {
            String res = null;
            String path = arg0[0];
            String formatMap = formatUrlMap(param, false, false);
            String mParam = "sign=" + MD5(formatMap) + "&" + formatMap;
            try {
                if (method.equals(POST)) {
                    res = httpPost(path, mParam);
                } else {
                    res = httpGet(path + "?" + mParam);
                }
            } catch (Exception e) {
                async.LoadException(e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            async.PreRequest();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result == null) {
                    async.LoadException("请求发起失败，服务器未响应。");
                    return;
                }
                JSONObject object = new JSONObject(result);
                String http = server + url;
                if (useCache) {
                    if (cache.size() < maxCacheCount) {
                        if (!cache.containsKey(http)) {
                            CacheBody c = new CacheBody();
                            c.setContent(result);
                            c.setExpireTime(expire_time);
                            c.setStampTime(System.currentTimeMillis());
                            cache.put(http, c);
                        }
                    }
                }
                async.LoadObjectRequest(object);
            } catch (JSONException e) {
                async.ParseException(e.getMessage());
            }
        }

    }

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        if (paraMap == null) {
            return "";
        }
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                String key = item.getKey();
                String val = item.getValue();
                if (urlEncode) {
                    val = URLEncoder.encode(val, "utf-8");
                }
                if (keyToLower) {
                    buf.append(key.toLowerCase() + "=" + val);
                } else {
                    buf.append(key + "=" + val);
                }
                buf.append("&");
            }
            buff = buf.toString();
            if (buff.equals("") == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    public interface AsyncRequestListener {
        public void LoadObjectRequest(JSONObject object);

        public void UpdateProgress(int progress);

        public void LoadException(String errorMessage);

        public void ParseException(String errorMessage);

        public void PreRequest();
    }

    class CacheBody {
        private String content;
        private long stampTime;
        private int expireTime;

        private void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }

        private int getExpireTime() {
            return expireTime;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setStampTime(long stampTime) {
            this.stampTime = stampTime;
        }

        public long getStampTime() {
            return stampTime;
        }

    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
