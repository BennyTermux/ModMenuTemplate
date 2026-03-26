package com.modmenu.template;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ModMenuPanel extends LinearLayout {
    private float dX, dY;

    public ModMenuPanel(Context context) {
        super(context);
    }

    public void makeDraggable(View titleBar) {
        titleBar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = event.getRawX() - getX();
                    dY = event.getRawY() - getY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setX(event.getRawX() - dX);
                    setY(event.getRawY() - dY);
                    return true;
            }
            return false;
        });
    }
}
