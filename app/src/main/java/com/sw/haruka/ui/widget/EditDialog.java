package com.sw.haruka.ui.widget;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;


import com.sw.haruka.R;


public class EditDialog implements PopupMenu.OnMenuItemClickListener {

    private EditTopDialog mEditTopDialog;
    private EditBottomDialog mEditBottomDialog;

    private MoreCore mMoreCore;

    private Context mContext;
    public EditDialog(Context context) {
        mContext = context;
        mEditTopDialog = new EditTopDialog(mContext);
        mEditBottomDialog = new EditBottomDialog(mContext);
    }

    /**
     * 绑定取消按钮监听器
     * @param listener  监听器
     */
    public void setCancelClickListener(View.OnClickListener listener) {
        mEditTopDialog.findViewById(R.id.iv_cancel).setOnClickListener(listener);
    }

    /**
     * 绑定更多弹出菜单核心
     * @param moreCore     弹出菜单核心实现
     */
    public void setMoreCore(MoreCore moreCore) {
        mMoreCore = moreCore;
    }

    /**
     * 获取更多按钮视图
     */
    public void showMorePopupMenu(boolean isShowShareButton) {
        MorePopupMenu menu = new MorePopupMenu(mContext, mEditBottomDialog.getMore());
        menu.setEnableShareButton(isShowShareButton);
        menu.setOnMenuItemClickListener(this);
        menu.show();
    }

    /*
       刷新多选工具栏，
       参数1：选择文件数量
       参数2：单选时，判断文件是否为文件夹
        */
    public void refreshSelectToolBar(int selectFileSize, String filePath) {
        mEditBottomDialog.refreshSelectToolBar(selectFileSize, filePath);
    }

    /**
     * 绑定更多按钮监听器
     * @param listener  监听器
     */
    public void setMoreClickListener(View.OnClickListener listener) {
        mEditBottomDialog.getMore().setOnClickListener(listener);
    }

    /**
     * 绑定删除按钮监听器
     * @param listener  监听器
     */
    public void setDeleteClickListener(View.OnClickListener listener) {
        mEditBottomDialog.getDelete().setOnClickListener(listener);
    }

    /**
     * 绑定移动按钮监听器
     * @param listener  监听器
     */
    public void setMoveClickListener(View.OnClickListener listener) {
        mEditBottomDialog.getMove().setOnClickListener(listener);
    }

    /**
     * 绑定二维码按钮监听器
     * @param listener  监听器
     */
    public void setQrCodeClickListener(View.OnClickListener listener) {
        mEditBottomDialog.getQrCode().setOnClickListener(listener);
    }

    /**
     * 绑定全选按钮监听器
     * @param listener  监听器
     */
    public void setSelectAllClickListener(View.OnClickListener listener) {
        mEditTopDialog.getIVSelectAll().setOnClickListener(listener);
    }

    /**
     * 设置选中多少文字
     * @param size  选中的数量
     */
    public void setSelectSizeText(int size) {
        mEditTopDialog.setSelectSize(size);
    }

    public void show() {
        mEditTopDialog.show();
        mEditBottomDialog.show();
    }

    public void dismiss() {
        if (!mEditTopDialog.isShowing()) {
            return;
        }
        mEditTopDialog.dismiss();
        mEditBottomDialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_tool_share:
                mMoreCore.share();
                break;
            case R.id.select_tool_rename:
                mMoreCore.rename();
                break;
            case R.id.select_tool_showPath:
                mMoreCore.showPath();
                break;
        }
        return false;
    }

    /**
     * 更多菜单
     */
    public interface MoreCore {
        public void share();
        public void rename();
        public void showPath();
    }
}
