package com.example.puccaring.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.puccaring.Fragments.SearchFragment;
import com.example.puccaring.Fragments.SearchParentFragment;
import com.example.puccaring.Fragments.SearchTagFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private SearchParentFragment searchParentFragment;

    private void createList() {
        fragments.add(new SearchFragment());
        fragments.add(new SearchTagFragment());
    }

    public SearchPagerAdapter(FragmentManager fm, SearchParentFragment searchParentFragment) {
        super(fm);
        this.searchParentFragment = searchParentFragment;
        createList();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "User";
        else return "Tag";
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
