package com.example.ximalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.example.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: AtlanticYu
 * Date: 2019/6/13
 * Time: 13:32
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {
    private List<Track> mDetailData = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");
    private ItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public DetailListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //装载view,可通过view寻找控件,实际指一个专辑的item

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.InnerHolder innerHolder, final int position) {
        //绑定控件,itemView已能够寻找到控件，否则用this,但是用this需要主activity使用setContentView绑定布局
        View itemView = innerHolder.itemView;
        //顺序id
        TextView orderTv = itemView.findViewById(R.id.order_text);
        //标题
        TextView titleTv = itemView.findViewById(R.id.detail_item_title);
        //播放次数
        TextView playCountTv = itemView.findViewById(R.id.detail_item_play_count);
        //时长
        TextView durationTv = itemView.findViewById(R.id.detail_item_duration);
        //更新日期
        TextView updateDateTv = itemView.findViewById(R.id.detail_item_update_time);

        //设置数据
        Track track = mDetailData.get(position);
        orderTv.setText(position +"");
        titleTv.setText(track.getTrackTitle());
        int playCount = track.getPlayCount();
        //播放数转换
        if (playCount > 10000)
        {
            playCountTv.setText(playCount/10000 +"万");
        }
        else {
            playCountTv.setText(playCount +"");
        }
        //毫秒转换
        int durationMil = track.getDuration() * 1000;
        String duration = mDurationFormat.format(durationMil);
        durationTv.setText(duration);

        String updatetimeText = mUpdateDateFormat.format(track.getUpdatedAt());
        updateDateTv.setText(updatetimeText);

        //设置item的点击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"你点击了第  " + i + "个",Toast.LENGTH_SHORT).show();
                if (mItemClickListener != null) {
                    //参数需要有列表和数据
                    mItemClickListener.onItemClick(mDetailData,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        //清楚原来数据
        mDetailData.clear();
        //添加新数据
        mDetailData.addAll(tracks);
        //UI更新
        notifyDataSetChanged();

    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    //让点击事件方法暴漏出去
    public interface ItemClickListener {
        void onItemClick(List<Track> detailData, int position);
    }

}
