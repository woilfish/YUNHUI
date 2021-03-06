package com.pengmin.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.pengmin.zxing.camera.CameraManager;
import com.pengmin.zxing.camera.open.CameraFacing;
import com.pengmin.zxing.common.Scanner;


import java.io.IOException;

/**
 * Created by hupei on 2016/7/1.
 */
public class ScannerView extends FrameLayout implements SurfaceHolder.Callback {

    private static final String TAG = ScannerView.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private ViewfinderView mViewfinderView;

    private boolean hasSurface;
    private CameraManager mCameraManager;
    private ScannerViewHandler mScannerViewHandler;
    private BeepManager mBeepManager;
    private OnScannerCompletionListener mScannerCompletionListener;

    private boolean lightMode = false;//闪光灯，默认关闭

    private ScannerOptions mScannerOptions;
    private ScannerOptions.Builder mScannerOptionsBuilder;

    public ScannerView(Context context) {
        this(context, null);
    }

    public ScannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        hasSurface = false;

        mSurfaceView = new SurfaceView(context, attrs, defStyle);
        addView(mSurfaceView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mViewfinderView = new ViewfinderView(context, attrs);
        addView(mViewfinderView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mScannerOptionsBuilder = new ScannerOptions.Builder();
        mScannerOptions = mScannerOptionsBuilder.build();
    }

    public void onResume() {
        mCameraManager = new CameraManager(getContext(), mScannerOptions);
        mViewfinderView.setCameraManager(mCameraManager);
        mViewfinderView.setScannerOptions(mScannerOptions);
        mViewfinderView.setVisibility(mScannerOptions.isViewfinderHide() ? View.GONE : View.VISIBLE);
        if (mBeepManager != null) mBeepManager.updatePrefs();

        mScannerViewHandler = null;

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }
    }

    public void onPause() {
        if (mScannerViewHandler != null) {
            mScannerViewHandler.quitSynchronously();
            mScannerViewHandler = null;
        }
        if (mBeepManager != null) mBeepManager.close();
        mCameraManager.closeDriver();
        mViewfinderView.laserLineBitmapRecycle();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            mCameraManager.setTorch(lightMode);
            // Creating the mScannerViewHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mScannerViewHandler == null) {
                mScannerViewHandler = new ScannerViewHandler(this, mScannerOptions.getDecodeFormats(), mCameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        //扫描成功
        if (mScannerCompletionListener != null) {
            //转换结果
            mScannerCompletionListener.OnScannerCompletion(rawResult, Scanner.parseResult(rawResult), barcode);
        }
        //设置扫描结果图片
        if (barcode != null) {
            mViewfinderView.drawResultBitmap(barcode);
        }

        if (mScannerOptions.getMediaResId() != 0) {
            if (mBeepManager == null) {
                mBeepManager = new BeepManager(getContext());
                mBeepManager.setMediaResId(mScannerOptions.getMediaResId());
            }
            mBeepManager.playBeepSoundAndVibrate();
            if (barcode != null)
                drawResultPoints(barcode, scaleFactor, rawResult);
        }
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of
     * the barcode.
     *
     * @param barcode     A bitmap of the captured image.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param rawResult   The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(Scanner.color.RESULT_POINTS);
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4
                    && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                // Hacky special case -- draw two lines, for the barcode and
                // metadata
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder == null) {
//            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
//        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        hasSurface = false;
        if (!hasSurface && surfaceHolder != null) {
            surfaceHolder.removeCallback(this);
        }
    }

    /**
     * 设置扫描成功监听器
     *
     * @param listener
     * @return
     */
    public ScannerView setOnScannerCompletionListener(OnScannerCompletionListener listener) {
        this.mScannerCompletionListener = listener;
        return this;
    }

    public void setScannerOptions(ScannerOptions scannerOptions) {
        this.mScannerOptions = scannerOptions;
    }

    /**
     * 切换闪光灯
     *
     * @param mode true开；false关
     */
    public ScannerView toggleLight(boolean mode) {
        this.lightMode = mode;
        if (mCameraManager != null) mCameraManager.setTorch(lightMode);
        return this;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS 毫秒
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mScannerViewHandler != null)
            mScannerViewHandler.sendEmptyMessageDelayed(Scanner.RESTART_PREVIEW, delayMS);
    }

    /**
     * 设置扫描线颜色
     *
     * @param color
     */
    @Deprecated
    public ScannerView setLaserColor(int color) {
        mScannerOptionsBuilder.setLaserStyle(ScannerOptions.LaserStyle.COLOR_LINE, color);
        return this;
    }

    /**
     * 设置线形扫描线资源
     *
     * @param resId resId
     */
    @Deprecated
    public ScannerView setLaserLineResId(int resId) {
        mScannerOptionsBuilder.setLaserStyle(ScannerOptions.LaserStyle.RES_LINE, resId);
        return this;
    }

    /**
     * 设置网格扫描线资源
     *
     * @param resId resId
     */
    @Deprecated
    public ScannerView setLaserGridLineResId(int resId) {
        mScannerOptionsBuilder.setLaserStyle(ScannerOptions.LaserStyle.RES_GRID, resId);
        return this;
    }

    /**
     * 设置扫描线高度
     *
     * @param height dp
     */
    @Deprecated
    public ScannerView setLaserLineHeight(int height) {
        mScannerOptionsBuilder.setLaserLineHeight(height);
        return this;
    }

    /**
     * 设置扫描框4角颜色
     *
     * @param color
     */
    @Deprecated
    public ScannerView setLaserFrameBoundColor(int color) {
        mScannerOptionsBuilder.setFrameCornerColor(color);
        return this;
    }

    /**
     * 设置扫描框4角长度
     *
     * @param length dp
     */
    @Deprecated
    public ScannerView setLaserFrameCornerLength(int length) {
        mScannerOptionsBuilder.setFrameCornerLength(length);
        return this;
    }

    /**
     * 设置扫描框4角宽度
     *
     * @param width dp
     */
    @Deprecated
    public ScannerView setLaserFrameCornerWidth(int width) {
        mScannerOptionsBuilder.setFrameCornerWidth(width);
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     */
    @Deprecated
    public ScannerView setDrawTextColor(int color) {
        mScannerOptionsBuilder.setTipTextColor(color);
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param size 文字大小 sp
     */
    @Deprecated
    public ScannerView setDrawTextSize(int size) {
        mScannerOptionsBuilder.setTipTextSize(size);
        return this;
    }

    /**
     * 设置文字
     *
     * @param text
     * @param bottom 是否在扫描框下方
     */
    @Deprecated
    public ScannerView setDrawText(String text, boolean bottom) {
        mScannerOptionsBuilder.setTipText(text);
        mScannerOptionsBuilder.setTipTextToFrameTop(!bottom);
        return this;
    }

    /**
     * 设置文字
     *
     * @param text
     * @param bottom 是否在扫描框下方
     * @param margin 离扫描框间距 dp
     */
    @Deprecated
    public ScannerView setDrawText(String text, boolean bottom, int margin) {
        mScannerOptionsBuilder.setTipText(text);
        mScannerOptionsBuilder.setTipTextToFrameTop(!bottom);
        mScannerOptionsBuilder.setTipTextToFrameMargin(margin);
        return this;
    }

    /**
     * 设置文字
     *
     * @param text
     * @param size   文字大小 sp
     * @param color  文字颜色
     * @param bottom 是否在扫描框下方
     * @param margin 离扫描框间距 dp
     */
    @Deprecated
    public ScannerView setDrawText(String text, int size, int color, boolean bottom, int margin) {
        mScannerOptionsBuilder.setTipText(text);
        mScannerOptionsBuilder.setTipTextSize(size);
        mScannerOptionsBuilder.setTipTextColor(color);
        mScannerOptionsBuilder.setTipTextToFrameTop(!bottom);
        mScannerOptionsBuilder.setTipTextToFrameMargin(margin);
        return this;
    }

    /**
     * 设置扫描完成播放声音
     *
     * @param resId
     */
    @Deprecated
    public ScannerView setMediaResId(int resId) {
        mScannerOptionsBuilder.setMediaResId(resId);
        return this;
    }

    /**
     * 设置扫描框大小
     *
     * @param width  dp
     * @param height dp
     */
    @Deprecated
    public ScannerView setLaserFrameSize(int width, int height) {
        mScannerOptionsBuilder.setFrameSize(width, height);
        return this;
    }

    /**
     * 设置扫描框与屏幕距离
     *
     * @param margin
     */
    @Deprecated
    public ScannerView setLaserFrameTopMargin(int margin) {
        mScannerOptionsBuilder.setFrameTopMargin(margin);
        return this;
    }

    /**
     * 设置扫描解码类型（二维码、一维码、商品条码）
     *
     * @param scanMode {@linkplain Scanner.ScanMode mode}
     * @return
     */
    @Deprecated
    public ScannerView setScanMode(String scanMode) {
        mScannerOptionsBuilder.setScanMode(scanMode);
        return this;
    }

    /**
     * 设置扫描解码类型
     *
     * @param barcodeFormat
     * @return
     */
    @Deprecated
    public ScannerView setScanMode(BarcodeFormat... barcodeFormat) {
        mScannerOptionsBuilder.setScanMode(barcodeFormat);
        return this;
    }

    /**
     * 是否显示扫描结果缩略图
     *
     * @param showResThumbnail
     * @return
     */
    @Deprecated
    public ScannerView isShowResThumbnail(boolean showResThumbnail) {
        mScannerOptionsBuilder.setCreateQrThumbnail(showResThumbnail);
        return this;
    }

    /**
     * 设置扫描框线移动间距，每毫秒移动 moveSpeed 像素
     *
     * @param moveSpeed px
     * @return
     */
    @Deprecated
    public ScannerView setLaserMoveSpeed(int moveSpeed) {
        mScannerOptionsBuilder.setLaserMoveSpeed(moveSpeed);
        return this;
    }

    /**
     * 设置扫描摄像头，默认后置
     *
     * @param cameraFacing
     * @return
     */
    @Deprecated
    public ScannerView setCameraFacing(CameraFacing cameraFacing) {
        mScannerOptionsBuilder.setCameraFacing(cameraFacing);
        return this;
    }

    /**
     * 是否全屏扫描
     *
     * @param scanFullScreen
     * @return
     */
    @Deprecated
    public ScannerView isScanFullScreen(boolean scanFullScreen) {
        mScannerOptionsBuilder.setScanFullScreen(scanFullScreen);
        return this;
    }

    /**
     * 设置隐藏取景视图，包括文字
     *
     * @param hide
     * @return
     */
    @Deprecated
    public ScannerView isHideLaserFrame(boolean hide) {
        mScannerOptionsBuilder.setViewfinderHide(hide);
        return this;
    }

    /**
     * 是否扫描反色二维码（黑底白码）
     *
     * @param invertScan
     * @return
     */
    @Deprecated
    public ScannerView isScanInvert(boolean invertScan) {
        mScannerOptionsBuilder.setScanInvert(invertScan);
        return this;
    }

    ScannerOptions getScannerOptions() {
        return mScannerOptions;
    }

    void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }
}
