//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.arcsoft.face;

import android.graphics.Rect;

public class FaceInfo {
    Rect rect;
    int orient;

    public FaceInfo(Rect rect, int orient) {
        this.rect = new Rect(rect);
        this.orient = orient;
    }

    public FaceInfo(FaceInfo obj) {
        if (obj == null) {
            this.rect = new Rect();
            this.orient = 0;
        } else {
            this.rect = new Rect(obj.getRect());
            this.orient = obj.getOrient();
        }

    }

    public FaceInfo() {
        this.rect = new Rect();
        this.orient = 0;
    }

    public Rect getRect() {
        return this.rect;
    }

    public int getOrient() {
        return this.orient;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setOrient(int orient) {
        this.orient = orient;
    }

    public String toString() {
        return this.rect.toString() + "," + this.orient;
    }

    public FaceInfo clone() {
        return new FaceInfo(this);
    }
}
