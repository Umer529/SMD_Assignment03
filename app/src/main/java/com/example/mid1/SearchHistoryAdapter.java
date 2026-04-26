package com.example.mid1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchHistoryAdapter extends ArrayAdapter<String> {

    private ArrayList<String> searchHistory;
    private Context context;

    public SearchHistoryAdapter(Context context, ArrayList<String> searchHistory) {
        super(context, 0, searchHistory);
        this.context = context;
        this.searchHistory = searchHistory;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_item_search_history, parent, false);
        }

        String searchQuery = searchHistory.get(position);

        TextView tvSearchItem = convertView.findViewById(R.id.tv_search_item);
        ImageButton btnRemoveSearch = convertView.findViewById(R.id.btn_remove_search);

        tvSearchItem.setText(searchQuery);

        btnRemoveSearch.setOnClickListener(v -> {
            searchHistory.remove(position);
            notifyDataSetChanged();

            // Update SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("SearchPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("search_history", new HashSet<>(searchHistory));
            editor.apply();
        });

        return convertView;
    }
}