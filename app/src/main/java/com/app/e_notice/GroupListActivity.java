package com.app.e_notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupListActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    ArrayList<Notice> noticeArrayList;
    ListView listview;
    NoticeAdapter noticeAdapter;
    String id, from;
    Button btn_create_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        apiInterface = ApiClient.getClient(GroupListActivity.this).create(ApiInterface.class);

        id = String.valueOf(getIntent().getIntExtra("id", 0));

        listview = findViewById(R.id.listview);

        getAllNotice(id);

        btn_create_group = findViewById(R.id.btn_create_group);

        from = getIntent().getStringExtra("from");

        if (from.equals("student")) {
            btn_create_group.setVisibility(View.GONE);
        } else {
            btn_create_group.setVisibility(View.VISIBLE);
        }

        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getBaseContext(), AddNoticeActivity.class).putExtra("id", id));
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Notice notice = (Notice) adapterView.getAdapter().getItem(i);

                startActivity(new Intent(getBaseContext(), NoticeDetailsActivity.class).putExtra("notice", notice));

            }
        });

    }

    private void getAllNotice(String id) {

        noticeArrayList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(getBaseContext(), noticeArrayList);

        apiInterface.getAllNews(id).enqueue(new Callback<JsonObject>() {
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