package com.sw.haruka.view;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sw.haruka.R;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.model.entity.FileExplorer;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.MediaStoreUtils;

import java.util.ArrayList;
import java.util.HashSet;

public class FolderActivity extends BaseStatusActivity {

    private RelativeLayout mTitleBar;
    private ImageButton mBack;
    private FileExplorer mExplorer;

    private String mPath;   // 传来的路径
    private String FOLDER_MODE = "move";    // 模式，默认是“移动文件/文件夹”模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        FOLDER_MODE = getIntent().getStringExtra("mode");
        mMoveToThis = findViewById(R.id.button_moveToThis);
        if (FOLDER_MODE != null && FOLDER_MODE.equals("move")) {
            // move模式
            mPath = HarukaConfig.ROOT_PATH;
            mMoveFilePathArray = getIntent().getStringArrayListExtra("moveFilePathArray");
            modeMove();
        } else {
            mMoveToThis.setVisibility(View.GONE);
            mPath = getIntent().getStringExtra("path");
        }
        mBack = findViewById(R.id.imageButton_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar = findViewById(R.id.folder_title_bar);
        mExplorer = findViewById(R.id.folder_explorer);
        mExplorer.setNoStorageBar(true);
        if (FOLDER_MODE != null && FOLDER_MODE.equals("move")) {
            mExplorer.setNoSelectTool(true);
            mExplorer.initList(HarukaConfig.ROOT_PATH);
        } else {
            mExplorer.initList(mPath);
        }
    }

    /*
    移动文件的模式
     */
    private Button mMoveToThis;
    private ArrayList<String> mMoveFilePathArray;
    private void modeMove() {
        mMoveToThis.setVisibility(View.VISIBLE);
        mMoveToThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<String> paths = new HashSet<>();
                for (String path : mMoveFilePathArray) {
                    if (!FileUtils.moveFile(path, mExplorer.getCurrentPath(), paths)) {
                        Toast.makeText(FolderActivity.this, R.string.move_failure, Toast.LENGTH_SHORT).show();
                    }
                }
                MediaStoreUtils.INSTANCE.refresh(paths.toArray(new String[0]));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mExplorer.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExplorer != null) {
            mExplorer.refresh();
        }
    }
}
