package com.zzia.rxjavademo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zzia.rxjavademo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends BaseActivity {
    /**
     * rxjava的各种使用
     */
    private static final String TAG = "zziafyc";
    @Bind(R.id.goLocalServer)
    Button goLocalBtn;
    @Bind(R.id.goOssServer)
    Button goOssBtn;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewsAndEvents() {
        AllRxjavaUse();
    }

    private void AllRxjavaUse() {
        basicUse();
        fromUse();
        justUse();
        intervalUse();
        rangeUse();
        filter();
        mapUse();
        fromFlatMapUse();
        justFlatMapUse();
        goLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalServerFileUploadActivity.class);
                startActivity(intent);
            }
        });
        goOssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OssServerFileUploadActivity.class);
                startActivity(intent);
            }
        });


    }

    private void basicUse() {
        //创建观察者
        Subscriber subscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "basicUse: " + s);

            }
        };
        //创建被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("第一个");
                subscriber.onNext("第二个");
                subscriber.onNext("第三个");
                subscriber.onCompleted();
            }

        });
        observable.subscribe(subscriber);
    }

    public void fromUse() {
        //创建观察者
        Subscriber subscriber = new Subscriber<UserModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserModel user) {
                Log.e(TAG, "fromUse: " + user.getName() + "," + user.getSex());

            }
        };
        String[] words = {"hello", "rxjava"};
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("boy", "男"));
        list.add(new UserModel("girl", "女"));
        Observable observable = Observable.from(list);
        observable.subscribe(subscriber);
    }

    public void justUse() {
        //创建观察者
        Subscriber subscriber = new Subscriber<List<UserModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<UserModel> users) {
                for (UserModel model : users) {
                    Log.e(TAG, "justUse: " + model.getName() + "," + model.getSex());
                }

            }
        };
        String[] words = {"hello", "rxjava"};
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("boy", "男"));
        list.add(new UserModel("girl", "女"));
        Observable observable = Observable.just(list);
        observable.subscribe(subscriber);
    }

    private void intervalUse() {
        //interval的用法,定时器的
        Observable observable = Observable.interval(2, TimeUnit.SECONDS);
        observable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long s) {
                Log.e(TAG, "intervalUse: " + s.intValue());

            }
        });
    }

    private void rangeUse() {
        //range的用法
        Observable observable = Observable.range(1, 10).repeat(2);
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "rangeUse: " + integer.intValue());
            }
        });
    }

    public void filter() {
        Observable.just(1, 2, 3, 4).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer i) {
                return i > 2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "filter:" + "i=" + integer);
            }
        });
    }

    private void mapUse() {
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("boy", "男"));
        list.add(new UserModel("girl", "女"));
        Observable observable = Observable.from(list);
        observable.map(new Func1<UserModel, String>() {
            @Override
            public String call(UserModel user) {
                return user.getName();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "mapUse: " + s);
            }
        });
    }

    private void justFlatMapUse() {
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("boy", "男"));
        list.add(new UserModel("girl", "女"));
        Observable observable = Observable.just(list);
        observable.flatMap(new Func1<List<UserModel>, Observable<?>>() {
            @Override
            public Observable<?> call(List<UserModel> users) {
                return Observable.from(users);
            }
        }).cast(UserModel.class).subscribe(new Action1<UserModel>() {
            @Override
            public void call(UserModel user) {
                Log.e(TAG, "justFlatMapUse: " + user.getName());

            }
        });
    }

    private void fromFlatMapUse() {
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("boy", "男"));
        list.add(new UserModel("girl", "女"));
        Observable observable = Observable.from(list);
        observable.flatMap(new Func1<UserModel, Observable<String>>() {
            @Override
            public Observable<String> call(UserModel user) {
                return Observable.just("名称：" + user.getName());
            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "fromFlatMapUse: " + s);

            }
        });
    }

}

