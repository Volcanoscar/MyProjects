package com.limxing.beijing.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.limxing.beijing.R;
import com.limxing.beijing.utils.BeiJingApi;

public class NewsActivity extends ActionBarActivity {
	private String url;
	private TextView txt_title;
	private ImageView imgbtn_text;
	private ImageView imgbtn_right;
	private WebView webView;
	private FrameLayout loading_view;
	private WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() != null) {
			url = getIntent().getStringExtra("url");
		}
		setContentView(R.layout.act_news_detail);

		initTile();
		webView = (WebView) findViewById(R.id.news_detail_wv);
		loading_view = (FrameLayout) findViewById(R.id.loading_view);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				loading_view.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		webSettings = webView.getSettings();
		webView.loadUrl(BeiJingApi.BASE_URL+url);

	}

	private void initTile() {
		Button btn_left = (Button) findViewById(R.id.btn_left);
		btn_left.setVisibility(View.GONE);
		ImageView imgbtn_left = (ImageView) findViewById(R.id.imgbtn_left);
		imgbtn_left.setVisibility(View.VISIBLE);
		imgbtn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		txt_title = (TextView) findViewById(R.id.txt_title);
		imgbtn_text = (ImageView) findViewById(R.id.imgbtn_text);
		imgbtn_right = (ImageView) findViewById(R.id.imgbtn_right);

		imgbtn_text.setVisibility(View.GONE);
		imgbtn_right.setImageResource(R.drawable.icon_textsize);
		imgbtn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				webSettings.setTextSize(TextSize.LARGER);
			}
		});
		imgbtn_right.setImageResource(R.drawable.icon_share);

	}

}
