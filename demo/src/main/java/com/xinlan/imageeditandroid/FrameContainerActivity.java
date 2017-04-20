package com.xinlan.imageeditandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xinlan.imageeditandroid.fragments.FrameEight;
import com.xinlan.imageeditandroid.fragments.FrameEleven;
import com.xinlan.imageeditandroid.fragments.FrameFive;
import com.xinlan.imageeditandroid.fragments.FrameFour;
import com.xinlan.imageeditandroid.fragments.FrameNine;
import com.xinlan.imageeditandroid.fragments.FrameOne;
import com.xinlan.imageeditandroid.fragments.FrameSeven;
import com.xinlan.imageeditandroid.fragments.FrameSix;
import com.xinlan.imageeditandroid.fragments.FrameTen;
import com.xinlan.imageeditandroid.fragments.FrameThirteen;
import com.xinlan.imageeditandroid.fragments.FrameThree;
import com.xinlan.imageeditandroid.fragments.FrameTwelve;
import com.xinlan.imageeditandroid.fragments.FrameTwo;
import com.xinlan.imageeditandroid.utils.AsyncTask;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.xinlan.imageeditandroid.MainActivity.SELECT_GALLERY_IMAGE_CODE;

public class FrameContainerActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    public static ArrayList<Uri> all_paths;
    public static ArrayList<Bitmap> all_bitmaps;
    public static ProgressDialog pd;
    private View done, reset;
    public static View mainView;
    private LoadImageTask imageTask;
    private String newPath;
    private int currentPosition = 0;

    private int[] tabIcons = {
            R.mipmap.nine,

            R.mipmap.ten,

            R.mipmap.three,
            R.mipmap.four,
            R.mipmap.five,
            R.mipmap.eight,

            R.mipmap.two,
            R.mipmap.six,
            R.mipmap.seven,
            R.mipmap.eleven,
            R.mipmap.twelve,

            R.mipmap.one,

            R.mipmap.thirteen
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        all_paths = new ArrayList<>();
        setUpPaths(MainActivity.uris);
        all_bitmaps = new ArrayList<>();

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        imageTask = new LoadImageTask();
        imageTask.execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mainView = findViewById(R.id.main_content_view);


        done = findViewById(R.id.done);
        reset = findViewById(R.id.reset);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ViewTOBitmap().execute();

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleBitmaps();
                finish();
                MainActivity.isReset = true;
            }
        });

    }

    private void setUpPaths(Uri[] uris) {
        if (uris.length >= 7)
            currentPosition = 12;
        else if (uris.length == 6)
            currentPosition = 11;
        else if (uris.length == 5)
            currentPosition = 6;
        else if (uris.length == 4)
            currentPosition = 2;
        else if (uris.length == 3)
            currentPosition = 1;
        else if (uris.length == 2)
            currentPosition = 0;
        for (int i = 0; i < uris.length; i++) {
            all_paths.add(uris[i]);
        }
    }


    @Override
    public void onClick(View view) {
        try {
            int tag = (int) view.getTag();
            if (tag >= 0) {
                currentPosition = tag;
                startActivityForResult(new Intent(
                                this, SelectPictureActivity.class),
                        SELECT_GALLERY_IMAGE_CODE);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_GALLERY_IMAGE_CODE) {
            newPath = data.getStringExtra("imgPath");
            new LoadOneImageTask().execute();
        }

    }

    public class ViewTOBitmap extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(Object[] params) {
            View view = mainView;
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            try {
                createAFile();
                FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.close();
//                Toast.makeText(FrameContainerActivity.this, "File saved to /Pictures/zersey/file.png", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
//                Toast.makeText(FrameContainerActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
//                Toast.makeText(FrameContainerActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png";
        }

        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);
            pd.dismiss();
            Intent intent = new Intent();
            intent.putExtra("path", o.toString());
            setResult(Activity.RESULT_OK, intent);
            recycleBitmaps();
            all_bitmaps = null;
            all_paths = null;
            pd = null;
            finish();
        }
    }

    private void recycleBitmaps() {
        for (int i = 0; i < all_bitmaps.size(); i++) {
            all_bitmaps.get(i).recycle();
        }
    }

    private void createAFile() {
        File new_file = new File(Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png");
        try {
            new_file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("Create File", "File exists?" + new_file.exists());
    }

    private void setUpIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
        tabLayout.getTabAt(6).setIcon(tabIcons[6]);
        tabLayout.getTabAt(7).setIcon(tabIcons[7]);
        tabLayout.getTabAt(8).setIcon(tabIcons[8]);
        tabLayout.getTabAt(9).setIcon(tabIcons[9]);
        tabLayout.getTabAt(10).setIcon(tabIcons[10]);
        tabLayout.getTabAt(11).setIcon(tabIcons[11]);
        tabLayout.getTabAt(12).setIcon(tabIcons[12]);
    }

    private class LoadImageTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            for (int i = 0; i < all_paths.size(); i++) {
                try {
                    all_bitmaps.add(BitmapFactory.decodeFile(all_paths.get(i).toString()));
                } catch (Exception e) {
                    Log.i("Image Loading", "File not found " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.dismiss();
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setUpIcons();
        }
    }

    private class LoadOneImageTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                all_bitmaps.add(BitmapFactory.decodeFile(newPath));
            } catch (Exception e) {
                Log.i("Image Loading", "File not found " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.dismiss();
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setUpIcons();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FrameNine.newInstance(this, "", ""), "");

        adapter.addFragment(FrameTen.newInstance(this, "", ""), "");

        adapter.addFragment(FrameThree.newInstance(this, "", ""), "");
        adapter.addFragment(FrameFour.newInstance(this, "", ""), "");
        adapter.addFragment(FrameFive.newInstance(this, "", ""), "");
        adapter.addFragment(FrameEight.newInstance(this, "", ""), "");

        adapter.addFragment(FrameTwo.newInstance(this, "", ""), "");
        adapter.addFragment(FrameSix.newInstance(this, "", ""), "");
        adapter.addFragment(FrameSeven.newInstance(this, "", ""), "");
        adapter.addFragment(FrameEleven.newInstance(this, "", ""), "");
        adapter.addFragment(FrameTwelve.newInstance(this, "", ""), "");

        adapter.addFragment(FrameOne.newInstance(this, "", ""), "");

        adapter.addFragment(FrameThirteen.newInstance(this, "", ""), "");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        recycleBitmaps();
        finish();
    }
}
