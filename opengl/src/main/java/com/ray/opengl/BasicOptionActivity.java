package com.ray.opengl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ray.opengl.basics.SimpleGraphActivity;
import com.ray.opengl.render.geometry.Ball;
import com.ray.opengl.render.geometry.ColorfulTriangle;
import com.ray.opengl.render.geometry.Cone;
import com.ray.opengl.render.geometry.Cube;
import com.ray.opengl.render.geometry.Cylinder;
import com.ray.opengl.render.geometry.Oval;
import com.ray.opengl.render.geometry.RightPolygon;
import com.ray.opengl.render.geometry.SimpleIsoscelesRightTriangle;
import com.ray.opengl.render.geometry.SimpleSquare;
import com.ray.opengl.render.geometry.SimpleTriangle;

public class BasicOptionActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, BasicOptionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    public void simpleTriangleClick(View view) {
        SimpleGraphActivity.launch(this, SimpleTriangle.class);
    }

    public void simpleIsoscelesTriangleClick(View view) {
        SimpleGraphActivity.launch(this, SimpleIsoscelesRightTriangle.class);
    }

    public void colorfulTriangleClick(View view) {
        SimpleGraphActivity.launch(this, ColorfulTriangle.class);
    }

    public void squareClick(View view) {
        SimpleGraphActivity.launch(this, SimpleSquare.class);
    }

    public void polygonClick(View view) {
        SimpleGraphActivity.launch(this, RightPolygon.class);
    }

    public void cubeClick(View view) {
        SimpleGraphActivity.launch(this, Cube.class);
    }

    public void circleClick(View view) {
        SimpleGraphActivity.launch(this, Oval.class);
    }

    public void coneClick(View view) {
        SimpleGraphActivity.launch(this, Cone.class);
    }

    public void cylinderClick(View view) {
        SimpleGraphActivity.launch(this, Cylinder.class);
    }

    public void ballClick(View view) {
        SimpleGraphActivity.launch(this, Ball.class);
    }
}
