package com.ray.opengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ray.opengl.basics.SimpleGraphActivity;
import com.ray.opengl.render.ColorfulTriangle;
import com.ray.opengl.render.Cone;
import com.ray.opengl.render.Cube;
import com.ray.opengl.render.Oval;
import com.ray.opengl.render.RightPolygon;
import com.ray.opengl.render.SimpleIsoscelesRightTriangle;
import com.ray.opengl.render.SimpleSquare;
import com.ray.opengl.render.SimpleTriangle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
