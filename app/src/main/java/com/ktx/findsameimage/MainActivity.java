package com.ktx.findsameimage;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /** khai báo mảng gồm 20 resource id của 20 ảnh (10 cặp ảnh) */
    private static final Integer[] IMAGES_RES_IDS = new Integer[]{R.drawable.ic_bloom, R.drawable.ic_butterfree, R.drawable.ic_clefairy, R.drawable.ic_cyndaquil,
            R.drawable.ic_eevee, R.drawable.ic_jiggly, R.drawable.ic_pikachu, R.drawable.ic_spinda, R.drawable.ic_whiscash, R.drawable.ic_wooper,
            R.drawable.ic_bloom, R.drawable.ic_butterfree, R.drawable.ic_clefairy, R.drawable.ic_cyndaquil,
            R.drawable.ic_eevee, R.drawable.ic_jiggly, R.drawable.ic_pikachu, R.drawable.ic_spinda, R.drawable.ic_whiscash, R.drawable.ic_wooper};

    /** Chỉ số default khi không chọn ảnh nào */
    private static final int NONE_INDEX = -1;
    /** Số điểm cộng vào tổng điểm khi chọn 2 ảnh giống nhau */
    private static final int SCORE_MATCH = 10;
    /** Số điểm cộng vào tổng điểm khi chọn 2 ảnh khác nhau */
    private static final int SCORE_WRONG = -1;
    /** Time delay để úp ảnh lại */
    private static final long DELAY_TIME = 1000;

    /** arraylist để lưu 20 ảnh khi xáo trộn vị trí */
    private ArrayList<Integer> mListImageId = new ArrayList<>(Arrays.asList(IMAGES_RES_IDS));

    /** Flag (cờ) để lưu trạng thái đã mở 1 ảnh hay chưa */
    private boolean mAlreadyOpenOne = false;

    // Các biến lưu thông tin của ảnh thứ nhất (view, resource id, index trong list)
    private View mTheFirstView;
    private int mTheFirstImageId;
    private int mTheFirstIndex = NONE_INDEX;

    /** biến lưu tổng điểm */
    private int mScore = 0;

    /** biến đếm số ảnh đã được mở. nếu count = toàn bộ số ảnh thì win game */
    private int mOpenedImageCount = 0;

    private TextView mTvScore;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvScore = (TextView) findViewById(R.id.tv_score);

        LinearLayout llRoot = (LinearLayout) findViewById(R.id.ll_root);

        // get screen width
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        // get padding
        int screenPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        // 4 columns, 5 rows
        int imageSize = (screenWidth - 2 * screenPadding) / 4;
        int llRootHeight = imageSize * 5;

        // set layout params for linearlayout
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, llRootHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        llRoot.setLayoutParams(layoutParams);

        // set OnClickListener for buttons
        setListenerForButtons();

        // shuffle image ids
        shuffleListImageId();
    }

    private void setListenerForButtons() {
        findViewById(R.id.btn_01).setOnClickListener(this);
        findViewById(R.id.btn_02).setOnClickListener(this);
        findViewById(R.id.btn_03).setOnClickListener(this);
        findViewById(R.id.btn_04).setOnClickListener(this);
        findViewById(R.id.btn_05).setOnClickListener(this);
        findViewById(R.id.btn_06).setOnClickListener(this);
        findViewById(R.id.btn_07).setOnClickListener(this);
        findViewById(R.id.btn_08).setOnClickListener(this);
        findViewById(R.id.btn_09).setOnClickListener(this);
        findViewById(R.id.btn_10).setOnClickListener(this);
        findViewById(R.id.btn_11).setOnClickListener(this);
        findViewById(R.id.btn_12).setOnClickListener(this);
        findViewById(R.id.btn_13).setOnClickListener(this);
        findViewById(R.id.btn_14).setOnClickListener(this);
        findViewById(R.id.btn_15).setOnClickListener(this);
        findViewById(R.id.btn_16).setOnClickListener(this);
        findViewById(R.id.btn_17).setOnClickListener(this);
        findViewById(R.id.btn_18).setOnClickListener(this);
        findViewById(R.id.btn_19).setOnClickListener(this);
        findViewById(R.id.btn_20).setOnClickListener(this);
    }

    /**
     * Enable or disable sự kiện click buttons
     * @param clickable
     */
    private void setClickableButtons(boolean clickable) {
        findViewById(R.id.btn_01).setClickable(clickable);
        findViewById(R.id.btn_02).setClickable(clickable);
        findViewById(R.id.btn_03).setClickable(clickable);
        findViewById(R.id.btn_04).setClickable(clickable);
        findViewById(R.id.btn_05).setClickable(clickable);
        findViewById(R.id.btn_06).setClickable(clickable);
        findViewById(R.id.btn_07).setClickable(clickable);
        findViewById(R.id.btn_08).setClickable(clickable);
        findViewById(R.id.btn_09).setClickable(clickable);
        findViewById(R.id.btn_10).setClickable(clickable);
        findViewById(R.id.btn_11).setClickable(clickable);
        findViewById(R.id.btn_12).setClickable(clickable);
        findViewById(R.id.btn_13).setClickable(clickable);
        findViewById(R.id.btn_14).setClickable(clickable);
        findViewById(R.id.btn_15).setClickable(clickable);
        findViewById(R.id.btn_16).setClickable(clickable);
        findViewById(R.id.btn_17).setClickable(clickable);
        findViewById(R.id.btn_18).setClickable(clickable);
        findViewById(R.id.btn_19).setClickable(clickable);
        findViewById(R.id.btn_20).setClickable(clickable);
    }

    /**
     * Reset buttons
     */
    private void resetAllButtons() {
        ((ImageButton)findViewById(R.id.btn_01)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_02)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_03)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_04)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_05)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_06)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_07)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_08)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_09)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_10)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_11)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_12)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_13)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_14)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_15)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_16)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_17)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_18)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_19)).setImageResource(R.drawable.hide_image_bg);
        ((ImageButton)findViewById(R.id.btn_20)).setImageResource(R.drawable.hide_image_bg);
    }

    /**
     * Reset score
     */
    private void resetValues() {
        mScore = 0;
        mTvScore.setText(String.valueOf(mScore));

        mOpenedImageCount = 0;
    }

    private void showAlertWinner() {
        new AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(String.format(Locale.ENGLISH, getString(R.string.msg_winner), mScore))
            .setPositiveButton(R.string.dialog_btn_replay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resetAllButtons();
                    shuffleListImageId();
                    resetValues();
                }
            })
            .setNegativeButton(R.string.dialog_btn_go_home, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            })
            .show();
    }

    /**
     * Xáo trộn ảnh
     */
    private void shuffleListImageId() {
        Collections.shuffle(mListImageId);
    }

    /**
     * Xử lý khi click ảnh
     * @param view
     * @param index vị trí của ảnh trong list
     */
    private void processClickBtn(final View view, final int index) {
        // check click button again
        if (index == mTheFirstIndex) {
            return;
        }

        view.setBackgroundResource(mListImageId.get(index));
        ((ImageButton) view).setImageResource(mListImageId.get(index));

        if (mAlreadyOpenOne) {
            mAlreadyOpenOne = false;
            // disable click buttons
            setClickableButtons(false);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mTheFirstImageId == mListImageId.get(index)) {
                        // match
                        mScore += SCORE_MATCH;
                        mOpenedImageCount += 2;
                        if (mOpenedImageCount == mListImageId.size()) {
                            showAlertWinner();
                        }
                        ((ImageButton) mTheFirstView).setImageBitmap(null);
                        ((ImageButton) view).setImageBitmap(null);
                    } else {
                        // not match
                        mScore += SCORE_WRONG;
                        ((ImageButton) mTheFirstView).setImageResource(R.drawable.hide_image_bg);
                        ((ImageButton) view).setImageResource(R.drawable.hide_image_bg);
                    }
                    // show score
                    mTvScore.setText(String.valueOf(mScore));

                    // reset thông tin ảnh thứ nhất
                    mTheFirstIndex = NONE_INDEX;
                    mTheFirstImageId = 0;
                    mTheFirstView = null;

                    // enable click buttons
                    setClickableButtons(true);
                }
            }, DELAY_TIME);

        } else {
            mAlreadyOpenOne = true;
            mTheFirstIndex = index;
            mTheFirstImageId = mListImageId.get(index);
            mTheFirstView = view;
        }
    }

    @Override
    public void onClick(View view) {
        Log.i("MainActivity", "onClick:" + view.getId());
        switch (view.getId()) {
            case R.id.btn_01:
                processClickBtn(view, 0);
                break;
            case R.id.btn_02:
                processClickBtn(view, 1);
                break;
            case R.id.btn_03:
                processClickBtn(view, 2);
                break;
            case R.id.btn_04:
                processClickBtn(view, 3);
                break;
            case R.id.btn_05:
                processClickBtn(view, 4);
                break;
            case R.id.btn_06:
                processClickBtn(view, 5);
                break;
            case R.id.btn_07:
                processClickBtn(view, 6);
                break;
            case R.id.btn_08:
                processClickBtn(view, 7);
                break;
            case R.id.btn_09:
                processClickBtn(view, 8);
                break;
            case R.id.btn_10:
                processClickBtn(view, 9);
                break;
            case R.id.btn_11:
                processClickBtn(view, 10);
                break;
            case R.id.btn_12:
                processClickBtn(view, 11);
                break;
            case R.id.btn_13:
                processClickBtn(view, 12);
                break;
            case R.id.btn_14:
                processClickBtn(view, 13);
                break;
            case R.id.btn_15:
                processClickBtn(view, 14);
                break;
            case R.id.btn_16:
                processClickBtn(view, 15);
                break;
            case R.id.btn_17:
                processClickBtn(view, 16);
                break;
            case R.id.btn_18:
                processClickBtn(view, 17);
                break;
            case R.id.btn_19:
                processClickBtn(view, 18);
                break;
            case R.id.btn_20:
                processClickBtn(view, 19);
                break;
        }
    }
}
