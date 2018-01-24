package com.bonepeople.android.future.future.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bonepeople.android.future.future.R;
import com.bonepeople.android.future.future.adapter.ConstructorAdapter;
import com.bonepeople.android.future.future.base.InternetServices;
import com.bonepeople.android.future.future.model.ConstructorInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListActivity extends AppCompatActivity implements Callback<ResponseBody> {
    private ConstructorAdapter adapter;
    private ArrayList<ConstructorInfo> data = new ArrayList<>();
    private int pageIndex = 0, pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recycler = findViewById(R.id.recyclerView);

        adapter = new ConstructorAdapter(data);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        onRefresh();
    }

    public void onRefresh() {
        pageIndex = 0;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.shownest.com/").build();
        InternetServices internetServices = retrofit.create(InternetServices.class);
        Call<ResponseBody> call = internetServices.getConList(true, 100101, "", 1, pageIndex);
        call.enqueue(this);
    }

    public void onLoad() {
        if (pageIndex < pageCount - 1) {
            pageIndex++;
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.shownest.com/").build();
            InternetServices internetServices = retrofit.create(InternetServices.class);
            Call<ResponseBody> call = internetServices.getConList(true, 100101, "", 1, pageIndex);
            call.enqueue(this);
        } else {
            Toast.makeText(this, "已经到底了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        try {
            System.out.println("onResponse");
            ResponseBody body = response.body();
            JSONObject object = new JSONObject(body == null ? "" : body.string());
            System.out.println(object);

            pageCount = object.getJSONObject("data").getInt("pageCount");
            JSONArray array = object.getJSONObject("data").getJSONArray("constructerBaseList");
            if (pageIndex == 0)
                data.clear();
            for (int index = 0; index < array.length(); index++) {
                ConstructorInfo info = new ConstructorInfo(array.getJSONObject(index));
                data.add(info);
            }
            adapter.notifyItemRangeInserted(0, adapter.getItemCount());
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JSONException json) {
            json.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        System.out.println("onFailure");
    }
}
