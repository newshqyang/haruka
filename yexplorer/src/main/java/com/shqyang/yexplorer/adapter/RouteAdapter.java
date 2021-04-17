package com.shqyang.yexplorer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shqyang.yexplorer.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Created by shqyang on 04/18/2021
 */
public class RouteAdapter extends BaseQuickAdapter<File, BaseViewHolder> {
    public RouteAdapter(List<File> routeList) {
        super(R.layout.explorer_route_item, routeList);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, File file) {
        String folderName = file.getName();
        if (folderName.equals("0")) {
            folderName = "根路径";
        }
        holder.setText(R.id.route_tv, folderName);
    }
}
