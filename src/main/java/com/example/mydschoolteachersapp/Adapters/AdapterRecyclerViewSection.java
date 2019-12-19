package com.example.mydschoolteachersapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydschoolteachersapp.Model.SectionModel;
import com.example.mydschoolteachersapp.R;

import java.util.ArrayList;

public class AdapterRecyclerViewSection extends RecyclerView.Adapter<AdapterRecyclerViewSection.ViewHolder> {
    ArrayList<SectionModel> mListSection;
    Context mContext;
    String mClassId=null;

    public AdapterRecyclerViewSection(Context mContext, ArrayList<SectionModel> mListSection,String mClassId ) {
        this.mListSection = mListSection;
        this.mContext = mContext;
        this.mClassId=mClassId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_section,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final SectionModel sectionModel=mListSection.get(i);
         viewHolder.textViewSection.setText(sectionModel.getSectionName());
         viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String section_id = String.valueOf(sectionModel.getSectionId());
                 String section_name = String.valueOf(sectionModel.getSectionName());
                 Log.d("Section_id", section_id);

//                 Intent intent=new Intent(v.getContext(), ViewStudentsListActivity.class);
//                 intent.putExtra("CLASS_ID",mClassId);
//                 intent.putExtra("SECTION_ID",section_id);
//                 v.getContext().startActivity(intent);
             }
         });
         viewHolder.textViewSection.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
    }

    @Override
    public int getItemCount() {
        return mListSection.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSection;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSection= itemView.findViewById(R.id.textViewSectionName);
            imageView= itemView.findViewById(R.id.imageView2);
        }
    }
}
