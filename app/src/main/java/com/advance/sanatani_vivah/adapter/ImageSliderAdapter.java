package com.advance.sanatani_vivah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH1> {

    private Context context;
    private List<String> arrayList = new ArrayList<>();
    private String imageUrl;

    public ImageSliderAdapter(Context context, ArrayList<String> urlList,String imageUrl) {
        this.context = context;
        this.arrayList = urlList;
        this.imageUrl = imageUrl;
    }


    @Override
    public SliderAdapterVH1 onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_image_slider_layout, null);
        return new SliderAdapterVH1(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH1 viewHolder, final int position) {
        String image = arrayList.get(position);

        if (!imageUrl.isEmpty()) {
            AppDebugLog.print("image urls : "+imageUrl+image);
            Picasso.get().load(imageUrl+image).error(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).resize(0, Common.convertDpToPixels(230, context)).centerCrop().into(viewHolder.imageViewBackground);
        } else {
            viewHolder.imageViewBackground.setImageResource(R.drawable.ic_placeholder);
        }

        //viewHolder.itemView.setOnClickListener(v -> Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return arrayList.size();
    }

    class SliderAdapterVH1 extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;

        public SliderAdapterVH1(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            this.itemView = itemView;
        }
    }

}

