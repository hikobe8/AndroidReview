package com.soda.androidreview.standardmode;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.soda.androidreview.BaseActivity;
import com.soda.androidreview.R;

import java.text.MessageFormat;

public class StandardMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_main);
        ((TextView)findViewById(R.id.tv)).setText(MessageFormat.format("{0}{1}", getClass().getSimpleName(), hashCode()));
    }

    public void launchMainActivity(View view) {
        Intent intent = new Intent(this, StandardMainActivity.class);
        startActivity(intent);
    }
}
