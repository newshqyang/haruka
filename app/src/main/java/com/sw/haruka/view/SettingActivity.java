package com.sw.haruka.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sw.haruka.R;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.helper.utils.SPUtils;

public class SettingActivity extends BaseStatusActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch mExplorerAnimationOffset;
    private Switch mShowHiddenFileOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mExplorerAnimationOffset = findViewById(R.id.eao_offset);
        mExplorerAnimationOffset.setChecked(SPUtils.getBooleanValueDefaultFalse(Namespace.EXPLORER_ANIMATION_OFFSET));
        mExplorerAnimationOffset.setOnCheckedChangeListener(this);

        mShowHiddenFileOffset = findViewById(R.id.shfo_offset);
        mShowHiddenFileOffset.setChecked(SPUtils.getBooleanValueDefaultFalse(Namespace.SHOW_HIDDEN_FILE_OFFSET));
        mShowHiddenFileOffset.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.eao_offset:
                eaoOperate(isChecked);
                break;
            case R.id.shfo_offset:
                shfoOperate(isChecked);
                break;
        }
    }

    private void shfoOperate(boolean isChecked) {
        SPUtils.saveBoolean(Namespace.SHOW_HIDDEN_FILE_OFFSET, isChecked);
    }

    private void eaoOperate(boolean isChecked) {
        SPUtils.saveBoolean(Namespace.EXPLORER_ANIMATION_OFFSET, isChecked);
    }


    public void manageCollections(View view) {
        startActivity(new Intent(this, ManageCollectionActivity.class));
    }

    public void explorerAnimationOffset(View view) {
        mExplorerAnimationOffset.performClick();
    }

    public void showHiddenFileOffset(View view) {
        mShowHiddenFileOffset.performClick();
    }

    public void finish(View view) {
        finish();
    }
}
