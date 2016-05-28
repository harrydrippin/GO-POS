package com.powerranger.go_pos.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.powerranger.go_pos.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by harryhong on 16. 5. 28..
 */
public class OrderActivity extends Activity implements TextWatcher, View.OnClickListener {

    EditText table_num;
    EditText kong, sam, spam, corn, so, hae, soju;
    Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        table_num = (EditText)findViewById(R.id.order_tablenum);

        kong = (EditText)findViewById(R.id.order_food_kong); // 10000
        sam = (EditText)findViewById(R.id.order_food_sam);   // 10000
        spam = (EditText)findViewById(R.id.order_food_spam); // 9000
        corn = (EditText)findViewById(R.id.order_food_corn); // 7000
        so = (EditText)findViewById(R.id.order_food_so);     // 9000
        hae = (EditText)findViewById(R.id.order_food_hae);   // 10000
        soju = (EditText)findViewById(R.id.order_food_soju); // 3000

        submit = (Button)findViewById(R.id.order_submit);

        kong.addTextChangedListener(this);
        sam.addTextChangedListener(this);
        spam.addTextChangedListener(this);
        corn.addTextChangedListener(this);
        so.addTextChangedListener(this);
        hae.addTextChangedListener(this);
        soju.addTextChangedListener(this);

        submit.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String[] captured = {kong.getText().toString(), sam.getText().toString(), spam.getText().toString(), corn.getText().toString(), so.getText().toString(), hae.getText().toString(), soju.getText().toString()};
        int[] price_array = {10000, 10000, 9000, 7000, 9000, 10000, 3000};
        int sum = 0;
        for(int i = 0 ; i < 7 ; i++) {
            if(captured[i] == null || captured[i].equals("")) {
                sum += 0;
            } else {
                sum += Integer.parseInt(captured[i]) * price_array[i];
            }
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        submit.setText(formatter.format(sum) + "원 결제 확인");

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.order_submit:
                OrderTask mTask = new OrderTask();
                mTask.execute();
                break;
        }
    }

    public class OrderTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mDialog;
        String num, menu, price;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(OrderActivity.this);
            mDialog.setMessage("데이터 로딩 중...");
            mDialog.show();

            num = table_num.getText().toString();
            menu = "콩나물제육볶음 1개\n삼겹살 꼬치 3개 1개\n소주 1개";
            price = "23000";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {
            final StringBuilder output;
            try {
                URL url = new URL("http://choms46.dothome.co.kr/order.php?table_num=" + num + "&menu=" + menu + "&price=" + price);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = "";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                return responseOutput.toString();
            } catch(Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }
}
