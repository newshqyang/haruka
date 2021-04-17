package com.shqyang.yexplorer.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shqyang.yexplorer.R;
import com.shqyang.yexplorer.util.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by shqyang on 2021/04/17
 */
public class FileAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    //list of checked file index
    private boolean[] mCheckedArray;
    //check mode flag
    private boolean isCheckMode = false;

    public FileAdapter(List<File> fileList) {
        super(R.layout.explorer_item, fileList);
    }

    public boolean isCheckMode() {
        return isCheckMode;
    }

    /**
     * set check mode
     * @param isCheckMode set adapter's mode to check if true
     */
    public void setCheckMode(boolean isCheckMode) {
        setCheckMode(isCheckMode, -1);
    }

    /**
     * set check mode
     * @param isCheckMode set adapter's mode to check if true
     * @param checkedItem checked item position firstly when set check mode
     */
    public void setCheckMode(boolean isCheckMode, int checkedItem) {
        this.isCheckMode = isCheckMode;
        if (!isCheckMode) {
            this.notifyDataSetChanged();
            return;
        }
        if (mCheckedArray == null || mCheckedArray.length != getData().size()) {
            mCheckedArray = new boolean[getData().size()];
        } else {
            Arrays.fill(mCheckedArray, false);
        }
        if (checkedItem != -1) {
            mCheckedArray[checkedItem] = true;
        }
        this.notifyDataSetChanged();
    }

    public boolean[] getCheckedArray() {
        return mCheckedArray;
    }

    public void check(int position) {
        mCheckedArray[position] = !mCheckedArray[position];
        this.notifyDataSetChanged();;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, File file) {
        holder.setText(R.id.filename_tv, file.getName());
        holder.setText(R.id.updateTime_tv, getUpdateTime(file));
        holder.setVisible(R.id.arrow_iv, file.isDirectory() && !isCheckMode);
        holder.setVisible(R.id.check_cb, isCheckMode);
        if (isCheckMode) {
            CheckBox cb = holder.getView(R.id.check_cb);
            cb.setChecked(mCheckedArray[getItemPosition(file)]);
        }
    }

    /**
     * get update time of file
     * @param file  source
     * @return update time string
     */
    private String getUpdateTime(File file) {
        String updateTime = "";
        updateTime += FileUtils.getFileUpdateTime(file);
        try {
            updateTime += "  " + FileUtils.getFormatSize(new FileInputStream(file).available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateTime;
    }
}
