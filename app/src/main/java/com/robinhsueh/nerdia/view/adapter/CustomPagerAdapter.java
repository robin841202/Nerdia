package com.robinhsueh.nerdia.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class CustomPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public CustomPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public CustomPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    /**
     * Add Fragment in ViewPager2
     * @param fragment tab fragment
     * @param title page title
     */
    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }

    /**
     * Get Page Title
     * @param position index
     * @return page title
     */
    public String getPageTitle(int position){
        return fragmentTitle.get(position);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {return fragmentList.get(position);}

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
