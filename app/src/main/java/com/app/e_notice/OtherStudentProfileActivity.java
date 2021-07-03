package com.app.e_notice;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.e_notice.webservices.ApiClient;
import com.squareup.picasso.Picasso;

public class OtherStudentProfileActivity extends AppCompatActivity {

    EditText edit_bio, edit_name, edit_email, edit_branch, edit_mobile;
    ImageView image_profile;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_student_profile);

        student = (Student) getIntent().getSerializableExtra("student");

        edit_bio = findViewById(R.id.edit_bio);
        edit_name = findViewById(R.id.edit_name);
        edit_branch = findViewById(R.id.edit_branch);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_email = findViewById(R.id.edit_email);
        image_profile = findViewById(R.id.image_profile);

        edit_bio.setText(student.getBio());
        edit_name.setText(student.getName());
        edit_mobile.setText(student.getMobile_no());
        edit_branch.setText(student.getBranch());
        edit_email.setText(student.getEmail());

        if (student.getProfile_pic() == null || student.getProfile_pic().isEmpty()) {

        } else {
            Picasso.get().load(ApiClient.MENU_SERVER_URL + student.getProfile_pic()).into(image_profile);
        }
    }
}