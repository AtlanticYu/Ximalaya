package com.example.ximalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ximalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;



public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    private List<Album> mData = new ArrayList<>();

    private static final String TAG = "RecommendListAdapter";
    private onRecommendItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public RecommendListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //这里是创建条目的回调函数
        //载入view
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommend,viewGroup,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListAdapter.InnerHolder innerHolder, int i) {
        //设置数据,涉及即将出现在屏幕上的一条数据,第一次翻到第i条数据就执行一次此函数,下翻最后一条+1，上翻牵扯之前的数据
        innerHolder.itemView.setTag(i);
        innerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    int clickposition = (int) view.getTag();
                    mItemClickListener.onItemClick(clickposition,mData.get(clickposition));
                }
                Log.d(TAG,"innerHolder.itemView click -->" + view.getTag());
            }
        });
        innerHolder.setData(mData.get(i));

    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {

        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        //更新一下UI
        notifyDataSetChanged();
        //调用onCreateViewHolder()和onBindViewHolder()方法

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到各个控件，设置数据
            //专辑封面
            ImageView albumCoverIy = itemView.findViewById(R.id.album_cover);
            //title
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDescrTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            //专辑内容数量
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);

            albumTitleTv.setText(album.getAlbumTitle());
//            LogUtil.d(TAG,"---->节目题目" + album.getAlbumTitle());
            albumDescrTv.setText(album.getAlbumIntro());
            //播放数目为long类型，加“”表示拼接为字符串类型
            albumPlayCountTv.setText(album.getPlayCount() + "");
            albumContentCountTv.setText(album.getIncludeTrackCount() + "");
            //节目封面, path.trim().length() == 0 ,如果不加判断可能造成图片url地址未获取到致使picasso失败，程序崩溃，（没错，就是没有数据）
            //多此判断也不会致使图片无法加载，只是图片需后加载
            if (album.getCoverUrlLarge().length() != 0)
            {
                Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIy);
            }
        }
    }

    public void setOnRecommendItemClickListener(onRecommendItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface onRecommendItemClickListener {
        void onItemClick(int position, Album album);
    }
}
