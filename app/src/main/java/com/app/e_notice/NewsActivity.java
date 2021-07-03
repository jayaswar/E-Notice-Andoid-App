package com.app.e_notice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    ArrayList<Notice> noticeArrayList;
    ListView listview;
    NoticeAdapter noticeAdapter;
    String from;
    Button btn_create_group;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        apiInterface = ApiClient.getClient(NewsActivity.this).create(ApiInterface.class);

        SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);

        listview = findViewById(R.id.listview);

        btn_create_group = findViewById(R.id.btn_create_group);

        from = getIntent().getStringExtra("from");

        getAllNews();

        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getBaseContext(), AddNoticeActivity.class));
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Notice notice = (Notice) adapterView.getAdapter().getItem(i);

                startActivity(new Intent(getBaseContext(), NoticeDetailsActivity.class).putExtra("notice", notice).putExtra("from", "ach"));

            }
        });

    }

    private void getAllNews() {

        noticeArrayList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(getBaseContext(), noticeArrayList);

        apiInterface.getAllNews("News").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("notice");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        Notice notice = new Notice();
                        notice.setId(Integer.parseInt(object.getString("id")));
                        notice.setTitle(object.getString("title"));
                        notice.setDesc(object.getString("desc"));
                        notice.setGroup_id(object.getString("group_id"));
                        notice.setImage_name(object.getString("image_name"));
                        notice.setGroup_name(object.getString("group_name"));
                        notice.setCreated_at(object.getString("created_at"));

                        noticeArrayList.add(notice);
                        noticeAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listview.setAdapter(noticeAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}