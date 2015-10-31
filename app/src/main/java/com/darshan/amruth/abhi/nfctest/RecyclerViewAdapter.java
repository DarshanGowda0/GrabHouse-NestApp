package com.darshan.amruth.abhi.nfctest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * Created by darshan on 31/10/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {

    Context context;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    ImageLoader imageLoader;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        // for dynamically loading images
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new SimpleBitmapDisplayer()).build();

        config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_house_card, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HouseDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        setData(holder,position);
    }

    private void setData(Holder holder, int position) {

        holder.priceTv.setText("Rs."+fetchData.dataList.get(position).price);
        holder.ratingTv.setText(fetchData.dataList.get(position).rating);
        holder.addressTv.setText(fetchData.dataList.get(position).address);
        holder.categoryTv.setText(fetchData.dataList.get(position).category);
//        imageLoader.displayImage(fetchData.dataList.get(position).image, (ImageAware) holder.imageLayout, defaultOptions);

    }

    @Override
    public int getItemCount() {
        return fetchData.dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        RelativeLayout imageLayout;
        TextView priceTv,ratingTv,addressTv,categoryTv;

        public Holder(View itemView) {
            super(itemView);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.rel1);
            priceTv = (TextView) itemView.findViewById(R.id.price);
            ratingTv = (TextView) itemView.findViewById(R.id.rating);
            addressTv = (TextView) itemView.findViewById(R.id.address);
            categoryTv = (TextView) itemView.findViewById(R.id.detail);
        }
    }
}
