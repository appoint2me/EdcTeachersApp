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

import com.example.mydschoolteachersapp.Fragments.SectionFragmentAttendance;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.R;

import java.util.ArrayList;

public class AdapterRecyclerViewClass extends RecyclerView.Adapter<AdapterRecyclerViewClass.ViewHolder>  {
    Context mContext;
    ArrayList<ClassModel> mListClass;
    String mClassId=null;
    public AdapterRecyclerViewClass(Context mContext, ArrayList<ClassModel> mListClass) {
        this.mContext=mContext;
        this.mListClass=mListClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_class_fragment,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ClassModel classModel = mListClass.get(i);
        viewHolder.textViewClassName.setText(classModel.getClassName());
        mClassId=classModel.getClassId();
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                        AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();
                        SectionFragmentAttendance sectionFragmentAttendance =new SectionFragmentAttendance();
                        Bundle bundle=new Bundle();
                        bundle.putString("CLASS_ID",classModel.getClassId());
                        sectionFragmentAttendance.setArguments(bundle);
                        // Toast.makeText(appCompatActivity, classModel.getClassId(), Toast.LENGTH_SHORT).show();
                        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation, sectionFragmentAttendance).addToBackStack("").commit();

                        // Or for doing a fragment transaction
                        // fragmentTransaction.commit();
            }
        });
        viewHolder.textViewClassName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
        // viewHolder.textViewClassId.setText(assignmentModel.getClassId());
        // Toast.makeText(mContext, assignmentModel.getClassName(), Toast.LENGTH_SHORT).show();



    }

    @Override
    public int getItemCount() {
        return mListClass.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewClassName,textViewClassId;
        ImageView imageView;
        public ViewHolder(@NonNull final View itemView) {

            super(itemView);
            imageView= itemView.findViewById(R.id.imageView3);
            textViewClassName= itemView.findViewById(R.id.textViewClassName);

        }
    }
}
