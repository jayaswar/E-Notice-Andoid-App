package com.app.e_notice;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    Button btn_update;
    EditText edit_bio, edit_name, edit_email, edit_branch, edit_mobile;
    String from;
    ApiInterface apiInterface;
    Student student;
    ImageView image_profile;
    AutoCompleteTextView edit_search;
    List<Student> studentList;
    StudentSearchAdapter studentSearchAdapter;
    ArrayList<String> studentNameList;
    HashMap<String, Student> nameStudentHashMap = new HashMap<>();
    File file;
    private int GALLERY = 1, CAMERA = 2, STORAGE_PERMISSION_CODE = 123, WRITE_STORAGE = 6;
    private Uri filePath;
    private Bitmap bitmap;
    private String selectedFilename = "", mCurrentPhotoPath;
    private String ADD_CAMERA_IMAGES = ApiClient.SERVER_URL + "updateProfile.php?apicall=uploadpic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_update = findViewById(R.id.btn_update);
        edit_bio = findViewById(R.id.edit_bio);
        edit_name = findViewById(R.id.edit_name);
        edit_branch = findViewById(R.id.edit_branch);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_email = findViewById(R.id.edit_email);
        edit_search = findViewById(R.id.edit_search);
        image_profile = findViewById(R.id.image_profile);

        checkPermission(Manifest.permission.CAMERA, CAMERA);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);

        apiInterface = ApiClient.getClient(ProfileActivity.this).create(ApiInterface.class);

        from = getIntent().getStringExtra("from");

        getAllStudents();

        edit_bio.setText(student.getBio());
        edit_name.setText(student.getName());
        edit_mobile.setText(student.getMobile_no());
        edit_branch.setText(student.getBranch());
        edit_email.setText(student.getEmail());

        if (from.equals("0")) {

            btn_update.setVisibility(View.GONE);
            edit_bio.setEnabled(false);
            edit_branch.setEnabled(false);
            edit_mobile.setEnabled(false);
            edit_name.setEnabled(false);
            edit_email.setEnabled(false);
            image_profile.setEnabled(false);

        } else {

            btn_update.setVisibility(View.VISIBLE);
            edit_bio.setEnabled(true);
            edit_branch.setEnabled(true);
            edit_mobile.setEnabled(true);
            edit_name.setEnabled(true);
            edit_email.setEnabled(true);
            image_profile.setEnabled(true);

        }

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        if (student.getProfile_pic() == null || student.getProfile_pic().isEmpty()) {

        } else {
            Picasso.get().load(ApiClient.MENU_SERVER_URL + student.getProfile_pic()).into(image_profile);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_bio.getText().toString().length() > 0) {

                    if (edit_name.getText().toString().length() > 0) {

                        if (edit_email.getText().toString().length() > 0) {

                            if (edit_branch.getText().toString().length() > 0) {

                                if (edit_mobile.getText().toString().length() > 0) {

                                    // updateProfile();

                                    if (mCurrentPhotoPath == null || mCurrentPhotoPath.isEmpty()) {

                                        Toast.makeText(ProfileActivity.this, "Please select profile image", Toast.LENGTH_SHORT).show();

                                    } else {

                                        new ImageCompressionAsyncTask(ProfileActivity.this).execute(mCurrentPhotoPath, getBaseContext().getExternalFilesDir("") + "");
                                    }

                                } else {
                                    Toast.makeText(ProfileActivity.this, "Please add mobile number", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(ProfileActivity.this, "Please add branch", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "Please add email", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ProfileActivity.this, "Please add name", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(ProfileActivity.this, "Please add bio", Toast.LENGTH_SHORT).show();

                }


            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String name = editable.toString();

                if (nameStudentHashMap.containsKey(name)) {


                    // Toast.makeText(ProfileActivity.this, "Student Found", Toast.LENGTH_SHORT).show();

                } else {

                }

            }
        });

        edit_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_search.getText().toString() == null || edit_search.getText().toString().isEmpty()) {

                    Toast.makeText(ProfileActivity.this, "Please enter student name", Toast.LENGTH_SHORT).show();

                } else {
                    if (nameStudentHashMap.containsKey(edit_search.getText().toString())) {

                        Student student = nameStudentHashMap.get(edit_search.getText().toString());

                        edit_search.setText("");

                        startActivity(new Intent(getBaseContext(), OtherStudentProfileActivity.class).putExtra("student", student));

                        // Toast.makeText(ProfileActivity.this, "Student Found", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ProfileActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(getBaseContext(), ProfileActivity.class).putExtra("from", "1"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateProfile() {

        apiInterface.updateProfile(String.valueOf(student.getId()), edit_name.getText().toString(), edit_email.getText().toString(), edit_mobile.getText().toString(), edit_branch.getText().toString(), edit_bio.getText().toString()).enqueue(new Callback<JsonObject>() {
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


                            SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(student);
                            prefsEditor.putString("Student", json);
                            prefsEditor.apply();

                            startActivity(new Intent(getBaseContext(), ProfileActivity.class).putExtra("from", "0"));

                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void getAllStudents() {

        studentList = new ArrayList<>();
        studentNameList = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.select_dialog_item, studentNameList);

        studentSearchAdapter = new StudentSearchAdapter(ProfileActivity.this, studentList);

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

                            studentList.add(student);

                            studentNameList.add(student.getName());
                            nameStudentHashMap.put(student.getName(), student);

                            arrayAdapter.notifyDataSetChanged();

                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                edit_search.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {


            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
//            Toast.makeText(ForgetPasswordActivity.this,
//                    "Permission already granted",
//                    Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ProfileActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(ProfileActivity.this,
                        "CAMERA Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ProfileActivity.this,
                        "STORAGE_PERMISSION permission granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(ProfileActivity.this,
                        "STORAGE_PERMISSION Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }

    public void choosePhotoFromGallary() {


        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);*/

        //uploadUrl = 3;
        dispatchTakePictureIntent();
    }

    private void showFileChooser() {


        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                filePath = data.getData();
                Bundle bundle = data.getExtras();
                try {
                    if (filePath != null) {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                        image_profile.setImageBitmap(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            if (bitmap != null) {

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                file = new File(getRealPathFromURI(tempUri));
                mCurrentPhotoPath = file.getPath();
                selectedFilename = file.getName();

                //updateProfile(bitmap);


                //updateProfile(bitmap);

                /*new ImageCompressionAsyncTask(this).execute(mCurrentPhotoPath,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "");*/
            }


        } else if (requestCode == CAMERA) {

            if (data != null) {
                filePath = data.getData();

                try {
                    if (filePath != null) {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                        image_profile.setImageBitmap(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            file = new File(mCurrentPhotoPath);

            selectedFilename = file.getName();


            /*new ImageCompressionAsyncTask(this).execute(mCurrentPhotoPath,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "");*/

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_" + getAlphaNumericString(2), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (bitmap == null) {

//            Commanutils.showToast(getBaseContext(), "Please select Image File");

        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // String imageFileName = "JPEG_" + timeStamp + "_";


        // String imageFileName = "Ans" + "_" + student.getStudentId() + "_" + Calendar.getInstance().getTimeInMillis();


        String imageFileName = "Title_" + getAlphaNumericString(2);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      /*  File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");*/

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("TAG", "photo path = " + mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Ensure that there's a camera activity to handle the intent
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File

                //Toast.makeText(ProfileActivity.this, "exception" + ex.getMessage(), Toast.LENGTH_LONG).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this, "com.app.genieususer.provider", photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, CAMERA);
            } else {
                //Toast.makeText(ProfileActivity.this, "exception: Photo file is null", Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        ProgressDialog progressDialog;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
            progressDialog = new ProgressDialog(ProfileActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   progressDialog.show();
            progressDialog.setMessage("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String filePath = null;

            if (params[0] == null || params[1] == null) {

            } else {
                filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            }


            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {

            File imageFile = null;

            if (s == null) {

            } else {

                imageFile = new File(s);

                selectedFilename = imageFile.getName();
            }

            AndroidNetworking.upload(ADD_CAMERA_IMAGES)
                    .addMultipartFile("pic", imageFile)
                    .addMultipartParameter("profile_img", selectedFilename)
                    .addMultipartParameter("name", edit_name.getText().toString())
                    .addMultipartParameter("email", edit_email.getText().toString())
                    .addMultipartParameter("branch", edit_branch.getText().toString())
                    .addMultipartParameter("mobile_no", edit_mobile.getText().toString())
                    .addMultipartParameter("bio", edit_bio.getText().toString())
                    .addMultipartParameter("id", String.valueOf(student.getId()))
                    .setTag("img")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // do anything with progress
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            progressDialog.dismiss();

                            image_profile.setImageURI(Uri.fromFile(file));


                            Log.e("ResponseImage", response.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());

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

                                        startActivity(new Intent(getBaseContext(), ProfileActivity.class).putExtra("from", "0"));

                                    }

                                } else {

                                    Toast.makeText(getBaseContext(), "Uploading Failed..Please Try Again.", Toast.LENGTH_SHORT).show();


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            {

                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error

                            progressDialog.dismiss();

                            Log.e("UploadingError", error.toString());

                            Toast.makeText(getBaseContext(), "Uploading Failed..Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}