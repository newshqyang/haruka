package com.sw.haruka.model.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.adapter.ExplorerAdapter;
import com.sw.haruka.model.dialog.DeleteDialog;
import com.sw.haruka.model.dialog.MyEditTextButtonDialog;
import com.sw.haruka.model.dialog.NormalDialog;
import com.sw.haruka.model.dialog.OperateDialog;
import com.sw.haruka.ui.search.SearchCore;
import com.sw.haruka.ui.widget.EditDialog;
import com.sw.haruka.helper.utils.AndroidFileUtils;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.helper.utils.NetUtils;

import com.sw.haruka.helper.utils.PxUtils;
import com.sw.haruka.helper.utils.StringUtil;
import com.sw.haruka.view.FolderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileExplorerBase extends FileExplorerBar {

    protected EditDialog mEditDialog;
    protected ExplorerAdapter mExplorerAdapter;
    private SearchCore mSearchCore;

    private boolean mNoSelectTool = false;  // 不使用多选功能

    public void setNoSelectTool(boolean noSelectTool) {
        mNoSelectTool = noSelectTool;
    }

    @Override
    public void refresh(Index index) {
        super.refresh(index);
        hideEditDialog();
    }

    protected void initBarComponent() {
        mEditDialog = new EditDialog(mContext);
        mEditDialog.setMoreCore(new EditDialog.MoreCore() {
            @Override
            public void share() {
                String path = mExplorerAdapter.getSelectFilePath();
                File file = new File(path);
                System.out.println("日志：" + Environment.getExternalStorageDirectory());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, "tech.haruka.fileProvider", file));
                shareIntent.setType("*/*");
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                System.out.println("日志：" + FileProvider.getUriForFile(mContext, "tech.haruka.fileProvider", file));
                //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
                shareIntent = Intent.createChooser(shareIntent, "选择一个以分享文件");
                mContext.startActivity(shareIntent);
            }

            @Override
            public void rename() {
                String path = mExplorerAdapter.getSelectFilePath();
                File file = new File(path);
                final MyEditTextButtonDialog renameDialog = new MyEditTextButtonDialog(mContext);
                renameDialog.getEditText().setHint("文件/文件夹名称");
                renameDialog.getEditText().setText(file.getName());
                renameDialog.getButton().setText("确定");
                renameDialog.getButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = renameDialog.getEditText().getText().toString();
                        if (newName.trim().length() > 0) {
                            mExplorerAdapter.renameFile(newName);
                        }
                        renameDialog.dismiss();
                        mExplorerAdapter.selectCancel();
                        hideEditDialog();
                    }
                });
                renameDialog.show();
            }

            @Override
            public void showPath() {
                final String path = mExplorerAdapter.getSelectFilePath();
                selectCancel();
                final NormalDialog dialog = new NormalDialog(mContext);
                dialog.getNoButton().setText("关闭");
                dialog.getYesButton().setText("复制");
                dialog.setText(path).setYesClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringUtil.setClipData(mContext, path);
                        Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        mEditDialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCancel();
            }
        });
        mEditDialog.setSelectAllClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll();
            }
        });
        mEditDialog.setQrCodeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQrCode();
            }
        });
        mEditDialog.setMoveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveListFile();
            }
        });
        mEditDialog.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListFile();
            }
        });
        mEditDialog.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMorePopupMenu();
            }
        });
        mExplorerAdapter = new ExplorerAdapter(mContext);
        mExplorerAdapter.setOnItemClickListener(new ExplorerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FileViewHolder holder, int position) {
                if (mExplorerAdapter.isShowCheckbox()) {
                    int size = mExplorerAdapter.getChildCheckedCount();
                    mEditDialog.setSelectSizeText(size);
                    mEditDialog.refreshSelectToolBar(size, mExplorerAdapter.getSelectFilePath());
                    return;
                }
                File file = mExplorerAdapter.getFile(position);
                if (file.isDirectory()) {   // 如果是文件夹，就进入文件夹
                    // 进入文件夹
                    mSearchCore.openFolder(file.getAbsolutePath());
                } else {    // 打开文件
                    AndroidFileUtils.openFile(mContext, file);
                }
            }
        });
        mExplorerAdapter.setOnItemLongClickListener(new ExplorerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (mNoSelectTool) {
                    return false;
                }
                mExplorerAdapter.showCheckbox();    // 文件浏览器显示多选框
                int selectSize = mExplorerAdapter.getChildCheckedCount();
                showEditDialog();

                mEditDialog.setSelectSizeText(selectSize);
                mEditDialog.refreshSelectToolBar(selectSize, mExplorerAdapter.getSelectFilePath());// 显示底部多选工具栏
                return false;
            }
        });
        setAdapter(mExplorerAdapter);
    }

    /**
     * 找到列表中第一个可见元素的位置
     * @return  位置
     */
    public int findFirstVisibleItemPosition() {
        return mManager.findFirstVisibleItemPosition();
    }

    /*
    返回上一级
     */
    public boolean back() {
        if (mExplorerAdapter.isShowCheckbox()) {
            selectCancel();
            return true;
        }
        return false;
    }

    /*
    根据路径载入文件列表
    */
    public void load(String path) {
        List<File> fileList = FileUtils.listAllFiles(path);
        FileUtils.sortFileByName(fileList);
        load(fileList);
    }

    /*
    载入指定的文件列表
     */
    public void load(List<File> fileList) {
        refresh(fileList);
    }

    /*
    移动文件
     */
    private void moveListFile() {
        ArrayList<String> moveFilePathSet = mExplorerAdapter.getAllCheckedPathArrayList();
        Intent intent = new Intent(mContext, FolderActivity.class);
        intent.putExtra("mode", "move");
        intent.putStringArrayListExtra("moveFilePathArray", moveFilePathSet);
        mContext.startActivity(intent);
    }

    /*
    删除文件
     */
    private void deleteListFile() {
        Set<String> deleteFilePathList = mExplorerAdapter.getAllCheckedPath();
        if (deleteFilePathList.size() == 0) {
            Toast.makeText(mContext, "请勾选需要删除的内容", Toast.LENGTH_SHORT).show();
            return;
        }
        final DeleteDialog dialog = new DeleteDialog(mContext);
        dialog.setFilePath(deleteFilePathList.size());
        dialog.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplorerAdapter.deleteSelectFile();
                showEmptyView();
                hideEditDialog();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void hideEditDialog() {
        // 收
        try {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.bottomMargin = PxUtils.dip2px(mContext, 0);
            setLayoutParams(layoutParams);
        } catch (Exception e) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            layoutParams.bottomMargin = PxUtils.dip2px(mContext, 0);
            setLayoutParams(layoutParams);
        }
        mEditDialog.dismiss();
    }

    private void showEditDialog() {
        // 提臀
        try {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.bottomMargin = PxUtils.dip2px(mContext, 80);
            setLayoutParams(layoutParams);
        } catch (Exception e) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            layoutParams.bottomMargin = PxUtils.dip2px(mContext, 80);
            setLayoutParams(layoutParams);
        }
        mEditDialog.show();
    }

    /*
    显示更多功能弹出菜单
     */
    private void showMorePopupMenu() {
        String path = mExplorerAdapter.getSelectFilePath();
        File file = new File(path);
        boolean isShowShareButton = true;
        if (file.isDirectory()) {
            isShowShareButton = false;
        }
        mEditDialog.showMorePopupMenu(isShowShareButton);
    }

    /*
    全选
     */
    private void selectAll() {
        mExplorerAdapter.selectAll();
        mEditDialog.setSelectSizeText(mExplorerAdapter.getChildCheckedCount());
        mEditDialog.refreshSelectToolBar(mExplorerAdapter.getChildCheckedCount(),
                mExplorerAdapter.getSelectFilePath());
    }

    /*
    取消多选模式，隐藏多选组件
     */
    protected void selectCancel() {
        hideEditDialog();
        mExplorerAdapter.selectCancel();
    }

    /*
    恢复
     */
    public void resume() {
        selectCancel();
    }

    /*
    显示二维码
     */
    private void showQrCode() {
        String filePath = mExplorerAdapter.getSelectFilePath();
        OperateDialog dialog = new OperateDialog(mContext);
        dialog.createQrCode(filePath, NetUtils.getLocalIP(mContext), HarukaConfig.Deliver_Port);
        dialog.show();
    }

    public void setSearchCore(SearchCore searchCore) {
        mSearchCore = searchCore;
    }

    public FileExplorerBase(@NonNull Context context) {
        super(context);
        initBarComponent();
    }
    public FileExplorerBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBarComponent();
    }
}
