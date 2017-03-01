/*
 * Copyright (c) 2016.
 * 长虹智慧健康科技有限公司.
 * All Rights Reserved.
 */

package com.android.weeklynote.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class XRecyclerViewAdapter<K> extends RecyclerView.Adapter<GeneralViewHolder>{
    private List<K> mDatas;
    private final int mLayoutId;
    private OnItemClickListener onItemClickListener;
    protected Context mContext;

    public XRecyclerViewAdapter(List<K> data, int layoutId){
        mDatas = data;
        mLayoutId = layoutId;
    }

    public void setData(List<K> data){
        mDatas = data;
        notifyDataSetChanged();
    }

    public void addData(K data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.add(data);
        notifyDataSetChanged();
    }

    public void onLoadData(List<K> data){
        if(mDatas == null){
            mDatas = data;
        }else{
            if(data != null && data.size() > 0){
                for(K d: data){
                    if(!mDatas.contains(d)){
                        mDatas.add(d);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public K getData(int position) {
        if (mDatas != null && mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    public List<K> getData() {
        return mDatas;
    }
    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        GeneralViewHolder holder = new GeneralViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, final int position) {
        final int itemCount = getItemCount();
        if(position >= 0 && position < itemCount){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(position);
                    }
                }
            });
            K t = mDatas.get(position);
            convert(holder, t, position);
        }
    }

    public abstract void convert(GeneralViewHolder holder, K t, int postion);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     *  模仿listview的onitemclicklistener.
     */
    public interface OnItemClickListener {
        /**
         * @param positon the positon which item is clicked
         */
        void onItemClicked(int positon);
    }
}
