package com.whut.surfacemonitorproject_wjj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.whut.surfacemonitorproject_wjj.surfacemonitorservice.SurfaceMonitorService;
import com.whut.surfaceproject_wjj.R;

public class MainActivity extends Activity {

    private Button startServiceBtn;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext  = this;

        startServiceBtn = (Button) findViewById(R.id.btn_start_service);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startService(new Intent(mContext, SurfaceMonitorService.class));
            }
        });
    }
}
