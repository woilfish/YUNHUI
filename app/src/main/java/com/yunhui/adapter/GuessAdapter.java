package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.GuessListBean;

import java.util.ArrayList;
import java.util.List;

public class GuessAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GuessListBean> guessListBeans;

    public GuessAdapter(Context context, List<GuessListBean> guessListBeans) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        if(guessListBeans == null){
            this.guessListBeans = new ArrayList<>();
        }else {
            this.guessListBeans = guessListBeans;
        }
    }

    @Override
    public int getCount() {
        return this.guessListBeans.size();
    }

    @Override
    public GuessListBean getItem(int position) {
        return this.guessListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_guess,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_guessTitle = convertView.findViewById(R.id.guesstitle);
            viewHolder.tv_firstTeam = convertView.findViewById(R.id.firstteam);
            viewHolder.tv_firstOdds = convertView.findViewById(R.id.firstodds);
            viewHolder.tv_secondTeam = convertView.findViewById(R.id.secondteam);
            viewHolder.tv_secondOdds = convertView.findViewById(R.id.secondodds);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    class ViewHolder{
        private TextView tv_guessTitle,tv_firstTeam,tv_firstOdds,tv_flat,tv_secondTeam,tv_secondOdds;
    }
}
