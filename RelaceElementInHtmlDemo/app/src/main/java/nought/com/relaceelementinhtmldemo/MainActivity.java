package nought.com.relaceelementinhtmldemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout rootView;
    private WebView webView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (FrameLayout) findViewById(R.id.root_view);
        initView();
        initWebView();
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("hhe", "onPageFinished");
                super.onPageFinished(view, url);
                view.loadUrl("javascript:jsFun.measureImagePlaceHolder();");
            }
        });

        webView.addJavascriptInterface(new JavaFun(), "JavaFun");
        webView.loadUrl("file:///android_asset/test.html");
    }

    private void initView() {
        webView = new WebView(this);
        imageView = new ImageView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(webView, layoutParams);
    }


    class JavaFun {

        @JavascriptInterface
        public void replaceImgWithImageView(int left, int top, int width, int height) {
            final Context context = MainActivity.this.getApplicationContext();
            if (imageView == null) {
                imageView = new ImageView(MainActivity.this);
            }
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp2px(context, width), dp2px(context, height));
            params.leftMargin = dp2px(context, left);
            params.topMargin = dp2px(context, top);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    rootView.addView(imageView, params);
                    imageView.setBackgroundColor(Color.WHITE);
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.shepherd));
                }
            });
        }
    }

    public static int dp2px(Context context, float dpVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }
}
