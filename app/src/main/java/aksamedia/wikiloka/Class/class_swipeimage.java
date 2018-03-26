package aksamedia.wikiloka.Class;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import aksamedia.wikiloka.R;

public class class_swipeimage extends PagerAdapter {
    private Context ctx;
    private String []url;
    private LayoutInflater layoutInflater;
    private ImageView img;

    public class_swipeimage(Context ctx, String[] url){
        this.ctx=ctx;
        this.url=url;
    }
    @Override
    public int getCount() {
        return url.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view==(LinearLayout)o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.custom_swipeimage,container,false);
        img=(ImageView) item_view.findViewById(R.id.img_swipe);
        Picasso.with(ctx)
                .load(url[position])
                .fit()
                .centerCrop()
                .into(img);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
