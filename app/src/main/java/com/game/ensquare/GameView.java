package com.game.ensquare;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    public interface CallBack {
        void endSession();
    }
    static CallBack mCallBack;
    Paint mPaint, mRectPaint, mBombPaint, mBomb2Paint, mScorePaint, mHelpPaint;
    Bitmap mHelpFinger;
    Bitmap mTenPoints, mHundredPoints;
    Bitmap mExplosion1, mExplosion2;
    Bitmap mGameOver;
    Bitmap mLevelUp;
    Bitmap mBestScore;
    Bitmap mHealth;
    Bitmap[] mItems = new Bitmap[13];
    Context mContext;
    int[] sourceX = new int[6];
    int[] sourceY = new int[6];
    int[] mX = new int[7];
    int[] mY = new int[7];
    int mScreenWidth, mScreenHeight;
    int[] path1 = new int[2];
    int[] path2 = new int[2];
    int[] path3 = new int[2];
    int[] path4 = new int[2];
    int[] path5 = new int[2];
    int[] path6 = new int[2];
    int[] path7 = new int[2];
    int[] path8 = new int[2];
    int[] path9 = new int[2];
    int[] path10 = new int[2];
    int[] path11 = new int[2];
    int[] path12 = new int[2];
    int[] path13 = new int[2];
    int[] path14 = new int[2];
    int[] path15 = new int[2];
    boolean[] mCanDraw = new boolean[13];
    float mFingerStartX, mFingerStartY;
    float mFingerEndX, mFingerEndY;
    boolean mTouchOngoing = false;
    int mFreezeCounter = 0;
    boolean mFreezeCounterOnGoing = false;
    int mSquare123Size, mSquare4Size, mSqaure5Size;
    int mCoin1Size, mCoin2Size;
    int mHealthWidth, mHealthHeight;
    int mGold1Width, mGold2Width, mGold3Width, mbomb1Width, mbomb2Width, mbomb3Width, mGold1Height, mGold2Height, mBomb1Height, mBomb2Height, mExplosion1Width, mExplosion2Width;
    int mExplosion1X = -10000, mExplosion1Y = -10000, mExplosion2X = -10000, mExplosion2Y = -10000;
    int mScore1X = -10000, mScore1Y, mScore2X = -10000, mScore2Y, mScore3X = -10000, mScore3Y, mScore4X = -10000, mScore4Y, mScore5X = -10000, mScore5Y = -10000;
    int mScore6X = -10000, mScore6Y = -10000, mScore7X = -10000, mScore7Y = -10000;
    int mHun1X = -10000, mHun1Y = -10000, mHun2X = -10000, mHun2Y = -10000, mHun3X = -10000, mHun3Y = -10000, mHun4X = -10000, mHun4Y = -10000, mHun5X = -10000, mHun5Y = -10000;
    int mHealthX = -10000, mHealthY = -10000;
    int mHealthAlpha = 256;
    Paint mHealthPaint;
    private int mCurrentScore = 0, mHighestScore = 0;
    int mGameOverWidth;

    int mExplosionAlpha = 256;
    int mExplosion2Alpha = 256;
    int mExplosionDelta = 0;

    int mCurrentHealth = 3;
    int mHealthBar = 0;
    int mHelpWaitCounter1 = 0, mHelpWaitCounter2 = 0;
    int mLevelSpeed = 0;

    boolean mHelpMode = false;

    float mLeft, mRight, mTop, mBottom;

    private int mCurrentLevel = 1;

    SharedPreferences mHighScorePrefs;
    int mHealthShown = 3;

    MediaPlayer mSquareSound, mGoldSound, mBombSound, mLevelChangeSound;

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public static void setListener(CallBack cb) {
        mCallBack = cb;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mCurrentScore = 0;
        Bitmap b;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.square1);
        mItems[0] = b.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);
        mSquare123Size = b.getWidth()/2;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.square2);
        mItems[1] = b.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.square3);
        mItems[2] = b.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.square4);
        mItems[3] = b.createScaledBitmap(b, b.getWidth()/7, b.getHeight()/7, false);
        mSquare4Size = b.getWidth()/7;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.square5);
        mItems[4] = b.createScaledBitmap(b, b.getWidth()/7, b.getHeight()/7, false);
        mSqaure5Size = b.getWidth()/7;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.coin1);
        mItems[5] = b.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);
        mCoin1Size = b.getWidth()/2;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.coin3);
        mItems[6] = b.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, false);
        mCoin2Size = b.getWidth()/2;
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gold1);
        mItems[7] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mGold1Width = b.getWidth();
        mGold1Height = b.getHeight();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gold_bag);
        mItems[8] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mGold2Width = b.getWidth();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb1);
        mItems[9] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mGold3Width = b.getWidth();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb2);
        mItems[10] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mbomb1Width = b.getWidth();
        mBomb1Height = b.getHeight();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb2);
        mItems[11] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mbomb2Width = b.getWidth();
        mBomb2Height = b.getHeight();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bomb2);
        mItems[11] = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mbomb2Width = b.getWidth();
        mBomb2Height = b.getHeight();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.health);
        mItems[12] = b.createScaledBitmap(b, b.getWidth()/3, b.getHeight()/3, false);
        mHealthWidth = b.getWidth();
        mHealthHeight = b.getHeight();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.explosion1);
        mExplosion1 = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mExplosion1Width = b.getWidth();
        b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.explosion1);
        mExplosion2 = b.createScaledBitmap(b, b.getWidth(), b.getHeight(), false);
        mExplosion2Width = b.getWidth();
        mExplosionDelta = mExplosion1Width - mbomb1Width;
        mTenPoints = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ten_points);
        mHundredPoints = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.hundred);
        mGameOver = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_over1);
        mGameOverWidth = mGameOver.getWidth();
        mLevelUp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_up);
        mBestScore = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.high_score);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mScreenWidth = width;
        mScreenHeight = height;
        mCounterLeft = width;
        mX[1] = width;
        path1[0] = 0;
        path1[1] = 0;

        path2[0] = width;
        path2[1] = 0;

        path3[0] = width;
        path3[1] = height;

        path4[0] = 0;
        path4[1] = height;

        path5[0] = 0;
        path5[1] = height/2;

        path6[0] = width;
        path6[1] = height/2;

        path7[0] = 0;
        path7[1] = height/2;

        path8[0] = 0;
        path8[1] = height;

        path9[0] = 0;
        path9[1] = height;

        path10[0] = mScreenWidth;
        path10[1] = (int) (mScreenHeight * 0.75);

        path11[0] = mScreenWidth/4;
        path11[1] = 0;

        path12[0] = mScreenWidth/4;
        path12[1] = 0;

        path13[0] = 0;
        path13[1] = mScreenHeight;

        path14[0] = 0;
        path14[1] = mScreenHeight;

        path15[0] = 0;
        path15[1] = mScreenHeight;

        sourceX[0] = 0;
        sourceY[0] = 0;
        sourceX[1] = 0;
        sourceY[1] = height/2;
        sourceX[2] = 0;
        sourceY[2] = height;
        sourceX[3] = width;
        sourceY[3] = 0;
        sourceX[4] = width;
        sourceY[4] = height/2;
        sourceX[5] = width;
        sourceY[5] = height;
        mPaint = new Paint();
        mBombPaint = new Paint();
        mBomb2Paint = new Paint();
        mRectPaint = new Paint();
        mScorePaint = new Paint();
        mLevelPaint = new Paint();
        mBestScorePaint = new Paint();
        mScorePaint.setColor(Color.BLUE);
        mScorePaint.setAlpha(150);
        mScorePaint.setTextSize(30);
        mScorePaint.setTypeface(Typeface.create("sans-serif-light",Typeface.NORMAL));
        mScorePaint.setAntiAlias(true);
        mRectPaint.setColor(Color.BLACK);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(5);
        mHelpPaint = new Paint();
        mHelpPaint.setAntiAlias(true);
        mHelpPaint.setTextSize(50);
        mLevelPaint.setAntiAlias(true);
        mBestScorePaint.setAntiAlias(true);
        mHealthBar = mScreenWidth;
        mHealthPaint = new Paint();
        mHealthPaint.setAntiAlias(true);

        mHighScorePrefs = mContext.getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        mHighestScore = mHighScorePrefs.getInt("HIGH_SCORE", 0);

        if(mHighestScore > 500) {
            mHelpMode = false;
        } else {
            mHelpMode = true;
            mHelpFinger = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finger);
        }
        mHelpLeft = mScreenWidth/6;
        mHelpTop = mScreenWidth/6;
        prepareSounds();
    }

    private void prepareSounds() {
        try{
            mSquareSound = MediaPlayer.create(mContext, R.raw.chime);
            mGoldSound = MediaPlayer.create(mContext, R.raw.gold);
            mBombSound = MediaPlayer.create(mContext, R.raw.bomb2);
            mLevelChangeSound = MediaPlayer.create(mContext, R.raw.level_upgrade);
        } catch (Exception e) {
            Log.e("EnSquare", "Gand fatt gayi "+e);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBestScoreCounter = 0;
        startGame();
    }

    Timer mLauncher;
    private void startGame() {
        mLauncher = new Timer();
        mLauncher.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                launchItem();
            }
        }, 0, 100);
    }

    int mItemToLaunch = 0;
    private void launchItem() {
        mItemToLaunch = new Random().nextInt(13);
        Log.d("Abhishek", "mItemToLaunch = "+mItemToLaunch);
        if(mCurrentScore < 200 && mItemToLaunch > 8) {
            return;
        }
        if(mCurrentScore < 1000 && mItemToLaunch > 9) {
            return;
        }
        if(mHealthShown == 0 && mItemToLaunch == 12 && mCurrentHealth < 3){
            mHealthShown = 3;
            mCanDraw[mItemToLaunch] = true;
            return;
        }
        if(mHealthShown > 0 && mItemToLaunch == 12){
            mHealthShown--;
            return;
        }
        mCanDraw[mItemToLaunch] = true;
    }

    int mCounterRight = 0,mCounter1, mCounter2X, mCounter2Y, mCounter3, mCounter4;
    int mCounterLeft;
    float mHelpLeft, mHelpRight, mHelpTop, mHelpBottom;
    int mHelpCounter = 0;
    int mLastLevelScore = 0;
    int mNextLevelScore = 500;
    int mLevelUpAlpha = 256;
    Paint mLevelPaint, mBestScorePaint;
    int mLevelUpCounter = 0;
    int mBestScoreCounter = 0;
    int mBestScoreAlpha = 256;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mHelpMode) {
//            if(path1[0] < mHelpLeft*1.5) {
//                path1[0] = path1[0] + 10;
//                path1[1] = path1[1] + 10;
//            }
            canvas.drawARGB(10, 0, 0, 0);
            if(mHelpWaitCounter1 < 20) {
                mHelpWaitCounter1++;
                invalidate();
                return;
            }
            if (path1[0] >= mScreenWidth || path1[1] >= mScreenHeight) {
                path1[0] = 0;
                path1[1] = 0;
                mCanDraw[0] = false;
            }
            if(mHelpRight < mScreenWidth - mHelpLeft) {
                path1[0] = path1[0] + 5;
                path1[1] = path1[1] + 5;
                path2[0] = path2[0] - 10;
                path2[1] = path2[1] + 10;
                mHelpRight = mHelpRight + 15;
                mHelpBottom = mHelpBottom + 20;
                canvas.drawBitmap(mItems[0], path1[0], path1[1], mPaint);
                canvas.drawBitmap(mItems[1], path2[0], path2[1], mPaint);
            } else {
                canvas.drawBitmap(mTenPoints, path1[0], path1[1], mPaint);
                canvas.drawBitmap(mTenPoints, path2[0], path2[1], mPaint);
                canvas.drawText("Touch anywhere to start playing", mScreenWidth/6, (float)(0.8*mScreenHeight), mHelpPaint);
            }
            canvas.drawBitmap(mHelpFinger, mHelpRight, mHelpBottom, mPaint);
            canvas.drawRect(mHelpLeft, mHelpTop, mHelpRight, mHelpBottom, mRectPaint);
        } else {
            mHelpWaitCounter1 = 0;
            if (mNextLevelScore - mCurrentScore <= 0) {
                mCurrentLevel++;
                mLevelSpeed++;
                mLevelChangeSound.start();
                mNextLevelScore = mCurrentScore + 500;
                mLevelUpCounter = 100;
                mLevelUpAlpha = 256;
            }
            if (mCurrentHealth <= 0) {
                canvas.drawBitmap(mGameOver, mScreenWidth/2 - mGameOverWidth/2, mScreenHeight/2, mPaint);
                canvas.drawText("Score : " + mCurrentScore, mScreenWidth / 2 - 80, mScreenHeight / 2, mScorePaint);
                if (mCurrentScore > mHighestScore) {
                    SharedPreferences.Editor editor = mHighScorePrefs.edit();
                    editor.putInt("HIGH_SCORE", mCurrentScore);
                    editor.commit();
                }
                if(mHelpWaitCounter2 < 1) {
                    mHelpWaitCounter2++;
                    invalidate();
                    return;
                } else {
                    mCallBack.endSession();
                    mLauncher.cancel();
                    return;
                }
            }
            canvas.drawText("Level : " + mCurrentLevel, mScreenWidth / 2, mScreenHeight / 2 + 70, mScorePaint);
            canvas.drawText("Health : " + mCurrentHealth, mScreenWidth / 2, 80, mScorePaint);
            if (mCurrentHealth == 3) {
                mPaint.setColor(Color.GREEN);
                mHealthBar = mScreenWidth;
            } else if (mCurrentHealth == 2) {
                mPaint.setColor(Color.YELLOW);
                mHealthBar = 2 * (mScreenWidth / 3);
            } else {
                mPaint.setColor(Color.RED);
                mHealthBar = mScreenWidth / 3;
            }
            canvas.drawRect(0, 0, mHealthBar, 20, mPaint);
            mCounterRight = mCounterRight + 10;
            mCounterLeft = mCounterLeft - 10;
            if (mCanDraw[0]) {
                mCounter1 = mCounter1 + 10;
                path1[0] = path1[0] + 10 + mLevelSpeed;
                path1[1] = path1[1] + 10 + mLevelSpeed;
                canvas.drawBitmap(mItems[0], path1[0], path1[1], mPaint);
                if (path1[0] >= mScreenWidth || path1[1] >= mScreenHeight) {
                    path1[0] = 0;
                    path1[1] = 0;
                    mCanDraw[0] = false;
                }
            }
            if (mCanDraw[1]) {
                path2[0] = path2[0] - 10 - mLevelSpeed;
                path2[1] = path2[1] + 10 + mLevelSpeed;
                canvas.drawBitmap(mItems[1], path2[0], path2[1], mPaint);
                if (path2[0] <= -mSquare123Size || path2[1] >= mScreenHeight) {
                    path2[0] = mScreenWidth;
                    path2[1] = 0;
                    mCanDraw[1] = false;
                }
            }
            if (mCanDraw[2]) {
                path3[0] = path3[0] - 10 - mLevelSpeed;
                path3[1] = path3[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[2], path3[0], path3[1], mPaint);
                if (path3[0] <= -mSquare123Size || path3[1] <= 0) {
                    path3[0] = mScreenWidth;
                    path3[1] = mScreenHeight;
                    mCanDraw[2] = false;
                }
            }
            if (mCanDraw[3]) {
                path4[0] = path4[0] + 10 + mLevelSpeed;
                path4[1] = path4[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[3], path4[0], path4[1], mPaint);
                if (path4[0] >= mScreenWidth || path4[1] <= 0) {
                    path4[0] = -mSquare4Size;
                    path4[1] = mScreenHeight;
                    mCanDraw[3] = false;
                }
            }
            if (mCanDraw[4]) {
                path5[0] = path5[0] + 10 + mLevelSpeed;
                path5[1] = path5[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[4], path5[0], path5[1], mPaint);
                if (path5[0] >= mScreenWidth || path5[1] <= 0) {
                    path5[0] = -mSquare4Size;
                    path5[1] = mScreenHeight / 2;
                    mCanDraw[4] = false;
                }
            }
            if (mCanDraw[5]) {
                path6[0] = path6[0] - 15 - mLevelSpeed;
                path6[1] = path6[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[5], path6[0], path6[1], mPaint);
                if (path6[0] >= mScreenWidth || path6[1] <= 0) {
                    path6[0] = mScreenWidth;
                    path6[1] = mScreenHeight / 2;
                    mCanDraw[5] = false;
                }
            }
            if (mCanDraw[6]) {
                path7[0] = path7[0] + 15 + mLevelSpeed;
                path7[1] = path7[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[6], path7[0], path7[1], mPaint);
                if (path7[0] >= mScreenWidth + mCoin2Size || path7[1] <= -mCoin2Size) {
                    path7[0] = -mCoin2Size;
                    path7[1] = mScreenHeight / 2;
                    mCanDraw[6] = false;
                }
            }
            if (mCanDraw[7]) {
                path10[0] = path10[0] - 15 - mLevelSpeed;
                path10[1] = path10[1] - 10 - mLevelSpeed;
                canvas.drawBitmap(mItems[7], path10[0], path10[1], mPaint);
                if (path10[0] <= -mSquare123Size || path10[1] <= 0) {
                    path10[0] = mScreenWidth;
                    path10[1] = (int) (mScreenHeight * 0.75);
                    mCanDraw[7] = false;
                }
            }
            if (mCanDraw[8]) {
                path11[0] = path11[0] + 7 + mLevelSpeed;
                path11[1] = path11[1] + 20 + mLevelSpeed;
                canvas.drawBitmap(mItems[8], path11[0], path11[1], mPaint);
                if (path11[0] >= mScreenWidth || path11[1] >= mScreenHeight) {
                    path11[0] = mScreenWidth / 4;
                    path11[1] = 0;
                    mCanDraw[8] = false;
                }
            }
            if (mCanDraw[9]) {
                path12[0] = path12[0] + 5 + mLevelSpeed;
                path12[1] = path12[1] + 20 + mLevelSpeed;
                canvas.drawBitmap(mItems[9], path12[0], path12[1], mPaint);
                if (path12[0] >= mScreenWidth || path12[1] >= mScreenHeight) {
                    path12[0] = mScreenWidth / 4;
                    path12[1] = 0;
                    mCanDraw[9] = false;
                }
            }
            if (mCanDraw[10]) {
                path13[0] = path13[0] + 5 + mLevelSpeed;
                path13[1] = path13[1] - 20 - mLevelSpeed;
                canvas.drawBitmap(mItems[10], path13[0], path13[1], mPaint);
                if (path13[0] >= mScreenWidth || path13[1] <= 0) {
                    path13[0] = 0;
                    path13[1] = mScreenHeight;
                    mCanDraw[10] = false;
                }
            }
            if (mCanDraw[12]) {
                path14[0] = path14[0] + 10 + mLevelSpeed;
                path14[1] = path14[1] - 30 - mLevelSpeed;
                canvas.drawBitmap(mItems[12], path14[0], path14[1], mPaint);
                if (path14[0] >= mScreenWidth + mHealthWidth || path14[1] <= -mHealthHeight) {
                    path14[0] = 0;
                    path14[1] = mScreenHeight;
                    mCanDraw[12] = false;
                }
            }
            if (mTouchOngoing) {
                canvas.drawRect(mFingerStartX, mFingerStartY, mFingerEndX, mFingerEndY, mRectPaint);
            }
            if (mFreezeCounterOnGoing) {
                if (mFingerEndY < mFingerStartY) {
                    mTop = mFingerEndY;
                    mBottom = mFingerStartY;
                } else {
                    mTop = mFingerStartY;
                    mBottom = mFingerEndY;
                }
                if (mFingerStartX < mFingerEndX) {
                    mLeft = mFingerStartX;
                    mRight = mFingerEndX;
                } else {
                    mLeft = mFingerEndX;
                    mRight = mFingerStartX;
                }
                mFreezeCounter++;
                if ((path1[0] > mLeft) && (path1[1] > mTop) && ((path1[0] + mSquare123Size) < mRight)
                        && (path1[1] + mSquare123Size) < mBottom) {
                    if (mScore1X == -10000) {
                        mScore1X = path1[0];
                        mScore1Y = path1[1];
                    }
                    mCanDraw[0] = false;
                    path1[0] = 0;
                    path1[1] = 0;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path2[0] > mLeft) && (path2[1] > mTop) && ((path2[0] + mSquare123Size) < mRight)
                        && (path2[1] + mSquare123Size) < mBottom) {
                    if (mScore2X == -10000) {
                        mScore2X = path2[0];
                        mScore2Y = path2[1];
                    }
                    mCanDraw[1] = false;
                    path2[0] = mScreenWidth;
                    path2[1] = 0;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path3[0] > mLeft) && (path3[1] > mTop) && ((path3[0] + mSquare123Size) < mRight)
                        && (path3[1] + mSquare123Size) < mBottom) {
                    if (mScore3X == -10000) {
                        mScore3X = path3[0];
                        mScore3Y = path3[1];
                    }
                    path3[0] = mScreenWidth;
                    path3[1] = mScreenHeight;
                    mCanDraw[2] = false;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path4[0] > mLeft) && (path4[1] > mTop) && ((path4[0] + mSquare4Size) < mRight)
                        && (path4[1] + mSquare4Size) < mBottom) {
                    if (mScore4X == -10000) {
                        mScore4X = path4[0];
                        mScore4Y = path4[1];
                    }
                    path4[0] = 0;
                    path4[1] = mScreenHeight;
                    mCanDraw[3] = false;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path5[0] > mLeft) && (path5[1] > mTop) && ((path5[0] + mSqaure5Size) < mRight)
                        && (path5[1] + mSqaure5Size) < mBottom) {
                    if (mScore5X == -10000) {
                        mScore5X = path5[0];
                        mScore5Y = path5[1];
                    }
                    path5[0] = -mSquare4Size;
                    path5[1] = mScreenHeight / 2;
                    mCanDraw[4] = false;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path6[0] > mLeft) && (path6[1] > mTop) && ((path6[0] + mCoin1Size) < mRight)
                        && (path6[1] + mCoin1Size) < mBottom) {
                    if (mScore6X == -10000) {
                        mScore6X = path6[0];
                        mScore6Y = path6[1];
                    }
                    path6[0] = mScreenWidth;
                    path6[1] = mScreenHeight / 2;
                    mCanDraw[5] = false;
                    mCurrentScore += 10;
                    mSquareSound.start();
                }
                if ((path7[0] > mLeft) && (path7[1] > mTop) && ((path7[0] + mCoin2Size) < mRight)
                        && (path7[1] + mCoin2Size) < mBottom) {
                    if (mScore7X == -10000) {
                        mScore7X = path7[0];
                        mScore7Y = path7[1];
                    }
                    path7[0] = -mCoin2Size;
                    path7[1] = mScreenHeight / 2;
                    mCanDraw[6] = false;
                    mCurrentScore += 20;
                }
                if ((path10[0] > mLeft) && (path10[1] > mTop) && ((path10[0] + mGold1Width) < mRight)
                        && (path10[1] + mGold1Height) < mBottom) {
                    if (mHun1X == -10000) {
                        mHun1X = path10[0];
                        mHun1Y = path10[1];
                    }
                    path10[0] = mScreenWidth;
                    path10[1] = (int) (mScreenHeight * 0.75);
                    mCanDraw[7] = false;
                    mCurrentScore += 100;
                    mGoldSound.start();
                }
                if ((path11[0] > mLeft) && (path11[1] > mTop) && ((path11[0] + mGold2Width) < mRight)
                        && (path11[1] + mGold2Height) < mBottom) {
                    if (mHun2X == -10000) {
                        mHun2X = path11[0];
                        mHun2Y = path11[1];
                    }
                    path11[0] = mScreenWidth / 4;
                    path11[1] = 0;
                    mCanDraw[8] = false;
                    mCurrentScore += 100;
                    mGoldSound.start();
                }
                if ((path12[0] > mLeft) && (path12[1] > mTop) && ((path12[0] + mbomb1Width) < mRight)
                        && (path12[1] + mBomb1Height) < mBottom) {
                    if (mExplosion1X == -10000) {
                        mExplosion1X = path12[0];
                        mExplosion1Y = path12[1];
                    }
                    path12[0] = mScreenWidth / 4;
                    path12[1] = 0;
                    mCanDraw[9] = false;
                    mCurrentHealth--;
                    mBombSound.start();
                }
                if ((path13[0] > mLeft) && (path13[1] > mTop) && ((path13[0] + mbomb2Width) < mRight)
                        && (path13[1] + mBomb2Height) < mBottom) {
                    if (mExplosion2X == -10000) {
                        mExplosion2X = path13[0];
                        mExplosion2Y = path13[1];
                    }
                    path13[0] = 0;
                    path13[1] = mScreenHeight;
                    mCanDraw[10] = false;
                    mCurrentHealth--;
                    mBombSound.start();
                }
                if ((path14[0] > mLeft) && (path14[1] > mTop) && ((path14[0] + mbomb2Width) < mRight)
                        && (path14[1] + mBomb2Height) < mBottom) {
                    if (mHealthX == -10000) {
                        mHealthX = path14[0];
                        mHealthY = path14[1];
                    }
                    path14[0] = 0;
                    path14[1] = mScreenHeight;
                    mCanDraw[12] = false;
                    if(mCurrentHealth < 3) {
                        mCurrentHealth++;
                    }
                    mLevelChangeSound.start();
                }
                if (mExplosion1X != -10000) {
                    mExplosion1X = mExplosion1X + 5;
                    mExplosion1Y = mExplosion1Y + 20;
                    mBombPaint.setAlpha(mExplosionAlpha);
                    canvas.drawBitmap(mExplosion1, mExplosion1X - mExplosionDelta / 2, mExplosion1Y - mExplosion1Width / 2, mBombPaint);
                    mExplosionAlpha = mExplosionAlpha - 5;
                }
                if (mExplosion2X != -10000) {
                    mExplosion2X = mExplosion2X + 5;
                    mExplosion2Y = mExplosion2Y - 20;
                    mBomb2Paint.setAlpha(mExplosion2Alpha);
                    canvas.drawBitmap(mExplosion2, mExplosion2X - mbomb2Width, mExplosion2Y, mBomb2Paint);
                    mExplosion2Alpha = mExplosion2Alpha - 5;
                }

                if (mHealthX != -10000) {
                    mHealthY = mHealthY - 70;
                    mHealthPaint.setAlpha(mHealthAlpha);
                    canvas.drawBitmap(mItems[12], mHealthX, mHealthY, mHealthPaint);
                    mHealthAlpha = mHealthAlpha - 40;
                }
                if (mScore1X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore1X, mScore1Y, mPaint);
                }
                if (mScore2X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore2X, mScore2Y, mPaint);
                }
                if (mScore3X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore3X, mScore3Y, mPaint);
                }
                if (mScore4X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore4X, mScore4Y, mPaint);
                }
                if (mScore5X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore5X, mScore5Y, mPaint);
                }
                if (mScore6X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore6X, mScore6Y, mPaint);
                }
                if (mScore6X != -10000) {
                    canvas.drawBitmap(mTenPoints, mScore7X, mScore7Y, mPaint);
                }
                if (mHun1X != -10000) {
                    canvas.drawBitmap(mHundredPoints, mHun1X, mHun1Y, mPaint);
                }
                if (mHun2X != -10000) {
                    canvas.drawBitmap(mHundredPoints, mHun2X, mHun2Y, mPaint);
                }
                if (mFreezeCounter > 20) {
                    mTouchOngoing = false;
                    mFreezeCounterOnGoing = false;
                    mFreezeCounter = 0;
                    mExplosion1X = -10000;
                    mExplosion1Y = -10000;
                    mExplosion2X = -10000;
                    mExplosion2Y = -10000;
                    mScore1X = -10000;
                    mScore1Y = -10000;
                    mScore2X = -10000;
                    mScore2Y = -10000;
                    mScore3X = -10000;
                    mScore3Y = -10000;
                    mScore4X = -10000;
                    mScore4Y = -10000;
                    mScore5X = -10000;
                    mScore5Y = -10000;
                    mScore6X = -10000;
                    mScore6Y = -10000;
                    mScore7X = -10000;
                    mScore7Y = -10000;
                    mHun1X = -10000;
                    mHun1Y = -10000;
                    mHun2X = -10000;
                    mHun2Y = -10000;
                    mHealthX = -10000;
                    mHealthY = -10000;

                    mExplosionAlpha = 256;
                    mExplosion2Alpha = 256;
                    mHealthAlpha = 256;
                }
            }

            if(mLevelUpCounter > 0) {
                canvas.drawBitmap(mLevelUp, mScreenWidth/2-mLevelUp.getWidth()/2, mScreenHeight/6 - mLevelUpCounter, mLevelPaint);
                mLevelUpCounter--;
                mLevelPaint.setAlpha(mLevelUpAlpha);
                mLevelUpAlpha -= 2;
            }

            if(mBestScoreCounter == 0 && mCurrentScore > 400 && mCurrentScore > mHighestScore){
                mBestScoreCounter = 100;
            }
            Log.d("Abhishek", "mBestScoreCounter = "+mBestScoreCounter);
            if(mBestScoreCounter > 0) {
                canvas.drawBitmap(mBestScore, mScreenWidth/2-mBestScore.getWidth()/2, mScreenHeight/2 - mBestScore.getHeight()/2, mBestScorePaint);
                mBestScoreCounter--;
                mBestScorePaint.setAlpha(mBestScoreAlpha);
                mBestScoreAlpha -= 2;
                if(mBestScoreCounter == 1){
                    mBestScoreCounter = -10000;
                }
            }
            canvas.drawText(Integer.toString(mCurrentScore), mScreenWidth / 2, mScreenHeight / 2, mScorePaint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mFreezeCounterOnGoing){
            return false;
        }
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                mFingerStartX = event.getRawX();
                mFingerStartY = event.getRawY();
                mFingerEndX = event.getRawX();
                mFingerEndY = event.getRawY();
                mTouchOngoing = true;
                mHelpMode = false;
                mRectPaint.setColor(Color.BLACK);
                break;

            case MotionEvent.ACTION_MOVE :
                mTouchOngoing = true;
                mFingerEndX = event.getRawX();
                mFingerEndY = event.getRawY();
                break;

            case MotionEvent.ACTION_CANCEL :
            case MotionEvent.ACTION_UP :
                //mTouchOngoing = false;
                mRectPaint.setColor(Color.RED);
                handleActionUp(true);
                break;
        }
        return true;
    }

    private void handleActionUp(boolean freeze) {
        mFreezeCounter = 0;
        if(freeze) {
            mFreezeCounterOnGoing = true;
        } else {
            mFreezeCounterOnGoing = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLauncher.cancel();
        for(int i = 0; i < 12; i++){
            if(mItems[i] != null && !mItems[i].isRecycled()){
                mItems[i].recycle();
            }
        }
        if(mExplosion1 != null && !mExplosion1.isRecycled()) {
            mExplosion1.recycle();
        }
        if(mExplosion2 != null && !mExplosion2.isRecycled()) {
            mExplosion2.recycle();
        }
        if(mTenPoints != null && !mTenPoints.isRecycled()) {
            mTenPoints.recycle();
        }
        if(mHundredPoints != null && !mHundredPoints.isRecycled()) {
            mHundredPoints.recycle();
        }
        if(mGameOver != null && !mGameOver.isRecycled()) {
            mGameOver.recycle();
        }
        if(mLevelUp != null && !mLevelUp.isRecycled()) {
            mLevelUp.recycle();
        }
        if(mBestScore != null && !mBestScore.isRecycled()) {
            mBestScore.recycle();
        }
    }
}
