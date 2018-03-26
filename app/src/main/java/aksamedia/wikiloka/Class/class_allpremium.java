package aksamedia.wikiloka.Class;

/**
 * Dibuat oleh ANONYMOUS pada 11/01/2016.
 */

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import aksamedia.wikiloka.R;

public class class_allpremium extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] url;
    private final String[] id_premium;
    private final String[] nama_premium;
    private final String[] status_web;
    public class_allpremium(Activity context, String[] url, String[] id_premium, String[] nama_premium, String []status_web) {
        super(context, R.layout.custom_listbelipremium, id_premium);
        this.context=context;
        this.url=url;
        this.id_premium=id_premium;
        this.nama_premium=nama_premium;
        this.status_web=status_web;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_listbelipremium, null, true);
        RelativeLayout list=(RelativeLayout) rowView.findViewById(R.id.bg_list);
        if(status_web[position].equals("tidakada")){
            list.setBackgroundColor(Color.parseColor("#46b8da"));
        }else if(status_web[position].equals("ada")){
            list.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        TextView tv1=(TextView) rowView.findViewById(R.id.tv_namapremium);
        tv1.setText(nama_premium[position]);
        TextView tv2=(TextView) rowView.findViewById(R.id.tv_idpremium);
        tv2.setText(id_premium[position]);
        TextView tv3=(TextView) rowView.findViewById(R.id.tv_statusweb);
        tv3.setText(status_web[position]);
        AQuery aq=new AQuery(context);
        aq.id(rowView.findViewById(R.id.icon)).image(url[position]);
        return rowView;
    };
}

