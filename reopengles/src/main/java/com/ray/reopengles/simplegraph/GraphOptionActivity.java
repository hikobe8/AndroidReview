package com.ray.reopengles.simplegraph;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ray.reopengles.R;

public class GraphOptionActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, GraphOptionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_option);
    }

    public void clickTriangle(View view) {
        SimpleGraphActivity.launch(this, 0);
    }

    public void clickSquare(View view) {
        SimpleGraphActivity.launch(this, 1);
    }
}
