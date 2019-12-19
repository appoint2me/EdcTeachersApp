package com.example.mydschoolteachersapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydschoolteachersapp.Fragments.SectionFragmentAssignment;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewAssignment extends RecyclerView.Adapter<AdapterRecyclerViewAssignment.ViewHolder> implements View.OnClickListener {
    Context mContext;
    List<ClassModel> assignmentModelList =new ArrayList<>();

    public AdapterRecyclerViewAssignment(Context mContext, List<ClassModel> assignmentModelList) {
        this.mContext=mContext;
        this.assignmentModelList = assignmentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_assignment,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ClassModel classModel = assignmentModelList.get(i);
        viewHolder.textViewClassName.setText(classModel.getClassName());
       // viewHolder.textViewClassId.setText(assignmentModel.getClassId());
       // Toast.makeText(mContext, assignmentModel.getClassName(), Toast.LENGTH_SHORT).show();
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();
                SectionFragmentAssignment sectionFragmentAssignment=new SectionFragmentAssignment();
                Bundle bundle=new Bundle();
                bundle.putString("CLASS_ID",classModel.getClassId());
                sectionFragmentAssignment.setArguments(bundle);
                // Toast.makeText(appCompatActivity, classModel.getClassId(), Toast.LENGTH_SHORT).show();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation,sectionFragmentAssignment).addToBackStack("").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        //System.out.println(assignmentModelList.size()+"SIZE");
       // Toast.makeText(mContext, assignmentModelList.size()+"", Toast.LENGTH_SHORT).show();
        return assignmentModelList.size();
    }

    @Override
    public void onClick(View v) {
       // Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClassName,textViewClassId;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageView2);
            textViewClassName= itemView.findViewById(R.id.textViewClassName);
           // textViewClassId=(TextView)itemView.findViewById(R.id.textViewClassId);

        }


    }
}
