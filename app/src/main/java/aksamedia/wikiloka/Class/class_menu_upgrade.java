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

public class class_menu_upgrade extends ArrayAdapter<String> {
    private final Activity context;
    public String[] keterangan,sub_keterangan,menu_upgrade;
    TextView txt_keterangan,txt_sub_keterangan,txt_namakolom;
    public class_menu_upgrade(Activity context, String[] keterangan, String[] sub_keterangan,String [] nama_kolom) {
        super(context, R.layout.custom_menu_upgrade, keterangan);
        this.context=context;
        this.keterangan=keterangan;
        this.sub_keterangan=sub_keterangan;
        this.menu_upgrade=nama_kolom;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_menu_upgrade, null);
        txt_keterangan = (TextView) rowView.findViewById(R.id.txt_keterangan);
        txt_sub_keterangan = (TextView) rowView.findViewById(R.id.txt_sub_keterangan);
        txt_namakolom=(TextView) rowView.findViewById(R.id.txt_namakolom);
        txt_keterangan.setText(keterangan[position]);
        txt_sub_keterangan.setText(sub_keterangan[position]);
        txt_namakolom.setText(menu_upgrade[position]);
        return rowView;
    };
}

