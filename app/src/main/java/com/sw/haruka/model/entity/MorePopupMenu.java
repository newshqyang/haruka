package com.sw.haruka.model.entity;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import com.sw.haruka.R;

public class MorePopupMenu extends PopupMenu {

    private void init() {

        getMenuInflater().inflate(R.menu.select_tool_more, getMenu());
    }

    public MorePopupMenu(Context context, View anchor) {
        super(context, anchor);
        init();
    }
    public MorePopupMenu(Context context, View anchor, int gravity) {
        super(context, anchor, gravity);
        init();
    }
    public MorePopupMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        init();
    }
}
