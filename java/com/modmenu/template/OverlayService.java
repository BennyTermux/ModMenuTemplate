package com.modmenu.template;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private ModMenuPanel floatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createFloatingView();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("modmenu_channel", "Mod Menu Overlay", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    private void createFloatingView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = (ModMenuPanel) LayoutInflater.from(this).inflate(R.layout.mod_menu_panel, null);
        floatingView.makeDraggable(floatingView.findViewById(R.id.title_bar));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 100;

        windowManager.addView(floatingView, params);

        floatingView.findViewById(R.id.btn_close).setOnClickListener(v -> stopSelf());
        floatingView.findViewById(R.id.btn_minimize).setOnClickListener(v -> {
            floatingView.setVisibility(View.GONE);
            // Re-show logic can be added later
        });

        startForeground(1, new Notification.Builder(this, "modmenu_channel")
                .setContentTitle("Mod Menu Running")
                .setContentText("Background service active")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build());
    }

    @Override
    public void onDestroy() {
        if (floatingView != null) windowManager.removeView(floatingView);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
              }
