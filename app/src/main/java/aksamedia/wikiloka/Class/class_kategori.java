package aksamedia.wikiloka.Class;

/**
 * Dibuat oleh ANONYMOUS pada 11/01/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;

import aksamedia.wikiloka.R;

public class class_kategori extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final String[] id_kategori;
    private final String[] url;
    public class_kategori(Activity context,String[]id_kat, String[] itemname, String[] url) {
        super(context, R.layout.custom_menu, itemname);
        this.context=context;
        this.itemname=itemname;
        this.url=url;
        this.id_kategori=id_kat;

    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_kategori, null, true);
        AQuery aq=new AQuery(context);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtlist);
        TextView id_kat = (TextView) rowView.findViewById(R.id.txtid);
        aq.id(rowView.findViewById(R.id.icon)).image(url[position]);
        id_kat.setText(id_kategori[position]);
        txtTitle.setText(itemname[position]);
        return rowView;
    };
}

