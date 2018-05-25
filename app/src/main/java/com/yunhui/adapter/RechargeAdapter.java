package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.RechargeBean;

import java.util.ArrayList;
import java.util.List;

public class RechargeAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<RechargeBean> rechargeBeans;

    public RechargeAdapter(Context context, List<RechargeBean> rechargeBeans) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        if(rechargeBeans == null){
            this.rechargeBeans = new ArrayList<>();
        }else {
            this.rechargeBeans = rechargeBeans;
        }
    }

    public void refreshData(List<RechargeBean> rechargeBeans){
        this.rechargeBeans = rechargeBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.rechargeBeans.size();
    }

    @Override
    public RechargeBean getItem(int position) {
        return this.rechargeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_recharge,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_rechargeMoney = convertView.findViewById(R.id.rechargeMoney);
            viewHolder.tv_rechargeNum = convertView.findViewById(R.id.rechargeNum);
            viewHolder.b_rechargeReduction = convertView.findViewById(R.id.rechargeReduction);
            viewHolder.b_rechargeAdd = convertView.findViewById(R.id.rechargeAdd);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        private TextView tv_rechargeMoney;
        private Button b_rechargeReduction;
        private Button b_rechargeAdd;
        private TextView tv_rechargeNum;
    }
}
