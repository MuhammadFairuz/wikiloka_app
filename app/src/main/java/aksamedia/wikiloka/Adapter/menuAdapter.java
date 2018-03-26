package aksamedia.wikiloka.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aksamedia.wikiloka.R;

/**
 * Created by congf on 11/01/2017.
 */

public class menuAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<String> menu;
    private ArrayList<String> icon_url;
    private static LayoutInflater inflater = null;
    Context context;

    public menuAdapter(Activity activity, ArrayList<String> menu, ArrayList<String> icon_url, Context context) {
        this.activity = activity;
        this.menu = menu;
        this.icon_url = icon_url;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (menu.size() <= 0)
            return 1;
        return menu.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;

        if (view == null) {
            vi = inflater.inflate(R.layout.layout_menu, null);
            holder = new ViewHolder();
            holder.text_menu = (TextView) vi.findViewById(R.id.text_menu);
            holder.image_menu = (ImageView) vi.findViewById(R.id.icon_menu);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        holder.text_menu.setText(menu.get(i));
        Picasso
                .with(context)
                .load(icon_url.get(i))
                .into(holder.image_menu);

        return null;
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder {
        public TextView text_menu;
        public ImageView image_menu;
    }
}
