package com.example.libraryaccess.admin.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.BorrowRequest;

import java.util.List;

public class BorrowRequestAdapter extends RecyclerView.Adapter<BorrowRequestAdapter.ViewHolder> {

    private List<BorrowRequest> borrowRequests;

    public BorrowRequestAdapter(List<BorrowRequest> borrowRequests) {
        this.borrowRequests = borrowRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrow_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRequest request = borrowRequests.get(position);
        holder.textBookName.setText("Book Name: " + request.getBookName());
        holder.textFriendId.setText("Friend's ID or Email: " + request.getFriendIdOrEmail());
        holder.textStartDate.setText("Borrow Start Date: " + request.getStartDate());
        holder.textEndDate.setText("Borrow End Date: " + request.getEndDate());
        // Convert image URL String to Uri
        Uri frontImageUri = Uri.parse(request.getFrontImageUrl());
        Uri backImageUri = Uri.parse(request.getBackImageUrl());

        // Set the image URIs
        holder.imageViewFront.setImageURI(frontImageUri);
        holder.imageViewBack.setImageURI(backImageUri);
        // Set up onClickListeners for approve and reject buttons
        holder.buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle approve action
            }
        });

        holder.buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reject action
            }
        });
    }

    @Override
    public int getItemCount() {
        return borrowRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textBookName;
        TextView textFriendId;
        TextView textStartDate;
        TextView textEndDate;
        Button buttonApprove;
        Button buttonReject;
        ImageView imageViewFront;
        ImageView imageViewBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBookName = itemView.findViewById(R.id.textBookName);
            textFriendId = itemView.findViewById(R.id.textFriendId);
            textStartDate = itemView.findViewById(R.id.textStartDate);
            textEndDate = itemView.findViewById(R.id.textEndDate);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            imageViewFront = itemView.findViewById(R.id.imageViewFront);
            imageViewBack = itemView.findViewById(R.id.imageViewBack);
        }
    }
}
