package com.android.weeklynote.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralViewHolder extends RecyclerView.ViewHolder{

    private final SparseArray<View> mViews;

    public GeneralViewHolder(View itemView) {
        super(itemView);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(params);

        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setText(int viewId, String text) {
        TextView txt = getView(viewId);
        txt.setText(text);
    }

    public GeneralViewHolder setViewVisible(int viewId, boolean visible) {
        View txt = getView(viewId);
        if (visible) {
            txt.setVisibility(View.VISIBLE);
        } else {
            txt.setVisibility(View.GONE);
        }
        return this;
    }

}
