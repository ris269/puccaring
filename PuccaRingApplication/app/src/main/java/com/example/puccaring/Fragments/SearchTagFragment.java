package com.example.puccaring.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puccaring.Adapter.SearchPagerAdapter;
import com.example.puccaring.Adapter.TagAdapter;
import com.example.puccaring.Adapter.UserAdapter;
import com.example.puccaring.Model.Post;
import com.example.puccaring.Model.User;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchTagFragment extends Fragment {

    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
    private List<String> strs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        strs = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext(), strs);
        recyclerView.setAdapter(tagAdapter);

        readUsers();
        return view;
    }

    private void searchTag(final String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Posts");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strs.clear();
                Set<String> stringSet = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
//                    strs.add(user);
                    if (post != null) {
                        List<String> strings = post.getListTagSeparate();
                        if (strings != null)
                            for (String value : strings) {
                                if (value.toLowerCase().contains(s))
                                    stringSet.add(value);
                            }
                    }

                }
                strs.addAll(stringSet);
                Log.e("value", new Gson().toJson(strs));

                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (search_bar.getText().toString().equals("")) {
//                    userList.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        User user = snapshot.getValue(User.class);
//
//                        userList.add(user);
//
//                    }
//
//                    userAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void clearSearch() {

    }

    public void seachingValue(CharSequence charSequence) {
        searchTag(charSequence.toString());
    }
}
