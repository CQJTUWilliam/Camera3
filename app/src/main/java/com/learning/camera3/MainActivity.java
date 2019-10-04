package com.learning.camera3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import org.w3c.dom.Text;

public class MainActivity extends BaseActivity {

    private static final String TAG ="MainActivity";
    //见名知意
    AlertDialog.Builder dialogBuilder = null;
    AlertDialog requestPermissionDialog = null;
    //request code
    private final static int CAMERA_AND_STORAGE_QUEST_CODE = 0;

    private CameraUtil camera = null;
    private TextureView tev_preview = null;
    private ImageButton btn_capture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        //view
        tev_preview = (TextureView) this.findViewById(R.id.tev_preview);
        btn_capture = (ImageButton)this.findViewById(R.id.btn_capture);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera != null){
                    try {
                        camera.takePicpure();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            if(camera == null)
                camera = new CameraUtil(this,tev_preview);
        }
    }

    //
    private void checkAndRequestPermission(){
        if(this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED |
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
                if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    if(requestPermissionDialog != null)
                        requestPermissionDialog.dismiss();
                    restartApplication();
                }
                else{
                    if(requestPermissionDialog != null)
                        requestPermissionDialog.show();
                    else{
                        requestPermissionDialog = dialogBuilder.show();
                    }
                }
                break;

        }
    }


    private void restartApplication() {

        //重新打开app启动页
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //杀掉以前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
