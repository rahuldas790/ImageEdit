package com.xinlan.imageeditlibrary.editimage.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Das on 09-03-2017.
 */

public class EmojiDialogAdapter extends BaseAdapter {

    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();

    public static final String stickerPath = "emojis";
    private ImageClick mImageClick = new ImageClick();
    private StickerView mStirckerView = EditImageActivity.mStickerView;

    public EmojiDialogAdapter(Context mContext) {
        this.mContext = mContext;

        addStickerImages(stickerPath);

    }

    private Context mContext;
    private List<String> pathList = new ArrayList<String>();

    @Override
    public int getCount() {
        return pathList.size();
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
        ImageView imageView = new ImageView(mContext);
        String path = pathList.get(position);
        ImageLoader.getInstance().displayImage("assets://" + path,
                imageView, imageOption);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        imageView.setTag(path);
        imageView.setOnClickListener(mImageClick);
        return imageView;
    }

    public void addStickerImages(String folderPath) {
        pathList.clear();
        try {
            String[] files = mContext.getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    private final class ImageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            //System.out.println("data---->" + data);
            Log.i("Rahul", "Emoji Click performed");
            mStirckerView.addBitImage(getImageFromAssetsFile(data));
        }
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = mContext.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
            MainMenuFragment.overlay.dismiss();
        } catch (IOException e) {
            Log.i("Rahul", "Emoji Click Error : " + e.getMessage());
            e.printStackTrace();
        }
        return image;
    }
}
