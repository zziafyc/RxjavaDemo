package com.zzia.rxjavademo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzia.rxjavademo.base.ApiClient;
import com.zzia.rxjavademo.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxTestActivity extends BaseActivity {

    /**
     * rxjava+retrofit配合使用效果
     */
    private static final String TAG = "RxTestActivity";
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.context)
    TextView contextTv;

    @Override
    public void initViewsAndEvents() {
        initViews();
        initListener();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_rx_test;
    }

    private void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }


    private void initViews() {

    }

    private void initData() {
        ApiClient.getApiService().getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> users) {
                        String content= users.get(0).getUserName()+","+users.get(0).getPassword();
                        Log.e(TAG, "onNext: " +content);
                        contextTv.setText("内容为：" + content);
                    }
                });
    }
}
