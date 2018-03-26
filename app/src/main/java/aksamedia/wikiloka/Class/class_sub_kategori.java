package aksamedia.wikiloka.Class;

/**
 * Dibuat oleh ANONYMOUS pada 11/01/2016.
 */

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import aksamedia.wikiloka.R;

public class class_sub_kategori extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final String[] id_kategori;
    private final String[] ada;
    public class_sub_kategori(Activity context, String[] id_kat, String[] itemname,String [] ada) {
        super(context, R.layout.custom_menu, itemname);
        this.context=context;
        this.itemname=itemname;
        this.id_kategori=id_kat;
        this.ada=ada;

    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_sub_kategori, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtlist);
        TextView id_kat = (TextView) rowView.findViewById(R.id.txtid);
        ImageView img_ada=(ImageView) rowView.findViewById(R.id.ada);
        if(ada[position].equals("ada")){
            img_ada.setVisibility(View.VISIBLE);
        }else if(ada[position].equals("kosong")){
            img_ada.setVisibility(View.INVISIBLE);
        }
        id_kat.setText(id_kategori[position]);
        txtTitle.setText(Html.fromHtml(itemname[position]));
        return rowView;
    };
}

