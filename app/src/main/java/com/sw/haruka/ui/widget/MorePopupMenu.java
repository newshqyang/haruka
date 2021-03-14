package com.sw.haruka.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import com.sw.haruka.R;


class MorePopupMenu extends PopupMenu {

    private Context mContext;
    private void init(Context context) {
        mContext = context;
        getMenuInflater().inflate(R.menu.select_tool_more, getMenu());
    }

    /**
     * 设置是否显示分享按钮
     * @param enable    是否显示
     */
    public void setEnableShareButton(boolean enable) {
        if (!enable) {
            getMenu().getItem(0).setVisible(false);
        }
    }

    public MorePopupMenu(Context context, View anchor) {
        super(context, anchor);
        init(context);
    }
    public MorePopupMenu(Context context, View anchor, int gravity) {
        super(context, anchor, gravity);
        init(context);
    }
    public MorePopupMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        init(context);
    }
}
