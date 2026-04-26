package com.example.mid1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    RecyclerView rvItems , rvDealItems;

    ItemAdapter adapter ;
    DealItemAdapter dealAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvItems = view.findViewById(R.id.rvItems);
        rvDealItems = view.findViewById(R.id.rvDealItems);

        // Only populate items list once to avoid duplicates when re-navigating
        if (MyApplication.items.isEmpty()) {
            MyApplication.deal_items.add(new items("Sony Premium Wireless Headphones", "$349.99", R.drawable.sony_premium_1, "Model: WH-1000M4, Black", getString(R.string.desc_cards), false));
            MyApplication.deal_items.add(new items("Sony Premium Wireless Headphones", "$349.99", R.drawable.sony_premium_2, "Model: WH-1000M4, Beige", getString(R.string.desc_cards), false));
            MyApplication.deal_items.add(new items("RODE PodMic", "$108.20", R.drawable.rod_podmic, "Dynamic microphone, Speaker microphone", getString(R.string.desc_deal), false));
            MyApplication.items.add(new items("Sony Premium Wireless Headphones", "$349.99", R.drawable.sony_premium_1, "Model: WH-1000M4, Black", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("Sony Premium Wireless Headphones", "$349.99", R.drawable.sony_premium_2, "Model: WH-1000M4, Beige", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("RODE PodMic", "$108.20", R.drawable.rod_podmic, "Dynamic microphone, Speaker microphone", getString(R.string.desc_deal), false));
            MyApplication.items.add(new items("Apple AirPods Pro (2nd Gen)", "$249.00", R.drawable.sony_premium_1, "Model: A2968, Active Noise Cancellation", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("Samsung Galaxy Buds2 Pro", "$199.99", R.drawable.sony_premium_1, "Model: SM-R510, Graphite", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("Bose QuietComfort 45", "$329.00", R.drawable.sony_premium_1, "Wireless Noise Cancelling Headphones", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("Logitech MX Master 3S Mouse", "$99.99", R.drawable.sony_premium_1, "Wireless Precision Mouse, 8000 DPI", getString(R.string.desc_cards), false));
            MyApplication.items.add(new items("Razer BlackWidow V3 Keyboard", "$129.99", R.drawable.sony_premium_1, "Mechanical Gaming Keyboard, Green Switches", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Dell UltraSharp 27 Monitor", "$399.99", R.drawable.sony_premium_1, "27-inch 4K IPS Display", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("HP Envy 13 Laptop", "$899.00", R.drawable.sony_premium_1, "Intel i7, 16GB RAM, 512GB SSD", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Apple iPad Air (5th Gen)", "$599.00", R.drawable.sony_premium_1, "M1 Chip, 10.9-inch Display", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Amazon Echo Dot (5th Gen)", "$49.99", R.drawable.sony_premium_1, "Smart Speaker with Alexa", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Google Nest Hub (2nd Gen)", "$99.99", R.drawable.sony_premium_1, "Smart Display with Assistant", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Canon EOS 1500D DSLR", "$549.00", R.drawable.sony_premium_1, "24.1MP Camera with Kit Lens", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Sony PlayStation 5", "$499.99", R.drawable.sony_premium_1, "Next-Gen Gaming Console", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Xbox Series X", "$499.99", R.drawable.sony_premium_1, "1TB SSD, 4K Gaming Console", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Anker PowerCore 20000", "$59.99", R.drawable.sony_premium_1, "High Capacity Power Bank", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("JBL Flip 6 Speaker", "$129.95", R.drawable.sony_premium_1, "Portable Waterproof Speaker", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("Fitbit Charge 6", "$159.95", R.drawable.sony_premium_1, "Fitness Tracker with Heart Rate Monitor", getString(R.string.desc_cards), false));

            MyApplication.items.add(new items("GoPro Hero 11 Black", "$399.99", R.drawable.sony_premium_1, "4K Action Camera", getString(R.string.desc_cards), false));
        }

        adapter = new ItemAdapter(requireContext(), MyApplication.items);
        dealAdapter = new DealItemAdapter(requireContext(),MyApplication.deal_items);
        rvItems.setLayoutManager(new GridLayoutManager(requireContext(),2));
        rvItems.setAdapter(adapter);
        rvItems.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvDealItems.setLayoutManager(gridLayoutManager);
        rvDealItems.setAdapter(dealAdapter);
        rvDealItems.setNestedScrollingEnabled(false);


    }
}