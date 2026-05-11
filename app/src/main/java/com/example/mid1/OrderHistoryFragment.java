package com.example.mid1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Order History Fragment
 * Displays seller's order history
 */
public class OrderHistoryFragment extends Fragment {

    private TextView tvOrdersEmpty;

    public OrderHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvOrdersEmpty = view.findViewById(R.id.tv_orders_empty);
        tvOrdersEmpty.setText("No orders yet");
    }
}

