package com.yunhui.component.slidingmenu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhui.R;


/**
 * 侧滑菜单ResideMenu 子条目
 */
public class ResideMenuItem extends LinearLayout {

    /** menu item  icon  */
    private ImageView iv_icon;
    /** menu item  title */
    private TextView tv_title;

    private int icon = 0;
    private int selectedIcon = 0 ;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        this.icon = icon;
    }

    public ResideMenuItem(Context context, int icon, int selectedIcon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        this.icon = icon;
        this.selectedIcon = selectedIcon;
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        this.icon = icon;
    }

    public ResideMenuItem(Context context, int icon, int selectedIcon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        this.icon = icon;
        this.selectedIcon = selectedIcon;
    }

    private void initViews(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.slide_menu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon){
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     * @param title
     */
    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * get the title
     * @return title
     */
    public String getTitle() {
        return tv_title.getText().toString();
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * set the icon with selected status
     *
     * @param selected
     */
    public void setItemSelected(boolean selected){
        if (!selected){
            iv_icon.setImageResource(icon);
            tv_title.setTextColor(getResources().getColor(R.color.color_white_d6d6d7));
            tv_title.setTextSize(16);
        }else {
            if (selectedIcon ==0) {
                iv_icon.setImageResource(icon);
            }else {
                iv_icon.setImageResource(selectedIcon);
            }
            tv_title.setTextColor(Color.WHITE);
            tv_title.setTextSize(17);
        }
    }
}
