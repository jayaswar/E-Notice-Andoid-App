package com.app.e_notice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edit_number, edit_password;
    String from, group_id;
    Button btn_submit;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient(MainActivity.this).create(ApiInterface.class);
        edit_number = findViewById(R.id.edit_number);
        edit_password = findViewById(R.id.edit_password);
        btn_submit = findViewById(R.id.btn_submit);

        from = getIntent().getStringExtra("from");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (from) {
                    case "student":

                        login();
                        break;

                    case "group":

                        login_group();

                        break;

                    case "admin":

                        admin_login();
                        break;
                }

            }
        });

    }

    private void login() {

        apiInterface.login(edit_number.getText().toString(), edit_password.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("user");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Student student = new Student();

                            student.setId(Integer.parseInt(object.getString("id")));
                            student.setName(object.getString("name"));
                            student.setBranch(object.getString("branch"));
                            student.setEmail(object.getString("email"));
                            student.setBio(object.getString("bio"));
                            student.setUsername(object.getString("username"));
                            student.setPassword(object.getString("password"));
                            student.setMobile_no(object.getString("mobile_no"));
                            student.setSubscribed_group_id(object.getString("subscribed_group_id"));
                            student.setProfile_pic(object.getString("profile_pic"));


                            SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(student);
                            prefsEditor.putString("Student", json);
                            prefsEditor.apply();


                        }




                                startActivity(new Intent(getBaseContext(), HomeActivity.class).putExtra("from", "student"));


                    } else if (jsonObject.getString("code").equals("401")) {
                        Toast.makeText(MainActivity.this,"Incorrect Login Credentials..",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Incorrect Login Credentials..",Toast.LENGTH_SHORT).show();
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


    private void login_group() {

        System.out.println("HELLO");

        apiInterface.login_group(edit_number.getText().toString(), edit_password.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("user");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Student student = new Student();

                            student.setId(Integer.parseInt(object.getString("id")));
                            student.setName(object.getString("name"));
                            student.setBranch(object.getString("branch"));
                            student.setEmail(object.getString("email"));
                            student.setBio(object.getString("bio"));
                            student.setUsername(object.getString("username"));
                            student.setPassword(object.getString("password"));
                            student.setMobile_no(object.getString("mobile_no"));
                            student.setSubscribed_group_id(object.getString("subscribed_group_id"));
                            student.setProfile_pic(object.getString("profile_pic"));

                            SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(student);
                            prefsEditor.putString("Student", json);
                            prefsEditor.apply();


                        }


                        group_id=jsonObject.getString("group_id");
                        Intent i= new Intent(getBaseContext(), HomeActivity.class).putExtra("from", "group");
                        i.putExtra("group_id", group_id);
                        startActivity(i);


                    } else if (jsonObject.getString("code").equals("401")) {

                        Toast.makeText(MainActivity.this,"Incorrect Login Credentials..",Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this,"Incorrect Login Credentials..",Toast.LENGTH_SHORT).show();
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





    private void admin_login() {

        apiInterface.adminlogin(edit_number.getText().toString(), edit_password.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {


                        startActivity(new Intent(getBaseContext(), AdminHomeActivity.class));


                    } else if (jsonObject.getString("code").equals("401")) {

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