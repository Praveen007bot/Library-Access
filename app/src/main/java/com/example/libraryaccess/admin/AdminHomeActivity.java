package com.example.libraryaccess.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.TextView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.databinding.ActivityAdminHomeBinding;
import com.example.libraryaccess.databinding.ActivityHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {


    ActivityAdminHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvAddBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        binding.tvReservationRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ReservationRequestActivity.class);
                startActivity(intent);
            }
        });

        binding.tvPostFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ReservedBookActivity.class);
                startActivity(intent);
            }
        });

        binding.tvAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AddEventsActivity.class);
                startActivity(intent);
            }
        });

        binding.tvBorrowBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, BorrowRequestAdminActivity.class);
                startActivity(intent);
            }
        });




    }
}