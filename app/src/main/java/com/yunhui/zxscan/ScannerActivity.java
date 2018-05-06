package com.yunhui.zxscan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.pengmin.zxing.OnScannerCompletionListener;
import com.pengmin.zxing.ScannerView;
import com.pengmin.zxing.result.AddressBookResult;
import com.pengmin.zxing.result.ProductResult;
import com.yunhui.R;
import com.yunhui.activity.BaseActionBarActivity;
import com.yunhui.util.StringUtil;

/**
 * Created by pengmin on 17/12/7.
 */

public class ScannerActivity extends BaseActionBarActivity implements OnScannerCompletionListener,View.OnClickListener {

    private ScannerView mScannerView;
    private Result mLastResult;
    private boolean showThumbnail = false;
    private static final String TAG = "ScannerActivity";
    private ProgressDialog progressDialog;
    private ImageView iv_ScannerBack;
    private LinearLayout titlehitescanner;
    private LinearLayout hitescanner;
    private TextView titleHiteText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initActivity(Bundle savedInstanceState) {

        setContentView(R.layout.activity_scanning);
        hideNavigationBar();
        titlehitescanner = (LinearLayout) findViewById(R.id.titlehitescanner);
        hitescanner = (LinearLayout) findViewById(R.id.hitescanner);
        titleHiteText = (TextView) findViewById(R.id.titleHiteText);
        if(StringUtil.isEmpty(getIntent().getStringExtra("topTitle"))){
            titlehitescanner.setVisibility(View.VISIBLE);
            hitescanner.setVisibility(View.GONE);
        }else{
            titlehitescanner.setVisibility(View.GONE);
            hitescanner.setVisibility(View.VISIBLE);
            titleHiteText.setText(getIntent().getStringExtra("topTitle"));
        }
        mScannerView = (ScannerView) findViewById(R.id.scanner_view);
        iv_ScannerBack = (ImageView) findViewById(R.id.scannerBack);
        iv_ScannerBack.setOnClickListener(this);
        mScannerView.setOnScannerCompletionListener(this);
        showThumbnail = false;
//        mScannerView.setMediaResId(R.raw.beep);//设置扫描成功的声音
        mScannerView.setDrawText("将条码放入框内, 即可以自动扫描", true);
        mScannerView.setDrawTextColor(getResources().getColor(R.color.AppTheThemeColor));
        //显示扫描成功后的缩略图
        mScannerView.isShowResThumbnail(showThumbnail);
        //全屏识别
        mScannerView.isScanFullScreen(false);
        //隐藏扫描框
        mScannerView.isHideLaserFrame(false);
//        mScannerView.isScanInvert(true);//扫描反色二维码
//        mScannerView.setCameraFacing(CameraFacing.FRONT);
//        mScannerView.setLaserMoveSpeed(1);//速度

//        mScannerView.setFrameTopMargin(100);//扫描框与屏幕上方距离
//        mScannerView.setFrameSize(400, 400);//扫描框大小
//        mScannerView.setFrameCornerLength(25);//设置4角长度
//        mScannerView.setLaserLineHeight(5);//设置扫描线高度
//        mScannerView.setFrameCornerWidth(5);
        mScannerView.setLaserLineResId(R.mipmap.wx_scan_line);//线图
        mScannerView.setLaserFrameBoundColor(getResources().getColor(R.color.AppTheThemeColor));
    }



    @Override
    protected void onResume() {
        mScannerView.onResume();
        resetStatusView();
        super.onResume();

    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (mLastResult != null) {
            restartPreviewAfterDelay(0L);
        }

        finish();

    }

    private void resetStatusView() {
        mLastResult = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mLastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void restartPreviewAfterDelay(long delayMS) {
        mScannerView.restartPreviewAfterDelay(delayMS);
        resetStatusView();
    }


    @Override
    public void OnScannerCompletion(final Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        if (rawResult == null) {
            Toast.makeText(this, "未发现二维码", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        final Bundle bundle = new Bundle();
        final ParsedResultType type = parsedResult.getType();
        Log.i(TAG, "ParsedResultType: " + type);
        switch (type) {
            case ADDRESSBOOK:
                AddressBookParsedResult addressBook = (AddressBookParsedResult) parsedResult;
                bundle.putSerializable("SNCode", new AddressBookResult(addressBook));
                bundle.putSerializable("type",ScannerEnum.ADDRESSBOOK);
                break;
            case PRODUCT:
                ProductParsedResult product = (ProductParsedResult) parsedResult;
                Log.i(TAG, "productID: " + product.getProductID());
                bundle.putSerializable("SNCode", new ProductResult(product));
                bundle.putSerializable("type",ScannerEnum.PRODUCT);
                break;
            case ISBN:
                ISBNParsedResult isbn = (ISBNParsedResult) parsedResult;
                Log.i(TAG, "isbn: " + isbn.getISBN());
                bundle.putString("SNCode",isbn.getISBN());
                bundle.putSerializable("type",ScannerEnum.ISBN);
                break;
            case URI:
                URIParsedResult uri = (URIParsedResult) parsedResult;
                Log.i(TAG, "uri: " + uri.getURI());
                bundle.putString("SNCode",uri.getURI());
                bundle.putSerializable("type",ScannerEnum.URI);
                break;
            case TEXT:
                TextParsedResult textParsedResult = (TextParsedResult) parsedResult;
                bundle.putString("SNCode", textParsedResult.getText());
                bundle.putSerializable("type",ScannerEnum.TEXT);
                break;
            case GEO:
                break;
            case TEL:
                break;
            case SMS:
                break;
        }
        showProgressDialog();
        if (showThumbnail) {
            onResultActivity(bundle);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResultActivity(bundle);
                }
            }, 3 * 1000);
        }
    }


    private void onResultActivity(Bundle bundle){
        dismissProgressDialog();
        Intent intent = getIntent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

    void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
    }

    void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
