package com.example.mydschoolteachersapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydschoolteachersapp.Fragments.AssignmentFragment;
import com.example.mydschoolteachersapp.Fragments.AttendanceFragment;
import com.example.mydschoolteachersapp.Fragments.CalendarFragment;
import com.example.mydschoolteachersapp.R;

public class AdapterRecyclerViewMain extends RecyclerView.Adapter<AdapterRecyclerViewMain.ViewHolder > {

    Context context;
    String [] categoryNamesList;
    int [] categoryImageList;
    public AdapterRecyclerViewMain(Context context, String[] categoryNamesList, int[] categoryImageList) {
        this.context=context;
        this.categoryNamesList=categoryNamesList;
        this.categoryImageList=categoryImageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_main_category,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewCategoryNames.setText(categoryNamesList[i]);
        viewHolder.imageViewCategoryImage.setImageResource(categoryImageList[i]);

    }

    @Override
    public int getItemCount() {
        return categoryNamesList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewCategoryNames;
        ImageView imageViewCategoryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             textViewCategoryNames= itemView.findViewById(R.id.textViewCategoryNames);
             imageViewCategoryImage= itemView.findViewById(R.id.imageViewCategoryImage);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
          //  Toast.makeText(v.getContext(), getLayoutPosition()+"", Toast.LENGTH_SHORT).show();
            if(getAdapterPosition()==0)
            {

              AttendanceFragment attendanceFragment = new AttendanceFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation, attendanceFragment).addToBackStack(null).commit();

            }else if(getAdapterPosition()==1)
            {
                AssignmentFragment assignmentFragment = new AssignmentFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation, assignmentFragment).addToBackStack(null).commit();

            }else if(getAdapterPosition()==2)
            {
                CalendarFragment calendarFragment = new CalendarFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation, calendarFragment).addToBackStack(null).commit();
            }else if(getAdapterPosition()==3)
            {
                Toast.makeText(v.getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
            else if(getAdapterPosition()==4)
            {
                Toast.makeText(v.getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
