package com.app.e_notice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.app.e_notice.webservices.ApiClient;
import com.app.e_notice.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Group> noticeArrayList;
    ApiInterface apiInterface;
    Student student;
    ArrayList<String> group_ids;
    Boolean returenToStudent;


    public GroupAdapter(Context context, ArrayList<Group> noticeArrayList) {

        this.context = context;
        this.noticeArrayList = noticeArrayList;
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Student", "");
        student = gson.fromJson(json, Student.class);
    }

    @Override
    public int getCount() {
        return noticeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.group_view, viewGroup, false);

        Group group = noticeArrayList.get(i);


        group_ids = new ArrayList<>();


        TextView text_notice_title = view.findViewById(R.id.text_notice_title);
        Button btn_subscriber = view.findViewById(R.id.btn_subscriber);


        if (student != null) {

            if (student.getSubscribed_group_id() != null) {

                group_ids = new ArrayList<>(Arrays.asList(student.getSubscribed_group_id().split(",")));

                Log.e("MAIN SUBSCRIBE", group_ids.toString());
            }


            if (group_ids != null)
                if (group_ids.contains(String.valueOf(group.getId()))) {

                    // btn_subscriber.setVisibility(View.GONE);
                    btn_subscriber.setText("UNSUBSCRIBE");
                } else {
                    btn_subscriber.setVisibility(View.VISIBLE);
                }

            btn_subscriber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (btn_subscriber.getText().toString().equals("UNSUBSCRIBE")) {

                        group_ids.remove(String.valueOf(group.getId()));

                        Log.e("UnSubscribed here", group_ids.toString());

                        // Toast.makeText(context, group_ids.toString(), Toast.LENGTH_SHORT).show();

                        Log.e("UnSubscribed", group_ids.toString());

                        addSubscriber(TextUtils.join(",", group_ids));

                    } else {

                        if (group_ids != null) {

                            if (group_ids.size() == 0) {

                                addSubscriber(String.valueOf(group.getId()));

                            } else {
                                if (group_ids.contains(String.valueOf(group.getId()))) {


                                } else {

                                    student.setSubscribed_group_id(student.getSubscribed_group_id() + "," + group.getId());

                                    //group_ids = Arrays.asList(student.getSubscribed_group_id().split(","));

                                    group_ids.add(String.valueOf(group.getId()));

                                    //group_ids = new ArrayList<>(Arrays.asList(student.getSubscribed_group_id().split(",")));
                                    //group_ids.add(String.valueOf(group.getId()));

                                    addSubscriber(TextUtils.join(",", group_ids));
                                }


                            }
                        }

                    }


                }
            });

            text_notice_title.setText(group.getName());
        }

        return view;
    }

    private void addSubscriber(String group_id) {

        apiInterface.searchStudentIdinGroup(String.valueOf(student.getId())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {
                        returenToStudent=true;

                    } else {
                        returenToStudent=false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });


        apiInterface.addSubscriber(group_id, String.valueOf(student.getId())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getString("code").equals("200")) {

                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                        student.setSubscribed_group_id(TextUtils.join(",", group_ids));

                        SharedPreferences sharedPreferences = context.getSharedPreferences("NOTICE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(student);
                        prefsEditor.putString("Student", json);
                        prefsEditor.apply();


                        if(returenToStudent==true) {
                            context.startActivity(new Intent(context, HomeActivity.class).putExtra("from", "student"));
                        }else{
                            context.startActivity(new Intent(context, HomeActivity.class).putExtra("from", "group"));
                        }


                    } else {
                        Toast.makeText(context, "Not Subscribed", Toast.LENGTH_SHORT).show();
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
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    ArrayList<Group> filterList = new ArrayList<Group>();
                    for (int i = 0; i < noticeArrayList.size(); i++) {
                        if ((noticeArrayList.get(i).getName().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {

                            Group bean = new Group(noticeArrayList.get(i).getName());
                            filterList.add(bean);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = noticeArrayList.size();
                    results.values = noticeArrayList;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                if (results.count > 0) {
                    noticeArrayList = (ArrayList<Group>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetChanged();
                }
            }
        };

        return myFilter;
    }
}
