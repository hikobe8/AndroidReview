package com.soda.androidreview.singleinstancemode;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.soda.androidreview.BaseActivity;
import com.soda.androidreview.R;

import java.text.MessageFormat;

public class SingleInstanceCActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task_c);
        ((TextView)findViewById(R.id.tv)).setText(MessageFormat.format("task id : {0} {1}{2}", getTaskId(), getClass().getSimpleName(), hashCode()));
    }

    public void jump2BActivity(View view) {
        Intent intent = new Intent(this, SingleInstanceBActivity.class);
        startActivity(intent);
    }

}
