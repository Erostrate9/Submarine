package com.example.submarine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.enums.DetectMode;
import com.example.submarine.background.BarView;
import com.example.submarine.background.BgLayout;
import com.example.submarine.cam.BaseActivity;
import com.example.submarine.cam.model.DrawInfo;
import com.example.submarine.cam.util.ConfigUtil;
import com.example.submarine.cam.util.DrawHelper;
import com.example.submarine.cam.util.camera.CameraHelper;
import com.example.submarine.cam.util.camera.CameraListener;
import com.example.submarine.cam.util.face.RecognizeColor;
import com.example.submarine.cam.widget.FaceRectView;
import com.example.submarine.fg.FgLayout;
import com.example.submarine.utils.Recc;
import com.example.submarine.utils.ScreenUtil;
import com.example.submarine.utils.Vector;

import java.util.ArrayList;
//import java.util.Formatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    TextView tv;
    FgLayout fgLayout;
    BgLayout bgLayout;
    Button btn_restart;
//    Arcface
    private static final String TAG = "FaceAttrPreviewActivity";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    private Integer rgbCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private FaceEngine faceEngine;
    private int afCode = -1;
    private int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;
    /**
     * ????????????????????????????????????SurfaceView???TextureView
     */
    private View previewView;
    private FaceRectView faceRectView;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * ???????????????????????????
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };
//    Arcface end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseActivity ba=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        //???????????? ????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity???????????????????????????????????????
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
        //???????????????????????????????????????
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        fgLayout=findViewById(R.id.FgLayout);
        bgLayout=findViewById(R.id.BgLayout);
        tv=findViewById(R.id.log);
        btn_restart=findViewById(R.id.btn_restart);
        //?????????????????????????????????,??????main???
        //????????????(100ms)
        //??????sub???bar
        //????????????
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //?????????????????????
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        int count = bgLayout.getChildCount();
                        for (int i =0; i < count; i++){
                            btn_restart.setX(tv.getWidth());
                            BarView b=(BarView) bgLayout.getChildAt(i);
                            Vector sub_v1=new Vector(fgLayout.subView.getX(),fgLayout.subView.getY());
                            Vector sub_v2=new Vector(fgLayout.subView.getX()+fgLayout.subView.getWidth(),fgLayout.subView.getY());
                            Vector sub_v3=new Vector(fgLayout.subView.getX()+fgLayout.subView.getWidth(),fgLayout.subView.getY()+fgLayout.subView.getHeight());
                            Vector sub_v4=new Vector(fgLayout.subView.getX(),fgLayout.subView.getY()+fgLayout.subView.getHeight());
                            Vector bt_v1=new Vector(b.x,b.y_top);
                            Vector bt_v2=new Vector(b.x+b.barWidth,b.y_top);
                            Vector bt_v3=new Vector(b.x+b.barWidth,b.y_top+b.barHeight);
                            Vector bt_v4=new Vector(b.x,b.y_top+b.barHeight);
                            Vector bb_v1=new Vector(b.x,b.y_bottom);
                            Vector bb_v2=new Vector(b.x+b.barWidth,b.y_bottom);
                            Vector bb_v3=new Vector(b.x+b.barWidth,b.y_bottom+b.barHeight);
                            Vector bb_v4=new Vector(b.x,b.y_bottom+b.barHeight);
                            Recc sub=new Recc(sub_v1,sub_v2,sub_v3,sub_v4);
                            Recc bar_top=new Recc(bt_v1,bt_v2,bt_v3,bt_v4);
                            Recc bar_bottom=new Recc(bb_v1,bb_v2,bb_v3,bb_v4);
                            if (Recc.safee(sub,bar_top) && Recc.safee(sub,bar_bottom)){
                                tv.setText(""+bgLayout.getScore());
                            }else{
                                bgLayout.stop();
                                fgLayout.stop();
                            }
                        }
                    }
                });
            }
        },1000L,150L);


    }
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, 20, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        Log.i(TAG, "initEngine:  init: " + afCode);
        if (afCode != ErrorInfo.MOK) {
            showToast( getString(R.string.init_failed, afCode));
        }
    }

    private void unInitEngine() {

        if (afCode == 0) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }
    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        super.onDestroy();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror, false, false);
            }


            @Override
            public void onPreview(byte[] nv21, Camera camera) {

                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FaceInfo> faceInfoList = new ArrayList<>();
//                long start = System.currentTimeMillis();
                int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
                if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                    code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
                    if (code != ErrorInfo.MOK) {
                        return;
                    }
                } else {
                    return;
                }

                List<AgeInfo> ageInfoList = new ArrayList<>();
                List<GenderInfo> genderInfoList = new ArrayList<>();
                List<Face3DAngle> face3DAngleList = new ArrayList<>();
                List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
                int ageCode = faceEngine.getAge(ageInfoList);
                int genderCode = faceEngine.getGender(genderInfoList);
                int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
                int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

                // ?????????????????????????????????ErrorInfo.MOK???return
                if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
                    return;
                }
                if (faceRectView != null && drawHelper != null) {
                    Rect rect = drawHelper.adjustRect(faceInfoList.get(0).getRect());
                    fgLayout.moveTo(rect.centerX(),rect.centerY()); //????????????
//                    tv.setText("x:"+rect.centerX()+",y:"+rect.centerY());
                    drawHelper.draw(faceRectView, null);
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }
    @Override
    protected void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                initEngine();
                initCamera();
            } else {
                showToast(getString( R.string.permission_denied));
            }
        }
    }

    /**
     * ???{@link #previewView}????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }

        //????????????
        bgLayout.start();
        fgLayout.start();
    }

    public void restart(View view){
        //????????????
        bgLayout.setScore(-1);
        bgLayout.start();
        fgLayout.start();
    }

}