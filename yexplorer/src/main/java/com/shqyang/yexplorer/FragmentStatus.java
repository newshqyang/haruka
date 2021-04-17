package com.shqyang.yexplorer;

/**
 * Created by shqyang on 04/18/2021
 */
public interface FragmentStatus {

    void initView();
    void initEvent();
    void loadData(boolean isRefresh);

}
