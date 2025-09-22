package com.advance.sanatani_vivah.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GallaryNewActivity extends AppCompatActivity {

    private ViewPager pager_gallary;
    private AppCompatTextView tvCount;
    private CustomPagerAdapter adapter;
    private int pos = 0;
    private JSONArray array;
    private String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary_new);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Gallary");
//        toolbar.setNavigationOnClickListener(view -> finish());
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        pager_gallary = findViewById(R.id.pager_gallary);

        tvCount = findViewById(R.id.tvCount);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("imagePosition")) {
                pos = b.getInt("imagePosition");
            }
            if (b.containsKey("imageArray")) {
                try {
                    array = new JSONArray(b.getString("imageArray"));
                    images = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        images[i] = object.getString("value");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        adapter = new CustomPagerAdapter(this, images);
        pager_gallary.setAdapter(adapter);

        pager_gallary.setCurrentItem(pos);

        pager_gallary.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                String text = (position + 1) + " Out of " + images.length;
                tvCount.setText(text);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        String[] img_arr;

        public CustomPagerAdapter(Context context, String[] arr) {
            this.mContext = context;
            this.img_arr = arr;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View layout = inflater.inflate(R.layout.gallary_image, collection, false);

            TouchImageView img = layout.findViewById(R.id.myZoomageView);
            Picasso.get().load(img_arr[position]).placeholder(R.drawable.placeholder).into(img);

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return img_arr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}