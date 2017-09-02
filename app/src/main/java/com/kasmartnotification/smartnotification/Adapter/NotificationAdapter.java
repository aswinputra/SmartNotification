package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Model.Notification;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kiman on 1/9/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

    private Context mCtx;
    private Notifications mNotifications;
    private SectionRecyclerViewAdapter mSectionAdapter;

    public NotificationAdapter(Context mCtx, Notifications mNotifications) {
        this.mCtx = mCtx;
        this.mNotifications = mNotifications;
    }

    public SectionRecyclerViewAdapter getmSectionAdapter() {
        return mSectionAdapter;
    }

    public void setmSectionAdapter(SectionRecyclerViewAdapter mSectionAdapter) {
        this.mSectionAdapter = mSectionAdapter;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.adapter_notification, parent, false);
        return new NotiViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NotiViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        holder.titleTV.setText(notification.getTitle());
        holder.messageTV.setText(notification.getMessage());
        Icon icon = notification.getAppIcon();
        if (icon != null) {
            icon.setTint(ContextCompat.getColor(mCtx, R.color.colorBlackSecondaryText));
            holder.appIconTV.setImageIcon(icon);
        }
        setTime(holder.minuteTV, notification.getPostedTime());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CircleImageView imageView;
        private TextView titleTV;
        private TextView messageTV;
        private TextView minuteTV;
        private ImageView appIconTV;
        private ConstraintLayout layout;

        public NotiViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.adapter_notification_circle_imageview);
            titleTV = itemView.findViewById(R.id.adapter_notification_title);
            messageTV = itemView.findViewById(R.id.adapter_noti_message);
            minuteTV = itemView.findViewById(R.id.adapter_noti_minute);
            appIconTV = itemView.findViewById(R.id.adapter_noti_app_icon);
            layout = itemView.findViewById(R.id.adapter_notification_layout);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int actualClickedPos = getAdapterPosition();
            int pos = mSectionAdapter.sectionedPositionToPosition(actualClickedPos);

            PackageManager packageManager = mCtx.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(mNotifications.get(pos).getPkgName());
            mCtx.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            int actualClickedPos = getAdapterPosition();
            int pos = mSectionAdapter.sectionedPositionToPosition(actualClickedPos);

            return true;
        }
    }

    private void setTime(TextView postedTimeTV, long postedTime) {
        int timeToShow = Utility.timeDiffInMinute(postedTime);
        String timeToShowStr = "";
        if (timeToShow < 60) {
            timeToShowStr += timeToShow + "m";
        } else {
            timeToShow = Utility.timeDiffInHour(timeToShow);
            timeToShowStr += timeToShow + "h";
        }
        postedTimeTV.setText(timeToShowStr);
    }
}
