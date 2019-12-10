package com.example.puccaring.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.puccaring.Fragments.ProfileFragment;
import com.example.puccaring.MainActivity;
import com.example.puccaring.Model.User;
import com.example.puccaring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ImageViewHolder> {

    private Context mContext;
    private List<String> strs = new ArrayList<>();
    private OnTagClicked onTagClicked;

    public TagAdapter(Context context, List<String> users, OnTagClicked onTagClicked) {
        mContext = context;
        this.strs = users;
        this.onTagClicked = onTagClicked;
    }

    @NonNull
    @Override
    public TagAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);
        return new TagAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagAdapter.ImageViewHolder holder, final int position) {
        holder.tv_tag.setText("#" + strs.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTagClicked != null)
                    onTagClicked.onTagClickString(strs.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return strs == null ? 0 : strs.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_tag;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tv_tag = itemView.findViewById(R.id.tv_tag);
        }
    }

    public interface OnTagClicked {
        void onTagClickString(String value);
    }
}