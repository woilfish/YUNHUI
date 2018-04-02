package com.yunhui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.common.Dimension;


/**
 * 导航栏
 * <p/>
 * +--------------------------+
 * |  back    title     action|
 * +--------------------------+
 */
public class NavigationBar extends LinearLayout implements View.OnClickListener {

    /**
     * 间隔时间击时间
     */
    private final long INTERVAL_TIME = 800;

    /**
     * 点击时间记录tag
     */
    private final int TAG_FORMER_CLICK_TIME = "TAG_FORMER_CLICK_TIME".hashCode();



    private ViewGroup mRootLayout; //根布局
    private FrameLayout navBackLayout;
    private int                   background;           //整体背景
    private int                   statusBarBackgroud;   //沉浸状态栏北京色
    private int                   bottomBg;             //底部背景条
    private int                   backBg;               //返回按钮背景
    private int                   closeBg;              //关闭按钮背景
    private String backText;             //返回按钮文字
    private String actionText;           //操作按钮文字
    private int                   actionBg;             //操作按钮背景
    private int                   textColor;            //文字颜色，包括title和操作按钮的文字颜色
    private float                 btnTextSize;          //按钮文字大小
    private float                 titleTextSize;        //title文字大小
    private int                   titleMaxLength;       //title最大长度
    private TextView backButton;           //返回文本
    private ImageView backImage;            //返回按钮
    private TextView closeButton;          //关闭文本
    private ImageView closeImage;           //关闭按钮
    private TextView actionButton;         //操作文本
    private ImageView actionImage;          //操作按钮
    private TextView titleText;            //title文本
    private ImageView bottomImage;          //底部imageview，导航栏下边的灰色条
    private ProgressBar mProgress;            //右侧进度提示bar
    private LinearLayout statusBar;            //沉浸状态栏
    private OnNavBarClickListener onNavBarClickListener;//导航栏点击事件监听器

    public NavigationBar(Context context) {
        super(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        initAttrs(context, attrs);
        initView();
    }

    /**
     * 设置导航栏事件监听器
     *
     * @param onNavBarClickListener 点击监听器
     */
    public void setOnNavBarClickListener(OnNavBarClickListener onNavBarClickListener) {
        this.onNavBarClickListener = onNavBarClickListener;
    }

    /**
     * 获取标题对象titleText
     *
     * @return
     */
    public TextView getNavbarTitleTextView() {
        return titleText;
    }

    /**
     * 配置事件监听器
     *
     * @param view 被点击的view
     */
    @Override
    public void onClick(View view) {
        if (isFastDoubleClick(view)) {
            return;
        }

        if (onNavBarClickListener == null) {
            return;
        }

        int id = view.getId();
        if ((id == R.id.nav_box_back && backButton.isShown()) || (id == R.id.nav_back_image && backImage.isShown())) {
            onNavBarClickListener.onNavItemClick(NavigationBarItem.back);

        } else if ((id == R.id.nav_close && closeButton.isShown()) || (id == R.id.nav_close_image && closeImage.isShown())) {
            onNavBarClickListener.onNavItemClick(NavigationBarItem.close);

        } else if (id == R.id.nav_center_text) {
            onNavBarClickListener.onNavItemClick(NavigationBarItem.title);

        } else if ((id == R.id.nav_box_action && actionButton.isShown()) || (id == R.id.nav_right_image && actionImage.isShown())) {
            onNavBarClickListener.onNavItemClick(NavigationBarItem.action);
        }
    }

    /**
     * 是否重复点击
     *
     * @return
     * @view 被点击view，如果前后是同一个view，则进行双击校验
     */
    private boolean isFastDoubleClick(View view) {
        boolean isFastDoubleClick = false;
        long now = System.currentTimeMillis();

        Object lastClickTime = view.getTag(TAG_FORMER_CLICK_TIME);
        if (lastClickTime != null && lastClickTime instanceof Long && (now - (long) lastClickTime) < INTERVAL_TIME){
            isFastDoubleClick = true;
        }
        view.setTag(TAG_FORMER_CLICK_TIME, now);
        return isFastDoubleClick;
    }

    /**
     * @param color 设置文字颜色
     */
    public void setTextColor(int color) {
        backButton.setTextColor(color);
        titleText.setTextColor(color);
        closeButton.setTextColor(color);
        actionButton.setTextColor(color);
    }


    /**
     * 设置返回按钮文字
     *
     * @param backText 按钮文字
     */
    public void setBackText(String backText) {
        backImage.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        backButton.setText(backText);
        backButton.setBackgroundResource(0);
        navBackLayout.setPadding(Dimension.dip2px(5, getContext()), 0, 0, 0);
    }

    /**
     * 设置返回按钮文字
     *
     * @param resId 按钮文字id
     */
    public void setBackText(int resId) {
        String text = getContext().getString(resId);
        setBackText(text);
    }


    /**
     * @param color 设置返回按钮文字颜色
     */
    public void setBackBtnTextColor(int color) {
        backButton.setTextColor(color);
    }

    /**
     * 设置返回图标
     * @param resource
     */
    public void setBackBtnResource(int resource){
        if (resource !=0){
            backImage.setBackgroundResource(resource);
            backImage.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.GONE);
        }
    }


    /**
     * 设置关闭按钮文字
     *
     * @param backText 按钮文字
     */
    public void setCloseText(String backText) {
        closeImage.setVisibility(View.GONE);
        closeImage.setVisibility(View.VISIBLE);
        closeButton.setText(backText);
        closeButton.setBackgroundResource(0);
    }

    /**
     * 设置关闭按钮文字
     *
     * @param resId 按钮文字id
     */
    public void setCloseText(int resId) {
        String text = getContext().getString(resId);
        setCloseText(text);
    }


    /**
     * @param color 设置关闭按钮文字颜色
     */
    public void setCloseBtnTextColor(int color) {
        closeButton.setTextColor(color);
    }


    /**
     * 设置返回图标
     * @param resource
     */
    public void setCloseBtnResource(int resource){
        if (resource !=0){
            closeImage.setBackgroundResource(resource);
            closeImage.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.GONE);
        }
    }


    /**
     * 设置标题
     *
     * @param title 标题文字
     */
    public void setTitle(String title) {

        if (isEmpty(title)){
            title = "";
        }

//        if(!isEmpty(title) && title.length() > 9){
//            title = title.substring(0,8) + "…";
//        }

        titleText.setText(title);
    }

    private boolean isEmpty(String str){

        if (TextUtils.isEmpty(str) || "null".equals(str)){
            return true;
        }

        return false;
    }

    /**
     * 获取标题名称
     *
     * @return 标题字符串
     */
    public String getTitle() {
        return titleText.getText().toString();
    }

    /**
     * 设置标题
     *
     * @param resId 标题文字id
     */
    public void setTitle(int resId) {
        String text = getContext().getString(resId);
        setTitle(text);
    }

    /**
     * 设置标题文字颜色
     */
    public void setTitleTextColor(int resId){
        titleText.setTextColor(resId);
    }

    /**
     * 设置标题文字大小
     *
     * @param sizePx
     */
    public void setTitleTextSize(int sizePx) {
        int sizeSp = Dimension.px2sp(sizePx, getContext());
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
    }

    /**
     * 给textview添加图标
     *
     * @param left   左侧图标id
     * @param top    上侧图标id
     * @param right  右侧图标id
     * @param bottom 底侧图标id
     */
    public void setTitleCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        titleText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        titleText.setCompoundDrawablePadding(Dimension.dip2px(5, getContext()));
    }

    /**
     * 设置操作按钮文字
     *
     * @param actionText
     */
    public void setActionBtnText(String actionText) {
        actionButton.setVisibility(View.VISIBLE);
        actionImage.setVisibility(View.GONE);
        actionButton.setText(actionText);
    }

    /**
     * 获取操作按钮的文字
     *
     * @return
     */
    public String getActionBtnText() {
        return actionButton.getText().toString();
    }

    /**
     * 获取返回textview
     * @return
     */
    public TextView getBackTextView(){
        return backButton;
    }

    /**
     * 获取返回按钮imageview
     * @return
     */
    public int getBackImageVisiable(){
        return backImage.getVisibility();
    }


    /**
     * 获取action textview
     * @return
     */
    public TextView getActionTextView(){
        return actionButton;
    }

    /**
     * 设置操作按钮文字
     *
     * @param resId 操作按钮文字id
     */
    public void setActionBtnText(int resId) {
        setActionBtnText(getContext().getString(resId));
    }

    /**
     * 设置操作按钮字体颜色
     *
     * @param color
     */
    public void setActionBtnTextColor(int color) {
        actionButton.setTextColor(color);
    }

    /**
     * 设置操作按钮是否可用
     *
     * @param isEnable
     */
    public void setActionBtnEnabled(boolean isEnable) {
        actionButton.setEnabled(isEnable);
    }

    /**
     * 设置返回按钮是否可见
     *
     * @param visibility
     */
    public void setBackBtnVisibility(int visibility) {
        backButton.setVisibility(visibility);
        backImage.setVisibility(visibility);
    }

    /**
     * 设置关闭按钮是否可见
     *
     * @param visibility
     */
    public void setCloseBtnVisibility(int visibility) {
        closeButton.setVisibility(visibility);
        closeImage.setVisibility(visibility);
    }

    /**
     * 设置操作按钮是否可见
     *
     * @param visibility
     */
    public void setActionBtnVisibility(int visibility) {
        actionButton.setVisibility(visibility);
        actionImage.setVisibility(visibility);
    }

    /**
     * 设置操作按钮是否可点击
     * 目前只实现到颜色的修改，是否可控制由B端控制 两平台
     * @param
     */
    public void setActionBtnHasAction(boolean hasAction) {
//        actionButton.setEnabled(hasAction);
//        actionImage.setEnabled(hasAction);
        actionButton.setTextColor(hasAction ? Color.WHITE : Color.rgb(193,193,193));
    }

    /**
     * 设置操作按钮背景
     *
     * @param resourceId 资源id
     */
    public void setActionBtnBackground(int resourceId) {
        actionImage.setImageResource(resourceId);
        actionImage.setVisibility(VISIBLE);
        actionButton.setVisibility(GONE);
    }

    /**
     * 设置操作按钮背景
     *
     * @param drawable
     */
    public void setActionBtnBackground(Drawable drawable) {
        actionImage.setBackgroundDrawable(drawable);
        actionImage.setVisibility(VISIBLE);
        actionButton.setVisibility(GONE);
    }

    /**
     * 设置关闭按钮背景
     *
     * @param resourceId 资源id
     */
    public void setCloseButtonBackground(int resourceId) {
        closeImage.setBackgroundResource(resourceId);
        closeImage.setVisibility(VISIBLE);
        closeButton.setVisibility(GONE);
    }

    /**
     * 设置返回按钮背景
     *
     * @param resourceId 资源id
     */
    public void setBackButtonBackground(int resourceId) {
        backButton.setText("");
        backButton.setVisibility(GONE);
        if (resourceId == R.mipmap.nav_back) {
            navBackLayout.setPadding(Dimension.dip2px(5, getContext()), 0, 0, 0);
        } else {
            navBackLayout.setPadding(Dimension.dip2px(10, getContext()), 0, 0, 0);
        }
        backImage.setImageResource(resourceId);
        backImage.setVisibility(VISIBLE);

    }

    /**
     * 设置按钮文本字体大小
     * @param btnTextSize
     */
    public void setBtnTextSize(int btnTextSize){
        //字体大小
        if (Float.compare(btnTextSize, 0.0f) > 0) {
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, btnTextSize);
            actionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, btnTextSize);
        }
    }

    /**
     * 设置标题背景
     *
     * @param resourceId 资源id
     */
    public void setTitleBackground(int resourceId) {
        titleText.setBackgroundResource(resourceId);
    }

    /**
     * 设置标题背景
     *
     * @param resourceId 资源id
     */
    public void setNavBackground(int resourceId) {
        mRootLayout.setBackgroundResource(resourceId);
    }

    /**
     * 设置标题背景色
     *
     * @param color
     */
    public void setNavBackgroundColor(int color) {
        mRootLayout.setBackgroundColor(color);
    }

    /**
     * 显示右侧进度提示bar
     */
    public void showRightProgress() {
        actionButton.setVisibility(GONE);
        actionImage.setVisibility(GONE);
        mProgress.setVisibility(VISIBLE);
    }

    /**
     * 隐藏右侧进度提示bar
     */
    public void hideRightProgress() {
        if (TextUtils.isEmpty(actionText)) {
            actionImage.setVisibility(VISIBLE);
        }else {
            actionButton.setVisibility(VISIBLE);
        }
        mProgress.setVisibility(GONE);
    }

    /**
     * 删掉所有元素
     */
    public void clear() {
        mProgress.setVisibility(GONE);
        titleText.setText(null);
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        titleText.setBackgroundResource(0);
        backButton.setText(null);
        backButton.setBackgroundResource(0);
        closeButton.setText(null);
        closeButton.setBackgroundResource(0);
        actionButton.setText(null);
        actionButton.setBackgroundResource(0);
    }

    public void hideNavStatusBar() {
        statusBar.setVisibility(View.GONE);
    }

    public  void showNavStatusBar() {
        statusBar.setVisibility(View.VISIBLE);
    }

    public void setStatusBarBackgroud(int color){
        statusBar.setBackgroundColor(color);
    }

    public void setStatusBarBackgroud(String color) {
        statusBar.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * 初始化属性值
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        //获取xml中配置的属性资源
        TypedArray typedArray   = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
        try {
            background          = typedArray.getResourceId(R.styleable.NavigationBar_navBg, 0);
            statusBarBackgroud  = typedArray.getResourceId(R.styleable.NavigationBar_statusBarBg, getResources().getColor(R.color.AppTheThemeColor));
            bottomBg            = typedArray.getResourceId(R.styleable.NavigationBar_bottomBg, 0);
            backBg              = typedArray.getResourceId(R.styleable.NavigationBar_backBg, 0);
            closeBg             = typedArray.getResourceId(R.styleable.NavigationBar_closeBg, 0);
            actionText          = typedArray.getString(R.styleable.NavigationBar_actionText);
            backText            = typedArray.getString(R.styleable.NavigationBar_backText);
            actionBg            = typedArray.getResourceId(R.styleable.NavigationBar_actionBg, 0);
            textColor           = typedArray.getColor(R.styleable.NavigationBar_textColor, 0xFFFFFFFF);
            btnTextSize         = typedArray.getDimension(R.styleable.NavigationBar_btnTextSize, 0.0f);
            titleTextSize       = typedArray.getDimension(R.styleable.NavigationBar_titleTextSize, 0.0f);
            titleMaxLength      = typedArray.getInt(R.styleable.NavigationBar_titleMaxLength, 9);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 初始化导航条中的相关元素
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.l_navigation_bar, this, true);

        backButton      = (TextView) findViewById(R.id.nav_back);
        backImage       = (ImageView) findViewById(R.id.nav_back_image);
        closeButton     = (TextView) findViewById(R.id.nav_close);
        closeImage      = (ImageView) findViewById(R.id.nav_close_image);
        actionButton    = (TextView) findViewById(R.id.nav_right_btn);
        actionImage     = (ImageView) findViewById(R.id.nav_right_image);
        titleText       = (TextView) findViewById(R.id.nav_center_text);
        bottomImage     = (ImageView) findViewById(R.id.nav_bottom_image);
        mProgress       = (ProgressBar) findViewById(R.id.nav_right_progress);
        navBackLayout   = (FrameLayout) findViewById(R.id.nav_box_back);
        statusBar       = (LinearLayout) findViewById(R.id.nav_statusBar);

        statusBar.setBackgroundColor(statusBarBackgroud);

        //背景
        mRootLayout = (ViewGroup) findViewById(R.id.nav_bar_root);
        mRootLayout.setBackgroundResource(background);
        //底部灰色横条
        bottomImage.setBackgroundResource(bottomBg);

        //返回按钮
        backButton.setText(backText);
        backButton.setTextColor(textColor);
        backButton.setBackgroundResource(backBg);
        navBackLayout.setPadding(Dimension.dip2px(5, getContext()), 0, 0, 0);

        //关闭按钮
        closeImage.setImageResource(closeBg);

        //操作按钮
        actionButton.setText(actionText);
        actionButton.setTextColor(textColor);

        actionImage.setImageResource(actionBg);

        //标题文字
        titleText.setTextColor(textColor);
//        InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(titleMaxLength);
//        InputFilter[]            filters      = {lengthFilter};
//        titleText.setFilters(filters);

        //字体大小
        if (Float.compare(btnTextSize, 0.0f) > 0) {
            backButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            actionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        if (Float.compare(titleTextSize, 0.0f) > 0) {
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }

        //设置事件监听器
        findViewById(R.id.nav_box_back).setOnClickListener(this);
        findViewById(R.id.nav_back_image).setOnClickListener(this);
        findViewById(R.id.nav_close).setOnClickListener(this);
        findViewById(R.id.nav_close_image).setOnClickListener(this);
        titleText.setOnClickListener(this);
        findViewById(R.id.nav_box_action).setOnClickListener(this);
        findViewById(R.id.nav_right_image).setOnClickListener(this);
    }


    public enum NavigationBarItem {

        back,//返回按钮
        close,//关闭按钮
        title, //title文字
        action  //操作按钮，即最右边的按钮
    }

    /**
     * 导航点击事件监听接口
     */
    public interface OnNavBarClickListener {
        public void onNavItemClick(NavigationBarItem navBarItem);
    }

    /**
     * 供外部调用设置导航栏背景颜色。
     * @param background
     */
    public void setBackground(int background) {
        mRootLayout.setBackgroundResource(background);
//        this.background = background;
    }

    /**
     * 工外部调用设置导航栏背景颜色。
     * @param color
     */
    public void setBackground(String color) {
        mRootLayout.setBackgroundColor(Color.parseColor(color));
    }
}
