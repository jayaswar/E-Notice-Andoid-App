package com.app.e_notice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeDetailsActivity extends AppCompatActivity {

    Notice notice;
    TextView text_title, text_desc;
    ImageView image_, image_send;
    ListView listview_comments;
    ArrayList<Comment> commentArrayList;
    ApiInterface apiInterface;
    EditText edit_comment;
    Student student;
    String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        notice = (Notice) getIntent().getSerializableExtra("notice");
        from = getIntent().getStringExtra("from");

        apiInterface = ApiClient.getClient(NoticeDetailsActivity.this).create(ApiInterface.class);

        SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);

        image_ = findViewById(R.id.image_);
        image_send = findViewById(R.id.image_send);
        text_title = findViewById(R.id.text_title);
        text_desc = findViewById(R.id.text_desc);
        listview_comments = findViewById(R.id.listview_comments);
        edit_comment = findViewById(R.id.edit_comment);

        text_title.setText(notice.getTitle());
        text_desc.setText(notice.getDesc());

        if (from.equals("ach")) {

            image_send.setVisibility(View.GONE);
            edit_comment.setVisibility(View.GONE);
            listview_comments.setVisibility(View.GONE);

        } else {

            image_send.setVisibility(View.VISIBLE);
            edit_comment.setVisibility(View.VISIBLE);
            listview_comments.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(ApiClient.MENU_SERVER_URL + notice.getImage_name()).into(image_);

        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_comment.getText().toString().length() > 0) {
                    addComment();
                } else {
                    Toast.makeText(NoticeDetailsActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }


            }
        });

        if (notice != null) {
            getComments();
        }

    }

    private void getComments() {

        commentArrayList = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(getBaseContext(), commentArrayList);

        apiInterface.getComments(String.valueOf(notice.getId())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("notice");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Comment comment = new Comment();
                            comment.setId(Integer.parseInt(object.getString("id")));
                            comment.setGroup_id(object.getString("group_id"));
                            comment.setNotice_id(object.getString("notice_id"));
                            comment.setText(object.getString("text"));
                            comment.setStudent_id(object.getString("student_id"));
                            comment.setCreated_at(object.getString("created_at"));

                            commentArrayList.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listview_comments.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void addComment() {

        apiInterface.addComment(notice.getGroup_id(), String.valueOf(notice.getId()), edit_comment.getText().toString(), String.valueOf(student.getId())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        Toast.makeText(NoticeDetailsActivity.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                        getComments();

                    } else {

                        Toast.makeText(NoticeDetailsActivity.this, "Comment not posted successfully", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}