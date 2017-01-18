package com.tencent.qqlive.okhttptest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Handler mHandler = new Handler();
    private OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView)findViewById(R.id.tv_result);

        findViewById(R.id.btn_send_get_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("同步请求中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request.Builder()
                                    .url("http://www.baidu.com")
                                    .build();
                            final Response response = httpClient.newCall(request).execute();
                            if (response.isSuccessful()) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvResult.setText(response.toString());
                                    }
                                });
                            } else {
                                throw new IOException("Unexpected code " + response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.btn_send_get_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("异步请求中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request.Builder()
                                    .url("http://www.baidu.com")
                                    .build();
                            httpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvResult.setText(response.toString());
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
