package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.EditDialog;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class KeywordsAdapter extends RecyclerView.Adapter<KeywordsAdapter.KeywordsViewHolder> {

    private Context mContext;
    private List<Keyword> mKeywords;

    public KeywordsAdapter(Context context, List<Keyword> keywords){
        mContext = context;
        mKeywords = keywords;
    }

    @Override
    public KeywordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_keywords,parent, false);
        KeywordsViewHolder holder = new KeywordsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(KeywordsViewHolder holder, int position) {
        Keyword keyword = mKeywords.get(position);

        holder.keywordTextView.setText(keyword.getName());
    }

    @Override
    public int getItemCount() {
        return mKeywords.size();
    }

    public class KeywordsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, OnDialogClickedListener {

        private TextView keywordTextView;
        private LinearLayout keywordLinearLayout;

        public KeywordsViewHolder(View itemView) {
            super(itemView);
            keywordTextView = itemView.findViewById(R.id.adapter_keywords);
            keywordLinearLayout = itemView.findViewById(R.id.adapter_keywords_linear_layout);

            keywordLinearLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            showEditDialog(getAdapterPosition());
            return true;
        }

        private void showEditDialog(int adapterPosition) {
            EditDialog editDialog = new EditDialog(mContext, mKeywords.get(adapterPosition),this);
        }

        @Override
        public void onOK(Object object, String newString) {
            ((Keyword)object).setName(newString);
            ((Keyword)object).save();
            notifyDataSetChanged();
        }

        @Override
        public void onDelete(Object object) {
            ((Keyword)object).delete();
            mKeywords.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}
