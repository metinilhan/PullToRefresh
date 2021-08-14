package com.mtn.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView listView;
    TextView txtEmptyList, txtLoading;


    PersonAdapter personAdapter;
    DataSource dataSource;

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
        listView = findViewById(R.id.listView);
    }

    private void bindDataAndListeners() {
        dataSource = new DataSource();
        listView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter();
        listView.setAdapter(personAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        OnScrollListener onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: ");
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == personAdapter.getItemCount() - 1 && next != null) {
                    loadMoreData();
                }

            }
        };

        listView.addOnScrollListener(onScrollListener);
    }

    private void refreshData() {
        next = null;
        getData(true);
    }

    private void loadMoreData() {
        Log.d(TAG, "loadMore: ");
        txtLoading.setVisibility(View.VISIBLE);
        getData(false);
    }

    private void getData(boolean isRefresh) {
        if (isLoading) {
            return;
        }
        if (!isRefresh) {
            txtLoading.setVisibility(View.VISIBLE);
        }
        isLoading = true;
        dataSource.fetch(next, ((fetchResponse, fetchError) -> {
            isLoading = false;
            swipeRefreshLayout.setRefreshing(false);
            txtLoading.setVisibility(View.GONE);
            txtEmptyList.setVisibility(View.GONE);
            if (fetchResponse != null) {
                next = fetchResponse.getNext();
                personAdapter.addItems(isRefresh, fetchResponse.getPeople());
                //listView.scrollToPosition(personAdapter.getItemCount() - fetchResponse.getPeople().size());
            } else {
                Snackbar snackbar = Snackbar.make(listView, fetchError.getErrorDescription(), Snackbar.LENGTH_LONG);
                snackbar.setAction(isRefresh ? "Refresh" : "Try again", view -> {
                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    } else {
                        loadMoreData();
                    }
                });
                snackbar.show();
            }
            if (personAdapter.getItemCount() <= 0) {
                txtEmptyList.setVisibility(View.VISIBLE);
            }
            return null;
        }));
    }


}