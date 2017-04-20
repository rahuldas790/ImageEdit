package com.xinlan.imageeditlibrary.editimage.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.R;

/**
 * Created by Rahul Kumar Das on 23-03-2017.
 */

public class FragmentDialog extends DialogFragment {


    private SPA sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
//        window.setLayout(MainMenuFragment.width, MainMenuFragment.height);
        window.getAttributes().windowAnimations = R.style.animationdialog;
        window.setBackgroundDrawableResource(R.color.semi_background);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setStyle( DialogFragment.STYLE_NORMAL, android.R.style.Theme );

// margin need to set here
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_dialog_layout, container);

        // tab slider
        sectionsPagerAdapter = new SPA(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setUpTabs();

        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void setUpTabs() {

        TextView tv1 = new TextView(getActivity());
        tv1.setTextColor(Color.WHITE);
        tv1.setText("Tab First");
        tv1.setGravity(Gravity.CENTER);
        tabs.getTabAt(0).setCustomView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setTextColor(Color.WHITE);
        tv2.setText("Tab Second");
        tv2.setGravity(Gravity.CENTER);
        tabs.getTabAt(1).setCustomView(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setTextColor(Color.WHITE);
        tv3.setText("Tab Third");
        tv3.setGravity(Gravity.CENTER);
        tabs.getTabAt(2).setCustomView(tv3);
    }


    public class SPA extends FragmentPagerAdapter {

        public SPA(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // find first fragment...
                return new Sticker();
            }
            if (position == 1) {
                // find first fragment...
                return new Sticker();
            } else if (position == 2) {
                // find first fragment...
                return new Sticker();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "First Tab";
                case 1:
                    return "Second Tab";
                case 2:
                    return "Third Tab";
            }
            return null;
        }
    }
}
