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

import aksamedia.wikiloka.R;

public class class_provinsi extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] prov;
    private final String[] id_prov;
    public class_provinsi(Activity context, String[] id_prov, String[] prov) {
        super(context, R.layout.custom_menu, prov);
        this.context=context;
        this.prov=prov;
        this.id_prov=id_prov;

    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_sub_kategori, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtlist);
        TextView id_kat = (TextView) rowView.findViewById(R.id.txtid);
        id_kat.setText(id_prov[position]);
        txtTitle.setText(prov[position]);
        return rowView;
    };
}

