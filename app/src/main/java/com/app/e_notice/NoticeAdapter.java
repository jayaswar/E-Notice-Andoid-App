package com.app.e_notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    Context context;
    ArrayList<Notice> noticeArrayList;

    public NoticeAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    @Override
    public int getCount() {
        return noticeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.notice_view, viewGroup, false);


        Notice notice = noticeArrayList.get(i);


        TextView text_notice_title = view.findViewById(R.id.text_notice_title);
        TextView text_ = view.findViewById(R.id.text_);

        text_notice_title.setText("Title : "+notice.getTitle());
        text_.setText("Group Name : "+notice.getGroup_name());

        return view;
    }
}
