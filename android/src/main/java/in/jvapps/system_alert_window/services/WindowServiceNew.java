package in.jvapps.system_alert_window.services;

import static in.jvapps.system_alert_window.utils.Constants.INTENT_EXTRA_PARAMS_MAP;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import in.jvapps.system_alert_window.R;
import in.jvapps.system_alert_window.SystemAlertWindowPlugin;
import in.jvapps.system_alert_window.utils.Constants;
import in.jvapps.system_alert_window.utils.LogUtils;

public class WindowServiceNew extends Service {

    private static final String TAG = WindowServiceNew.class.getSimpleName();
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int WINDOW_VIEW_ID = 1947;
    public static final String INTENT_EXTRA_IS_CLOSE_WINDOW = "IsCloseWindow";

    private WindowManager wm;
    private View inflatedView;
    HashMap<String, Object> extraData;

    private Context mContext = this;


    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onCreate() {
        mContext = this;
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, SystemAlertWindowPlugin.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Overlay window service is running")
                .setSmallIcon(R.drawable.ic_desktop_windows_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        if (null != intent && intent.getExtras() != null) {
            mContext = this;
            LogUtils.getInstance().setContext(this.mContext);
            @SuppressWarnings("unchecked")
            HashMap<String, Object> paramsMap = (HashMap<String, Object>) intent.getSerializableExtra(Constants.INTENT_EXTRA_PARAMS_MAP);
            boolean isCloseWindow = intent.getBooleanExtra(INTENT_EXTRA_IS_CLOSE_WINDOW, false);
            extraData = (HashMap<String, Object>) intent.getSerializableExtra(INTENT_EXTRA_PARAMS_MAP);
            if (!isCloseWindow) {
                assert paramsMap != null;
                createWindow(paramsMap);
            } else {
                closeWindow(true);
            }
        }
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void setWindowManager() {
        if (wm == null) {
            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
    }

    private WindowManager.LayoutParams getLayoutParams() {
        final WindowManager.LayoutParams params;
        params = new WindowManager.LayoutParams();
        params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            params.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        params.gravity = Gravity.CENTER;
        return params;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setWindowView(WindowManager.LayoutParams params, boolean isCreate) {
        if (isCreate) {
            inflatedView = new View(mContext);
            inflatedView.setId(WINDOW_VIEW_ID);
        }
        inflatedView = LayoutInflater.from(mContext).inflate(R.layout.attention_seeker_activity, null);
        inflatedView.setLayoutParams(     // THIS!
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        inflatedView.findViewById(R.id.dismissButton).setOnClickListener(view -> closeWindow(true));
        TextView attentionMessage = inflatedView.findViewById(R.id.attentionMessage);
        ImageView profileImage = inflatedView.findViewById(R.id.profileImage);
        String nickName = (String) extraData.get("nickName");
        if (nickName != null && nickName.length() > 0) {
            String message = nickName + " has requested attention";
            attentionMessage.setText(message);
        }
        String imageUrl = (String) extraData.get("profileImageUrl");
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImage);
        }
    }

    private void createWindow(HashMap<String, Object> paramsMap) {
        closeWindow(false);
        setWindowManager();
        WindowManager.LayoutParams params = getLayoutParams();
        setWindowView(params, true);
        try {
            wm.addView(inflatedView, params);
        } catch (Exception ex) {
            LogUtils.getInstance().e(TAG, ex.toString());
            retryCreateWindow();
        }
    }

    private void retryCreateWindow() {
        try {
            closeWindow(false);
            setWindowManager();
            //setWindowLayoutFromMap(paramsMap);
            WindowManager.LayoutParams params = getLayoutParams();
            setWindowView(params, true);
            wm.addView(inflatedView, params);
        } catch (Exception ex) {
            LogUtils.getInstance().e(TAG, ex.toString());
        }
    }

    private void closeWindow(boolean isEverythingDone) {
        LogUtils.getInstance().i(TAG, "Closing the overlay window");
        try {
            if (wm != null) {
                if (inflatedView != null) {
                    wm.removeView(inflatedView);
                    inflatedView = null;
                    extraData = null;
                }
            }
            wm = null;
        } catch (IllegalArgumentException e) {
            LogUtils.getInstance().e(TAG, "view not found");
        }
        if (isEverythingDone) {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        LogUtils.getInstance().d(TAG, "Destroying the overlay window service");
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
