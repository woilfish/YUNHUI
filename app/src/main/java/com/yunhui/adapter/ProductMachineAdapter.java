package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.ProductMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/19.
 */

public class ProductMachineAdapter extends BaseAdapter{

    private Context context;
    private List<ProductMachine> productMachines;
    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;

    public ProductMachineAdapter(Context context, List<ProductMachine> productMachines, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        if(productMachines == null){
            this.productMachines = new ArrayList<>();
        }else {
            this.productMachines = productMachines;
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<ProductMachine> productMachines){
        this.productMachines = productMachines;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productMachines.size();
    }

    @Override
    public ProductMachine getItem(int position) {
        return productMachines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_earnings,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_itemrarningstitle = convertView.findViewById(R.id.itemrarningstitle);
            viewHolder.b_itemearningbuy = convertView.findViewById(R.id.itemearningbuy);
            viewHolder.tv_itemrarningsinfo = convertView.findViewById(R.id.itemrarningsinfo);
            viewHolder.tv_itemrarning = convertView.findViewById(R.id.itemrarning);
            viewHolder.tv_itemrarningday = convertView.findViewById(R.id.itemrarningday);
            viewHolder.tv_itemrarningdayinfo = convertView.findViewById(R.id.itemrarningdayinfo);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductMachine productMachine = getItem(position);

        if("3".equals(productMachine.getId())){
            viewHolder.tv_itemrarningstitle.setText(productMachine.getTitle()+ "-" + productMachine.getAmout() + "元/年");
        }else {
            viewHolder.tv_itemrarningstitle.setText(productMachine.getTitle()+ "-" + productMachine.getAmout());
        }
        viewHolder.tv_itemrarningsinfo.setText(productMachine.getDayBenifit());
        viewHolder.tv_itemrarning.setText(productMachine.getTotalBenifit());
        viewHolder.tv_itemrarningday.setText(productMachine.getCircle());
        viewHolder.tv_itemrarningdayinfo.setText(productMachine.getContent());

        // 通常将position设置为tag，方便之后判断点击的button是哪一个
        viewHolder.b_itemearningbuy.setTag(position);
        viewHolder.b_itemearningbuy.setOnClickListener(this.onClickListener);
        return convertView;
    }

    private class ViewHolder{
        TextView tv_itemrarningstitle;
        Button b_itemearningbuy;
        TextView tv_itemrarningsinfo;
        TextView tv_itemrarning;
        TextView tv_itemrarningday;
        TextView tv_itemrarningdayinfo;
    }
}
