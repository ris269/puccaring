package com.example.puccaring.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.puccaring.Adapter.SearchPagerAdapter;
import com.example.puccaring.R;
import com.google.android.material.tabs.TabLayout;


public class SearchParentFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tab_layout;

    private SearchPagerAdapter searchPagerAdapter;


    private EditText search_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_search, container, false);
        search_bar = view.findViewById(R.id.search_bar);
        viewPager = view.findViewById(R.id.view_pager_search);
        tab_layout = view.findViewById(R.id.tab_layout);
        searchPagerAdapter = new SearchPagerAdapter(this.getChildFragmentManager(), this);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searching(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewPager.setAdapter(searchPagerAdapter);
        tab_layout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                search_bar.setText("");
                Log.e("pageChange", "" + position);
                ((SearchFragment) searchPagerAdapter.getItem(0)).clearSearch();
                ((SearchTagFragment) searchPagerAdapter.getItem(1)).clearSearch();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void searching(CharSequence charSequence) {
        if (viewPager.getCurrentItem() == 0) {

            ((SearchFragment) searchPagerAdapter.getItem(0)).seachingValue(charSequence);
        } else if (viewPager.getCurrentItem() == 1) {

            ((SearchTagFragment) searchPagerAdapter.getItem(1)).seachingValue(charSequence);
        }
    }
}