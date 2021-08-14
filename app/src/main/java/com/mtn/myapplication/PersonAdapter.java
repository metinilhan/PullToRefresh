package com.mtn.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    List<Person> personList;

    public PersonAdapter() {
        this.personList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_item_person, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = personList.get(position);
        String displayText = String.format("%s(%d)", person.getFullName(), person.getId());
        holder.txtName.setText(displayText);

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void addItems(boolean isRefresh, List<Person> newPersons) {
        if (isRefresh) {
            personList.clear();
        }
        for (Person newPerson : newPersons) {
           boolean isExist = personList.stream().anyMatch(x->x.getId()==newPerson.getId());
           if(!isExist) personList.add(newPerson);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
