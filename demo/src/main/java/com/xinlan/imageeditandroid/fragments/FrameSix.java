package com.xinlan.imageeditandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xinlan.imageeditandroid.R;
import com.xinlan.imageeditandroid.TouchImageView;

import static com.xinlan.imageeditandroid.FrameContainerActivity.all_bitmaps;

public class FrameSix extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Context mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TouchImageView imageView, imageView2, imageView3, imageView4, imageView5;
    public static View mainView;
    private static View.OnClickListener clickListener;


    public FrameSix() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrameSix.
     */
    // TODO: Rename and change types and number of parameters
    public static FrameSix newInstance(Context context, String param1, String param2) {
        FrameSix fragment = new FrameSix();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        mContext = context;
        clickListener = (View.OnClickListener)context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_six, null);
        imageView = (TouchImageView) view.findViewById(R.id.imageView);
        imageView2 = (TouchImageView) view.findViewById(R.id.imageView2);
        imageView3 = (TouchImageView) view.findViewById(R.id.imageView3);
        imageView4 = (TouchImageView) view.findViewById(R.id.imageView4);
        imageView5 = (TouchImageView) view.findViewById(R.id.imageView5);

        imageView3.setTag(-1);
        imageView4.setTag(-1);
        imageView4.setTag(-1);
        imageView5.setTag(-1);

        switch (all_bitmaps.size()) {
            case 5:
                imageView3.setOnClickListener(null);
                imageView4.setOnClickListener(null);
                imageView5.setOnClickListener(null);
                imageView5.setImageBitmap(all_bitmaps.get(4));
                imageView5.setZoom(3);
            case 4:
                imageView3.setOnClickListener(null);
                imageView4.setOnClickListener(null);
                imageView5.setOnClickListener(null);
                imageView4.setImageBitmap(all_bitmaps.get(3));
                imageView4.setZoom(3);
                imageView5.setTag(5);
            case 3:
                imageView3.setOnClickListener(null);
                imageView4.setOnClickListener(clickListener);
                imageView5.setOnClickListener(clickListener);
                imageView3.setImageBitmap(all_bitmaps.get(2));
                imageView3.setZoom(3);
                imageView4.setTag(5);
                imageView5.setTag(5);
            case 2:
                imageView3.setOnClickListener(clickListener);
                imageView4.setOnClickListener(clickListener);
                imageView5.setOnClickListener(clickListener);
                imageView2.setImageBitmap(all_bitmaps.get(1));
                imageView.setImageBitmap(all_bitmaps.get(0));
                imageView.setZoom(3);
                imageView2.setZoom(3);
                imageView3.setTag(5);
                imageView4.setTag(5);
                imageView5.setTag(5);
                break;
            default:
                imageView5.setImageBitmap(all_bitmaps.get(4));
                imageView5.setZoom(3);
                imageView4.setImageBitmap(all_bitmaps.get(3));
                imageView4.setZoom(3);
                imageView3.setImageBitmap(all_bitmaps.get(2));
                imageView3.setZoom(3);
                imageView2.setImageBitmap(all_bitmaps.get(1));
                imageView.setImageBitmap(all_bitmaps.get(0));
                imageView.setZoom(3);
                imageView2.setZoom(3);
                break;
        }

        mainView = view;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (imageView != null) {

            }
        } else {

        }
    }
}
