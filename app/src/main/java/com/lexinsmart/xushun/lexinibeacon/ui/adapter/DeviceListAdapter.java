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
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.RssiUtil;
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
        viewHolder.signalHeaderImg = (ImageView) view.findViewById(R.id.imgSignalHeader);
        viewHolder.tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        viewHolder.llDeviceListItem = (LinearLayout) view.findViewById(R.id.llDeviceListItem);
        viewHolder.tvMajor = (TextView) view.findViewById(R.id.tvMajor);
        viewHolder.tvMinor = (TextView) view.findViewById(R.id.tvMinor);
        viewHolder.tvPower = (TextView) view.findViewById(R.id.tvPower);
        viewHolder.tvMac = (TextView) view.findViewById(R.id.tvMac);
        viewHolder.tvRssi = (TextView) view.findViewById(R.id.tvRssi);
        viewHolder.tvDistance = (TextView) view.findViewById(R.id.tvDistance);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeviceListAdapter.ViewHolder holder, final int position) {

        holder.tvDeviceName.setText(mDatas.get(position).getDeviceName());
        holder.tvMajor.setText(mDatas.get(position).getMajor());
        holder.tvMinor.setText(mDatas.get(position).getMinor());
        holder.tvMac.setText(mDatas.get(position).getMac());
        holder.tvPower.setText(mDatas.get(position).getPower());
        holder.tvRssi.setText(""+mDatas.get(position).getRssi());

        if (mDatas.get(position).getRssi() != null){
            Double distance = RssiUtil.getDistance(mDatas.get(position).getRssi());
            holder.tvDistance.setText(String.valueOf(distance));

        }


        if (mOnItemClickListener != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);

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
        public ImageView signalHeaderImg;
        public TextView tvDeviceName;
        public TextView tvMajor,tvMinor,tvPower,tvMac,tvRssi,tvDistance;
        public LinearLayout llDeviceListItem;
    }
    public interface OnItemClickListener {
        void onClick(int position);
    }
}
