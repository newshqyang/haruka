package com.example.testclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.shqyang.yexplorer.ExplorerFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ExplorerFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        frag = new ExplorerFragment(rootPath);
        getSupportFragmentManager().beginTransaction().add(R.id.container_ll, frag).commit();
        PermissionUtils.requestReadFile(this, 1);
        frag.setClickAction(new ExplorerFragment.FileClickAction() {
            @Override
            public void openFile(File file) {
                Toast.makeText(MainActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!frag.back2previous()) {
            Toast.makeText(this, "没了到顶了", Toast.LENGTH_SHORT).show();
        }
    }
}