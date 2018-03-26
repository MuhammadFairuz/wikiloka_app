package aksamedia.wikiloka.Class;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import aksamedia.wikiloka.R;

public class class_adapterlistpremium extends RecyclerView.Adapter<class_adapterlistpremium.BarangViewHolder>{
    List<class_listpremium> cls_barang;
    ImageView gifiklan;
    private View v;
    static Context ctx;
    private class_listpremium cls;
    public static String id_premium;
    public interface OnItemCheckListener {
        void onItemCheck(class_listpremium item);
        void onItemUncheck(class_listpremium item);
    }


    @NonNull
    private OnItemCheckListener onItemCheckListener;
    public class_adapterlistpremium(List<class_listpremium> class_barang, @NonNull OnItemCheckListener onItemCheckListener){
        this.cls_barang = class_barang;
        this.onItemCheckListener = onItemCheckListener;
    }
    public class BarangViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView detail_foto;
        TextView detail_judul, detail_tanggal;
        TextView detail_harga;
        TextView detail_seo;
        CheckBox cb_beli;
        View item_view;
        BarangViewHolder(View itemView) {
            super(itemView);
            this.item_view=itemView;
            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "font_serif.ttf");
            cv = (CardView) itemView.findViewById(R.id.cv);
            detail_foto = (ImageView) itemView.findViewById(R.id.detail_foto);
            detail_judul = (TextView) itemView.findViewById(R.id.detail_judul);
            detail_tanggal = (TextView) itemView.findViewById(R.id.detail_tanggal);
            detail_harga = (TextView) itemView.findViewById(R.id.detail_harga);
            detail_seo=(TextView) itemView.findViewById(R.id.detail_seo);
            cb_beli=(CheckBox) itemView.findViewById(R.id.cekbox_beli);
            cb_beli.setClickable(false);
            detail_judul.setTypeface(font);
            detail_tanggal.setTypeface(font);
            detail_harga.setTypeface(font);
        }
    }

    @Override
    public int getItemCount() {
        return cls_barang.size();
    }
    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listpremium, viewGroup, false);
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
    public void onBindViewHolder(final BarangViewHolder BarangViewHolder, int i) {
        cls=cls_barang.get(i);
        final class_listpremium currentItem = cls_barang.get(i);

        ctx=v.getContext();
        BarangViewHolder.detail_judul.setText(cls.judul);
        BarangViewHolder.detail_harga.setText(ubah_rupiah(Long.parseLong(cls.harga)));
        BarangViewHolder.detail_tanggal.setText(cls.tanggal);
        Picasso.with(v.getContext())
                .load(cls.url)
                .fit()
                .centerCrop()
                .into(BarangViewHolder.detail_foto);
        BarangViewHolder.detail_seo.setText(cls.seo_iklan);
        BarangViewHolder.detail_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarangViewHolder.cb_beli.setChecked(!BarangViewHolder.cb_beli.isChecked());
                if(BarangViewHolder.cb_beli.isChecked()){
                    onItemCheckListener.onItemCheck(currentItem);
                }else{
                    onItemCheckListener.onItemUncheck(currentItem);
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
