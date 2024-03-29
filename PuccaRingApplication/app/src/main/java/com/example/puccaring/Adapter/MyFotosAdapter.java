package com.example.puccaring.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.puccaring.Fragments.PostDetailFragment;
import com.example.puccaring.Fragments.SearchParentFragment;
import com.example.puccaring.Fragments.TagFragment;
import com.example.puccaring.Model.Post;
import com.example.puccaring.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyFotosAdapter extends RecyclerView.Adapter<MyFotosAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public MyFotosAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public MyFotosAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fotos_item, parent, false);
        return new MyFotosAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFotosAdapter.ImageViewHolder holder, final int position) {

        final Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                Fragment fragment= ((FragmentActivity)mContext).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                PostDetailFragment searchFragment = new PostDetailFragment();
                FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.hide(fragment);
                }
                fragmentTransaction.add(R.id.fragment_container, searchFragment);
                fragmentTransaction.addToBackStack(PostDetailFragment.class.getSimpleName());
                fragmentTransaction.commit();
//                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new PostDetailFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;


        public ImageViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);

        }
    }
}