package aksamedia.wikiloka.Class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import aksamedia.wikiloka.Activity.activity_detail;
import aksamedia.wikiloka.R;
import cz.msebera.android.httpclient.Header;

public class class_adapteriklanku_on extends RecyclerView.Adapter<class_adapteriklanku_on.BarangViewHolder> implements View.OnClickListener {
    List<class_iklanku_on> cls_barang;
    private View v;
    private class_iklanku_on cls;
    private SimpleArcDialog mDialog;
    private String respon_server;
    private SnackBar snackBar;
    private AsyncHttpClient client;

    public class_adapteriklanku_on(List<class_iklanku_on> class_barang){
        this.cls_barang = class_barang;
    }


    public class BarangViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView judul;
        TextView harga;
        TextView deskripsi;
        ImageView image,ic_delete;
        Button btn_laku;

        BarangViewHolder(View itemView) {
            super(itemView);
            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "font_serif.ttf");
            cv = (CardView)itemView.findViewById(R.id.cv);
            judul = (TextView)itemView.findViewById(R.id.detail_judul);
            harga = (TextView)itemView.findViewById(R.id.detail_harga);
            deskripsi = (TextView)itemView.findViewById(R.id.detail_desk);
            image = (ImageView)itemView.findViewById(R.id.detail_foto);
            ic_delete=(ImageView) itemView.findViewById(R.id.ic_close);
            btn_laku=(Button) itemView.findViewById(R.id.btn_laku);
            judul.setTypeface(font);
            harga.setTypeface(font);
            deskripsi.setTypeface(font);
        }


    }
    private void ubah_ke_laku(final Context ctx,String url,String api_key,String id, final int position){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id", id);
        mDialog = new SimpleArcDialog(ctx);
        respon_server ="";

        client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        mDialog = new SimpleArcDialog(ctx);
                        mDialog.setConfiguration(new ArcConfiguration(ctx));
                        mDialog.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        respon_server = new String(responseBody);
                        if (respon_server.equals("failed")) {
                            mDialog.dismiss();
                            dialog_sukses(ctx, "Proses ubah iklan anda gagal");
                        } else if (respon_server.equals("Success")) {
                            mDialog.dismiss();
                            cls_barang.remove(position);
                            notifyDataSetChanged();
                            dialog_sukses(ctx, "Proses ubah iklan ke laku sukses.");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        mDialog.dismiss();
                        Log.e("ubah_ke_laku", new String(responseBody));
                        dialog_sukses(ctx, "Gagal terkoneksi.");
                    }

                }
        );
    }
    @Override
    public int getItemCount() {
        return cls_barang.size();
    }
    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listaktif, viewGroup, false);
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
        BarangViewHolder.judul.setText(cls.judul);
        BarangViewHolder.harga.setText(ubah_rupiah(Long.parseLong(cls.harga)));
        BarangViewHolder.deskripsi.setText(cls.deskripsi);

        Picasso.with(v.getContext())
                .load(cls.url)
                .fit()
                .centerCrop()
                .into(BarangViewHolder.image);
        BarangViewHolder.judul.setOnClickListener(this);
        final String id_iklan=cls.id_iklan;
        final int position=i;
        BarangViewHolder.btn_laku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah_ke_laku(v.getContext(),class_bantuan.base_url+"ubahaktifkelaku",class_bantuan.api_key,id_iklan,position);
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
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.detail_judul){
            Intent intent = new Intent(v.getContext(),activity_detail.class);
            v.getContext().startActivity(intent);
        }else if(view.getId()==R.id.ic_close){

        }
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
                    if (respon.equals("sukses")) {
                        mDialog.dismiss();
                        cls_barang.remove(position);
                        notifyDataSetChanged();
                        dialog_sukses(ctx, "Upgrade iklan sukses");
                    }
                } catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_sukses(ctx, "Kesalahan :" + ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_sukses(ctx, "Gagagl koneksi");
            }
        });
    }
}
