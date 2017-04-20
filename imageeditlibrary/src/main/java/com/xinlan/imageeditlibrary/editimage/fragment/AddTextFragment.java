package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.ui.ColorPicker;
import com.xinlan.imageeditlibrary.editimage.view.CircleImageView;
import com.xinlan.imageeditlibrary.editimage.view.TextStickerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


/**
 * 添加文本贴图
 *
 * @author 潘易
 */
public class AddTextFragment extends Fragment implements TextWatcher {
    public static final int INDEX = 5;
    public static final String TAG = AddTextFragment.class.getName();
    private int VIEW_MODE = 0;

    private View mainView;
    private EditImageActivity activity;
    private View backToMenu;// 返回主菜单
    private View editText;
    private View fontStyle;

    private EditText mInputText;//输入框
    private ImageView mTextColorSelector;//颜色选择器
    private ImageView mBgColorSelector;//颜色选择器
    public TextStickerView mTextStickerView;// 文字贴图显示控件

    private ColorPicker mColorPicker;

    private int mTextColor = Color.WHITE;
    private int mBgColor = Color.BLACK;
    private InputMethodManager imm;

    private SaveTextStickerTask mSaveTask;

    private String styles[] = {"Black", "BItalic", "Bold", "BoldItalic", "Italic", "Light", "LightItalic", "Medium", "MediumItalic", "Regular",
            "Thin", "ThinItalic", "C-Bold", "C-BoldItalic", "C-Italic", "C-Light", "C-LightItalic", "C-Regular"};
    private String fontPaths[] = {"-Black", "-BlackItalic", "-Bold", "-BoldItalic", "-Italic", "-Light", "-LightItalic", "-Medium", "-MediumItalic", "-Regular",
            "-Thin", "-ThinItalic", "Condensed-Bold", "Condensed-BoldItalic", "Condensed-Italic", "Condensed-Light", "Condensed-LightItalic", "Condensed-Regular"};
    private int colors[] = {Color.WHITE, Color.LTGRAY, Color.GRAY, Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA};
    private ArrayList<Typeface> typeface;

    public static AddTextFragment newInstance(EditImageActivity activity) {
        AddTextFragment fragment = new AddTextFragment();
        fragment.activity = activity;
        fragment.mTextStickerView = activity.mTextStickerView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mainView = inflater.inflate(R.layout.fragment_edit_image_add_text, null);
        backToMenu = mainView.findViewById(R.id.back_to_main);
        mInputText = (EditText) mainView.findViewById(R.id.text_input);
        mTextColorSelector = (ImageView) mainView.findViewById(R.id.text_color);
        mBgColorSelector = (ImageView) mainView.findViewById(R.id.bg_color);
        editText = mainView.findViewById(R.id.edit);
        fontStyle = mainView.findViewById(R.id.style);


        initFontStyles();
        return mainView;
    }

    private void initFontStyles() {


        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuFragment.stickerDialog.show();
//                activity.fontStyles.setVisibility(View.INVISIBLE);
            }
        });

        fontStyle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VIEW_MODE = 0;
                updateView();
            }
        });

        mTextColorSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VIEW_MODE = 2;
                updateView();
            }
        });
        mBgColorSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VIEW_MODE = 1;
                updateView();
            }
        });

    }

    private void updateView() {

        typeface = new ArrayList<>();
        AssetManager am = activity.getAssets();
        LinearLayout main = new LinearLayout(activity);
        main.setGravity(Gravity.CENTER);
        main.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        StyleClickListener clicks = new StyleClickListener();


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 10;
        params.rightMargin = 10;
        params.gravity = Gravity.CENTER;
        activity.fontStyles.removeAllViews();
        main.removeAllViews();
        switch (VIEW_MODE) {
            case 0:

                for (int i = 0; i < styles.length; i++) {
                    Typeface typ = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "Roboto" + fontPaths[i] + ".ttf"));
                    typeface.add(typ);
                    Button name = new Button(activity);
                    name.setGravity(Gravity.CENTER);
                    name.setLayoutParams(params);
                    name.setTextSize(20);
                    name.setTypeface(typ);
                    name.setText(styles[i]);
                    name.setTag(i);
                    name.setOnClickListener(clicks);
                    main.addView(name);
                }
                break;
            case 1:
                BgColorClicks click2 = new BgColorClicks();
                for (int i = colors.length - 1; i >= 0; i--) {

                    TextView name = new Button(activity);
                    name.setGravity(Gravity.CENTER);
                    params.leftMargin = 40;
                    params.rightMargin = 40;
                    params.rightMargin = 40;
                    params.rightMargin = 40;
                    name.setLayoutParams(params);
                    name.setTextSize(20);
                    name.setText("Color");
                    name.setTextColor(Color.TRANSPARENT);
                    name.setBackgroundColor(colors[i]);
                    name.setTag(i);
                    name.setOnClickListener(click2);
                    main.addView(name);
                }
                break;
            case 2:
                TextColorClicks click = new TextColorClicks();
                for (int i = 0; i < colors.length; i++) {

                    TextView name = new Button(activity);
                    name.setGravity(Gravity.CENTER);
                    params.leftMargin = 40;
                    params.rightMargin = 40;
                    params.rightMargin = 40;
                    params.rightMargin = 40;
                    name.setLayoutParams(params);
                    name.setTextSize(20);
                    name.setText("Color");
                    name.setTextColor(Color.TRANSPARENT);
                    name.setBackgroundColor(colors[i]);
                    name.setTag(i);
                    name.setOnClickListener(click);
                    main.addView(name);
                }
                break;
        }

        activity.fontStyles.addView(main);
        activity.fontStyles.setVisibility(View.VISIBLE);
    }

    private class TextColorClicks implements OnClickListener {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            activity.mTextStickerView.setTextColor(colors[pos]);
        }
    }

    private class BgColorClicks implements OnClickListener {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            activity.mTextStickerView.setBgColor(colors[pos]);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
        mColorPicker = new ColorPicker(getActivity(), 255, 255, 255);

        mInputText.addTextChangedListener(this);

        mTextStickerView.setEditText(mInputText);
    }

    private final class StyleClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            activity.mTextStickerView.mPaint.setTypeface(typeface.get(pos));
            mTextStickerView.setText(mTextStickerView.mText);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        //mTextStickerView change
        String text = s.toString().trim();
        mTextStickerView.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 颜色选择 按钮点击
     */
    private final class SelectColorBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mColorPicker.show();
            Button okColor = (Button) mColorPicker.findViewById(R.id.okColorButton);
            okColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextColor(mColorPicker.getColor());
                    mColorPicker.dismiss();
                }
            });
        }
    }//end inner class

    private final class SelectBgColorBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mColorPicker.show();
            Button okColor = (Button) mColorPicker.findViewById(R.id.okColorButton);
            okColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeBgColor(mColorPicker.getColor());
                    mColorPicker.dismiss();
                }
            });
        }
    }//end inner class

    /**
     * 修改字体颜色
     *
     * @param newColor
     */
    private void changeTextColor(int newColor) {
        this.mTextColor = newColor;
        mTextColorSelector.setBackgroundColor(mTextColor);
        mTextStickerView.setTextColor(mTextColor);
    }

    private void changeBgColor(int newColor) {
        this.mBgColor = newColor;
        mBgColorSelector.setBackgroundColor(mBgColor);
        mTextStickerView.setBgColor(mBgColor);
    }

    public void hideInput() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null && isInputMethodShow()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isInputMethodShow() {
        return imm.isActive();
    }

    /**
     * 返回按钮逻辑
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end class

    /**
     * 返回主菜单
     */
    public void backToMain() {
        hideInput();
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(MainMenuFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        activity.bannerFlipper.showPrevious();
        mTextStickerView.setVisibility(View.GONE);
        activity.fontStyles.setVisibility(View.GONE);
    }

    public void onShow() {
        activity.mode = EditImageActivity.MODE_TEXT;
        activity.bottomGallery.setCurrentItem(AddTextFragment.INDEX);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.bannerFlipper.showNext();

        mTextStickerView.setVisibility(View.VISIBLE);
        mInputText.clearFocus();
    }

    /**
     * 保存贴图图片
     */
    public void saveTextImage() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }

        //启动任务
        mSaveTask = new SaveTextStickerTask(activity);
        mSaveTask.execute(activity.mainBitmap);
    }

    /**
     * 文字合成任务
     * 合成最终图片
     */
    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);
            //System.out.println("scale = " + scale_x + "       " + scale_y + "     " + dx + "    " + dy);

            mTextStickerView.isFinal = true;
            mTextStickerView.drawContent(canvas);
//            mTextStickerView.drawText(canvas, mTextStickerView.layout_x,
//                    mTextStickerView.layout_y, mTextStickerView.mScale, mTextStickerView.mRotateAngle);

            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            mTextStickerView.clearTextContent();
            mTextStickerView.resetView();

            activity.changeMainBitmap(result);
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSaveTask != null && !mSaveTask.isCancelled()) {
            mSaveTask.cancel(true);
        }
    }
}// end class
