package com.example.mid1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView rvItems , rvDealItems;

    ItemAdapter adapter ;
    DealItemAdapter dealAdapter;
    BuyerProductViewModel viewModel;
    
    LinearLayout llLoading, llEmpty, llError;
    TextView tvErrorMessage;
    MaterialButton btnRetry;
    View svContent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvItems = view.findViewById(R.id.rvItems);
        rvDealItems = view.findViewById(R.id.rvDealItems);
        
        llLoading = view.findViewById(R.id.ll_loading);
        llEmpty = view.findViewById(R.id.ll_empty);
        llError = view.findViewById(R.id.ll_error);
        tvErrorMessage = view.findViewById(R.id.tv_error_message);
        btnRetry = view.findViewById(R.id.btn_retry);
        svContent = view.findViewById(R.id.sv_content);

        adapter = new ItemAdapter(requireContext(), new ArrayList<>());
        dealAdapter = new DealItemAdapter(requireContext(), new ArrayList<>());
        
        rvItems.setLayoutManager(new GridLayoutManager(requireContext(),2));
        rvItems.setAdapter(adapter);
        rvItems.setNestedScrollingEnabled(false);
        
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvDealItems.setLayoutManager(gridLayoutManager);
        rvDealItems.setAdapter(dealAdapter);
        rvDealItems.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(this).get(BuyerProductViewModel.class);
        
        setupObservers();
        setupRetryButton();
        
        viewModel.attachListener();
    }

    private void setupObservers() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                ArrayList<items> itemsList = new ArrayList<>(products);
                adapter.updateItems(itemsList);
                
                if (MyApplication.items.isEmpty()) {
                    MyApplication.items.addAll(itemsList);
                } else {
                    MyApplication.items.clear();
                    MyApplication.items.addAll(itemsList);
                }
            }
        });

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), state -> {
            if (state != null) {
                updateLoadingState(state);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                tvErrorMessage.setText(error);
            }
        });
    }

    private void setupRetryButton() {
        btnRetry.setOnClickListener(v -> {
            viewModel.detachListener();
            viewModel.attachListener();
        });
    }

    private void updateLoadingState(int state) {
        llLoading.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
        svContent.setVisibility(View.GONE);

        switch (state) {
            case BuyerProductRepository.LOADING_STATE_LOADING:
                llLoading.setVisibility(View.VISIBLE);
                break;
            case BuyerProductRepository.LOADING_STATE_SUCCESS:
                svContent.setVisibility(View.VISIBLE);
                break;
            case BuyerProductRepository.LOADING_STATE_EMPTY:
                llEmpty.setVisibility(View.VISIBLE);
                break;
            case BuyerProductRepository.LOADING_STATE_ERROR:
                llError.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.detachListener();
        }
    }
}