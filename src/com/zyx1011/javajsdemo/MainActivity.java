package com.zyx1011.javajsdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	final private static String EXTRA_IMAGE_INDEX = "image_index";
	final private static String EXTRA_IMAGE_URLS = "image_urls";

	private ProgressBar mProgressBar;
	private EditText mEditText;
	private View mTitle;
	private Button mButton;
	private WebView mWebview;
	private CheckBox mCheckBox;
	private Button mBtnPre;
	private Button mBtnNext;
	private ArrayList<String> mHttps;
	private int mPosution;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		init();
		initData();
		initEvent();
	}

	private void initView() {
		mWebview = (WebView) findViewById(R.id.web_view);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		mEditText = (EditText) findViewById(R.id.edit_text);
		mTitle = findViewById(R.id.title);
		mButton = (Button) findViewById(R.id.button);
		mCheckBox = (CheckBox) findViewById(R.id.check_box);
		mBtnPre = (Button) findViewById(R.id.btn_pre);
		mBtnNext = (Button) findViewById(R.id.btn_next);
	}

	private void initEvent() {
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("http://jscalljava")) {
					// System.out.println(url);
					Uri uri = Uri.parse(url);
					String imgPosition = uri.getQueryParameter("imgPosition");
					String allUrls = uri.getQueryParameter("allUrls");
					// System.out.println(imgPosition + "");
					openImage(imgPosition, allUrls);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				injectJavaScript();
				// toast("º”‘ÿÕÍ≥…£°");

				if (!mHttps.contains(url)) {
					mHttps.add(url);
					mPosution = mHttps.size() - 1;
				} else {
					for (int i = 0; i < mHttps.size(); i++) {
						if (mHttps.get(i).equals(url)) {
							mPosution = i;
							break;
						}
					}
				}

				if (mPosution > 0 && mHttps.contains(url)) {
					mBtnPre.setVisibility(View.VISIBLE);
				} else {
					mBtnPre.setVisibility(View.GONE);
				}

				if (mPosution < mHttps.size() - 1 && mHttps.contains(url)) {
					mBtnNext.setVisibility(View.VISIBLE);
				} else {
					mBtnNext.setVisibility(View.GONE);
				}

				toast("s: " + mHttps.size() + "/" + "p:" + mPosution);

				mEditText.setText("");
				mEditText.setText(url);
			}
		});
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
				hide();
			}
		});
		mBtnPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPosution >= 1) {
					mWebview.loadUrl(mHttps.get(mPosution - 1));
				}
			}
		});
		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPosution < mHttps.size()) {
					mWebview.loadUrl(mHttps.get(mPosution + 1));
				}
			}
		});
	}

	protected void hide() {
		mProgressBar.setVisibility(View.VISIBLE);
		mWebview.setVisibility(View.GONE);
		mTitle.setVisibility(View.GONE);
	}

	protected void openImage(String imgPosition, String allUrls) {
		String[] urls = allUrls.split(";");
		ArrayList<String> urlDatas = new ArrayList<String>(Arrays.asList(urls));
		int pos = Integer.parseInt(imgPosition);
		imageBrower(pos, urlDatas);
	}

	private void imageBrower(int pos, ArrayList<String> urlDatas) {
		try {
			Intent intent = new Intent("com.zyx1011.javajademo.image.Browser");
			intent.putExtra(EXTRA_IMAGE_URLS, urlDatas);
			intent.putExtra(EXTRA_IMAGE_INDEX, pos);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void injectJavaScript() {
		BufferedReader br = null;
		try {
			if (mCheckBox.isChecked()) {
				br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("js.txt"), "utf-8"));
				String js, jsMethod = "";
				while ((js = br.readLine()) != null) {
					jsMethod += js;
				}
				mWebview.loadUrl(jsMethod);
			}

			show();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					br = null;
				}
			}
		}
	}

	private void show() {
		mProgressBar.setVisibility(View.GONE);
		mWebview.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
	}

	private void initData() {
		String http = mEditText.getText().toString().trim();
		mWebview.loadUrl(http);
		// mWebview.loadUrl("http://news.qq.com/original/tuhua/oldpicture.html");
	}

	private void init() {
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDisplayZoomControls(true);
		settings.setUseWideViewPort(true);
		mHttps = new ArrayList<String>();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHttps.clear();
	}

	private void toast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}

}
