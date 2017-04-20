package com.xinlan.imageeditandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;

    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int TAKE_PHOTO_CODE = 8;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    public static final int ACTION_STICKERS_IMAGE = 10;
    public static final int ACTION_COLLAGE_CHOOSER = 15;
    public static final int ACTION_COLLAGE_FINAL = 17;
    private MainActivity context;

    private View openAblum;

    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;//
    private String path;
    public static Uri[] uris;
    public boolean isCollage;
    public static boolean isReset;


    private View mTakenPhoto; // Take a photo for editing
    private View mCollageView; // select multiple photos for collage
    private Uri photoURI = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        context = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        openAblum = findViewById(R.id.select_ablum);
        openAblum.setOnClickListener(this);
        mTakenPhoto = findViewById(R.id.take_photo);
        mTakenPhoto.setOnClickListener(this);
        mCollageView = findViewById(R.id.collage);
        mCollageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
//                takePhotoClick();
                startActivityForResult(new Intent(this, CameraActivity.class), TAKE_PHOTO_CODE);
                break;
            case R.id.select_ablum:
                isCollage = false;
                selectFromAblum();
                break;
            case R.id.collage:
                isCollage = true;
                openCustomAlbum();
                break;
        }//end switch
    }

    private void openCustomAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openCollagePermissionsCheck();
        } else {
            openCollage();
        }

    }

    private void openCollagePermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        openCollage();
    }

    private void openCollage() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        startActivityForResult(intent, ACTION_COLLAGE_CHOOSER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReset) {
            openCollage();
            isReset = false;
        }
    }

    /**
     * Taking Pictures
     */
    protected void takePhotoClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestTakePhotoPermissions();
        } else {
            doTakePhoto();
        }//end if
    }

    /**
     * Request permission to take pictures
     */
    private void requestTakePhotoPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSON_CAMERA);
            doTakePhoto();
        } else {
            doTakePhoto();
        }
    }

    /**
     * Taking Pictures
     */
    private void doTakePhoto() {
        Log.i("Rahul", "Inside doTakePhoto()");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.i("Rahul", "takePictureIntent.resolveActivity(getPackageManager()) is not null");
            File photoFile = FileUtils.genEditFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i("Rahul", "new empty photo file successfully created");
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }

//            startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
        }
    }

    /**
     * Edit the selected picture
     *
     * @author panyi
     */
    private void editImageClick() {
        File outputFile = FileUtils.genEditFile();
        EditImageActivity.start(this, path, outputFile.getAbsolutePath(), ACTION_REQUEST_EDITIMAGE);
    }

    /**
     * Select an edit picture from the album
     */
    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck();
        } else {
            openAblum();
        }//end if
    }

    private void openAblum() {
        MainActivity.this.startActivityForResult(new Intent(
                        MainActivity.this, SelectPictureActivity.class),
                SELECT_GALLERY_IMAGE_CODE);
    }

    private void openAblumWithPermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        openAblum();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON_SORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!isCollage) {
                openAblum();
            } else {
                openCollage();
            }
            return;
        }//end if

        if (requestCode == REQUEST_PERMISSON_CAMERA
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doTakePhoto();
            return;
        }//end if
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE://
                    handleSelectFromAblum(data);
                    editImageClick();
                    break;
                case TAKE_PHOTO_CODE://Take pictures back
                    handleTakePhoto(data);
                    editImageClick();
                    break;
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
                case ACTION_COLLAGE_CHOOSER:
                    Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.TAG_IMAGE_URI);
                    uris = new Uri[parcelableUris.length];
                    System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                    Intent i = new Intent(this, FrameContainerActivity.class);
//                    i.putExtra("all_path", uris);
                    startActivityForResult(i, ACTION_COLLAGE_FINAL);
                    break;
                case ACTION_COLLAGE_FINAL:
                    Toast.makeText(context, "I am here", Toast.LENGTH_SHORT).show();
                    String path = data.getStringExtra("path");
                    handleCollage(path);
                    editImageClick();
                    break;
            }// end swi

        }
    }

    private void handleCollage(String path) {
        this.path = path;
        startLoadTask();
    }

    /**
     * Handle the camera to return
     *
     * @param data
     */
    private void handleTakePhoto(Intent data) {
//        if (photoURI != null) {// Shooting successful
        path = data.getStringExtra("path");
        startLoadTask();
//        }
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.SAVE_FILE_PATH);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit)
            Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
        //System.out.println("newFilePath---->" + newFilePath);
        Log.d("image is edit", isImageEdit + "");
        LoadImageTask loadTask = new LoadImageTask();
        loadTask.execute(newFilePath);
    }

    private void handleSelectFromAblum(Intent data) {
        String filepath = data.getStringExtra("imgPath");
        path = filepath;
        // System.out.println("path---->"+path);
        startLoadTask();
    }

    private void startLoadTask() {
        LoadImageTask task = new LoadImageTask();
        task.execute(path);
    }


    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth / 4, imageHeight / 4);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mainBitmap != null) {
                mainBitmap.recycle();
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
        }
    }// end inner class

}//end class
