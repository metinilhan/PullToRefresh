package com.mtn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsActivity extends AppCompatActivity implements ConnectionResponce, View.OnClickListener {
    private static final String TAG = "MTNRefreshMainActivity";

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView txtEmptyList, txtLoading;

    NewsAdapter personAdapter;

    boolean isLoading = false;
    String next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        bindDataAndListeners();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        txtEmptyList = findViewById(R.id.txtEmpty);
        txtLoading = findViewById(R.id.txtLoaing);
        recyclerView = findViewById(R.id.listView);

    }

    private void bindDataAndListeners() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new NewsAdapter(this);
        recyclerView.setAdapter(personAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        OnScrollListener onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == personAdapter.getItemCount() - 1) {
                    if (next != null) {
                        loadMoreData();
                    } else {
                        Toast.makeText(NewsActivity.this, "No More Data", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

        recyclerView.addOnScrollListener(onScrollListener);
        getData(true);
    }

    private void refreshData() {
        Log.d(TAG, "refreshData: ");
        getData(true);
    }

    private void loadMoreData() {
        Log.d(TAG, "loadMoreData: ");
        getData(false);
    }

    private void getData(boolean isRefresh) {
        if (isLoading) {
            if (isRefresh) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        if (!isRefresh) {
            txtLoading.setVisibility(View.VISIBLE);
        } else {
            next = null;
        }
        isLoading = true;

        ConnectionHelper.getInstance(this).get(this, isRefresh);
    }


    @Override
    public String getUrl() {
        return Constants.NEWS_URL;
    }

    @Override
    public void onSuccess(String response, Boolean isRefresh) {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        txtLoading.setVisibility(View.GONE);
        txtEmptyList.setVisibility(View.GONE);
        Log.d(TAG, "onSuccess: " + response);
        next = "2";//fetchResponse.getNext();
        // personAdapter.addItems(isRefresh, fetchResponse.getPeople());

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("news");
            ArrayList<NewsItem> newsItems = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemJson = jsonArray.getJSONObject(i);
                NewsItem newsItem = new NewsItem();
                newsItem.setTitle(itemJson.getString("title"));
                newsItem.setCategory(itemJson.getString("category"));
                newsItem.setSpot(itemJson.getString("spot"));
                newsItem.setImageUrl(itemJson.getString("imageUrl"));
                newsItem.setVideoUrl(itemJson.getString("videoUrl"));
                newsItems.add(newsItem);

            }
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle("Titlr");
            newsItem.setImageUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
            newsItem.setSpot("Spot");
            newsItem.setSpot("Açıklama");
            newsItems.add(newsItem);
            personAdapter.addItems(isRefresh, newsItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (personAdapter.getItemCount() <= 0) {
            txtEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFail(String message) {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        txtLoading.setVisibility(View.GONE);
        txtEmptyList.setVisibility(View.GONE);
        Log.d(TAG, "getData: Fail" + message);
        Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
        NewsItem newsItem = new NewsItem();
        newsItem.setTitle("Titlr");
        newsItem.setVideoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
        newsItem.setSpot("Spot");
        newsItem.setSpot("Açıklama");
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        newsItems.add(newsItem);
        personAdapter.addItems(true, newsItems);
        if (personAdapter.getItemCount() <= 0) {
            txtEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        NewsItem item = personAdapter.getItem(itemPosition);
        Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
        intent.putExtra("newsItem", item);
        startActivity(intent);
    }
}