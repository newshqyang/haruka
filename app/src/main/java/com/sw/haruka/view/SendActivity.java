package com.sw.haruka.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.sw.haruka.R;
import com.sw.haruka.helper.utils.AndroidFileUtils;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_SEND)) {
            if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                try {
                    Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
                    String path = AndroidFileUtils.getRealPathFromUri(this, uri);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
