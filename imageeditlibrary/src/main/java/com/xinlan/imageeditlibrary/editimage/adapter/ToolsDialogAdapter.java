package com.xinlan.imageeditlibrary.editimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.fragment.CropFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.RotateFragment;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;

/**
 * Created by Rahul Kumar Das on 09-03-2017.
 */

public class ToolsDialogAdapter extends BaseAdapter {

    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();


    private ImageClick mImageClick = new ImageClick();
    private String titles[] = {"Paint", "Crop", "Rotate"};
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.ic_brush, R.drawable.ic_crop,
            R.drawable.ic_rotate,
    };

    public ToolsDialogAdapter(EditImageActivity mContext) {
        this.mContext = mContext;
    }

    private EditImageActivity mContext;
    private MainMenuFragment fragment = new MainMenuFragment();

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = new LinearLayout(mContext);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        TextView tv = new TextView(mContext);
        tv.setTextSize(17);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setText(titles[position]);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.addView(imageView);
        view.addView(tv);
        imageView.setTag(position);
        imageView.setOnClickListener(mImageClick);
        return view;
    }


    private final class ImageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (position) {
                case 0:
                    mContext.mPaintFragment.onShow();
                    MainMenuFragment.stickerDialog.dismiss();
                    break;
                case 1:
//                    fragment.onCropClick();
                    mContext.mode = EditImageActivity.MODE_CROP;
                    mContext.mCropPanel.setVisibility(View.VISIBLE);
                    mContext.mainImage.setImageBitmap(mContext.mainBitmap);
                    mContext.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
                    mContext.bottomGallery.setCurrentItem(CropFragment.INDEX);
                    mContext.mainImage.setScaleEnabled(false);// 禁用缩放
                    //
                    RectF r = mContext.mainImage.getBitmapRect();
                    mContext.mCropPanel.setCropRect(r);
                    // System.out.println(r.left + "    " + r.top);
                    mContext.bannerFlipper.showNext();
                    MainMenuFragment.stickerDialog.dismiss();
                    break;
                case 2:
//                    fragment.onRotateClick();
                    mContext.mode = EditImageActivity.MODE_ROTATE;
                    mContext.bottomGallery.setCurrentItem(RotateFragment.INDEX);
                    mContext.mainImage.setImageBitmap(mContext.mainBitmap);
                    mContext.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
                    mContext.mainImage.setVisibility(View.GONE);

                    mContext.mRotatePanel.addBit(mContext.mainBitmap,
                            mContext.mainImage.getBitmapRect());
                    mContext.mRotateFragment.mSeekBar.setProgress(0);
                    mContext.mRotatePanel.reset();
                    mContext.mRotatePanel.setVisibility(View.VISIBLE);
                    mContext.bannerFlipper.showNext();
                    MainMenuFragment.stickerDialog.dismiss();
                    break;
            }
        }
    }

    private class CropTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

}
