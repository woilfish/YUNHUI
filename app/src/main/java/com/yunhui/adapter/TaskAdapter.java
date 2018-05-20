package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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
    private MyClickListener onClickListener;

    public TaskAdapter(Context context, List<TaskInfo> taskInfos, MyClickListener onClickListener) {
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
            viewHolder.download_pb = (ProgressBar)convertView.findViewById(R.id.download_pb);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskInfo taskInfo = getItem(position);
//        viewHolder.tv_dayTime.setText("第一天");
        viewHolder.tv_appName.setText(taskInfo.getAppname());
        // 通常将position设置为tag，方便之后判断点击的button是哪一个
        viewHolder.b_appDownload.setTag(position);
        viewHolder.b_appDownload.setOnClickListener(this.onClickListener);
        viewHolder.b_appDownload.setText(taskInfo.getType());
        Picasso.with(context).load(getItem(position).getIconUrl()).resize(50,50).centerCrop().error(R.mipmap.icon_log).placeholder(R.mipmap.icon_log).into(viewHolder.im_appIcon);
        return convertView;
    }

    private class  ViewHolder{
        TextView tv_dayTime;
        TextView tv_appName;
        Button b_appDownload;
        ImageView im_appIcon;
        private ProgressBar download_pb;
    }

    /**
     * 用于回调的抽象类
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
