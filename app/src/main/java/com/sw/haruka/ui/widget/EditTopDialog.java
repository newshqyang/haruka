package com.sw.haruka.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;


import com.sw.haruka.R;

class EditTopDialog extends TopDialog {

    private TextView mTVSelectSize;
    private ImageView mIVCancel;
    private ImageView mIVSelectAll;

    private Context mContext;
    private void init(Context c) {
        mContext = c;
        setContentView(R.layout.dialog_edit_top);
        setCanceledOnTouchOutside(false);

        mTVSelectSize = findViewById(R.id.tv_selectSize);
        mIVCancel = findViewById(R.id.iv_cancel);
        mIVSelectAll = findViewById(R.id.iv_selectAll);
    }

    public ImageView getIVSelectAll() {
        return mIVSelectAll;
    }

    @SuppressLint("SetTextI18n")
    public void setSelectSize(int size) {
        mTVSelectSize.setText("已选中 " + size + " 项");
    }

    @Override
    public void dismiss() {
        setSelectSize(0);
        super.dismiss();
    }

    public EditTopDialog(Context context) {
        super(context);
        init(context);
    }
}
