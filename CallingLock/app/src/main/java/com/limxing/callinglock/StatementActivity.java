package com.limxing.callinglock;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by limxing on 2015/6/5.
 */
public class StatementActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        findViewById(R.id.iv_open_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                onBackPressed();
            }
        });
    }
}
