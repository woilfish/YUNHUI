package com.yunhui.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by pengmin on 2018/4/2.
 */

public class FragmentController {


    private SparseArray<Fragment> fragmentHashMap = new SparseArray<Fragment>();
    //保存状态的fragment
    private SparseArray<Fragment> retainFragmentHashMap = new SparseArray<Fragment>();
    private FragmentActivity context;

    /**
     * fragment切换容器Id
     */
    private int containerId;

    /**
     * 容器
     */
    private ViewGroup container;

    /**
     * 当前Fragment标识
     */
    private int currentTag;

    private OnSwitchListener onSwitchListener;

    public FragmentController(FragmentActivity context, int containerId){
        this.context        = context;
        this.containerId    = containerId;
        this.container      = (ViewGroup) context.findViewById(containerId);
    }

    /**
     * 设置切换监听器
     * @param onSwitchListener
     */
    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    /**
     * 切换监听器
     */
    public interface OnSwitchListener{

        /**
         * 切换回调
         * @param id
         */
        void onSwitch(int id);
    }

    /**
     * 获取当前Fragment
     * @return  currentFragment
     */
    public Fragment getCurrentFragment(){
        return getFragmentAtIndex(currentTag);
    }

    /**
     * 获取指定id的Fragment
     * @param index fragment 的id
     * @return
     */
    public Fragment getFragmentAtIndex(int index){
        return fragmentHashMap.get(currentTag);
    }

    /**
     * 获取保存当前实例 fragment 的map
     *
     * @return  map
     */
    public SparseArray<Fragment> getFragmentHashMap(){
        return this.fragmentHashMap;
    }

    /**
     * 切换Fragment
     * @param id
     * @param savedInstanceState 是否保存状态
     * @param fragmentName
     * @param arguments
     */
    public void switchFragment(int id,boolean savedInstanceState,String fragmentName,Bundle arguments){
        Fragment current = context.getSupportFragmentManager().findFragmentByTag(String.valueOf(currentTag));
        if (id == currentTag && current != null){
            return;
        }
        //将当前Fragment移出
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        if (current != null){
            if (isCurrentFragmentRetain()){//当前fragment设置保证状态，则隐藏当前fragment
                transaction.hide(current);
            }else {
                transaction.detach(current);
            }
        }
        //首先手动清空Fragment视图
//        container.removeAllViews();
        //获取新Fragment
        Fragment newFragment = context.getSupportFragmentManager().findFragmentByTag(String.valueOf(id));
        //之前添加过,则直接attach
        if (newFragment != null){
            transaction.attach(newFragment);
        } else {
            //如果之前没有添加过,或者被回收掉,则新建一个Fragment添加到管理中
            try {
               /* Class fragementClass = Class.forName(fragmentName);
                Class[] parameterTypes = new Class[0];
                Method method = fragementClass.getDeclaredMethod("newInstance", parameterTypes);
                newFragment = (Fragment) method.invoke(new Object[0]);*/
                newFragment = (Fragment) Fragment.instantiate(context, fragmentName, arguments);
                transaction.add(containerId, newFragment, String.valueOf(id));
            } catch (Exception e) {

            }
        }

        //标记当前Fragment
        currentTag = id;
        fragmentHashMap.put(currentTag, newFragment);

        if (savedInstanceState) {//设置保存当前状态属性，则直接显示
            retainFragmentHashMap.put(currentTag,newFragment);
            transaction.show(newFragment).commitAllowingStateLoss();
        }else {
            transaction.commitAllowingStateLoss();
        }

        context.getSupportFragmentManager().executePendingTransactions();

        if (onSwitchListener !=null){//切换回调
            onSwitchListener.onSwitch(id);
        }
    }

    /**
     * 当前的fragment是否设置保存状态
     * @return
     */
    private boolean isCurrentFragmentRetain() {

        Fragment fragment = retainFragmentHashMap.get(currentTag);
        if (fragment == null) {
            return false;
        } else {
            return true;
        }
    }
}
