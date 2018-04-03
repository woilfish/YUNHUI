package com.yunhui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yunhui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/3.
 * 引导页适配器
 */

public class GuideAdapter extends PagerAdapter implements View.OnClickListener{

    private List<View> lstImageView = new ArrayList<View>();
    private EnterHomeCallBack enterHomeCallBack;

    /**
     * 经典版本引导图片id
     */
    public static final int[] ORIGINAL_IMAGE_IDS = {
//            R.drawable.guide_orginal_image_0,
//            R.drawable.guide_orginal_image_1,
//            R.drawable.guide_orginal_image_2
    };

    public static final int[] ORIGINAL_BOTTOM_IMAGE_IDS = {
//            R.drawable.guide_dio_orange_on,
//            R.drawable.guide_dio_orange
    };

    private Context context;

    public GuideAdapter(Context context,EnterHomeCallBack enterHomeCallBack) {
        super();
        this.context = context;
        this.enterHomeCallBack = enterHomeCallBack;
        setUpdateItemView(ORIGINAL_IMAGE_IDS, R.mipmap.guide_original_image_btn);
    }

    @Override
    public int getCount() {
        return lstImageView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = lstImageView.get(position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void onClick(View v) {
        this.enterHomeCallBack.enterHomeActivity();
    }

    /**
     * 初始化view
     */
    private void setUpdateItemView(int[] images,int btnImgId){
        int viewSize = images.length;
        for(int i = 0;i < viewSize;i++){
            View itemView;
            if(i < viewSize - 1){
                itemView = LayoutInflater.from(context).inflate(R.layout.activity_guide_item, null);
                itemView.setTag(i);
                itemView.setBackgroundResource(images[i]);
            }else{
                itemView =  LayoutInflater.from(context).inflate(R.layout.activity_guide_item2, null);
                final Button btnOpen = (Button) itemView.findViewById(R.id.activity_guide_open_app_button);
                btnOpen.setBackgroundResource(btnImgId);
                btnOpen.setOnClickListener(this);
                itemView.setBackgroundResource(images[i]);
            }
            lstImageView.add(itemView);
        }
    }

    public interface EnterHomeCallBack{
        void enterHomeActivity();
    }
}
