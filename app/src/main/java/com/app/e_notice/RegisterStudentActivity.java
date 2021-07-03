package com.app.e_notice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStudentActivity extends AppCompatActivity {

    EditText edit_enter_name, edit_enter_email, edit_enter_mobile, edit_enter_branch;
    Button btn_register;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        checkPermission(Manifest.permission.SEND_SMS, 1);

        apiInterface = ApiClient.getClient(RegisterStudentActivity.this).create(ApiInterface.class);
        edit_enter_name = findViewById(R.id.edit_enter_name);
        edit_enter_email = findViewById(R.id.edit_enter_email);
        edit_enter_mobile = findViewById(R.id.edit_enter_mobile);
        edit_enter_branch = findViewById(R.id.edit_enter_branch);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edit_enter_email.getText().toString().length() > 0) {

                    register();

                } else {

                    Toast.makeText(RegisterStudentActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void register() {

        apiInterface.register(edit_enter_name.getText().toString(), edit_enter_email.getText().toString(), edit_enter_mobile.getText().toString(), edit_enter_branch.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {
                        Toast.makeText(RegisterStudentActivity.this, "Student registered successfully", Toast.LENGTH_SHORT).show();

                        String username = jsonObject.getString("username");
                        String password = jsonObject.getString("password");

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(edit_enter_mobile.getText().toString(), null, "Your Login Credentials are \n Username = " + username + " & Password = " + password, null, null);


                        startActivity(new Intent(getBaseContext(), AdminHomeActivity.class));

                    } else if (jsonObject.getString("code").equals("401")) {
                        Toast.makeText(RegisterStudentActivity.this, "Student already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterStudentActivity.this, "Student registration failed", Toast.LENGTH_SHORT).show();
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

    public void checkPermission(String permission, int requestCode) {

        if (ContextCompat.checkSelfPermission(RegisterStudentActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(RegisterStudentActivity.this, new String[]{permission}, requestCode);

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
                Toast.makeText(RegisterStudentActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(RegisterStudentActivity.this,
                        "CAMERA Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }
}