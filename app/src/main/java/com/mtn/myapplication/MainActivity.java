package com.mtn.myapplication;

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


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PullRefreshMainActivity";

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
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
        recyclerView = findViewById(R.id.listView);
    }

    private void bindDataAndListeners() {
        dataSource = new DataSource();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personAdapter = new PersonAdapter();
        recyclerView.setAdapter(personAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        OnScrollListener onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == personAdapter.getItemCount() - 1) {
                    if(next!=null) {
                        loadMoreData();
                    }
                    else  {
                        Toast.makeText(MainActivity.this, "No More Data", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

        recyclerView.addOnScrollListener(onScrollListener);
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
        dataSource.fetch(next, ((fetchResponse, fetchError) -> {
            isLoading = false;
            swipeRefreshLayout.setRefreshing(false);
            txtLoading.setVisibility(View.GONE);
            txtEmptyList.setVisibility(View.GONE);
            if (fetchResponse != null) {
                next = fetchResponse.getNext();
                personAdapter.addItems(isRefresh, fetchResponse.getPeople());
                Log.d(TAG, "getData: Success count " + fetchResponse.getPeople().size());
                Log.d(TAG, "getData: next : " + next);
                recyclerView.scrollBy(0,30);
            } else {
                Log.d(TAG, "getData: Fail" + fetchError.getErrorDescription());
                Snackbar snackbar = Snackbar.make(recyclerView, fetchError.getErrorDescription(), Snackbar.LENGTH_LONG);
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