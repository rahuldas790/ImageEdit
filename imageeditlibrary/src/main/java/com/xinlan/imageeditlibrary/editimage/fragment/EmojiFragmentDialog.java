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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.R;

/**
 * Created by Rahul Kumar Das on 26-03-2017.
 */

public class EmojiFragmentDialog extends DialogFragment {
    private SPA sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.getWindow().setBackgroundDrawableResource(R.color.semi_background);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_dialog_layout, container);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Emotes");
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
        tabs.getTabAt(0).setCustomView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setTextColor(Color.WHITE);
        tv2.setText("Tab Second");
        tabs.getTabAt(1).setCustomView(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setTextColor(Color.WHITE);
        tv3.setText("Tab Third");
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
                return new Emoji();
            }
            if (position == 1) {
                // find first fragment...
                return new Emoji();
            } else if (position == 2) {
                // find first fragment...
                return new Emoji();
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
