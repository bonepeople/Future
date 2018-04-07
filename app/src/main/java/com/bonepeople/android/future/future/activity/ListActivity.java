package com.bonepeople.android.future.future.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bonepeople.android.future.future.R;
import com.bonepeople.android.future.future.adapter.ConstructorAdapter;
import com.bonepeople.android.future.future.base.InternetServices;
import com.bonepeople.android.future.future.model.ConstructorInfo;
import com.bonepeople.android.future.future.widget.recyclerbox.RecyclerBox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstructorAdapter adapter;
    private ArrayList<ConstructorInfo> data = new ArrayList<>();
    private int pageIndex = 0, pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerBox recyclerBox = findViewById(R.id.recyclerBox);
        RecyclerView recycler = recyclerBox.getRecyclerView();

        adapter = new ConstructorAdapter(data, this);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        onRefresh();
    }

    public void onRefresh() {
        pageIndex = 0;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.shownest.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        InternetServices internetServices = retrofit.create(InternetServices.class);
        Observable<ResponseBody> observable = internetServices.getConList(true, 100101, "", 1, pageIndex);
        onHttpResponse(observable);
    }

    public void onLoad() {
        if (pageIndex < pageCount - 1) {
            pageIndex++;
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.shownest.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            InternetServices internetServices = retrofit.create(InternetServices.class);
            Observable<ResponseBody> observable = internetServices.getConList(true, 100101, "", 1, pageIndex);
            onHttpResponse(observable);
        } else {
            Toast.makeText(this, "已经到底了", Toast.LENGTH_SHORT).show();
        }
    }

    private void onHttpResponse(final Observable<ResponseBody> observable) {
        observable
            .subscribeOn(Schedulers.io())
            .map(new Function<ResponseBody, JSONObject>() {
                @Override
                public JSONObject apply(ResponseBody responseBody) throws Exception {
                    return new JSONObject(responseBody.string());
                }
            })
            .filter(new Predicate<JSONObject>() {
                @Override
                public boolean test(JSONObject jsonObject) throws Exception {
                    if (jsonObject.optInt("state", 0) == 1)
                        return true;
                    else
                        throw new IllegalArgumentException(jsonObject.optString("msg"));
                }
            })
            .flatMap(new Function<JSONObject, ObservableSource<ConstructorInfo>>() {
                @Override
                public ObservableSource<ConstructorInfo> apply(JSONObject jsonObject) throws Exception {
                    pageCount = jsonObject.optJSONObject("data").optInt("pageCount", 1);
                    final JSONArray array = jsonObject.optJSONObject("data").optJSONArray("constructerBaseList");
                    if (pageIndex == 0)
                        data.clear();
                    return Observable.create(new ObservableOnSubscribe<ConstructorInfo>() {
                        @Override
                        public void subscribe(ObservableEmitter<ConstructorInfo> e) throws Exception {
                            for (int index = 0; index < array.length(); index++) {
                                JSONObject object = array.optJSONObject(index);
                                e.onNext(new ConstructorInfo(object));
                            }
                            e.onComplete();
                        }
                    });
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ConstructorInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(ConstructorInfo value) {
                    data.add(value);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    adapter.notifyItemRangeInserted(0, adapter.getItemCount());
                }
            });
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.clickableTagKey);
        Toast.makeText(this, data.get(position).getConsName(), Toast.LENGTH_SHORT).show();
    }
}
