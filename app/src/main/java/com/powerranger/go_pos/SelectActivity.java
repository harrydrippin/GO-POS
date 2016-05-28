package com.powerranger.go_pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.powerranger.go_pos.cook.OrderListActivity;
import com.powerranger.go_pos.server.TableListActivity;

/**
 * Created by harryhong on 16. 5. 27..
 */
public class SelectActivity extends Activity implements View.OnClickListener {

    Button server, cook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        server = (Button)findViewById(R.id.btn_select_server);
        cook   = (Button)findViewById(R.id.btn_select_cook);

        server.setOnClickListener(this);
        cook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_select_server:
                Intent i = new Intent(getApplicationContext(), TableListActivity.class);
                startActivity(i);
                break;
            case R.id.btn_select_cook:
                Intent j = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(j);
                break;
        }
    }
}