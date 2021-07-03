package com.app.e_notice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    ListView listview_new;
    SearchView searchView;
    ApiInterface apiInterface;
    ArrayList<Notice> noticeArrayList;
    ArrayList<Group> groupArrayList;
    String from, token, group_id;
    GroupAdapter groupAdapter;
    Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        apiInterface = ApiClient.getClient(HomeActivity.this).create(ApiInterface.class);

        SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);

        listview_new = findViewById(R.id.listview_new);
        searchView = findViewById(R.id.searchView);

        from = getIntent().getStringExtra("from");

        group_id = getIntent().getStringExtra("group_id");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {

                    case R.id.notice:

                        startActivity(new Intent(getBaseContext(), NoticeListActivity.class).putExtra("from", from));

                        return true;

                    case R.id.news:

                        startActivity(new Intent(getBaseContext(), NewsActivity.class).putExtra("from", from));

                        return true;

                    case R.id.profile:

                        startActivity(new Intent(getBaseContext(), ProfileActivity.class).putExtra("from", "0"));

                        return true;


                }

                return true;

            }
        });

        // getAllGroups();

        getStudentsGroups();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("HomeActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        updateFirebaseToken();

                    }
                });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 0) {
                    getAllGroups();
                } else {
                    getStudentsGroups();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 1) {
                    getAllGroups();
                }

                if (newText.length() > 1) {

                    groupAdapter.getFilter().filter(newText);
                    groupAdapter.notifyDataSetChanged();

                } else {

                    getStudentsGroups();
                }

                return true;
            }

        });
        listview_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Group group = (Group) adapterView.getAdapter().getItem(i);

                Log.e("GROUP==========", group.toString());

                //  startActivity(new Intent(getBaseContext(), NoticeListActivity.class).putExtra("id", group.getId()).putExtra("from", from));

            }
        });

    }

    private void getAllGroups() {

        groupArrayList = new ArrayList<>();
        groupAdapter = new GroupAdapter(HomeActivity.this, groupArrayList);

        apiInterface.getAllGroups().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("group");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Group group = new Group();
                            group.setId(Integer.parseInt(object.getString("id")));
                            group.setName(object.getString("name"));
                            group.setAdmin(object.getString("admin"));
                            group.setUsername(object.getString("username"));
                            group.setPassword(object.getString("password"));
                            group.setCreated_at(object.getString("created_at"));

                            groupArrayList.add(group);

                            groupAdapter.notifyDataSetChanged();
                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listview_new.setAdapter(groupAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getStudentsGroups() {

        groupArrayList = new ArrayList<>();
        groupAdapter = new GroupAdapter(HomeActivity.this, groupArrayList);

        apiInterface.getStudentsGroups(student.getSubscribed_group_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("group");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Group group = new Group();
                            group.setId(Integer.parseInt(object.getString("id")));
                            group.setName(object.getString("name"));
                            group.setAdmin(object.getString("admin"));
                            group.setUsername(object.getString("username"));
                            group.setPassword(object.getString("password"));
                            group.setCreated_at(object.getString("created_at"));

                            groupArrayList.add(group);

                            groupAdapter.notifyDataSetChanged();
                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listview_new.setAdapter(groupAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        if (from.equals("student")) {

            menu.findItem(R.id.action_create_group).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setVisible(!from.equals("student"));

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
                return true;
            case R.id.action_create_group:
                startActivity(new Intent(getBaseContext(), AddNoticeActivity.class).putExtra("group_id", group_id));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateFirebaseToken() {

        apiInterface.updateFirebaseToken(String.valueOf(student.getId()), token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {


                    } else {

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