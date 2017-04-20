package com.xinlan.imageeditandroid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xinlan.imageeditandroid.utils.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.xinlan.imageeditandroid.MainActivity.REQUEST_PERMISSON_CAMERA;
import static com.xinlan.imageeditlibrary.editimage.EditImageActivity.FILE_PATH;

/**
 * Created by Rahul Kumar Das on 21-03-2017.
 */

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        Camera.ShutterCallback, Camera.PictureCallback {

    private static final String TAG = CameraActivity.class.getSimpleName();

    Camera mCamera;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    ImageButton mTakePictureBtn, rotate, flash, discard, save;
    private static int nextCam;
    private String path;
    private Image image;
    private Bitmap picture;
    private ProgressDialog pd;
    private View view, upper;
    private boolean flashOn = false;
    private int imaWidth, screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestTakePhotoPermissions();
    }

    private void initViews() {
        setContentView(R.layout.fragment_camera);
        nextCam = Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = null;
        view = findViewById(R.id.view);
        pd = new ProgressDialog(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        Display d = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        screenWidth = width;
        Toast.makeText(this, "" + width, Toast.LENGTH_SHORT).show();
//        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
        upper = findViewById(R.id.upper);
        upper.setLayoutParams(new RelativeLayout.LayoutParams(width, width));

        mTakePictureBtn = (ImageButton) findViewById(R.id.take_picture);
        rotate = (ImageButton) findViewById(R.id.rotate);
        flash = (ImageButton) findViewById(R.id.flash);
        discard = (ImageButton) findViewById(R.id.discard);
        save = (ImageButton) findViewById(R.id.save);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTakePictureBtn.isEnabled()) {
                    mTakePictureBtn.setEnabled(false);
                    mCamera.takePicture(CameraActivity.this, null, CameraActivity.this);
                }
            }
        });
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nextCam == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    nextCam = Camera.CameraInfo.CAMERA_FACING_BACK;
                } else {
                    nextCam = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }

                mCamera.release();
                mCamera = null;
                startCamera(mSurfaceHolder);

            }
        });
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(CameraActivity.this, "Clicked Flash", Toast.LENGTH_SHORT).show();
                Camera.Parameters parameters = mCamera.getParameters();
                if (flashOn) {
                    flash.setImageResource(R.drawable.ic_flash_two);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flashOn = false;
                } else {
                    flash.setImageResource(R.drawable.ic_flash);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    flashOn = true;
                }
                if (nextCam == Camera.CameraInfo.CAMERA_FACING_BACK)
                    mCamera.setParameters(parameters);
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.startPreview();
                save.setVisibility(View.INVISIBLE);
                discard.setVisibility(View.INVISIBLE);
                mTakePictureBtn.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                discard.setVisibility(View.GONE);
                flash.setVisibility(View.GONE);
                rotate.setVisibility(View.GONE);
                insertIMmage();
                Intent intent = new Intent();
                intent.putExtra("path", image.mUri.toString());
                intent.putExtra(FILE_PATH, image.mUri.toString());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera(surfaceHolder);
    }

    private void startCamera(SurfaceHolder surfaceHolder) {
        if (mCamera == null) {
            mCamera = getCameraInstance();
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.setDisplayOrientation(90);

                int first, last, size;
                Camera.Parameters parameters = mCamera.getParameters();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//                List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();

//                first = pictureSizes.get(0).width;
//                last = pictureSizes.get(pictureSizes.size() - 1).width;
//                size = first > last ? 0 : pictureSizes.size() - 1;

//                parameters.setPictureSize(pictureSizes.get(size).width, pictureSizes.get(size).height);
//                Toast.makeText(this, "Preview size is " + pictureSizes.get(size).width + ":" + pictureSizes.get(size).height, Toast.LENGTH_LONG).show();

                first = previewSizes.get(0).width;
                last = previewSizes.get(previewSizes.size() - 1).width;
                size = first > last ? 0 : previewSizes.size() - 1;

                parameters.setPictureSize(previewSizes.get(size).width, previewSizes.get(size).height);
                parameters.setPreviewSize(previewSizes.get(size).width, previewSizes.get(size).height);
//                Toast.makeText(this, "Preview size is " + previewSizes.get(size).width + ":" + previewSizes.get(size).height, Toast.LENGTH_LONG).show();

                previewSizes = Collections.EMPTY_LIST;
//                pictureSizes = Collections.EMPTY_LIST;

                parameters.set("jpeg-quality", 70);
                parameters.setPictureFormat(PixelFormat.JPEG);

                flash.setImageResource(R.drawable.ic_flash_two);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashOn = false;

                if (nextCam == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    flash.setVisibility(View.VISIBLE);
                } else {

                    flash.setVisibility(View.GONE);
                }

                mCamera.setParameters(parameters);
                mCamera.startPreview();
                mTakePictureBtn.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(nextCam); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Request permission to take pictures
     */
    private void requestTakePhotoPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_CAMERA);
            return;
        } else {
            initViews();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSON_CAMERA
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initViews();
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
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

    public class ViewTOBitmap extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(Object[] params) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            try {
                createAFile();
                FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png";
            return Environment.getExternalStorageDirectory() + "/Pictures/zersey/file.png";
        }

        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);
            pd.dismiss();
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

        mTakePictureBtn.setEnabled(true);
        picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

//        rotates the image to portrate
        Matrix matrix = new Matrix();
        if (nextCam == Camera.CameraInfo.CAMERA_FACING_BACK)
            matrix.postRotate(90);
        else {
            matrix.preScale(-1, 1);
            matrix.postRotate(90);
        }


        picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, false);
        imaWidth = picture.getWidth();
        picture = Bitmap.createBitmap(picture, 0, 0, screenWidth, screenWidth);

        save.setVisibility(View.VISIBLE);
        discard.setVisibility(View.VISIBLE);
        mTakePictureBtn.setVisibility(View.GONE);
    }

    private void insertIMmage() {
        path = MediaStore.Images.Media.insertImage(getContentResolver(), picture, "zerseyPic", "");
        Uri contentUri = Uri.parse(path);
        image = getImageFromContentUri(contentUri);

    }

    @Override
    public boolean deleteFile(String name) {
        return super.deleteFile(name);
    }

    public Image getImageFromContentUri(Uri contentUri) {

        String[] cols = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION
        };
        // can post image
        Cursor cursor = getContentResolver().query(contentUri, cols, null, null, null);
        cursor.moveToFirst();
        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
        return new Image(uri, orientation);
    }

    @Override
    public void onShutter() {

    }
}