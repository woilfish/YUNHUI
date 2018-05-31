package com.yunhui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.MyBettingInfo;
import com.yunhui.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class MyBettingAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MyBettingInfo> myBettingInfos;

    public MyBettingAdapter(Context context, List<MyBettingInfo> myBettingInfos) {
        this.context = context;
        if(myBettingInfos == null){
            this.myBettingInfos = new ArrayList<>();
        }else{
            this.myBettingInfos = myBettingInfos;
        }
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<MyBettingInfo> myBettingInfos){
        this.myBettingInfos = myBettingInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.myBettingInfos.size();
    }

    @Override
    public MyBettingInfo getItem(int position) {
        return this.myBettingInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_mybetting,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_myBettingdate = convertView.findViewById(R.id.myBettingdate);
            viewHolder.tv_myBettinghite = convertView.findViewById(R.id.myBettinghite);
            viewHolder.tv_myBettingResult = convertView.findViewById(R.id.myBettingResult);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyBettingInfo myBettingInfo = getItem(position);
        viewHolder.tv_myBettingdate.setText(DateUtil.getTime(myBettingInfo.getCreatetime()));
        viewHolder.tv_myBettinghite.setText(myBettingInfo.getGuessListBeans().size() + " 串 " + 1);
        if("1".equals(myBettingInfo.getQrystate())){
            viewHolder.tv_myBettingResult.setText("待开奖");
        }
        if("2".equals(myBettingInfo.getQrystate())){
            viewHolder.tv_myBettingResult.setText("已中奖");
            viewHolder.tv_myBettingResult.setTextColor(context.getResources().getColor(R.color.red));
        }
        if("3".equals(myBettingInfo.getQrystate())){
            viewHolder.tv_myBettingResult.setText("未中奖");
            viewHolder.tv_myBettingResult.setTextColor(context.getResources().getColor(R.color.black));
        }


        return convertView;
    }

    class ViewHolder{
        private TextView tv_myBettingdate,tv_myBettinghite,tv_myBettingResult;
    }
}
