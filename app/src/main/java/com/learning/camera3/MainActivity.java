package com.learning.camera3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    //见名知意
    AlertDialog.Builder dialogBuilder = null;
    AlertDialog requestPermissionDialog = null;
    //request code
    private final static int CAMERA_AND_STORAGE_QUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化警告框
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("需要权限");
        dialogBuilder.setMessage("没权限怎么玩儿啊");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("再次申请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkAndRequestPermission();
            }
        });
        dialogBuilder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
            }
        });
        //检查权限
        checkAndRequestPermission();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    //
    private void checkAndRequestPermission(){
        if(this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_AND_STORAGE_QUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_AND_STORAGE_QUEST_CODE:
                if(grantResults.length > 0 && (grantResults[0] | grantResults[1]) != PackageManager.PERMISSION_GRANTED){
                    if(requestPermissionDialog != null)
                        requestPermissionDialog.show();
                    else{
                        requestPermissionDialog = dialogBuilder.show();
                    }
                }
                else{
                    if(requestPermissionDialog != null)
                        requestPermissionDialog.dismiss();
                }
                break;

        }
    }

}
