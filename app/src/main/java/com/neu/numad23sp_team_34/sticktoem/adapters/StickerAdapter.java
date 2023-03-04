package com.neu.numad23sp_team_34.sticktoem.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.models.Message;
import com.neu.numad23sp_team_34.sticktoem.models.Sticker;

import java.util.List;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private final Context mContext;
    private final List<Sticker> mStickers;

    private final LayoutInflater inflater;

    private final String senderName;
    private final String recipientName;


    private final DatabaseReference mDatabase;

//    private final OnStickerClickListener mClickListener;

    public StickerAdapter(Context context, List<Sticker> stickers,String senderName, String recipientName) {
        mContext = context;
        mStickers = stickers;
//        mClickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.sticker_item, parent, false);
        StickerViewHolder holder = new StickerViewHolder(itemView);
        itemView.setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Sticker sticker = mStickers.get(position);
//        holder.imageView.setOnClickListener(view -> mClickListener.onStickerClick(sticker));
        holder.imageView.setImageResource(sticker.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return mStickers.size();
    }

    class StickerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.stickerImage);

        }

        @Override
        public void onClick(View view) {
            Sticker sticker = mStickers.get(getAdapterPosition());
            Message message = new Message(senderName, recipientName, Integer.toString(sticker.getImageResourceId()), System.currentTimeMillis());
            mDatabase.child("messages").push().setValue(message);

            Toast.makeText(view.getContext(), "Sticker sent!", Toast.LENGTH_SHORT).show();




        }
    }

    public interface OnStickerClickListener {
        void onStickerClick(Sticker sticker);
    }
}

