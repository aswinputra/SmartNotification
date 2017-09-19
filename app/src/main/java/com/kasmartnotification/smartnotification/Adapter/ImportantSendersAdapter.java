package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Controller.EditDialog;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.R;

import java.util.List;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class ImportantSendersAdapter extends RecyclerView.Adapter<ImportantSendersAdapter.ImportantSendersViewHolder> {

    private Context mContext;
    private List<ImportantSender> mImportantSenders;

    public ImportantSendersAdapter(Context mContext, List<ImportantSender> mImportantSenders) {
        this.mContext = mContext;
        this.mImportantSenders = mImportantSenders;
    }

    @Override
    public ImportantSendersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_important_senders,parent,false);
        ImportantSendersViewHolder holder = new ImportantSendersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImportantSendersViewHolder holder, int position) {
        ImportantSender sender = mImportantSenders.get(position);

        holder.senderName.setText(sender.getName());

    }

    @Override
    public int getItemCount() {
        return mImportantSenders.size();
    }

    public class ImportantSendersViewHolder extends ViewHolder implements View.OnLongClickListener, OnDialogClickedListener {

        private TextView senderName;
        private LinearLayout importantSenderLinearLayout;

        public ImportantSendersViewHolder(View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.adapter_important_senders);
            importantSenderLinearLayout = itemView.findViewById(R.id.adapter_important_senders_linear_layout);

            importantSenderLinearLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            showEditDialog(getAdapterPosition());
            return true;
        }

        private void showEditDialog(int position) {
            EditDialog editDialog = new EditDialog(mContext, mImportantSenders.get(position), this, true);
        }

        @Override
        public void onOK(Object object, String newString) {
            ((ImportantSender)object).setName(newString);
            ((ImportantSender)object).save();
            notifyDataSetChanged();
        }

        @Override
        public void onDelete(Object object) {
            ((ImportantSender) object).delete();
            mImportantSenders.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
