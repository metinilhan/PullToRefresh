package com.mtn.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<NewsItem> newsItems;
    View.OnClickListener onClickListener;
    public NewsAdapter(View.OnClickListener onClickListener) {
        this.newsItems = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_item_news, parent, false);
        contactView.setOnClickListener(onClickListener);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem person = newsItems.get(position);
        holder.txtName.setText(person.getTitle());
        holder.txtName.setText(person.getTitle());
        holder.txtName.setTag(person);
        Picasso.get().load(person.getImageUrl()).fit().centerCrop(Gravity.TOP)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public void addItems(boolean isRefresh, List<NewsItem> newPersons) {
        if (isRefresh) {
            newsItems.clear();
        }
        for (NewsItem newPerson : newPersons) {
            newsItems.add(newPerson);
        }
        notifyDataSetChanged();
    }

    public NewsItem getItem(int itemPosition) {
       return newsItems.get(itemPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtTitle);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
