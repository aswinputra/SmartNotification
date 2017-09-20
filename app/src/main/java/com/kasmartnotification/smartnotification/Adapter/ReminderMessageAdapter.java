package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Controller.EditDialog;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.ReminderMessage;
import com.kasmartnotification.smartnotification.R;

import java.util.List;

/**
 * Created by kiman on 19/9/17.
 */

public class ReminderMessageAdapter extends RecyclerView.Adapter<ReminderMessageAdapter.ReminderMessageViewHolder> {
    private Context mContext;
    private List<ReminderMessage> mReminderMessages;

    public ReminderMessageAdapter(Context context, List<ReminderMessage> reminderMessages){
        mContext = context;
        mReminderMessages = reminderMessages;
    }

    @Override
    public ReminderMessageAdapter.ReminderMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_keywords,parent, false);
        return new ReminderMessageAdapter.ReminderMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderMessageAdapter.ReminderMessageViewHolder holder, int position) {
        ReminderMessage message = mReminderMessages.get(position);

        holder.keywordTextView.setText(message.getName());
    }

    @Override
    public int getItemCount() {
        return mReminderMessages.size();
    }

    public class ReminderMessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, OnDialogClickedListener {

        private TextView keywordTextView;
        private LinearLayout keywordLinearLayout;

        public ReminderMessageViewHolder(View itemView) {
            super(itemView);
            keywordTextView = itemView.findViewById(R.id.adapter_keywords);
            keywordLinearLayout = itemView.findViewById(R.id.adapter_keywords_linear_layout);

            keywordLinearLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if(mReminderMessages.size() > 1) {
                showEditDialog(getAdapterPosition(), true);
            }else{
                showEditDialog(getAdapterPosition(), false);
            }
            return true;
        }

        private void showEditDialog(int adapterPosition, boolean needsDelete) {
            new EditDialog(mContext, mReminderMessages.get(adapterPosition),this, needsDelete);
        }

        @Override
        public void onOK(Object object, String newString) {
            ((ReminderMessage)object).setName(newString);
            ((ReminderMessage)object).save();
            notifyDataSetChanged();
        }

        @Override
        public void onDelete(Object object) {
            ((ReminderMessage)object).delete();
            mReminderMessages.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}
