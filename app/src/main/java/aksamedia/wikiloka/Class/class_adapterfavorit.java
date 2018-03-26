package aksamedia.wikiloka.Class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.github.mrengineer13.snackbar.SnackBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import aksamedia.wikiloka.Activity.activity_detail;
import aksamedia.wikiloka.Gajelas.LabelImageView;
import aksamedia.wikiloka.R;

public class class_adapterfavorit extends RecyclerView.Adapter<class_adapterfavorit.BarangViewHolder>{
    static List<class_favorit> cls_barang;
    ImageView gifiklan;
    private View v;
    private class_favorit cls;
    //static List<class_favorit> cls_barang1;
    static Context ctx;
    static boolean init_favorit=false;

    public class_adapterfavorit(List<class_favorit> class_barang){
        this.cls_barang = class_barang;
        //this.cls_barang1=class_barang;
    }
    public class BarangViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, MaterialFavoriteButton.OnFavoriteChangeListener {
        CardView cv;
        TextView judul;
        TextView harga;
        TextView deskripsi;
        TextView seo_iklan;
        LabelImageView image;
        MaterialFavoriteButton btn_favorit;
        private String respon_server;
        private SnackBar snackBar;

        BarangViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            judul = (TextView)itemView.findViewById(R.id.detail_judul);
            harga = (TextView)itemView.findViewById(R.id.detail_harga);
            deskripsi = (TextView)itemView.findViewById(R.id.detail_desk);
            seo_iklan = (TextView) itemView.findViewById(R.id.seo_iklan);
            image = (LabelImageView)itemView.findViewById(R.id.detail_foto);
            btn_favorit=(MaterialFavoriteButton) itemView.findViewById(R.id.btn_favorit);
            image.setOnClickListener(this);
            //btn_favorit.setOnFavoriteAnimationEndListener(this);
            btn_favorit.setOnFavoriteChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == image.getId()){
                Intent intent = new Intent(v.getContext(),activity_detail.class);
                intent.putExtra("seo_iklan", seo_iklan.getText().toString());
                v.getContext().startActivity(intent);
            }
        }

        /*@Override
        public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
            class_favorit cls= class_adapterfavorit.cls_barang1.get(getAdapterPosition());
            if(favorite==true){
                proses_favorit("http://192.168.0.34/wikiloka/api/tambahfavorit", class_bantuan.api_key, cls.id_iklan, new class_prosessql(ctx).baca_profil()[1],"true");
            }
            else if(favorite==false){
                proses_favorit("http://192.168.0.34/wikiloka/api/hapusfavorit", class_bantuan.api_key,cls.id_iklan, new class_prosessql(ctx).baca_profil()[1],"false");
            }
        }*/

        /*private void proses_favorit(String url, String api_key, final String id_iklan, final String username,final String favorit) {
            respon_server = "";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("key", api_key);
            params.put("username", username);
            params.put("id_iklan", id_iklan);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    respon_server = new String(responseBody);
                    if (respon_server.equals("failed") || respon_server.isEmpty()) {
                        dialog_gagal("Gagal tambah favorit");
                    }
                    else if(respon_server.equals("Success")){
                        if(favorit.equals("true")){
                            new class_prosessql(ctx).insert_favorit(id_iklan,username);
                        }
                        else{
                            new class_prosessql(ctx).hapus_favorit(id_iklan, username);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    dialog_gagal("Gagal akses ke server");
                }
            });
        }*/
        private void proses_favorit(String url, String api_key, final String id_iklan, final String username,final String favorit) {
            respon_server = "";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("key", api_key);
            params.put("username", username);
            params.put("id_iklan", id_iklan);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    respon_server = new String(responseBody);
                    if (respon_server.equals("failed") || respon_server.isEmpty()) {
                        if(favorit.equals("true")) {
                            dialog_gagal("Gagal tambah favorit");
                        }
                        else{
                            dialog_gagal("Gagal menghapus favorit");
                        }
                    }
                    else if(respon_server.equals("Success")){
                        if(favorit.equals("true")){
                            if(new class_prosessql(ctx).cek_favorit(id_iklan,new class_prosessql(ctx).baca_profil()[1]).equals("false")) {
                                new class_prosessql(ctx).insert_favorit(id_iklan,username);
                                dialog_gagal("Iklan telah di tambahkan ke favorit");
                            }
                        }
                        else{
                            if(new class_prosessql(ctx).cek_favorit(id_iklan,new class_prosessql(ctx).baca_profil()[1]).equals("true")) {
                                new class_prosessql(ctx).hapus_favorit(id_iklan, username);
                                cls_barang.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                dialog_gagal("Iklan dihapus dari daftar favorit");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    dialog_gagal("Gagal akses ke server");
                }
            });
        }
        public void dialog_gagal(String pesan){
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

        @Override
        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
            if(new class_prosessql(ctx).cek_session() == "ada"){
                class_favorit cls=cls_barang.get(getAdapterPosition());
                if(favorite==true){
                    if(init_favorit == false) {
                        proses_favorit(class_bantuan.base_url + "tambahfavorit", class_bantuan.api_key, cls.id_iklan, new class_prosessql(ctx).baca_profil()[1], "true");
                    }
                    init_favorit = false;
                }
                else if(favorite==false){
                    proses_favorit(class_bantuan.base_url+"hapusfavorit", class_bantuan.api_key,cls.id_iklan, new class_prosessql(ctx).baca_profil()[1],"false");
                }
            }
            else if(favorite==true){
                buttonView.setFavorite(false);
                dialog_gagal("Silahkan login dulu untuk menambahkan favorit");
            }
        }
    }
    @Override
    public int getItemCount() {
        return cls_barang.size();
    }
    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_favorit, viewGroup, false);
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
        BarangViewHolder.seo_iklan.setText(cls.seo_iklan);
        BarangViewHolder.judul.setText(cls.judul);
        BarangViewHolder.harga.setText(ubah_rupiah(Long.parseLong(cls.harga)).substring(0, ubah_rupiah(Long.parseLong(cls.harga)).length() - 3));
        BarangViewHolder.deskripsi.setText(cls.deskripsi);
        /*if(new class_prosessql(ctx).cek_favorit(cls.id_iklan,new class_prosessql(ctx).baca_profil()[1]).equals("true")){
            BarangViewHolder.btn_favorit.setFavorite(true);
        }else{
            BarangViewHolder.btn_favorit.setFavorite(false);
        }*/
        if(new class_prosessql(ctx).cek_favorit(cls.id_iklan,new class_prosessql(ctx).baca_profil()[1]).equals("true")){
            init_favorit = true;
            BarangViewHolder.btn_favorit.setFavorite(true);
        }else{
            BarangViewHolder.btn_favorit.setFavorite(false);
        }

        Picasso.with(v.getContext())
                .load(cls.url)
                .fit()
                .centerCrop()
                .into(BarangViewHolder.image);
        if(cls.label.equals(""))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.TRANSPARENT);
            BarangViewHolder.image.setLabelText("");

        }
        else if(cls.label.equals("iklan"))
        {
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.ly1);
            linearLayout.removeAllViews();
            gifiklan=new ImageView(v.getContext());
            gifiklan.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifiklan.setBackgroundColor(Color.BLACK);
            gifiklan.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            gifiklan.getLayoutParams().height=150;

            Picasso.with(v.getContext())
                    .load("http://www.ul.ie/artsoc/sites/default/files/images/ethics-banner.jpg")
                    .into(gifiklan);
            linearLayout.addView(gifiklan);

        }
        else if(cls.label.equals("TOP PROMO"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#F44336"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("TOP PROMO");
        }
        else if(cls.label.equals("TOP SHOP"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#E91E63"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("TOP SHOP");

        }
        else if(cls.label.equals("TERLARIS"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#E91E63"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("TERLARIS");

        }
        else if(cls.label.equals("TOP GROSIR"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#2196F3"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("TOP GROSIR");

        }
        else if(cls.label.equals("TOP SELLER"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#673AB7"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("TOP SELLER");

        }
        else if(cls.label.equals("TERBARU"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#039BE5"));
            BarangViewHolder.image.setLabelText("TERBARU");

        }
        else if(cls.label.equals("REKOMENDED"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#FF5722"));
            BarangViewHolder.image.setLabelDistance(65);
            BarangViewHolder.image.setLabelText("REKOMENDED");

        }
        else if(cls.label.equals("FAVORIT"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#795548"));
            BarangViewHolder.image.setLabelDistance(50);
            BarangViewHolder.image.setLabelText("FAVORIT");

        }
        else if(cls.label.equals("TOP 25"))
        {
            BarangViewHolder.image.setLabelBackgroundColor(Color.parseColor("#448AFF"));
            BarangViewHolder.image.setLabelDistance(50);
            BarangViewHolder.image.setLabelText("TOP 25");

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
