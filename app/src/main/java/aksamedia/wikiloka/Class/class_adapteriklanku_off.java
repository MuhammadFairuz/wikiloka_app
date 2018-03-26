package aksamedia.wikiloka.Class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Gajelas.tv_ubuntu;
import cz.msebera.android.httpclient.Header;

public class class_adapteriklanku_off extends RecyclerView.Adapter<class_adapteriklanku_off.BarangViewHolder> {
    List<class_iklanku_off> cls_barang;
    private View v;
    private class_iklanku_off cls;
    private AsyncHttpClient client;
    private SimpleArcDialog mDialog;
    private SnackBar snackBar;

    public class_adapteriklanku_off(List<class_iklanku_off> class_barang) {
        this.cls_barang = class_barang;
    }

    public class BarangViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView judul,harga,deskripsi,label;

        ImageView image,ic_delete;
        tv_ubuntu btn_renew;

        BarangViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "font_serif.ttf");

            judul = (TextView) itemView.findViewById(R.id.detail_judul);
            harga = (TextView) itemView.findViewById(R.id.detail_harga);
            deskripsi = (TextView) itemView.findViewById(R.id.detail_desk);
            image = (ImageView) itemView.findViewById(R.id.detail_foto);
            btn_renew = (tv_ubuntu) itemView.findViewById(R.id.link_renew);
            label=(TextView) itemView.findViewById(R.id.tv_label);
            ic_delete=(ImageView) itemView.findViewById(R.id.ic_close);
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
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listnonaktif, viewGroup, false);
        BarangViewHolder pvh = new BarangViewHolder(v);
        return pvh;
    }

    public String ubah_rupiah(long harga) {
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
        cls = cls_barang.get(i);
        final int position=i;
        BarangViewHolder.judul.setText(cls.judul);
        BarangViewHolder.harga.setText(ubah_rupiah(Long.parseLong(cls.harga)));
        BarangViewHolder.deskripsi.setText(cls.deskripsi);
        if(cls.label.equals("Validasi")){
            BarangViewHolder.label.setBackgroundColor(Color.parseColor("#8c4dd0e1"));
        }else{
            BarangViewHolder.label.setBackgroundColor(Color.parseColor("#8cd50000"));
        }
        BarangViewHolder.label.setText(cls.label);
        Picasso.with(v.getContext())
                .load(cls.url)
                .fit()
                .centerCrop()
                .into(BarangViewHolder.image);
        BarangViewHolder.btn_renew.setVisibility(View.INVISIBLE);
        if(cls.renew.equals("validasi")){
            BarangViewHolder.btn_renew.setVisibility(View.INVISIBLE);
        }else {
            BarangViewHolder.btn_renew.setVisibility(View.VISIBLE);
        }
        final String renew=cls.renew;
        final String id_iklan=cls.id_iklan;
        BarangViewHolder.btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("renew", renew);
                if (renew.equals("renew")) {
                    renew(v.getContext(), class_bantuan.api_key, id_iklan, position);
                } else {
                    //buka form upgrade
                }
            }
        });
        BarangViewHolder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusiklan(v.getContext(), class_bantuan.api_key, id_iklan, position);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void hapusiklan(final Context ctx,String api_key,String id_iklan, final int position){
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_iklan", id_iklan);
        client = new AsyncHttpClient();
        client.post(class_bantuan.base_url + "hapusiklan", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(ctx);
                mDialog.setConfiguration(new ArcConfiguration(ctx));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String respon = "";
                respon = new String(responseBody);
                try {
                    mDialog.dismiss();
                    if (respon.equals("sukses")) {
                        cls_barang.remove(position);
                        notifyDataSetChanged();
                        dialog_sukses(ctx, "Iklan berhasil dihapus");
                    }
                } catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_sukses(ctx, "Kesalahan :" + ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_sukses(ctx, "Gagal koneksi");
            }
        });
    }
    public void renew(final Context ctx,String api_key,String id_iklan, final int position){
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_iklan", id_iklan);
        client = new AsyncHttpClient();
        client.post(class_bantuan.base_url + "renew", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(ctx);
                mDialog.setConfiguration(new ArcConfiguration(ctx));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String respon = "";
                respon = new String(responseBody);
                try {
                    if (respon.equals("sukses")) {
                        mDialog.dismiss();
                        cls_barang.remove(position);
                        notifyDataSetChanged();
                        dialog_sukses(ctx,"Upgrade iklan sukses");
                    }
                } catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_sukses(ctx,"Kesalahan :" + ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_sukses(ctx,"Gagagl koneksi");
            }
        });
    }
    public void dialog_sukses(Context ctx,String pesan) {
        snackBar = new SnackBar.Builder((Activity)ctx)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik", "tutup");
                    }
                })
                .withMessage(pesan)
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }
}
