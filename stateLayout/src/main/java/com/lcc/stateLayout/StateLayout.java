package com.lcc.stateLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by lcc_luffy on 2016/1/30.
 */
public class StateLayout extends FrameLayout{
    protected View contentView;
    protected View emptyView;
    protected View errorView;
    protected View progressView;

    protected TextView emptyTextView;
    protected TextView errorTextView;
    protected TextView progressTextView;


    protected Button errorButton;
    protected Button emptyButton;

    public StateLayout(Context context) {
        this(context,null);
    }


    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        parseAttrs(context,attrs);

        emptyView.setVisibility(View.GONE);

        errorView.setVisibility(View.GONE);

        progressView.setVisibility(View.GONE);

        currentShowingView = contentView;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        int progressViewId;
        int errorViewId;
        int emptyViewId;
        try
        {
            progressViewId = a.getResourceId(R.styleable.StateLayout_progressView, -1);
            errorViewId = a.getResourceId(R.styleable.StateLayout_errorView, -1);
            emptyViewId = a.getResourceId(R.styleable.StateLayout_emptyView, -1);

        } finally
        {
            a.recycle();
        }

        /******************************************************************************************/

        if (progressViewId < 0) {
            progressView = inflater.inflate(R.layout.view_progress, this, false);
            progressTextView = (TextView) progressView.findViewById(R.id.progressTextView);
        } else {
            progressView = inflater.inflate(progressViewId, this, false);
        }
        addView(progressView);
        /******************************************************************************************/

        /******************************************************************************************/

        if (errorViewId < 0) {
            errorView = inflater.inflate(R.layout.view_error, this, false);
            errorTextView = (TextView) errorView.findViewById(R.id.errorTextView);
            errorButton = (Button) errorView.findViewById(R.id.errorButton);
        } else {
            errorView = inflater.inflate(errorViewId, this, false);
        }

        addView(errorView);
        /******************************************************************************************/

        /******************************************************************************************/

        if (emptyViewId < 0) {
            emptyView = inflater.inflate(R.layout.view_empty, this, false);
            emptyTextView = (TextView) emptyView.findViewById(R.id.emptyTextView);
            emptyButton = (Button) emptyView.findViewById(R.id.emptyButton);
        } else {
            emptyView = inflater.inflate(emptyViewId, this, false);
        }

        addView(emptyView);
        /******************************************************************************************/

    }

    private void checkIsContentView(View view)
    {
        Log.i("state",view != null ? view.getClass().getName() : "add null view");
        if(contentView == null && view != errorView && view != progressView && view != emptyView)
        {
            contentView = view;
            currentShowingView = contentView;
        }
    }


    private View currentShowingView;
    public void switchWithAnimation(final View toBeShown)
    {
        final View toBeHided = currentShowingView;
        if(toBeHided == toBeShown)
            return;

        if(toBeHided != null)
        {
            toBeHided.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0)
                    .setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toBeHided.setVisibility(GONE);
                }
            });
        }

        if(toBeShown != null)
        {
            currentShowingView = toBeShown;
            toBeShown.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toBeShown.setVisibility(VISIBLE);
                }
            });
        }
    }



    public void showContentView()
    {
        switchWithAnimation(contentView);
    }
    public void showEmptyView()
    {
        showEmptyView(null);
    }

    public void showEmptyView(String msg)
    {
        onHideContentView();
        emptyTextView.setText(msg);
        switchWithAnimation(emptyView);
    }

    public void showErrorView()
    {
        showErrorView(null);
    }

    public void showErrorView(String msg)
    {
        onHideContentView();
        if (msg != null)
            errorTextView.setText(msg);
        switchWithAnimation(errorView);
    }

    public void showProgressView()
    {
        showProgressView(null);
    }
    public void showProgressView(String msg)
    {
        onHideContentView();
        if (msg != null)
            progressTextView.setText(msg);
        switchWithAnimation(progressView);
    }

    public void setErrorAction(String action, final OnClickListener onErrorButtonClickListener)
    {
        errorButton.setText(action);
        errorButton.setOnClickListener(onErrorButtonClickListener);
    }


    public void setEmptyAction(String action, final OnClickListener onEmptyButtonClickListener)
    {
        emptyButton.setText(action);
        emptyButton.setOnClickListener(onEmptyButtonClickListener);
    }

    protected void onHideContentView()
    {
        //Override me
    }



    /**
     * addView
     */

    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}
