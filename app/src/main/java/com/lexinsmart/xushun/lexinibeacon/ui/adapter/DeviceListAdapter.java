package com.lexinsmart.xushun.lexinibeacon.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexinsmart.xushun.lexinibeacon.R;
import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by xushun on 2017/5/24.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>  {
    private LayoutInflater mInflater;
    private List<DeviceInfo> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public DeviceListAdapter(Context context, List<DeviceInfo> datas){
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lv_device_scan,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImageView = (ImageView) view.findViewById(R.id.imgSignalHeader);
        viewHolder.mTextView = (TextView) view.findViewById(R.id.tvDeviceName);
        viewHolder.llDeviceListItem = (LinearLayout) view.findViewById(R.id.llDeviceListItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeviceListAdapter.ViewHolder holder, final int position) {

        holder.mTextView.setText(mDatas.get(position).getDeviceName());


        if (mOnItemClickListener != null){
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            holder.llDeviceListItem.setBackgroundResource(R.drawable.bg_main_tab);
                            mOnItemClickListener.onClick(position);
                            break;
                        case MotionEvent.ACTION_UP:
                            holder.llDeviceListItem.setBackgroundResource(R.drawable.bg_main_tab_transparent);
                            break;
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
        public ImageView mImageView;
        public TextView mTextView;
        public LinearLayout llDeviceListItem;
    }
    public interface OnItemClickListener {
        void onClick(int position);
    }
}
