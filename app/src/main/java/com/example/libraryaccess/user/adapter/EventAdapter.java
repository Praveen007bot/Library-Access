package com.example.libraryaccess.user.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textEventName.setText(event.getEventName());
        holder.textEventDate.setText(event.getEventDate());
        holder.textOrganizerName.setText(event.getOrganizerName());
        holder.textOrganizerNumber.setText(event.getOrganizerNumber());

        // Load event poster image using Picasso or any other image loading library
        Picasso.get().load(event.getEventPosterUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textEventName;
        TextView textEventDate;
        TextView textOrganizerName;
        TextView textOrganizerNumber;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textEventName = itemView.findViewById(R.id.textEventName);
            textEventDate = itemView.findViewById(R.id.textEventDate);
            textOrganizerName = itemView.findViewById(R.id.textOrganizerName);
            textOrganizerNumber = itemView.findViewById(R.id.textOrganizerNumber);
        }
    }
}
