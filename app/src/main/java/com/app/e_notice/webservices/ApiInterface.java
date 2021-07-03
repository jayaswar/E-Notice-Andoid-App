package com.app.e_notice.webservices;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("searchStudentIdinGroup.php")
    Call<JsonObject> searchStudentIdinGroup(@Query("id") String id);

    @GET("login.php")
    Call<JsonObject> login(@Query("username") String email, @Query("password") String password);

    @GET("login_group.php")
    Call<JsonObject> login_group(@Query("username") String email, @Query("password") String password);

    @GET("admin_login.php")
    Call<JsonObject> adminlogin(@Query("username") String email, @Query("password") String password);

    @GET("register.php")
    Call<JsonObject> register(@Query("name") String name,
                              @Query("email") String email,
                              @Query("mobile_no") String mobile_no,
                              @Query("branch") String branch);

    @GET("addGroup.php")
    Call<JsonObject> addGroup(@Query("name") String name,
                              @Query("admin") String admin,
                              @Query("admin_id") String admin_id,
                              @Query("username") String username,
                              @Query("password") String password
    );

    @GET("getAllNotice.php")
    Call<JsonObject> getAllNotice(@Query("id") String id);

    @GET("getAllNews.php")
    Call<JsonObject> getAllNews(@Query("id") String id);

    @GET("getAllGroups.php")
    Call<JsonObject> getAllGroups();

    @GET("getStudentsGroups.php")
    Call<JsonObject> getStudentsGroups(@Query("id") String id);


    @GET("getComments.php")
    Call<JsonObject> getComments(@Query("id") String id);

    @GET("addComment.php")
    Call<JsonObject> addComment(@Query("group_id") String group_id,
                                @Query("notice_id") String notice_id,
                                @Query("text") String text,
                                @Query("student_id") String student_id);

    @GET("addSubscriber.php")
    Call<JsonObject> addSubscriber(@Query("group_id") String group_id,
                                   @Query("student_id") String student_id);

    @GET("getAllStudents.php")
    Call<JsonObject> getAllStudents();

    @GET("updateProfile.php")
    Call<JsonObject> updateProfile(@Query("id") String id,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("mobile_no") String mobile_no,
                                   @Query("branch") String branch,
                                   @Query("bio") String bio);

    @GET("updateFirebaseToken.php")
    Call<JsonObject> updateFirebaseToken(@Query("user_id") String user_id,
                                         @Query("token") String token);


}
