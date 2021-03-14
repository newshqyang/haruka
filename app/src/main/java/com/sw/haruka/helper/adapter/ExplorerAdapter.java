package com.sw.haruka.helper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.helper.utils.ImageUtil;
import com.sw.haruka.model.entity.FileViewHolder;
import com.sw.haruka.helper.utils.MediaStoreUtils;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.AndroidFileUtils;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.helper.utils.PathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExplorerAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private Context mContext;
    private List<File> mFileList;
    private SparseBooleanArray mStateCheckedMap;
    private int mChildCheckedCount = 0; // 多选框勾选的个数
    public ExplorerAdapter(Context context) {
        super();
        mContext = context;
        mStateCheckedMap = new SparseBooleanArray();
        mFileList = new ArrayList<>();
    }

    /*
    初始化文件列表
     */
    public void initFileList(String path) {
        refreshFileList(path);
    }

    /*
    根据位置获取文件
     */
    public File getFile(int position) {
        return mFileList.get(position);
    }

    /*
    刷新文件列表
     */
    public void refreshFileList(String path) {
        mFileList = FileUtils.listAllFiles(path);
        FileUtils.sortFileByName(mFileList);
        hiddenFileOperate();
        notifyDataSetChanged();
    }
    /*
    手动指定要显示的文件列表
     */
    public void setFileList(List<File> fileList) {
        mFileList = fileList;
        hiddenFileOperate();
        notifyDataSetChanged();
    }

    /*
    隐藏文件显示操作
     */
    private void hiddenFileOperate() {
        if (!SPUtils.getBooleanValueDefaultFalse(Namespace.SHOW_HIDDEN_FILE_OFFSET)) {
            AndroidFileUtils.removeHiddenFile(mFileList);
        }
    }

    /*
    返回文件列表的大小
     */
    public int getFileListSize() {
        return mFileList.size();
    }

    private boolean isShowCheckbox = false; // 是否显示多选框
    public boolean isShowCheckbox() {
        return isShowCheckbox;
    }
    public void showCheckbox() {
        isShowCheckbox = true;
        notifyDataSetChanged();
    }
    public void selectCancel() {
        isShowCheckbox = false;
        selectNone();
    }

    /*
    全选
     */
    private boolean isSelectAll = false;
    public void selectAll() {
        isSelectAll = !isSelectAll;
        int index = 0;
        while (index < mFileList.size()) {
            mStateCheckedMap.put(index, isSelectAll);
            index++;
        }
        mChildCheckedCount = isSelectAll ? index : 0;
        notifyDataSetChanged();
    }

    /*
    全不选
     */
    public void selectNone() {
        mChildCheckedCount = 0;
        mStateCheckedMap.clear();
        notifyDataSetChanged();
    }

    /*
    删除选择的文件
     */
    public void deleteSelectFile() {
        List<File> newFileList = new ArrayList<>();
        for (int i = 0;i < mFileList.size();i++) {      // 取出不用删的文件组成新的列表
            if (!mStateCheckedMap.get(i)) {
                newFileList.add(mFileList.get(i));
            }
        }
        for (int i = 0;i < mStateCheckedMap.size();i++) {
            int key = mStateCheckedMap.keyAt(i);
            if (mStateCheckedMap.get(key)) {
                String path = mFileList.get(key).getAbsolutePath();
                HashSet<String> paths = new HashSet<>();
                if (!FileUtils.deleteFile(path, paths)) {
                    Toast.makeText(mContext, "删除文件“" + PathUtils.getLast(path) + "”失败", Toast.LENGTH_SHORT).show();
                }
                String[] pathArr = paths.toArray(new String[0]);
                MediaStoreUtils.INSTANCE.refresh(pathArr);
            }
        }
        mFileList = newFileList;
        selectCancel();
    }

    /*
    修改被选中的文件的文件名
     */
    public void renameFile(String newName) {
        String path = getSelectFilePath();
        File file = new File(path);
        if (FileUtils.isRepeatName(path.replace("/" + file.getName(), ""), newName)) {
            Toast.makeText(mContext, R.string.same_name, Toast.LENGTH_SHORT).show();
            return;
        }
        int position = getSelectFilePosition();
        File newFile = new File(path.replace(file.getName(), "")
                + newName);
        boolean result = mFileList.get(position).renameTo(newFile);
        mFileList.set(position, newFile);
        if (result) {
            Toast.makeText(mContext, R.string.replaceSuccess, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, R.string.replaceFailure, Toast.LENGTH_SHORT).show();
        }
        selectNone();
    }

    /*
    获取所有多选文件的路径
     */
    public Set<String> getAllCheckedPath() {
        Set<String> pathSet = new HashSet<>();
        for (int i = 0;i < mStateCheckedMap.size();i++) {
            int key = mStateCheckedMap.keyAt(i);
            if (mStateCheckedMap.get(key)) {
                pathSet.add(mFileList.get(key).getAbsolutePath());
            }
        }
        return pathSet;
    }
    public ArrayList<String> getAllCheckedPathArrayList() {
        ArrayList<String> pathList = new ArrayList<>();
        for (int i = 0;i < mStateCheckedMap.size();i++) {
            int key = mStateCheckedMap.keyAt(i);
            if (mStateCheckedMap.get(key)) {
                pathList.add(mFileList.get(key).getAbsolutePath());
            }
        }
        return pathList;
    }

    /*
    获取单选的文件路径
     */
    public String getSelectFilePath() {
        return getAllCheckedPathArrayList().size() == 0 ? null : getAllCheckedPathArrayList().get(0);
    }

    /*
    获取勾选的第一条在文件列表的位置
     */
    public int getSelectFilePosition() {
        for (int i = 0;i < mStateCheckedMap.size();i++) {
            int key = mStateCheckedMap.keyAt(i);
            if (mStateCheckedMap.get(key)) {
                return key;
            }
        }
        return -1;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder holder, final int position) {
        if (isShowCheckbox) {
            holder.operate.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(mStateCheckedMap.get(position));
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.operate.setVisibility(View.VISIBLE);
        }
        final File file = mFileList.get(position);
        if (file.isFile()) {
            // 如果是文件，修改类型图标，隐藏右边的箭头
            String t = FileUtils.getSuffix(file.getName());
            holder.type.setImageDrawable(mContext.getDrawable(FileUtils.getTypeImage(t)));
            holder.operate.setVisibility(View.INVISIBLE);
            try {
                holder.updateDateTime.setText(FileUtils.getFileUpdateTime(file) + "  " + FileUtils.getFormatSize(new FileInputStream(file).available()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            holder.type.setImageDrawable(mContext.getDrawable(R.drawable.folder_icon));
            if (!isShowCheckbox) {
                holder.operate.setVisibility(View.VISIBLE);
            }
            holder.updateDateTime.setText(FileUtils.getFileUpdateTime(file));
        }
        holder.name.setText(file.getName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.toggle();
                holder.itemView.performClick();
            }
        });

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowCheckbox()) {
                        holder.checkBox.toggle();
                        checkboxOperate(holder, position);
                    }
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder, position);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isShowCheckbox) {
                        holder.checkBox.setChecked(true);
                        checkboxOperate(holder, position);
                    }
                    int position = holder.getLayoutPosition();
                    boolean ret = mOnItemLongClickListener.onItemLongClick(v, position);
                    return ret;
                }
            });
        }
        if (SPUtils.getBooleanValueDefaultFalse(Namespace.EXPLORER_ANIMATION_OFFSET)) {
            holder.itemView.startAnimation(AnimationUtils.makeInAnimation(mContext, true));
        }
    }

    /*
    多选框点击操作
     */
    private void checkboxOperate(FileViewHolder holder, int position) {
        mStateCheckedMap.put(position, holder.checkBox.isChecked());
        if (holder.checkBox.isChecked()) {
            mChildCheckedCount++;
        } else {
            mChildCheckedCount--;
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(FileViewHolder holder, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    private OnItemLongClickListener mOnItemLongClickListener;
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_explorer, parent, false));
    }
    public int getChildCheckedCount() {
        return mChildCheckedCount;
    }
    @Override
    public int getItemCount() {
        return mFileList.size();
    }

}
