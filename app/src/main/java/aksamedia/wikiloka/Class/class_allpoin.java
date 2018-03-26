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
import aksamedia.wikiloka.Gajelas.tv_euphemia;

public class class_allpoin extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] url;
    private final String[]  id_poin;
    private final String[] nama_poin;
    private final String[] harga_poin;
    private final String[] jumlah_poin;
    public class_allpoin(Activity context,String[] url, String[] id_poin, String[] nama_poin, String[] harga_poin,String[] jumlah_poin) {
        super(context, R.layout.custom_listpoin, nama_poin);
        this.context=context;
        this.url=url;
        this.id_poin=id_poin;
        this.nama_poin=nama_poin;
        this.harga_poin=harga_poin;
        this.jumlah_poin=jumlah_poin;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_listpoin, null, true);
        TextView tv1=(TextView) rowView.findViewById(R.id.tv_nama);
        tv1.setText(nama_poin[position]);
        TextView tv2=(TextView) rowView.findViewById(R.id.tv_harga);
        tv2.setText(harga_poin[position]);
        tv_euphemia tv3=(tv_euphemia) rowView.findViewById(R.id.tv_jumlah);
        tv3.setText(jumlah_poin[position]);
        TextView tv4=(TextView) rowView.findViewById(R.id.id_poin);
        tv4.setText(id_poin[position]);
        AQuery aq=new AQuery(context);
        aq.id(rowView.findViewById(R.id.icon)).image(url[position]);
        return rowView;
    };
}

