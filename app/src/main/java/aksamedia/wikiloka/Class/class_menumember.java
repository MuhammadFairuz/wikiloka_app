package aksamedia.wikiloka.Class;

/**
 * Dibuat oleh ANONYMOUS pada 11/01/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import aksamedia.wikiloka.R;

public class class_menumember extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] text;
    private final int[] gambar;
    private final String[] expand;

    public class_menumember(Activity context, int[] gambar, String[] text, String[] expand) {
        super(context, R.layout.list_menumember, text);
        this.context = context;
        this.text = text;
        this.gambar = gambar;
        this.expand = expand;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_menumember, null, true);
        ImageView exp_col = (ImageView) rowView.findViewById(R.id.exp_col);

        if (expand[position].equals("")) {
            exp_col.setVisibility(View.GONE);
        } else {
            exp_col.setVisibility(View.VISIBLE);
        }

        ImageView img_gambar = (ImageView) rowView.findViewById(R.id.gambar);
        TextView teks = (TextView) rowView.findViewById(R.id.teks);
        teks.setText(text[position]);

        if (position > 6) {
            img_gambar.setVisibility(View.GONE);
        }
        img_gambar.setImageResource(gambar[position]);

        return rowView;
    }

    ;
}

