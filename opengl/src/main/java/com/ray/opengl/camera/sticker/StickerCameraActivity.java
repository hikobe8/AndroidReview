package com.ray.opengl.camera.sticker;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ray.opengl.R;
import com.ray.opengl.filter.GrayFilter;
import com.ray.opengl.util.Accelerometer;
import com.sensetime.stmobileapi.STMobile106;
import com.sensetime.stmobileapi.STMobileFaceAction;
import com.sensetime.stmobileapi.STMobileMultiTrack106;
import com.sensetime.stmobileapi.STUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

public class StickerCameraActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Renderer mRenderer;
    private int cameraId = 1;
    private TextureController mController;
    private TextView mTvInfo;
    private Accelerometer mAccelerometer;
    private WaterMarkFilter filter;
    private static final int MAX_TRACK_WIDTH = 640;
    private static final int MAX_TRACK_HEIGHT = 480;

    public static void launch(Context context){
        Intent intent = new Intent(context, StickerCameraActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
                return;
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mController != null) {
            mController.onResume();
        }
        if (mAccelerometer != null) {
            mAccelerometer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mController != null) {
            mController.onPause();
        }
        if (mAccelerometer != null) {
            mAccelerometer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mController != null) {
            mController.destroy();
        }
    }

    public void handleFaceLandmark(STMobileFaceAction[] faceActions, int orientation, int mouthAh,
                                   int previewWidth, int previewHeight) {
        boolean rotate270 = orientation == 270;
        if (faceActions != null && faceActions.length > 0) {
            STMobileFaceAction faceAction = faceActions[0];
            Log.i("Test", "-->> face count = "+faceActions.length);
            PointF[] points = faceAction.getFace().getPointsArray();
            float[] landmarkX = new float[points.length];
            float[] landmarkY = new float[points.length];
            for (int i = 0; i < points.length; i++) {
                if (rotate270) {
                    points[i] = STUtils.RotateDeg270(points[i], previewWidth, previewHeight);
                } else {
                    points[i] = STUtils.RotateDeg90(points[i], previewWidth, previewHeight);
                }

                landmarkX[i] = 1 - points[i].x / MAX_TRACK_HEIGHT;
                landmarkY[i] = points[i].y / MAX_TRACK_WIDTH;
            }
            Rect rect;
            if (rotate270) {
                rect = STUtils.RotateDeg270(faceAction.getFace().getRect(), previewWidth, previewHeight);
            } else {
                rect = STUtils.RotateDeg90(faceAction.getFace().getRect(), previewWidth, previewHeight);
            }
            onGetFaceLandmark(landmarkX, landmarkY, rect);
        }
    }

    private List<PointF> mDynamicPoints = new ArrayList<>();

    private void onGetFaceLandmark(final float[] landmarkX, final float[] landmarkY, Rect rect) {
        mDynamicPoints.clear();

//        int length = landmarkX.length;
//        for (int i=0; i<length; i++) {
//            landmarkX[i] = (landmarkX[i] * 2f - 1f) * 7f;
//            landmarkY[i] = ((1-landmarkY[i]) * 2f - 1f) * 9.3f;
//        }

//        // 额头
//        mDynamicPoints.add(new PointF(landmarkX[41], landmarkY[41]));
//        mDynamicPoints.add(new PointF( landmarkX[39], landmarkY[39], 0.0f);
//        mDynamicPoints.add(new PointF(20, (landmarkX[36] + landmarkX[39])*0.5f, (landmarkY[36] + landmarkY[39])*0.5f, 0.0f));
//        mDynamicPoints.add(new PointF(10, landmarkX[36], landmarkY[36], 0.0f));
//        mDynamicPoints.add(new PointF(30, landmarkX[34], landmarkY[34], 0.0f));
//        // 左脸
//        mDynamicPoints.add(new PointF(21, landmarkX[32], landmarkY[32], 0.0f));
//        mDynamicPoints.add(new PointF(22, landmarkX[29], landmarkY[29], 0.0f));
//        mDynamicPoints.add(new PointF(23, landmarkX[24], landmarkY[24], 0.0f));
//        mDynamicPoints.add(new PointF(24, landmarkX[20], landmarkY[20], 0.0f));
//        // 下巴
//        mDynamicPoints.add(new PointF(9, landmarkX[16], landmarkY[16], 0.0f));
//        // 右脸
//        mDynamicPoints.add(new PointF(28, landmarkX[0], landmarkY[0], 0.0f));
//        mDynamicPoints.add(new PointF(27, landmarkX[3], landmarkY[3], 0.0f));
//        mDynamicPoints.add(new PointF(26, landmarkX[8], landmarkY[8], 0.0f));
//        mDynamicPoints.add(new PointF(25, landmarkX[12], landmarkY[12], 0.0f));
//        // 左眼
//        mDynamicPoints.add(new PointF(19, landmarkX[61], landmarkY[61], 0.0f));
//        mDynamicPoints.add(new PointF(32, landmarkX[60], landmarkY[60], 0.0f));
//        mDynamicPoints.add(new PointF(16, landmarkX[75], landmarkY[75], 0.0f));
//        mDynamicPoints.add(new PointF(31, landmarkX[59], landmarkY[59], 0.0f));
//        mDynamicPoints.add(new PointF(17, landmarkX[58], landmarkY[58], 0.0f));
//        mDynamicPoints.add(new PointF(33, landmarkX[63], landmarkY[63], 0.0f));
//        mDynamicPoints.add(new PointF(18, landmarkX[76], landmarkY[76], 0.0f));
//        mDynamicPoints.add(new PointF(34, landmarkX[62], landmarkY[62], 0.0f));
//        // 右眼
//        mDynamicPoints.add(new PointF(14, landmarkX[52], landmarkY[52], 0.0f));
//        mDynamicPoints.add(new PointF(36, landmarkX[53], landmarkY[53], 0.0f));
//        mDynamicPoints.add(new PointF(11, landmarkX[72], landmarkY[72], 0.0f));
//        mDynamicPoints.add(new PointF(35, landmarkX[54], landmarkY[54], 0.0f));
//        mDynamicPoints.add(new PointF(12, landmarkX[55], landmarkY[55], 0.0f));
//        mDynamicPoints.add(new PointF(38, landmarkX[56], landmarkY[56], 0.0f));
//        mDynamicPoints.add(new PointF(13, landmarkX[73], landmarkY[73], 0.0f));
//        mDynamicPoints.add(new PointF(37, landmarkX[57], landmarkY[57], 0.0f));
        // 鼻子
        mDynamicPoints.add(new PointF(landmarkX[49], landmarkY[49]));
        mDynamicPoints.add(new PointF(landmarkX[82], landmarkY[82]));
        mDynamicPoints.add(new PointF(landmarkX[83], landmarkY[83]));
        mDynamicPoints.add(new PointF(landmarkX[46], landmarkY[46]));
        mDynamicPoints.add(new PointF(landmarkX[43], landmarkY[43]));
        // 嘴巴
//        mDynamicPoints.add(new PointF(landmarkX[90], landmarkY[90]));
//        mDynamicPoints.add(new PointF(landmarkX[99], landmarkY[99]));
//        mDynamicPoints.add(new PointF( landmarkX[98], landmarkY[98]));
//        mDynamicPoints.add(new PointF( landmarkX[97], landmarkY[97]));
//        mDynamicPoints.add(new PointF( landmarkX[84], landmarkY[84]));
//        mDynamicPoints.add(new PointF( landmarkX[103], landmarkY[103]));
//        mDynamicPoints.add(new PointF( landmarkX[102], landmarkY[102]));
//        mDynamicPoints.add(new PointF( landmarkX[101], landmarkY[101]));
//        mDynamicPoints.add(new PointF( landmarkX[93], landmarkY[93]));

        if (filter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rect.left = (int) (1f*((Camera2Renderer)mRenderer).mPreviewSize.getHeight() * rect.left / MAX_TRACK_HEIGHT);
                rect.right = (int) (1f*((Camera2Renderer)mRenderer).mPreviewSize.getHeight() * rect.right / MAX_TRACK_HEIGHT);
                int bmpWidth = rect.width();
                filter.setPosition((int) ((int) (((Camera2Renderer)mRenderer).mPreviewSize.getHeight()*mDynamicPoints.get(0).x) - bmpWidth/2f), (int) ((int) (((Camera2Renderer)mRenderer).mPreviewSize.getWidth()*mDynamicPoints.get(0).y) - bmpWidth/2f), bmpWidth, bmpWidth);
            }
        }
    }

    private void setContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 用于人脸检测
            mAccelerometer = new Accelerometer(this);
            mRenderer = new Camera2Renderer();
            ((Camera2Renderer)mRenderer).setTrackCallBackListener(new TrackCallBackListener() {
                @Override
                public void onTrackDetected(final STMobileFaceAction[] faceActions, int orientation, final int value, final float pitch, final float roll, final float yaw, final int eye_dist, final int id, final int eyeBlink, final int mouthAh, final int headYaw, final int headPitch, final int browJump) {
                    handleFaceLandmark(faceActions, orientation, mouthAh, MAX_TRACK_WIDTH, MAX_TRACK_HEIGHT);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvInfo.setText("TRACK: " + value + " MS"
                                    + "\nPITCH: " + pitch + "\nROLL: " + roll + "\nYAW: " + yaw + "\nEYE_DIST:" + eye_dist + "\n" +
                                    "ID:" + id + "\nEYE_BLINK:" + eyeBlink + "\nMOUTH_AH:"
                                    + mouthAh + "\nHEAD_YAW:" + headYaw + "\nHEAD_PITCH:" + headPitch + "\nBROW_JUMP:" + browJump);
                        }
                    });
                }
            });
        } else {
            mRenderer = new Camera1Renderer();
        }
        setContentView(R.layout.activity_sticker_camera);
        mSurfaceView = findViewById(R.id.mSurface);
        mTvInfo = findViewById(R.id.tv);
        mController = new TextureController(this);
//        mController.addFilter(new GrayFilter(getResources()));
        filter = new WaterMarkFilter(getResources());
        filter.setWaterMark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_sticker_mouth10));
        mController.addFilter(filter);
//        StickerFilter filter2=new StickerFilter(getResources());
//        filter2.setSticker(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
//        filter2.setPosition(300,300,150,150);
//        mController.addFilter(filter2);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mController.surfaceCreated(holder);
                mController.setRenderer(mRenderer);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mController.surfaceChanged(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mController.surfaceDestroyed();
            }
        });
    }

    private class Camera1Renderer implements Renderer {

        private Camera mCamera;

        @Override
        public void onDestroy() {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            mCamera = Camera.open(cameraId);
            mController.setImageDirection(cameraId);
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            mController.setDataSize(size.height, size.width);
            try {
                mCamera.setPreviewTexture(mController.getTexture());
                mController.getTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        mController.requestRender();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }

    }

    ///< 检测脸部动作：张嘴、眨眼、抬眉、点头、摇头
    private static final int ST_MOBILE_TRACKING_ENABLE_FACE_ACTION = 0x00000020;
    private static final int ST_MOBILE_FACE_DETECT   =  0x00000001;    ///<  人脸检测
    private static final int ST_MOBILE_EYE_BLINK     =  0x00000002;  ///<  眨眼
    private static final int ST_MOBILE_MOUTH_AH      =  0x00000004;    ///<  嘴巴大张
    private static final int ST_MOBILE_HEAD_YAW      =  0x00000008;    ///<  摇头
    private static final int ST_MOBILE_HEAD_PITCH    =  0x00000010;    ///<  点头
    private static final int ST_MOBILE_BROW_JUMP     =  0x00000020;    ///<  眉毛挑动

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class Camera2Renderer implements Renderer {

        private static final String TAG = "Camera2Renderer";
        CameraDevice mDevice;
        CameraManager mCameraManager;
        private HandlerThread mThread;
        private Handler mHandler;
        private Size mPreviewSize;
        private byte nv21[];
        private STMobileMultiTrack106 tracker;
        private ImageReader imageReader;
        private HandlerThread mInferenceThread;
        private Handler mInferenceHandler;

        Camera2Renderer() {
            mCameraManager = (CameraManager)getSystemService(CAMERA_SERVICE);
            mThread = new HandlerThread("camera2 ");
            mThread.start();
            mHandler = new Handler(mThread.getLooper());


            //人脸检测
            mInferenceThread = new HandlerThread("InferenceThread");
            mInferenceThread.start();
            mInferenceHandler = new Handler(mInferenceThread.getLooper());
            nv21 = new byte[MAX_TRACK_WIDTH * MAX_TRACK_HEIGHT * 2];
            tracker = new STMobileMultiTrack106(StickerCameraActivity.this, ST_MOBILE_TRACKING_ENABLE_FACE_ACTION);
            int max = 1;
            tracker.setMaxDetectableFaces(max);
        }

        @Override
        public void onDestroy() {
            if(mDevice!=null){
                mDevice.close();
                mDevice=null;
            }
        }

        private void handlePreviewData(ImageReader reader) {
            Image image = null;
            try {
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                Log.i(TAG, "get image bytes size: " + bytes.length);

                System.arraycopy(bytes, 0, nv21, 0, bytes.length);

                mInferenceHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            long startTime = System.currentTimeMillis();

                            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(String.valueOf(cameraId));
                            int orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                            boolean frontCamera = (cameraId == 1);
                            int direction = Accelerometer.getDirection();

                            if (frontCamera &&
                                    ((orientation == 270 && (direction & 1) == 1) ||
                                            (orientation == 90 && (direction & 1) == 0))) {
                                direction = (direction ^ 2);
                            }

                            STMobileFaceAction[] faceActions = tracker.trackFaceAction(nv21, direction, MAX_TRACK_WIDTH, MAX_TRACK_HEIGHT);

                            long endTime = System.currentTimeMillis();
                            float trackTime = endTime - startTime;
                            Log.i(TAG, "start time: " + startTime);
                            Log.i(TAG, "end time: " + endTime);
                            Log.i(TAG, "track time: " + trackTime);

                            if (faceActions != null && faceActions.length > 0) {
                                Log.i(TAG, "-->> faceActions: faceActions[0].face=" + faceActions[0].face.rect.toString() + ", " +
                                        "pitch = " + faceActions[0].face.pitch + ", " +
                                        "roll=" + faceActions[0].face.roll + ", " +
                                        "yaw=" + faceActions[0].face.yaw + ", " +
                                        "face_action = " + faceActions[0].face_action + ", " +
                                        "face_count = " + faceActions.length);
                                if (trackCallBackListener != null) {
                                    trackCallBackListener.onTrackDetected(faceActions, orientation, (int) trackTime,
                                            faceActions[0].face.pitch,
                                            faceActions[0].face.roll,
                                            faceActions[0].face.yaw,
                                            faceActions[0].face.eye_dist,
                                            faceActions[0].face.ID,
                                            checkFlag(faceActions[0].face_action, ST_MOBILE_EYE_BLINK),
                                            checkFlag(faceActions[0].face_action, ST_MOBILE_MOUTH_AH),
                                            checkFlag(faceActions[0].face_action, ST_MOBILE_HEAD_YAW),
                                            checkFlag(faceActions[0].face_action, ST_MOBILE_HEAD_PITCH),
                                            checkFlag(faceActions[0].face_action, ST_MOBILE_BROW_JUMP));
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (image != null) {
                    image.close();
                }
            }
        }

        private int checkFlag(int action, int flag) {
            int res = action & flag;
            return res == 0 ? 0 : 1;
        }

        private TrackCallBackListener trackCallBackListener;

        public void setTrackCallBackListener(TrackCallBackListener trackCallBackListener) {
            this.trackCallBackListener = trackCallBackListener;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            try {
                if(mDevice!=null){
                    mDevice.close();
                    mDevice=null;
                }
                CameraCharacteristics c=mCameraManager.getCameraCharacteristics(cameraId+"");
                StreamConfigurationMap map=c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] sizes=map.getOutputSizes(SurfaceHolder.class);
                //自定义规则，选个大小
                mPreviewSize=sizes[0];
                mController.setDataSize(mPreviewSize.getHeight(),mPreviewSize.getWidth());
                imageReader = ImageReader.newInstance(MAX_TRACK_WIDTH, MAX_TRACK_HEIGHT, ImageFormat.YUV_420_888, 2);
                imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        handlePreviewData(reader);
                    }
                }, mHandler);
                mCameraManager.openCamera(cameraId + "", new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(CameraDevice camera) {
                        mDevice=camera;
                        try {
                            Surface surface=new Surface(mController
                                    .getTexture());
                            final CaptureRequest.Builder builder=mDevice.createCaptureRequest
                                    (TEMPLATE_PREVIEW);
                            builder.addTarget(surface);
                            builder.addTarget(imageReader.getSurface());
                            mController.getTexture().setDefaultBufferSize(
                                    mPreviewSize.getWidth(),mPreviewSize.getHeight());
                            mDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()),  new
                                    CameraCaptureSession.StateCallback() {
                                        @Override
                                        public void onConfigured(CameraCaptureSession session) {
                                            try {
                                                session.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                                    @Override
                                                    public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                                                        super.onCaptureProgressed(session, request, partialResult);
                                                    }

                                                    @Override
                                                    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                                        super.onCaptureCompleted(session, request, result);
                                                        mController.requestRender();
                                                    }
                                                },mHandler);
                                            } catch (CameraAccessException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onConfigureFailed(CameraCaptureSession session) {

                                        }
                                    },mHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDisconnected(CameraDevice camera) {
                        mDevice=null;
                    }

                    @Override
                    public void onError(CameraDevice camera, int error) {

                    }
                }, mHandler);
            } catch (SecurityException | CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }

    public interface TrackCallBackListener {
        void onTrackDetected(STMobileFaceAction[] faceActions, int orientation,
                             int value, float pitch, float roll, float yaw, int eye_dist,
                             int id, int eyeBlink, int mouthAh, int headYaw, int headPitch, int browJump);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0) {
            setContent();
        }
    }
}
