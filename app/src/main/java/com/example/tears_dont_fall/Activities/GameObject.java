package com.example.tears_dont_fall.Activities;

import android.graphics.drawable.VectorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameObject {

    private int pos = 1;
    private int maxLane;

    private int sep;
    private View[] connectedViews;

    public GameObject(int maxLane, int SEP, View[] connectedViews){
        this.maxLane = maxLane;
        sep = SEP;
        this.connectedViews = connectedViews;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
        setView(pos);
    }

    public void setView(int idx){
        for (int i = 0; i < connectedViews.length; i++) {
            connectedViews[i].setVisibility((i == idx) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setView(int idx1, int idx2){
        for (int i = 0; i < connectedViews.length; i++) {
            connectedViews[i].setVisibility((i == idx1 || i == idx2) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void modPos(int direction){
        if (pos + direction >= 0 && pos + direction < maxLane){
            this.pos += direction;
        }

        setView(pos);
    }

    public void switchViews(View[] iv){
        this.connectedViews = iv;

        setView(getPos());
    }

    public View[] getConnectedViews() {
        return connectedViews;
    }
}
