package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/23.
 */

public class TaskAdapter extends BaseAdapter{

    private Context context;
    private List<TaskInfo> taskInfos;
    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;

    public TaskAdapter(Context context, List<TaskInfo> taskInfos, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        if(taskInfos == null){
            this.taskInfos = new ArrayList<>();
        }else {
            this.taskInfos = taskInfos;
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<TaskInfo> taskInfos){
        this.taskInfos = taskInfos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return taskInfos.size();
    }

    @Override
    public TaskInfo getItem(int position) {
        return taskInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_task,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_dayTime = (TextView) convertView.findViewById(R.id.appDayTime);
            viewHolder.tv_appName = (TextView) convertView.findViewById(R.id.appName);
            viewHolder.b_appDownload = (Button) convertView.findViewById(R.id.appDownload);
            viewHolder.im_appIcon = (ImageView) convertView.findViewById(R.id.imageAppIcon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskInfo taskInfo = getItem(position);
        viewHolder.tv_dayTime.setText("第一天");
        viewHolder.tv_appName.setText(taskInfo.getAppname());
        // 通常将position设置为tag，方便之后判断点击的button是哪一个
        viewHolder.b_appDownload.setTag(position);
        viewHolder.b_appDownload.setOnClickListener(this.onClickListener);
        return convertView;
    }

    private class  ViewHolder{
        TextView tv_dayTime;
        TextView tv_appName;
        Button b_appDownload;
        ImageView im_appIcon;
    }
}
