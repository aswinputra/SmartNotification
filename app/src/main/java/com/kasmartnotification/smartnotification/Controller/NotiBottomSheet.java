package com.kasmartnotification.smartnotification.Controller;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Adapter.NotificationAdapter;
import com.kasmartnotification.smartnotification.Adapter.SectionRecyclerViewAdapter;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.ImageDecoder;
import com.kasmartnotification.smartnotification.Interfaces.NotificationUpdateListener;
import com.kasmartnotification.smartnotification.Model.BlackListPackage;
import com.kasmartnotification.smartnotification.Model.Notification;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Model.Section;
import com.kasmartnotification.smartnotification.Model.Sections;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Utility;

/**
 * Created by kiman on 1/9/17.
 */

public class NotiBottomSheet implements NotificationUpdateListener, View.OnClickListener {
    private Context context;
    private Toolbar toolbar;
    private NotificationAdapter notificationAdapter;
    private SectionRecyclerViewAdapter sectionAdapter;
    private ImageButton closeBtn;
    private ImageButton clearAllBtn;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView zeroNotiTv;
    private RecyclerView notiRV;
    private int bottomSheetState;
    private ImageView expandHintIV;

    public NotiBottomSheet(Context context, Activity activity) {
        this.context = context;
        final AppBarLayout appBarLayout = activity.findViewById(R.id.activity_main_appbarlayout);
        LinearLayout mBottomSheet = activity.findViewById(R.id.notification_bottom_sheet_layout);
        toolbar = activity.findViewById(R.id.activity_main_toolbar);
        closeBtn = activity.findViewById(R.id.bottom_sheet_close);
        clearAllBtn = activity.findViewById(R.id.bottom_sheet_clear_all);
        zeroNotiTv = activity.findViewById(R.id.bottom_sheet_zero_noti);
        notiRV = activity.findViewById(R.id.bottom_sheet_recylerView);
        expandHintIV = activity.findViewById(R.id.notification_expand_hint_imageView);

        closeBtn.setOnClickListener(this);
        clearAllBtn.setOnClickListener(this);

        bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED;

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(Constants.BOTTOM_SHEET_COLLAPSE_HEIGHT);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                bottomSheetState = newState;
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    toolbar.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    closeBtn.setVisibility(View.INVISIBLE);
                    expandHintIV.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    toolbar.setVisibility(View.INVISIBLE);
                    appBarLayout.setVisibility(View.INVISIBLE);
                    closeBtn.setVisibility(View.INVISIBLE);
                    expandHintIV.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    toolbar.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.VISIBLE);
                    expandHintIV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        setUpRecyclerView(activity);

        //TODO: can we make it better?
        Utility.createOrSetDBObject(BlackListPackage.class, "android", null, null, null);
        Utility.createOrSetDBObject(BlackListPackage.class, context.getPackageName(), null, null, null);
    }

    private void setUpRecyclerView(Activity activity) {
        Notifications notifications = Notifications.getInstance();
        notifications.setListener(this);

        toggleNotificationsVisibility(notifications);

        notificationAdapter = new NotificationAdapter(context, notifications);
        notiRV.setLayoutManager(new LinearLayoutManager(context));

        Section[] sections = Sections.getInstance().getSectionArray();

        sectionAdapter = new SectionRecyclerViewAdapter(context, R.layout.adapter_section,
                R.id.adapter_section_textview, notificationAdapter);
        sectionAdapter.setSections(sections);
        notificationAdapter.setmSectionAdapter(sectionAdapter);

        notiRV.setAdapter(sectionAdapter);
    }

    @Override
    public void onNotiAdded(Notification notification) {
        update();
        if (notification.isImportant()) {
            notifyImportant(notification);
        }
    }

    @Override
    public void onNotiRemoveAll() {
        update();
    }

    @Override
    public void onRemove(Notification notification) {
        update();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_sheet_close: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            break;
            case R.id.bottom_sheet_clear_all: {
                Notifications notifications = Notifications.getInstance();
                notifications.removeAll();
            }
            break;
        }
    }

    public void update() {
        try {
            if (Sections.getInstance().update()) {
                notificationAdapter.notifyDataSetChanged();
                sectionAdapter.setSections(Sections.getInstance().getSectionArray());
                toggleNotificationsVisibility(Notifications.getInstance());
            }
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getMessage());
        }
    }

    private void toggleNotificationsVisibility(Notifications notifications) {
        if (notifications.size() == 0) {
            clearAllBtn.setVisibility(View.GONE);
            zeroNotiTv.setVisibility(View.VISIBLE);
            notiRV.setVisibility(View.GONE);

        } else {
            clearAllBtn.setVisibility(View.VISIBLE);
            zeroNotiTv.setVisibility(View.GONE);
            notiRV.setVisibility(View.VISIBLE);
        }
    }

    public boolean isBottomSheetCollapsed() {
        return bottomSheetState == BottomSheetBehavior.STATE_COLLAPSED;
    }

    public void collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void notifyImportant(Notification notification) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(notification.getPkgName());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundPath = Uri.parse("android.resource://com.kasmartnotification.smartnotification/raw/rhea");
        long[] vibrationPattern = {0, 200, 100, 200}; //sleep, vibrate, sleep, vibrate
        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        NotificationCompat.Builder importantNoti =  new NotificationCompat.Builder(context, Constants.IMPORTANT)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(color)
                .setColorized(true)
                .setSound(soundPath)
                .setVibrate(vibrationPattern);

        Icon appIcon = notification.getAppIcon();
        if (appIcon != null) {
            Drawable drawable = appIcon.loadDrawable(context);
            Bitmap bitmap = ImageDecoder.convertDrawableToBitmap(drawable);
            bitmap = ImageDecoder.changeImageColor(bitmap, notification.getColor());
            importantNoti.setLargeIcon(bitmap);
        }

        if (notificationManager != null) {
            notificationManager.notify(Constants.NOTIFICATION_ID, importantNoti.build());
        }
    }
}
