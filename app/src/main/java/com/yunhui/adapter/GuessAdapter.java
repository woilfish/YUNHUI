package com.yunhui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.TaskInfo;
import com.yunhui.clickinterface.ListItemClickHelp;
import com.yunhui.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class GuessAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GuessListBean> guessListBeans;
    private ListItemClickHelp callBack;

    public GuessAdapter(Context context, List<GuessListBean> guessListBeans,ListItemClickHelp callback) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.callBack = callback;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
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
            viewHolder.tv_flatT = convertView.findViewById(R.id.flatT);
            viewHolder.l_homeTeam = convertView.findViewById(R.id.homeTeam);
            viewHolder.l_flatTeam = convertView.findViewById(R.id.flatTeam);
            viewHolder.l_visitingTeam = convertView.findViewById(R.id.visitingTeam);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GuessListBean guessListBean = getItem(position);
        viewHolder.tv_guessTitle.setText(DateUtil.getTime(guessListBean.getMath_date()) + "比赛开始     " + DateUtil.getMTime(guessListBean.getTime_endsale()) + "停止竞猜");
        viewHolder.tv_firstTeam.setText(guessListBean.getHome_team());
        viewHolder.tv_firstOdds.setText("主胜 " + guessListBean.getOdds_h());
        viewHolder.tv_flat.setText("平 " + guessListBean.getOdds_d());
        viewHolder.tv_secondTeam.setText(guessListBean.getAway_team());
        viewHolder.tv_secondOdds.setText("主负 " + guessListBean.getOdds_a());

        if(guessListBean.isHome()){
            viewHolder.l_homeTeam.setBackground(context.getResources().getDrawable(R.drawable.bg_left));
            viewHolder.tv_firstTeam.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tv_firstOdds.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.l_flatTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_flat.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_flatT.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_secondTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_secondOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
        }else {
            viewHolder.l_homeTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_firstTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_firstOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
        }
        if(guessListBean.isFlat()){
            viewHolder.l_flatTeam.setBackgroundColor(context.getResources().getColor(R.color.color_EE9707));
            viewHolder.tv_flat.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tv_flatT.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.l_homeTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_firstTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_firstOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.l_visitingTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_secondTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_secondOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
        }else {
            viewHolder.l_flatTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_flat.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_flatT.setTextColor(context.getResources().getColor(R.color.color_959697));
        }
        if(guessListBean.isVisiting()){
            viewHolder.l_visitingTeam.setBackground(context.getResources().getDrawable(R.drawable.bg_right));
            viewHolder.tv_secondTeam.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tv_secondOdds.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.l_homeTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_firstTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_firstOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.l_flatTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_flat.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_flatT.setTextColor(context.getResources().getColor(R.color.color_959697));
        }else {
            viewHolder.l_visitingTeam.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            viewHolder.tv_secondTeam.setTextColor(context.getResources().getColor(R.color.color_959697));
            viewHolder.tv_secondOdds.setTextColor(context.getResources().getColor(R.color.color_959697));
        }

        final View view = convertView;
        final int p = position;
        //获取按钮id
        final int one = viewHolder.l_homeTeam.getId();
        viewHolder.l_homeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(view,parent,p,one);
            }
        });
        final int two = viewHolder.l_flatTeam.getId();
        viewHolder.l_flatTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(view,parent,p,two);
            }
        });
        final int three = viewHolder.l_visitingTeam.getId();
        viewHolder.l_visitingTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(view,parent,p,three);
            }
        });

        return convertView;
    }

    class ViewHolder{
        private TextView tv_guessTitle,tv_firstTeam,tv_firstOdds,tv_flat,tv_flatT,tv_secondTeam,tv_secondOdds;
        private LinearLayout l_homeTeam,l_flatTeam,l_visitingTeam;
    }
}
