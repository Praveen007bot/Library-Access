package com.example.libraryaccess.user.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libraryaccess.databinding.FragmentMoreBinding; // Import the generated binding class

import com.example.libraryaccess.user.BorrowRequestActivity;
import com.example.libraryaccess.user.EventsActivity;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding; // Declare binding variable

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using data binding
        binding = FragmentMoreBinding.inflate(inflater, container, false);

        // Set onClickListener for the button or view that should trigger navigation
        binding.tvEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EventsActivity when the button is clicked
                Intent intent = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }
        });

        binding.tvBorrowBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BorrowRequestActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Nullify the binding reference
    }
}
