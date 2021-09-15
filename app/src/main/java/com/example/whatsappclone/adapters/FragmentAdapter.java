package com.example.whatsappclone.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappclone.fragments.CallsFragment;
import com.example.whatsappclone.fragments.ChatsFragment;
import com.example.whatsappclone.fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 1 :
                return new StatusFragment();
            case 2 :
                return new CallsFragment();
            default:
                return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";

        switch (position) {
            case 1 :
                title = "Status";
                break;
            case 2 :
                title = "Calls";
                break;
            default :
                title = "Chats";
        }

        return title;
    }
}
