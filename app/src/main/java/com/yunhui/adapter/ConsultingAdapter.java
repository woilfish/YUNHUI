package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.ConsultingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/2.
 */

public class ConsultingAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ConsultingInfo> consultingInfos;

    public ConsultingAdapter(Context context, List<ConsultingInfo> consultingInfos) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        if(consultingInfos == null){
            this.consultingInfos = new ArrayList<>();
        }else{
            this.consultingInfos = consultingInfos;
        }
    }

    public void refreshData(List<ConsultingInfo> consultingInfos){
        this.consultingInfos = consultingInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return consultingInfos.size();
    }

    @Override
    public ConsultingInfo getItem(int position) {
        return consultingInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_consult_view,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_Title = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHolder.tv_Time = (TextView) convertView.findViewById(R.id.itemTime);
            viewHolder.tv_Content = (TextView) convertView.findViewById(R.id.itemContent);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ConsultingInfo consultingInfo = getItem(position);
        viewHolder.tv_Title.setText(consultingInfo.getTitle());
        viewHolder.tv_Time.setText(consultingInfo.getTime());
        viewHolder.tv_Content.setText(consultingInfo.getMessage());

        return convertView;
    }

    private class ViewHolder{
        TextView tv_Title,tv_Time,tv_Content;
    }
}
