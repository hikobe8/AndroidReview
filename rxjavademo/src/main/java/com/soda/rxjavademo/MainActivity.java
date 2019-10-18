package com.soda.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "rxjavademo.MainActivity";
    Observable<String> mSwitcher;
    Observer<String> mLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //标准方式生成被观察者(观察源)
        mSwitcher = Observable.create(new ObservableOnSubscribe<String>(){

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("On");
                emitter.onNext("Off");
                emitter.onComplete(); //下游接收一次compete/error事件之后就不再执行onNext方法
                //onComplete方法在下游只会响应一次,所以多次调用并无意义
//                emitter.onError(new RuntimeException("test exception"));
                emitter.onNext("On");
                emitter.onNext("off");
            }
        });
        //创建被观察者的简便方式,使用just,还可以使用数组加fromArray操作符
//        mSwitcher = Observable.just("on", "off", "on");
        mLight = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext : " + s);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError : " + t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
    }

    public void switchLight(View view) {
        mSwitcher.subscribe(mLight);
    }

    public void useFlowable(View view) {
        Intent intent = new Intent(this, FlowableActivity.class);
        startActivity(intent);
    }

}
