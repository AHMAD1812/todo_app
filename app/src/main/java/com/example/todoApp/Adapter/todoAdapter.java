package com.example.todoApp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoApp.MainActivity;
import com.example.todoApp.Model.todoModel;
import com.example.todoApp.R;
import com.example.todoApp.Utils.DatabaseHelper;
import com.example.todoApp.addNewTask;

import java.util.List;

public class todoAdapter extends RecyclerView.Adapter<todoAdapter.myViewHandler>{

    private List<todoModel> mlist;
    private MainActivity activity;
    private DatabaseHelper myDB;

    public todoAdapter(DatabaseHelper myDB, MainActivity activity){
        this.activity=activity;
        this.myDB=myDB;
    }

    @NonNull
    @Override
    public myViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent , false);
        return new myViewHandler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHandler holder, int position) {
        final todoModel item= mlist.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    myDB.updateStatus(item.getId(), 1);
                }else{
                    myDB.updateStatus(item.getId(), 0);
                }

            }
        });

    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<todoModel> mlist){
        this.mlist=mlist;
        notifyDataSetChanged();
    }

    public  void deleteTask(int position){
        todoModel item=mlist.get(position);
        myDB.deleteTask(item.getId());
        mlist.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){

        todoModel item=mlist.get(position);

        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        addNewTask task=new addNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class myViewHandler extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;
        public myViewHandler(@NonNull View itemView) {
            super(itemView);
            mCheckBox= itemView.findViewById(R.id.checkbox);
        }
    }

}
