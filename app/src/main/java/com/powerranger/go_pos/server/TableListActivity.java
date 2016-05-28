package com.powerranger.go_pos.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.powerranger.go_pos.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by harryhong on 16. 5. 27..
 */

public class TableListActivity extends Activity {

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablelist);

        fab = (FloatingActionButton)findViewById(R.id.tablelist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(i);
            }
        });

        mListView = (ListView)findViewById(R.id.list_table);
        mAdapter = new ListViewAdapter(this);

        mListView.setAdapter(mAdapter);

        InfoTask mTableTask = new InfoTask();
        mTableTask.execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TableListData mData = mAdapter.mListData.get(position);
                Toast.makeText(TableListActivity.this, "아이템 클릭 검출, 테이블 번호 " + mData.getTableNumber(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class ViewHolder {
        public TextView tableNumber;
        public TextView menu;
        public TextView price;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<TableListData> mListData = null;

        public ListViewAdapter(Context m) {
            super();
            this.mContext = m;
            mListData = new ArrayList<>();
        }

        public void addItem(int tableNumber, String menu, int price) {
            TableListData mData = new TableListData();
            mData.setTableNumber(tableNumber);
            mData.setMenu(menu);
            mData.setPrice(price);

            mListData.add(mData);
        }

        public void sort() {
            Collections.sort(mListData, TableListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.item_tablelist, null);

                viewHolder.tableNumber = (TextView)convertView.findViewById(R.id.tablelist_item_tablenum);
                viewHolder.menu = (TextView)convertView.findViewById(R.id.tablelist_item_menu);
                viewHolder.price = (TextView)convertView.findViewById(R.id.tablelist_item_price);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            TableListData mData = mListData.get(position);
            viewHolder.tableNumber.setText(mData.getTableNumber() + "");
            viewHolder.menu.setText(mData.getMenu());
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            viewHolder.price.setText("합계 " + formatter.format(mData.getPrice()) + "원");

            return convertView;
        }
    }

    public class InfoTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(TableListActivity.this);
            mDialog.setMessage("데이터 로딩 중...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDialog.dismiss();
            Log.d("TAG", s);
            String[] result = s.split("\n");
            for(String i : result) {
                String[] temp = i.split("!");
                temp[1] = temp[1].replace(":", "\n");
                mAdapter.addItem(Integer.parseInt(temp[0]), temp[1], Integer.parseInt(temp[2]));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            final StringBuilder output;
                try {
                    URL url = new URL("http://choms46.dothome.co.kr/infomation.php");
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