package com.app.e_notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class StudentSearchAdapter extends BaseAdapter {


    Context context;
    List<Student> studentList;

    public StudentSearchAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int i) {
        return studentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.name_subscribe, viewGroup, false);

        TextView text_name = view.findViewById(R.id.text_name);
        Button subscribe = view.findViewById(R.id.subscribe);

        Student student = studentList.get(i);

        text_name.setText(student.getName());

        subscribe.setVisibility(View.GONE);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
