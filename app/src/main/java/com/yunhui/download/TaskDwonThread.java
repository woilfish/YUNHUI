package com.yunhui.download;

import android.content.Context;

import com.yunhui.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pengmin on 2018/4/26.
 * 任务下载
 */

public class TaskDwonThread extends Thread{

    private String downLoadUrl;
    private Context context;
    private String path;

    public TaskDwonThread(Context context,String path,String downLoadUrl){
        this.context = context;
        this.downLoadUrl = downLoadUrl;
        this.path = path;
    }

    @Override
    public void run() {
        InputStream is = null;
        FileOutputStream fos = null;

        try{
            //初始化下载地址
            URL url = new URL(downLoadUrl);
            //创建连接对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //连接
            connection.connect();
            //从连接中获取文件长度
            int length = connection.getContentLength();
            //创建输入和输出流
            is = connection.getInputStream();
            File file = createFile(path,downLoadUrl);
            fos = new FileOutputStream(file);
            int count = 0;
            byte[] buffer = new byte[1024];
            while ((count = is.read(buffer)) > 0){
                //写入文件
                fos.write(buffer,0,count);
            }
            fos.flush();
            //判断文件的大小和下载的大小是都一样
            if(length == file.length()){
                //安装APK
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(fos != null){
                    fos.close();
                }
                if(is != null){
                    is.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private File createFile(String path,String downLoadUrl) {
        if (StringUtil.isEmpty(downLoadUrl) || !downLoadUrl.contains("/")) {
            return null;
        }
        String name = downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1);
        String filePath = path + File.separator + name;
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            if (folder != null && folder.isFile()) {
                folder.delete();
            }
            folder.mkdirs();
        }
        File file = new File(path, name);
        if (file != null && file.exists()) {
            file.delete();
        }
        return file;
    }
}
