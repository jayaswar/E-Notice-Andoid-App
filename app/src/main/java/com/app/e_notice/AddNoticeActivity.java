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
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.e_notice.firebase.Config;
import com.app.e_notice.firebase.MyVolleySingleton;
import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AddNoticeActivity extends AppCompatActivity {

    public static final String ADD_CAMERA_IMAGES = ApiClient.SERVER_URL + "addImages.php?apicall=uploadpic";

    final private String serverKey = "key=" + Config.FIREBASE_SERVER_KEY;
    final private String contentType = "application/json";
    EditText edit_title, edit_desc;
    Button btn_attach, btn_post_notice, btn_post_news;
    ImageView image;
    byte[] inputData;
    File file;
    String selectedFilename = "", mCurrentPhotoPath, selectedType, group_id, group_name, intent_group_id;
    ProgressDialog progressDialog, pd;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    //Spinner spinner_type, spinner_group;
    ArrayList<String> arrayList = new ArrayList<>();
    HashMap<String, Group> groupHashmap = new HashMap<>();
    ArrayList<String> namesList;
    ApiInterface apiInterface;
    Group group;
    Student student;
    private int GALLERY = 1, CAMERA = 2, STORAGE_PERMISSION_CODE = 123, PICK_PDF_REQUEST = 3, uploadUrl;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        apiInterface = ApiClient.getClient(AddNoticeActivity.this).create(ApiInterface.class);

        SharedPreferences sharedPreferences = getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);


        intent_group_id = getIntent().getStringExtra("group_id");


        pd = new ProgressDialog(AddNoticeActivity.this);
        pd.setTitle("Uploading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        progressDialog = new ProgressDialog(AddNoticeActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        edit_title = findViewById(R.id.edit_title);
        edit_desc = findViewById(R.id.edit_desc);
        btn_attach = findViewById(R.id.btn_attach);
        btn_post_notice = findViewById(R.id.btn_post_notice);
        btn_post_news = findViewById(R.id.btn_post_news);
        image = findViewById(R.id.image);
        //spinner_type = findViewById(R.id.spinner_type);
        //spinner_group = findViewById(R.id.spinner_group);

        getAllGroups();

        arrayList.add("News");
        arrayList.add("Achievements");

        //spinner_type.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, arrayList));

        /*spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedType = (String) adapterView.getSelectedItem();

                Toast.makeText(AddNoticeActivity.this, selectedType, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        /*spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected = (String) adapterView.getSelectedItem();
                group = groupHashmap.get(selected.split(" ")[0]);

                if (selected.split(" ").length == 2) {

                    group_id = selected.split(" ")[0];
                    group_name = selected.split(" ")[1];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        checkPermission(Manifest.permission.CAMERA, CAMERA);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 10);

        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();

            }
        });

        btn_post_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedType = "Notice";

                if (mCurrentPhotoPath != null) {

                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                    new ImageCompressionAsyncTask(AddNoticeActivity.this).execute(mCurrentPhotoPath, getBaseContext().getExternalFilesDir("") + "");
                } else {

                    Toast.makeText(AddNoticeActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_post_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedType = "News";

                if (mCurrentPhotoPath != null) {

                    new ImageCompressionAsyncTask(AddNoticeActivity.this).execute(mCurrentPhotoPath, getBaseContext().getExternalFilesDir("") + "");
                } else {

                    Toast.makeText(AddNoticeActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //method to show file chooser
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

    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);*/

        dispatchTakePictureIntent();
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            file = new File(getRealPathFromURI(tempUri));
            selectedFilename = file.getName();

            mCurrentPhotoPath = file.getAbsolutePath();

           /* AndroidNetworking.upload(ADD_CAMERA_IMAGES)
                    .addMultipartFile("pic", file)
                    .addMultipartParameter("image_name", selectedFilename)
                    .addMultipartParameter("group_id", ApiClient.UserId)
                    .addMultipartParameter("desc", edit_desc.getText().toString())
                    .addMultipartParameter("title", edit_title.getText().toString())
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

                            image.setImageURI(Uri.fromFile(file));

                            Toast.makeText(getBaseContext(), "Image Uploaded successfully", Toast.LENGTH_SHORT).show();

                            Log.e("ResponseImage", response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error

                            progressDialog.dismiss();

                            Log.e("UploadingError", error.toString());

                            Toast.makeText(getBaseContext(), "Uploading Failed..Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });*/


        } else if (requestCode == CAMERA) {
           /* bitmap = (Bitmap) data.getExtras().get("data");

            image.setImageBitmap(bitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            selectedFilename = removeSpace(finalFile.getName());
*/
            file = new File(mCurrentPhotoPath);


        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (bitmap == null) {

            Toast.makeText(getBaseContext(), "Please select Image File", Toast.LENGTH_SHORT).show();

        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
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

    public void checkPermission(String permission, int requestCode) {

        if (ContextCompat.checkSelfPermission(AddNoticeActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(AddNoticeActivity.this, new String[]{permission}, requestCode);

        } else {

//            Toast.makeText(ForgetPasswordActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddNoticeActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(AddNoticeActivity.this,
                        "CAMERA Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddNoticeActivity.this,
                        "STORAGE_PERMISSION permission granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(AddNoticeActivity.this,
                        "STORAGE_PERMISSION Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String removeSpace(String str) {

        String string_ = str.replaceAll("[^a-zA-Z0-9]", " ");

        String string = string_.replaceAll("\\s", "");


        return string;
    }

    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);

            image.setImageBitmap(bmp);
            //saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            //todo with exception
        }
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

                Toast.makeText(AddNoticeActivity.this, "exception" + ex.getMessage(), Toast.LENGTH_LONG).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this, "com.app.e_notice.provider", photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, CAMERA);
            } else {
                Toast.makeText(AddNoticeActivity.this, "exception: Photo file is null", Toast.LENGTH_LONG).show();
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // String imageFileName = "JPEG_" + timeStamp + "_";


        // String imageFileName = "Ans" + "_" + student.getStudentId() + "_" + Calendar.getInstance().getTimeInMillis();


        String imageFileName = "Notice" + "_" + getAlphaNumericString(5);
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

    private String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.FIREBASE_API, notification,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddNoticeActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", contentType);
                params.put("Authorization", serverKey);

                return params;
            }
        };
        MyVolleySingleton.getmInstance(AddNoticeActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    private void getAllGroups() {

        namesList = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(AddNoticeActivity.this, android.R.layout.simple_list_item_1, namesList);

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

                            if(String.valueOf(group.getId()) == intent_group_id) {
                                //namesList.add(object.getString("id") + " " + object.getString("name"));
                            }

                            Log.d("namesList", namesList.toString());


                            groupHashmap.put(String.valueOf(group.getId()), group);

                            arrayAdapter.notifyDataSetChanged();


                        }

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //spinner_group.setAdapter(arrayAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        ProgressDialog progressDialog;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
            progressDialog = new ProgressDialog(AddNoticeActivity.this);
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

            String filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {

            File imageFile = new File(s);

            selectedFilename = imageFile.getName();

            AndroidNetworking.upload(ADD_CAMERA_IMAGES)
                    .addMultipartFile("pic", imageFile)
                    .addMultipartParameter("image_name", selectedFilename)
                    .addMultipartParameter("group_id", intent_group_id)
                    .addMultipartParameter("desc", edit_desc.getText().toString())
                    .addMultipartParameter("title", edit_title.getText().toString())
                    .addMultipartParameter("type", selectedType)
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

                            image.setImageURI(Uri.fromFile(file));


                            Log.e("ResponseImage", response.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());

                                if (jsonObject.getString("code").equals("200")) {

                                    Toast.makeText(getBaseContext(), "Successfully Uploaded.", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getBaseContext(), HomeActivity.class).putExtra("from", "group"));


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