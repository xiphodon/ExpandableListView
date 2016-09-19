package com.qiyun.cyt.expandablelistview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FristActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);

        initUI();
        initData();
    }

    private void initData() {

    }

    private void initUI() {
        Button btn_Main = (Button) findViewById(R.id.btn_Main);
        Button btn_Main2 = (Button) findViewById(R.id.btn_Main2);

        btn_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FristActivity.this,MainActivity.class));
            }
        });

        btn_Main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FristActivity.this,Main2Activity.class));
            }
        });
    }
}
