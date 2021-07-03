package com.app.e_notice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends AppCompatActivity {

    EditText edit_group_name;
    Spinner edit_group_admin;
    Button btn_create;
    ApiInterface apiInterface;
    ArrayList<String> namesList;
    String student_name, student_id;
    HashMap<String, Student> studentHashmap = new HashMap<>();
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        btn_create = findViewById(R.id.btn_create);
        edit_group_name = findViewById(R.id.edit_group_name);
        edit_group_admin = findViewById(R.id.edit_group_admin);

        checkPermission(Manifest.permission.SEND_SMS, 1);

        apiInterface = ApiClient.getClient(CreateGroupActivity.this).create(ApiInterface.class);

        getAllStudents();

        edit_group_admin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected = (String) adapterView.getSelectedItem();
                student = studentHashmap.get(selected.split(" ")[0]);


                student_id = selected.split(" ")[0];
                student_name = selected.split(" ")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addGroup();
            }
        });
    }

    private void addGroup() {
        System.out.println(student.toString());
        apiInterface.addGroup(edit_group_name.getText().toString(), student_name, student_id, student.getUsername(), student.getPassword()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        Toast.makeText(CreateGroupActivity.this, "Group added successfully", Toast.LENGTH_SHORT).show();

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(student.getMobile_no(), null, "Your Group : " + edit_group_name.getText().toString() + "'s login credentials are \n Username = " + student.getUsername() + " & Password = " + student.getPassword(), null, null);


                        startActivity(new Intent(getBaseContext(), AdminHomeActivity.class));

                    } else if (jsonObject.getString("code").equals("401")) {
                        Toast.makeText(CreateGroupActivity.this, "Group Name and Admin Should be Unique...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Group not added successfully", Toast.LENGTH_SHORT).show();
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

    private void getAllStudents() {

        namesList = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, namesList);

        apiInterface.getAllStudents().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("student");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Student student = new Student();

                            student.setId(Integer.parseInt(object.getString("id")));
                            student.setName(object.getString("name"));
                            student.setBranch(object.getString("branch"));
                            student.setUsername(object.getString("username"));
                            student.setPassword(object.getString("password"));
                            student.setMobile_no(object.getString("mobile_no"));
                            student.setSubscribed_group_id(object.getString("subscribed_group_id"));

                            namesList.add(object.getString("id") + " " + object.getString("name"));

                            studentHashmap.put(object.getString("id"), student);

                            arrayAdapter.notifyDataSetChanged();

                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                edit_group_admin.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void checkPermission(String permission, int requestCode) {

        if (ContextCompat.checkSelfPermission(CreateGroupActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(CreateGroupActivity.this, new String[]{permission}, requestCode);

        } else {

//            Toast.makeText(ForgetPasswordActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CreateGroupActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(CreateGroupActivity.this,
                        "CAMERA Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }
}