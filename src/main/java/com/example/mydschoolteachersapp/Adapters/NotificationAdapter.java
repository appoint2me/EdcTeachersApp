package com.example.mydschoolteachersapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mydschoolteachersapp.Model.NotificationsModel;
import com.example.mydschoolteachersapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<NotificationsModel> mListNotificaton=new ArrayList<>();
    Context mContext;

    public NotificationAdapter(Context mContext, List<NotificationsModel> mListNotification) {
        this.mListNotificaton=mListNotification;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NotificationsModel mNotificationModel=mListNotificaton.get(i);
        String notification=mNotificationModel.getNotification();
        viewHolder.textViewNotification.setText(notification);
    }

    @Override
    public int getItemCount() {
        return mListNotificaton.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNotification= itemView.findViewById(R.id.textViewNotification);
        }
    }
}
