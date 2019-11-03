package com.example.puccaring.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.puccaring.Model.User;
import com.example.puccaring.PostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.puccaring.Adapter.PostAdapter;
import com.example.puccaring.Adapter.StoryAdapter;
import com.example.puccaring.Model.Post;
import com.example.puccaring.Model.Story;
import com.example.puccaring.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    String currentUserId;
    private List<String> followingList;
    private ImageView inBox_btn,search_btn,post_profile_image,post_btn;
    private TextView post_status;

    ProgressBar progress_circular;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        inBox_btn = view.findViewById(R.id.inbox_btn);
        search_btn = view.findViewById(R.id.search_btn);
        post_btn = view.findViewById(R.id.post_btn);
        post_status = view.findViewById(R.id.post_status);
        post_profile_image = view.findViewById(R.id.post_profile_image);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        recyclerView_story = view.findViewById(R.id.recycler_view_story);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager);
        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(getContext(), storyList);
        recyclerView_story.setAdapter(storyAdapter);

        progress_circular = view.findViewById(R.id.progress_circular);

        inBox_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListFragment chatListFragment = new ChatListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,chatListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
         post_status.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(), PostActivity.class));
             }
         });
         post_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(),PostActivity.class));
             }
         });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageurl().equals("default")){
                    post_profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    try {
                        Glide.with(getContext()).load(user.getImageurl()).into(post_profile_image);
                    } catch (Exception e) {
                        Log.e("@DKTK@Ex", e.getMessage());
                        post_profile_image.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkFollowing();
        return view;
    }


    private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readPosts();
                readStory();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for (String id : followingList){
                        if (post.getPublisher().equals(id)){
                            postList.add(post);
                        }
                    }
                    currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(post.getPublisher().equals(currentUserId)){
                            postList.add(post);
                        }


                }

                postAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("", 0, 0, "",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id : followingList) {
                    int countStory = 0;
                    Story story = null;
                    for (DataSnapshot snapshot : dataSnapshot.child(id).getChildren()) {
                        story = snapshot.getValue(Story.class);
                        if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                            countStory++;
                        }
                    }
                    if (countStory > 0){
                        storyList.add(story);
                    }
                }

                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
