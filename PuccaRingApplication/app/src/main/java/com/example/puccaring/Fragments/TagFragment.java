package com.example.puccaring.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puccaring.Adapter.MyFotosAdapter;
import com.example.puccaring.Adapter.TagAdapter;
import com.example.puccaring.Model.Post;
import com.example.puccaring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageView imv_back;
    private TextView tv_title, tv_content;
    private MyFotosAdapter myFotosAdapter;
    private List<Post> postList;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        imv_back = view.findViewById(R.id.imv_back);
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        postList = new ArrayList<>();
        myFotosAdapter = new MyFotosAdapter(getContext(), postList);
        recyclerView.setAdapter(myFotosAdapter);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        String filterTag = null;
        if (getArguments() != null)
            filterTag = getArguments().getString("FILTER_TAG");
        tv_title.setText("#" + filterTag);
        tv_content.setText(filterTag);
        if (filterTag != null)
            getTags(filterTag);
        return view;
    }

    private void getTags(final String filterTag) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                Set<Post> postHashSet = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        List<String> strings = post.getListTagSeparate();
                        if (strings != null)
                            for (String value : strings) {
                                if (value.toLowerCase().equals(filterTag.toLowerCase()))
                                    postHashSet.add(post);
                            }
                    }
                }
                postList.addAll(postHashSet);
                Collections.reverse(postList);
                myFotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static TagFragment createInstance(String tag) {
        TagFragment fragment = new TagFragment();
        Bundle bundle = new Bundle();
        bundle.putString("FILTER_TAG", tag);
        fragment.setArguments(bundle);
        return fragment;
    }
}