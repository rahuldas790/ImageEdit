package com.xinlan.imageeditlibrary.editimage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.adapter.StickerDialogAdapter;

/**
 * Created by Rahul Kumar Das on 23-03-2017.
 */

public class Sticker extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_sticker, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new StickerDialogAdapter(getActivity()));
        return view;
    }
}
