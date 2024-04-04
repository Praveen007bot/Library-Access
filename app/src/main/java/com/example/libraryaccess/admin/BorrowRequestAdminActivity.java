package com.example.libraryaccess.admin;

// MainActivity.java

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.admin.adapter.BorrowRequestAdapter;
import com.example.libraryaccess.dataclass.BorrowRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BorrowRequestAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowRequestAdapter adapter;
    private List<BorrowRequest> borrowRequests;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_request_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        borrowRequests = new ArrayList<>();
        adapter = new BorrowRequestAdapter(borrowRequests);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("borrowRequests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                borrowRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                        BorrowRequest request = requestSnapshot.getValue(BorrowRequest.class);
                        borrowRequests.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
