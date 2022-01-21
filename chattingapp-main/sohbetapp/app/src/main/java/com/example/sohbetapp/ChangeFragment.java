package com.example.sohbetapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class ChangeFragment {

    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;
    }

    public void change(Fragment fragment) {
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentlayout, fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void changeWithParameter(Fragment fragment,String userid)
    {
        Bundle bundle=new Bundle();
        bundle.putString("userid",userid);
        fragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentlayout, fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}