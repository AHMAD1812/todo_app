package com.example.todoApp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoApp.Model.todoModel;
import com.example.todoApp.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class addNewTask extends BottomSheetDialogFragment {

    public static final String tag="AddNewTask";

    private EditText medit;
    private Button mbutton;

    private DatabaseHelper mydb;

    public static addNewTask newInstance(){
        return new addNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_addtask,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medit=view.findViewById(R.id.edittext);
        mbutton=view.findViewById(R.id.button_save);

        mydb=new DatabaseHelper(getActivity());

        boolean isUpdate=false;

        final Bundle bundle=getArguments();
        if(bundle != null){
            isUpdate=true;

            String task=bundle.getString("task");
            medit.setText(task);

            if(task.length() > 0){
                mbutton.setEnabled(false);
            }
        }

        medit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    mbutton.setEnabled(false);
                    mbutton.setBackgroundColor(Color.GRAY);
                }else{
                    mbutton.setEnabled(true);
                    mbutton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final boolean finalIsUpdate = isUpdate;

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=medit.getText().toString();

                if(finalIsUpdate){
                    mydb.updateTask(bundle.getInt("id"),text);
                }else{
                    todoModel item=new todoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    mydb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity=getActivity();

        if(activity instanceof onDialogCloseListener){
            ((onDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
