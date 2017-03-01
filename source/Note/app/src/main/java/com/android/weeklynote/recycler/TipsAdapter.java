package com.android.weeklynote.recycler;

import com.android.weeklynote.R;

import java.util.List;

/**
 * Created by liujin on 2017/1/10.
 */
public class TipsAdapter extends XRecyclerViewAdapter<String>{


    public TipsAdapter(List<String> data) {
        super(data, R.layout.tips_item);
    }

    @Override
    public void convert(GeneralViewHolder holder, String t, int postion) {
        holder.setText(R.id.tips_str, t);
    }

}
