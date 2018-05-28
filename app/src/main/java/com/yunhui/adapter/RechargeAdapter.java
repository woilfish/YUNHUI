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
import com.yunhui.clickinterface.ListItemClickHelp;

import java.util.ArrayList;
import java.util.List;

public class RechargeAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<RechargeBean> rechargeBeans;
    private ListItemClickHelp callback;

    public RechargeAdapter(Context context, List<RechargeBean> rechargeBeans, ListItemClickHelp callback) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.callback = callback;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
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

        RechargeBean rechargeBean = getItem(position);
        viewHolder.tv_rechargeMoney.setText(rechargeBean.getContent());
        final View view = convertView;
        final int p = position;
        //获取按钮id
        final int one = viewHolder.b_rechargeReduction.getId();
        viewHolder.b_rechargeReduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view,parent,p,one);
            }
        });

        final int two = viewHolder.b_rechargeAdd.getId();
        viewHolder.b_rechargeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view,parent,p,two);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView tv_rechargeMoney;
        private Button b_rechargeReduction;
        private Button b_rechargeAdd;
        private TextView tv_rechargeNum;
    }
}
