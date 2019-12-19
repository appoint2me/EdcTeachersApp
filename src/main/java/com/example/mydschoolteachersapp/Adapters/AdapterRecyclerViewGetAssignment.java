package com.example.mydschoolteachersapp.Adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Model.AssignmentModel;
import com.example.mydschoolteachersapp.R;

import java.util.ArrayList;

public class AdapterRecyclerViewGetAssignment extends RecyclerView.Adapter<AdapterRecyclerViewGetAssignment.ViewHolder> {
    ArrayList<AssignmentModel> mListAssignment;
    Context context;
    public AdapterRecyclerViewGetAssignment(Context context, ArrayList<AssignmentModel> mListAssignment) {
        this.context=context;
        this.mListAssignment=mListAssignment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_get_assignment_list,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AssignmentModel assignmentModel=mListAssignment.get(i);
        viewHolder.textViewAssignmentName.setText(assignmentModel.getTitle());
        viewHolder.textViewAssignmentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataid = String.valueOf(assignmentModel.getDocumentPath());
               // Toast.makeText(v.getContext(), "" + assignmentModel.getDocumentPath(), Toast.LENGTH_SHORT).show();
                // Log.d("123456", dataid);
                try {
                    String path = Config.URL_DOWNLOAD_ASSIGNMENT;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(path + "" + dataid);
                    intent.setData(uri);
                    v.getContext().startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, "Activity not found" +
                            "", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(v.getContext());
                    alertDialog.setTitle("Message from server");
                    alertDialog.setMessage("Please Install a Web Browser to view this file");
                   alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   alertDialog.create();
                   alertDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListAssignment.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAssignmentName;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssignmentName= itemView.findViewById(R.id.textViewAssignmnetName);
        }
    }
}
