package com.limxing.callinglock;

import android.content.Intent;
import android.os.Bundle;
//import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by limxing on 2015/6/7.
 */
public class CountActivity extends ActionBarActivity {
    private TextView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        share = (TextView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setType("text/plain");
                intent.putExtra(
                        Intent.EXTRA_TEXT,
                        getResources().getString(R.string.sharecontent) +
                                getSharedPreferences("info", MODE_PRIVATE).getInt("count", 0) + getResources()
                                .getString(R.string.sharecontent1)
                                + "https://play.google.com/store/apps/details?id=com.limxing.callinglock"
                                );
                startActivity(intent);
            }
        });
        findViewById(R.id.iv_count_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
        super.onBackPressed();
    }
}
