package com.soda.androidreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.soda.androidreview.singleinstancemode.SingleInstanceAActivity;
import com.soda.androidreview.standardmode.StandardMainActivity;

public class MainActivity extends BaseActivity {

    Intent mIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * activity的默认启动模式，采用此模式都是新建activity,压入task栈
     * 如果app,应用过多的话，可能会引起oom,毕竟这样每次都会有创建心新对象的操作
     * @param view
     */
    public void launch2StandardMode(View view) {
        mIntent.setClass(this, StandardMainActivity.class);
        startActivity(mIntent);
    }

    /**
     * SingleTop模式是判断启动的activity是否位于栈顶，如果是的话，
     * 将会服用栈顶的此activity，不会存在新建activity压栈的操作．
     * 如果不在栈顶的话，会跟standard模式一样，新建activity并压栈．
     * 应用场景可以用于消息页面，点击几十个通知栏消息提醒不会创建几十个页面，而是复用，达到性能优化目的
     * @param view
     */
    public void launch2SingleTopMode(View view) {

    }

    /**
     * SingleTask模式是判断启动的activity是否存在于栈中，如果存在的话，
     * 移除此activity上面的其他activity,提升至栈顶，多用于应用的主页面，保证页面的单一性.
     *
     * taskAffinity activity与task的亲和关系
     * 1.当一个activity在manifest中设置allowTaskReparenting=true后，如果与启动的activity的taskAffinity相同的话，
     * 当前activity会被加入到启动的activity的task中去.
     * 2.如果加载某个Activity的intent，Flag被设置成FLAG_ACTIVITY_NEW_TASK时，它会首先检查是否存在与自己taskAffinity相同的Task，
     * 如果存在，那么它会直接宿主到该Task中，如果不存在则重新创建Task。
     * 当一个应用程序加载一个singleTask模式的Activity时，首先该Activity会检查是否存在与它的taskAffinity相同的Task。

     * 1、如果存在，那么检查是否实例化，如果已经实例化，那么销毁在该Activity以上的Activity并调用onNewIntent。如果没有实例化，那么该Activity实例化并入栈。

     * 2、如果不存在，那么就重新创建Task，并入栈。
     * @param view
     */
    public void launch2SingleTaskMode(View view) {

    }

    /**
     * SingleInstance是为activity提供一个单独的task栈，并且该栈只会有此activity,可以通过Activity.getTaskId()验证，
     * 这个模式的话需要谨慎使用，不然会有很奇怪的效果，比如这个例子的效果，main, a, c 的模式都为standard, 所以他们三个
     * 为同一个task1栈，b 使用了SingleInstance模式，所以通过a启动b的时候会创建一个全新的只包含一个b实例的task2栈，
     * 所以现在就会有两个task栈，在b启动c，又会进入之前的task1栈，这时在c点击返回的时候先是根据栈FILO的特点，
     * 销毁c,a,main,最后会弹出b,这样就很奇怪，home页面(main)都销毁了，还弹出b,这就是SingleInstance的原因，
     * 还有一种就是　A（home页面） -> B (singleInstance) -> C，完全退出后，再次启动，首先打开的是B。
     * 所以不要尽量使用这个模式在中间页面．一般用于单独的设置页面，一般都为单页面，不会跳转的那种比如，设置个人信息，设置推送消息
     * @param view
     */
    public void launch2SingleInstanceMode(View view) {
        mIntent.setClass(this, SingleInstanceAActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
