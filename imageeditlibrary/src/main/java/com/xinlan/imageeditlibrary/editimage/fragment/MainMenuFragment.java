package com.xinlan.imageeditlibrary.editimage.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.WindowInsetsCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.adapter.ToolsDialogAdapter;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


/**
 * 工具栏主菜单
 *
 * @author panyi
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    public static final int INDEX = 0;

    public static final String TAG = MainMenuFragment.class.getName();
    private View mainView;
    private EditImageActivity activity;

    private View stickerBtn;// 贴图按钮
    private View fliterBtn;// 滤镜按钮
    private View cropBtn;// 剪裁按钮
    private View rotateBtn;// 旋转按钮
    private View mTextBtn;//文字型贴图添加
    private View mPaintBtn;//编辑按钮
    private View mEmojiBtn;//Emojis
    private View mFrameBtn;//Frmaes
    public static Dialog stickerDialog;
    public static DialogFragment overlay;
    public static FragmentManager fm;
    private final String[] stickers = {"Type 1", "Type 2", "Type 3", "Type 4"};
    public static int width, height;

    public static MainMenuFragment newInstance(EditImageActivity activity) {
        MainMenuFragment fragment = new MainMenuFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_main_menu,
                null);
        stickerBtn = mainView.findViewById(R.id.btn_stickers);
        fliterBtn = mainView.findViewById(R.id.btn_fliter);
        cropBtn = mainView.findViewById(R.id.btn_crop);
        rotateBtn = mainView.findViewById(R.id.btn_rotate);
        mTextBtn = mainView.findViewById(R.id.btn_text);
        mPaintBtn = mainView.findViewById(R.id.btn_paint);
        mEmojiBtn = mainView.findViewById(R.id.btn_emoji);
        mFrameBtn = mainView.findViewById(R.id.btn_frame);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stickerBtn.setOnClickListener(this);
        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
        mEmojiBtn.setOnClickListener(this);
        mFrameBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == stickerBtn) {
            onStickClick();
        } else if (v == fliterBtn) {
            onFilterClick();
        } else if (v == cropBtn) {
            onCropClick();
        } else if (v == rotateBtn) {
            onRotateClick();
        } else if (v == mTextBtn) {
            onAddTextClick();
        } else if (v == mPaintBtn) {
            onPaintClick();
        } else if (v == mEmojiBtn) {
            onEmojiClick();
        } else if (v == mFrameBtn) {
            Toast.makeText(activity, "Frames Not available!!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 贴图模式
     *
     * @author panyi
     */
    private void onStickClick() {

        activity.mode = EditImageActivity.MODE_STICKERS;
        activity.mStirckerFragment.getmStickerView().setVisibility(
                View.VISIBLE);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        fm = activity.getSupportFragmentManager();
        overlay = new FragmentDialog();
        overlay.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        activity.bottomGallery.setCurrentItem(StirckerFragment.INDEX);
        activity.bannerFlipper.showNext();
    }

    private void onEmojiClick() {
        overlay = new EmojiFragmentDialog();
        overlay.show(activity.getSupportFragmentManager(), "EmojiFragmentDialog");
        activity.mode = EditImageActivity.MODE_STICKERS;
        activity.mStirckerFragment.getmStickerView().setVisibility(
                View.VISIBLE);
//        activity.bottomGallery.setCurrentItem(StirckerFragment.INDEX);
        activity.bannerFlipper.showNext();
    }

    /**
     * 滤镜模式
     *
     * @author panyi
     */
    private void onFilterClick() {
        activity.mode = EditImageActivity.MODE_FILTER;
        activity.mFliterListFragment.setCurrentBitmap(activity.mainBitmap);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);
        activity.bottomGallery.setCurrentItem(FliterListFragment.INDEX);
        activity.bannerFlipper.showNext();
    }

    /**
     * 裁剪模式
     *
     * @author panyi
     */
    public void onCropClick() {
        activity.mode = EditImageActivity.MODE_CROP;
        activity.mCropPanel.setVisibility(View.VISIBLE);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.bottomGallery.setCurrentItem(CropFragment.INDEX);
        activity.mainImage.setScaleEnabled(false);// 禁用缩放
        //
        RectF r = activity.mainImage.getBitmapRect();
        activity.mCropPanel.setCropRect(r);
        // System.out.println(r.left + "    " + r.top);
        activity.bannerFlipper.showNext();
    }

    /**
     * 图片旋转模式
     *
     * @author panyi
     */
    public void onRotateClick() {
        activity.mode = EditImageActivity.MODE_ROTATE;
        activity.bottomGallery.setCurrentItem(RotateFragment.INDEX);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setVisibility(View.GONE);

        activity.mRotatePanel.addBit(activity.mainBitmap,
                activity.mainImage.getBitmapRect());
        activity.mRotateFragment.mSeekBar.setProgress(0);
        activity.mRotatePanel.reset();
        activity.mRotatePanel.setVisibility(View.VISIBLE);
        activity.bannerFlipper.showNext();
    }

    /**
     * 插入文字模式
     *
     * @author panyi
     */
    public void onAddTextClick() {
        stickerDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        stickerDialog.getWindow().setBackgroundDrawableResource(R.color.semi_background);
        stickerDialog.setContentView(R.layout.stickers_dialog_layout);
        stickerDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        GridView gridView = (GridView) stickerDialog.findViewById(R.id.grid);
        gridView.setVisibility(View.GONE);
        final EditText text = (EditText) stickerDialog.findViewById(R.id.edtxt);
        text.setVisibility(View.VISIBLE);
        text.setTextColor(Color.WHITE);
        TextView title = (TextView) stickerDialog.findViewById(R.id.title);
        title.setText("Text");
        Button done = (Button) stickerDialog.findViewById(R.id.done);
        done.setVisibility(View.VISIBLE);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.mAddTextFragment.mTextStickerView.setText(text.getText().toString());
                stickerDialog.dismiss();
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView close = (ImageView) stickerDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerDialog.dismiss();
            }
        });
        stickerDialog.show();
        activity.mAddTextFragment.onShow();
    }

    /**
     * 自由绘制模式
     */
    public void onPaintClick() {
        stickerDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        stickerDialog.getWindow().setBackgroundDrawableResource(R.color.semi_background);
        stickerDialog.setContentView(R.layout.stickers_dialog_layout);
        stickerDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        GridView gridView = (GridView) stickerDialog.findViewById(R.id.grid);
        gridView.setAdapter(new ToolsDialogAdapter(activity));
        TextView title = (TextView) stickerDialog.findViewById(R.id.title);
        title.setText("Tools");
        ImageView close = (ImageView) stickerDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerDialog.dismiss();
            }
        });
        stickerDialog.show();
//        activity.mPaintFragment.onShow();
    }

}// end class
