package com.example.mid1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private ImageButton btnSearchBack;
    private Button btnClearAll;
    private ListView lvSearchHistory;
    private SearchHistoryAdapter historyAdapter;
    private ArrayList<String> searchHistoryList;

    // Required in task
    private static final String SearchHashKey = "search_history";
    private static final String SEARCH_PREFS = "SearchPrefs";

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);
        btnSearchBack = view.findViewById(R.id.btn_search_back);
        btnClearAll = view.findViewById(R.id.btn_clear_all);
        lvSearchHistory = view.findViewById(R.id.lv_search_history);

        sharedPreferences = requireContext().getSharedPreferences(SEARCH_PREFS, Context.MODE_PRIVATE);
        searchHistoryList = new ArrayList<>();

        historyAdapter = new SearchHistoryAdapter(requireContext(), searchHistoryList);
        lvSearchHistory.setAdapter(historyAdapter);

        loadSearchHistory();
        setupSearchAction();

        // Click history item -> search again
        lvSearchHistory.setOnItemClickListener((parent, itemView, position, id) -> {
            String selectedSearch = searchHistoryList.get(position);
            etSearch.setText(selectedSearch);
            etSearch.setSelection(selectedSearch.length());
            handleSearch(selectedSearch);
        });

        // Back arrow requirement: hide keyboard only (do NOT close app)
        btnSearchBack.setOnClickListener(v -> {
            etSearch.clearFocus();
            hideKeyboard();
        });

        // Clear all history
        btnClearAll.setOnClickListener(v -> clearSearchHistory());
    }

    private void setupSearchAction() {
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String query = etSearch.getText().toString().trim();
                handleSearch(query);
                hideKeyboard();
                etSearch.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void handleSearch(String query) {
        if (query == null || query.trim().isEmpty()) return;

        if (searchProduct(query.trim())) {
            saveSearchHistory(query.trim());
            showProductFoundDialog();
        }
    }

    // Exact match, case-insensitive
    private boolean searchProduct(String query) {
        for (items item : MyApplication.items) {
            if (item != null && item.getName() != null &&
                    item.getName().trim().equalsIgnoreCase(query)) {
                return true;
            }
        }
        return false;
    }

    private void saveSearchHistory(String query) {
        Set<String> current = sharedPreferences.getStringSet(SearchHashKey, new HashSet<>());
        Set<String> updated = new HashSet<>(current); // avoid mutating returned set directly

        // prevent duplicates ignoring case
        boolean exists = false;
        for (String s : updated) {
            if (s.equalsIgnoreCase(query)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            updated.add(query);
            sharedPreferences.edit().putStringSet(SearchHashKey, updated).apply();
            loadSearchHistory();
        }
    }

    private void loadSearchHistory() {
        Set<String> searchHistory = sharedPreferences.getStringSet(SearchHashKey, new HashSet<>());
        searchHistoryList.clear();
        searchHistoryList.addAll(searchHistory);
        historyAdapter.notifyDataSetChanged();
    }

    private void clearSearchHistory() {
        sharedPreferences.edit().remove(SearchHashKey).apply();
        searchHistoryList.clear();
        historyAdapter.notifyDataSetChanged();
    }

    private void showProductFoundDialog() {
        new AlertDialog.Builder(requireContext())
                .setMessage("Product Found.")
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSearchHistory();
    }
}