package com.app.e_notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Comment> commentArrayList;

    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @Override
    public int getCount() {
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.comment_view, viewGroup, false);

        Comment comment = commentArrayList.get(i);

        TextView text_comment = view.findViewById(R.id.text_comment);
        TextView text_date = view.findViewById(R.id.text_date);

        text_comment.setText(comment.getText());
        text_date.setText(comment.getCreated_at());

        return view;
    }
}
