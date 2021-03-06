package com.ray.reopengles;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.ray.reopengles.simplegraph.GraphOptionActivity;
import com.ray.reopengles.simplegraph.SimpleGraphActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clickSimpleGraph(View view) {
        GraphOptionActivity.launch(this);
    }

    public void clickTexture(View view) {
        SimpleGraphActivity.launch(this, 2);
    }

}
