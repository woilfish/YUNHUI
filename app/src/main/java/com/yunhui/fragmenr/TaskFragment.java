package com.yunhui.fragmenr;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.TaskAdapter;
import com.yunhui.bean.TaskInfo;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.download.TaskDwonThread;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;
import com.yunhui.util.LogUtil;
import com.yunhui.util.StringUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by pengmin on 2018/4/2.
 * 任务
 */

public class TaskFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener{

    private HomeActivity homeActivity;
    private View parentView;
    private TextView tv_taskdate;
    private RefreshListView rlv_taskList;
    private List<TaskInfo> taskInfoList;
    private TaskAdapter taskAdapter;
    private TaskDwonThread taskDwonThread;
    private HttpHandler<File> sHandler = null;
    private HttpUtils http = null;
    private String filePath;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5:
                    taskAdapter.refreshData(taskInfoList);
                    break;
            }
        }
    };

//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Button btn = (Button) view;
//            int postion = (Integer) btn.getTag();
//            taskDwonThread = new TaskDwonThread(homeActivity,downLoadPath(),taskInfoList.get(postion).getApplink());
//            taskDwonThread.start();
//            download(postion);
//        }
//    };

    private  String downLoadPath(){
        // 获取项目包名文件路径
        File filePath = homeActivity.getFilesDir();
        String path = filePath.getAbsolutePath();

        File file = new File(path + File.separator + "TaskApk");
        if (!file.exists()) {
            file.mkdir();
        }

        return file.getAbsolutePath();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();

        if(parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_task, null);
            initView();
            queryTaskList();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) parentView.getParent();
        if (parent != null)
        {
            parent.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (null != sHandler) {
            sHandler.cancel();
        }
        super.onDestroy();
    }

    private void initView() {

        tv_taskdate = parentView.findViewById(R.id.taskdate);
        rlv_taskList = parentView.findViewById(R.id.taskList);
        tv_taskdate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        taskAdapter = new TaskAdapter(homeActivity,taskInfoList,mListener);
        rlv_taskList.setAdapter(taskAdapter);
        rlv_taskList.setOnRefreshListViewListener(this);
        rlv_taskList.setPullRefreshEnable(false);
        rlv_taskList.setPullLoadEnable(false);
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private TaskAdapter.MyClickListener mListener = new TaskAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View view) {
            if(!taskInfoList.get(position).isDownload()) {
                download(position);
            }
        }
    };

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    private void queryTaskList(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(homeActivity,"user/queryUserTask", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("taskList")){
                    taskInfoList = TaskInfo.initAttrWithJson(jsonObject.optJSONArray("taskList"),1);
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    /**
     * 下载APK
     */
    private void download(final int postion){
        taskInfoList.get(postion).setDownload(true);
        String path = downLoadPath();
        final File file = createFile(path,taskInfoList.get(postion).getApplink());
        LogUtil.printE("APK路径",file.getAbsolutePath());
        if(file.exists()){
            file.delete();
        }
        http = new HttpUtils();
        sHandler = http.download(taskInfoList.get(postion).getApplink(), file.getPath().toString(), false,// 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false,// 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        taskInfoList.get(postion).setType("正在下载");
                        taskAdapter.refreshData(taskInfoList);
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        double i = current / (double) total * 100;
                        int progress = (int) Math.ceil(i);
                        taskInfoList.get(postion).setProgress(progress);

                        int firtPosition = rlv_taskList.getFirstVisiblePosition();
                        LogUtil.printE("first",firtPosition + "");
                        int lastPosition = rlv_taskList.getLastVisiblePosition();
                        LogUtil.printE("last",lastPosition + "");

                        if (postion >= rlv_taskList.getFirstVisiblePosition() && postion <= rlv_taskList.getLastVisiblePosition()) {
                            int positionInListView = postion - rlv_taskList.getFirstVisiblePosition() + 1;
                            ProgressBar item = (ProgressBar) rlv_taskList.getChildAt(positionInListView).findViewById(R.id.download_pb);
                            item.setProgress(progress);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        LogUtil.printE("下载成功","OK");
                        taskInfoList.get(postion).setType("下载完成");
//                        taskInfoList.get(postion).setDownload(true);
                        taskAdapter.refreshData(taskInfoList);
                        installApk(file);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        LogUtil.printE("下载失败","Faile");
                        taskInfoList.get(postion).setType("下载失败");
                        taskInfoList.get(postion).setDownload(false);
                        taskAdapter.refreshData(taskInfoList);
                    }
                });
    }

    private File createFile(String path,String downLoadUrl) {
        if (StringUtil.isEmpty(downLoadUrl) || !downLoadUrl.contains("/")) {
            return null;
        }
        String name = downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1);
        filePath = path + File.separator + name;
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

    /**
     * 安装已下载的AP
     * @param file
     */
    private void installApk(File file){
        Intent intent =new Intent(Intent.ACTION_VIEW);

        //判断是否是AndroidN以及更高的版本

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {

            Uri contentUri = FileProvider.getUriForFile(homeActivity,"com.yunhui.fileProvider",file);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");

        }else{

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");

        }

        homeActivity.startActivity(intent);
    }
}
