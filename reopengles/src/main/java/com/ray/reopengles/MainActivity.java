package com.ray.reopengles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ray.reopengles.simplegraph.GraphOptionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clickSimpleGraph(View view) {
        GraphOptionActivity.launch(this);
    }
}
