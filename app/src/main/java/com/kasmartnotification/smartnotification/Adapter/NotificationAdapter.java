package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Tools.CalendarHelper;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Tools.ImageDecoder;
import com.kasmartnotification.smartnotification.Model.Notification;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Tools.NotificationHelper;
import com.kasmartnotification.smartnotification.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kiman on 1/9/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

    private Context mContext;
    private Notifications mNotifications;
    private SectionRecyclerViewAdapter mSectionAdapter;

    public NotificationAdapter(Context context, Notifications mNotifications) {
        this.mContext = context;
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
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_notification, parent, false);
        return new NotiViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NotiViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        holder.titleTV.setText(notification.getTitle());
        holder.messageTV.setText(notification.getMessage());
        Icon smallIcon = notification.getAppIcon();
        Icon lrgIcon = notification.getLargeIcon();
        int color = notification.getColor();
        if (smallIcon != null) {
            smallIcon.setTint(color);
            holder.appIconTV.setImageIcon(smallIcon);
        }
        if (lrgIcon != null) {
            holder.imageView.setImageDrawable(lrgIcon.loadDrawable(mContext));
        }else {
            if (smallIcon != null) {
                Drawable drawable = smallIcon.loadDrawable(mContext);
                Bitmap bitmap = ImageDecoder.convertDrawableToBitmap(drawable);
                bitmap = ImageDecoder.changeImageColor(bitmap, color);
                holder.imageView.setImageBitmap(bitmap);
            }
        }
        if(notification.isImportant()){
            holder.imageView.setBorderColor(ContextCompat.getColor(mContext, R.color.colorRed500));
        }else{
            holder.imageView.setBorderColor(ContextCompat.getColor(mContext, R.color.colorGrey));
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

        /**
         * When a notification is clicked, the corresponding application will be open
         * using getLaunchIntentForPackage from PackageManager
         * Also, the notification will be deleted from the list
         * !!!!!Notice that there are 2 positions variables in this function, ALWAYS use the pos to get the correct
         * item from the notification list!!!!!!
         * @param view is the View object that has clicked
         */
        @Override
        public void onClick(View view) {
            int actualClickedPos = getAdapterPosition();
            int pos = mSectionAdapter.sectionedPositionToPosition(actualClickedPos);

            if(view == layout) {
                PackageManager packageManager = mContext.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(mNotifications.get(pos).getPkgName());

                //this needs to go first
                NotificationHelper.cancelNotification(mContext, mNotifications.get(pos).getShortId());

                Notifications.getInstance().remove(mNotifications.get(pos));


                mContext.startActivity(intent);
            }
        }

        //for showing context menu if we need to
        //return true means that the long click is used
        @Override
        public boolean onLongClick(View view) {
            if(view == layout) {
                int actualClickedPos = getAdapterPosition();
                int pos = mSectionAdapter.sectionedPositionToPosition(actualClickedPos);

                return true;
            }
            return false;
        }
    }

    private void setTime(TextView postedTimeTV, long postedTime) {
        int timeToShow = CalendarHelper.timeDiffInMinute(postedTime);
        String timeToShowStr = "";
        if (timeToShow < Constants.ONE_MINUTE) {
            timeToShowStr += timeToShow + "m";
        } else {
            timeToShow = CalendarHelper.timeDiffInHour(timeToShow);
            timeToShowStr += timeToShow + "h";
        }
        postedTimeTV.setText(timeToShowStr);
    }
}
