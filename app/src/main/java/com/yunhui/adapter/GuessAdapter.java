package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.TaskInfo;
import com.yunhui.util.DateUtil;

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

    public void refreshData(List<GuessListBean> guessListBeans){
        this.guessListBeans = guessListBeans;
        notifyDataSetChanged();
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
            viewHolder.tv_flat = convertView.findViewById(R.id.flat);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GuessListBean guessListBean = getItem(position);
        viewHolder.tv_guessTitle.setText(DateUtil.getTime(guessListBean.getMath_date()) + "比赛开始 " + DateUtil.getTime(guessListBean.getTime_endsale()) + "停止竞猜");
        viewHolder.tv_firstTeam.setText(guessListBean.getHome_team());
        viewHolder.tv_firstOdds.setText(guessListBean.getOdds_h());
        viewHolder.tv_flat.setText(guessListBean.getOdds_d());
        viewHolder.tv_secondTeam.setText(guessListBean.getAway_team());
        viewHolder.tv_secondOdds.setText(guessListBean.getOdds_a());

        return convertView;
    }

    class ViewHolder{
        private TextView tv_guessTitle,tv_firstTeam,tv_firstOdds,tv_flat,tv_secondTeam,tv_secondOdds;
    }
}
