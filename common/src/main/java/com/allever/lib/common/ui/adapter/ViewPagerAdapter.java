package com.allever.lib.common.ui.adapter;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.allever.lib.common.app.BaseFragment;

import java.util.List;

/**
 * @author Allever
 * @date 18/5/21
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragmentList;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<BaseFragment> fragmentList) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = fragmentList;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
