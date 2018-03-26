package aksamedia.wikiloka.Class;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import aksamedia.wikiloka.R;

public class class_adapteriklanku_laku extends RecyclerView.Adapter<class_adapteriklanku_laku.BarangViewHolder>{
    List<class_iklanku_laku> cls_barang;
    ImageView gifiklan;
    private View v;
    private class_iklanku_laku cls;
    static Context ctx;
    public class_adapteriklanku_laku(List<class_iklanku_laku> class_barang){
        this.cls_barang = class_barang;
    }
    public class BarangViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView judul;
        TextView harga;
        TextView deskripsi;
        ImageView image;

        BarangViewHolder(View itemView) {
            super(itemView);
            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "font_serif.ttf");

            cv = (CardView)itemView.findViewById(R.id.cv);
            judul = (TextView)itemView.findViewById(R.id.detail_judul);
            harga = (TextView)itemView.findViewById(R.id.detail_harga);
            deskripsi = (TextView)itemView.findViewById(R.id.detail_desk);
            image = (ImageView)itemView.findViewById(R.id.detail_foto);
            judul.setTypeface(font);
            harga.setTypeface(font);
            deskripsi.setTypeface(font);
        }
    }
    @Override
    public int getItemCount() {
        return cls_barang.size();
    }
    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listlaku, viewGroup, false);
        BarangViewHolder pvh = new BarangViewHolder(v);
        return pvh;
    }

    public String ubah_rupiah(long harga){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");

        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        return "Rp. " + df.format(harga);

    }
    @Override
    public void onBindViewHolder(BarangViewHolder BarangViewHolder, int i) {
        cls=cls_barang.get(i);
        ctx=v.getContext();
        BarangViewHolder.judul.setText(cls.judul);
        BarangViewHolder.harga.setText(ubah_rupiah(Long.parseLong(cls.harga)));
        BarangViewHolder.deskripsi.setText(cls.deskripsi);

        Picasso.with(v.getContext())
                .load(cls.url)
                .fit()
                .centerCrop()
                .into(BarangViewHolder.image);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
