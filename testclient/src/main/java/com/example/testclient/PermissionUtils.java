package com.example.testclient;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

/**
 * Created by shqyang on 04/18/2021
 */
public class PermissionUtils {

    /**
     * request permission to read file
     * @param act Activity
     * @param requestCode request code
     */
    public static boolean requestReadFile(Activity act, int requestCode) {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(act, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, requestCode);
        return false;
    }

    public static boolean hasReadFile(Activity act) {
        return ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) == 0;
    }

}
