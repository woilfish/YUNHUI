package com.yunhui.fragmenr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.TaskAdapter;
import com.yunhui.bean.TaskInfo;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;

import org.json.JSONObject;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_task,null);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryTaskList();
    }

    private void initView() {

        tv_taskdate = parentView.findViewById(R.id.allTotalRevenue);
        rlv_taskList = parentView.findViewById(R.id.taskList);
        tv_taskdate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        taskAdapter = new TaskAdapter(homeActivity,taskInfoList);
        rlv_taskList.setAdapter(taskAdapter);
        rlv_taskList.setOnRefreshListViewListener(this);
        rlv_taskList.setPullRefreshEnable(false);
        rlv_taskList.setPullLoadEnable(false);
    }

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
}
