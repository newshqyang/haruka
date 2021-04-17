package com.sw.haruka.view.test;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shqyang.yexplorer.ExplorerFragment;
import com.sw.haruka.R;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    ExplorerFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_act);

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        frag = new ExplorerFragment(rootPath);
        getSupportFragmentManager().beginTransaction().add(R.id.container_ll, frag).commit();
        frag.setClickAction(new ExplorerFragment.FileClickAction() {
            @Override
            public void openFile(File file) {
                Toast.makeText(TestActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!frag.back2previous()) {
            Toast.makeText(this, "没了到顶了", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }
}