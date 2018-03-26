package aksamedia.wikiloka.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_swipeimage;
import aksamedia.wikiloka.Gajelas.tv_euphemia;
import aksamedia.wikiloka.Gajelas.tv_moserrat;
import butterknife.ButterKnife;

public class activity_detail_listiklan extends AppCompatActivity implements View.OnClickListener, BottomNavigationBar.OnTabSelectedListener {
    ViewPager viewPager;
    Dialog rankDialog;
    class_swipeimage adapter_swipe;
    String[] url_foto;
    String telp="", bbm="";
    private String respon_server;
    private SnackBar snackBar;
    tv_moserrat tv_harga;
    tv_euphemia txt_like,txt_unlike,tv_judul,tv_kota,tv_deskripsi,tv_nama,tv_provinsi_member,tv_kota_member,tv_jmlgambar,tv_dilihat;
    TextView tv_laporkan;
    RelativeLayout btn_like,btn_unlike;
    private AlertDialog alertDialogObject;
    private SimpleArcDialog mDialog;
    private BottomNavigationBar bottomNavigationBar;
    private TextView id_iklan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_listiklan);
        inisialisasi();
        //ambil data dan upsate data lihat
        //ambil_data(class_bantuan.base_url, class_bantuan.api_key, getIntent().getStringExtra("seo_iklan"));
    }

    void inisialisasi(){
        viewPager=(ViewPager) findViewById(R.id.view_pager);
        ButterKnife.bind(this);
        tv_harga=(tv_moserrat)findViewById(R.id.tv_harga);
        tv_judul=(tv_euphemia) findViewById(R.id.tv_judul);
        tv_kota=(tv_euphemia) findViewById(R.id.tv_kota);
        tv_deskripsi=(tv_euphemia) findViewById(R.id.tv_deskripsi);
        tv_nama=(tv_euphemia) findViewById(R.id.tv_nama);
        tv_provinsi_member=(tv_euphemia) findViewById(R.id.tv_provinsi_member);
        tv_kota_member=(tv_euphemia) findViewById(R.id.tv_kota_member);
        tv_jmlgambar=(tv_euphemia) findViewById(R.id.tv_jmlgambar);
        tv_laporkan=(TextView) findViewById(R.id.tv_laporkan);
        btn_like=(RelativeLayout) findViewById(R.id.like_layout);
        btn_unlike=(RelativeLayout) findViewById(R.id.unlike_layout);
        txt_like=(tv_euphemia) findViewById(R.id.txt_like);
        txt_unlike=(tv_euphemia) findViewById(R.id.txt_unlike);
        tv_dilihat=(tv_euphemia) findViewById(R.id.tv_lihat);
        id_iklan=(TextView) findViewById(R.id.id_iklan);
        tv_laporkan.setOnClickListener(this);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_phone, "Telp"))
                .addItem(new BottomNavigationItem(R.drawable.ic_pesan, "Pesan"))
                .addItem(new BottomNavigationItem(R.drawable.ic_obrolan, "WA"))
                .addItem(new BottomNavigationItem(R.drawable.ic_rating, "Rating"))
                .addItem(new BottomNavigationItem(R.drawable.ic_share, "Share"))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        btn_like.setOnClickListener(this);
        btn_unlike.setOnClickListener(this);
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
    void ambil_data(String url,String api_key, final String seo_iklan){
        respon_server = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("seo_iklan", seo_iklan);
        client.post(url + "detail", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    respon_server = new String(responseBody);
                    if (respon_server.equals("failed") || respon_server.isEmpty()) {
                        dialog_gagal("Gagal ambil data detail");
                    } else {
                        JSONArray jarr = new JSONArray(respon_server);
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            tv_harga.setText(ubah_rupiah(Long.parseLong(jobj.getString("harga_iklan"))).substring(0, ubah_rupiah(Long.parseLong(jobj.getString("harga_iklan"))).length() - 3));
                            tv_judul.setText(jobj.getString("judul_iklan"));
                            tv_deskripsi.setText(Html.fromHtml(jobj.getString("deskripsi_iklan")));
                            telp = jobj.getString("telepon_member");
                            bbm = jobj.getString("bbm_member");
                            tv_nama.setText(jobj.getString("nama_member"));
                            txt_like.setText(jobj.getString("like"));
                            txt_unlike.setText(jobj.getString("unlike"));
                            tv_dilihat.setText(jobj.getString("dilihat") + " Kali");
                            id_iklan.setText(jobj.getString("id_iklan"));
                            if (Integer.parseInt(jobj.getString("kota")) == 0) {
                                tv_kota.setText("");
                            } else {
                                baca_txt_kota("iklan", class_bantuan.base_url, class_bantuan.api_key, jobj.getString("kota"));
                            }

                            if (Integer.parseInt(jobj.getString("kota_member")) == 0) {
                                tv_kota_member.setText("");
                            } else {
                                baca_txt_kota("member", class_bantuan.base_url, class_bantuan.api_key, jobj.getString("kota"));
                            }

                            if (Integer.parseInt(jobj.getString("provinsi_member")) == 0) {
                                tv_provinsi_member.setText("");
                            } else {
                                baca_txt_provinsi(class_bantuan.base_url, class_bantuan.api_key, jobj.getString("provinsi_member"));
                            }
                            baca_detail_photo(class_bantuan.base_url, class_bantuan.api_key, seo_iklan);
                        }
                    }

                } catch (JSONException ex) {
                    dialog_gagal("Gagal parsing data detail.");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                dialog_gagal("Gagal mengakses data detail.");
            }
        });
    }
    void baca_detail_photo(final String url, String token, String seo_iklan) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("seo_iklan", seo_iklan);
        client.post(url + "detail_foto", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
               // tv_provinsi_member.setText(new String(responseBody));
                try {
                    respon_server = new String(responseBody);
                    if (respon_server.equals("failed") || respon_server.isEmpty()) {
                        dialog_gagal("Gagal ambil data detail");
                    } else {
                        JSONArray jarr = new JSONArray(respon_server);
                        url_foto=new String[jarr.length()];
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            url_foto[i]=jobj.getString("photo");
                        }
                        tv_jmlgambar.setText(String.valueOf(jarr.length())+ " Gambar");
                        adapter_swipe=new class_swipeimage(activity_detail_listiklan.this,url_foto);
                        viewPager.setAdapter(adapter_swipe);
                    }
                }catch (JSONException ex){
                    tv_jmlgambar.setText("0 Gambar");
                    dialog_gagal("Gagal parsing data detail foto.");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                tv_jmlgambar.setText("0 Gambar");
                dialog_gagal("Gagal mengakses data detail foto.");
            }
        });
    }
    void baca_txt_provinsi(String url, String token, String id_prov) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("id_prov", id_prov);
        client.post(url + "txt_prov", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                tv_provinsi_member.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                tv_provinsi_member.setText("");
            }
        });
    }
    void baca_txt_kota(final String status,String url, String token, String id_kota) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("id_kota", id_kota);
        client.post(url + "txt_kota", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if(status.equals("iklan")) {
                    tv_kota.setText(new String(responseBody));
                }else if(status.equals("member")) {
                    tv_kota_member.setText(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                if(status.equals("iklan")) {
                    tv_kota.setText("");
                }else if(status.equals("member")) {
                    tv_kota_member.setText("");
                }
            }
        });
    }
    void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(activity_detail_listiklan.this)
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

      /*
                Intent intent=new Intent(activity_detail.this,activity_listitem.class);
                if(class_bantuan.menu_kategori!=""){
                    intent.putExtra("kategori",class_bantuan.menu_kategori);
                }else if(class_bantuan.menu_kategori==""){
                    intent.putExtra("word",class_bantuan.search_word);
                    intent.putExtra("kota",class_bantuan.search_kota);
                    intent.putExtra("seo_kategori",class_bantuan.search_seo_kategori);
                }
                startActivity(intent);
     */
    @Override
    public void onBackPressed() {
        finish();
        Intent intent=new Intent(activity_detail_listiklan.this,activity_listitem.class);
        if(class_bantuan.menu_kategori!=""){
            intent.putExtra("kategori",class_bantuan.menu_kategori);
        }else if(class_bantuan.menu_kategori==""){
            intent.putExtra("word",class_bantuan.search_word);
            intent.putExtra("kota",class_bantuan.search_kota);
            intent.putExtra("seo_kategori",class_bantuan.search_seo_kategori);
        }
        startActivity(intent);
    }
    void laporkan_pesan(String url,String api_key,String nama,String email,String pesan){
        mDialog = new SimpleArcDialog(activity_detail_listiklan.this);
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("nama", nama);
        params.put("email", email);
        params.put("pesan", pesan);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(activity_detail_listiklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                mDialog.dismiss();
                if (respon_server.equals("Success")) {
                    new AlertDialog.Builder(activity_detail_listiklan.this)
                            .setTitle("Sukses")
                            .setMessage("Terima kasih atas laporan anda")
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    new AlertDialog.Builder(activity_detail_listiklan.this)
                            .setTitle("Gagal")
                            .setMessage("Terjadi kesalahan, silahkan coba kembali")
                            .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                new AlertDialog.Builder(activity_detail_listiklan.this)
                        .setTitle("Gagal")
                        .setMessage("Gagal menyambung, silahkan coba kembali")
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    void proses_like(String url,String api_key,String id_iklan){
        mDialog = new SimpleArcDialog(activity_detail_listiklan.this);
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_iklan",id_iklan);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url+"like", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(activity_detail_listiklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                mDialog.dismiss();
                txt_like.setText(respon_server+" Kali");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                new AlertDialog.Builder(activity_detail_listiklan.this)
                        .setTitle("Gagal")
                        .setMessage("Gagal menyambung, silahkan coba kembali")
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                txt_like.setText("0 Kali");
            }
        });
    }
    void proses_unlike(String url,String api_key,String id_iklan){
        mDialog = new SimpleArcDialog(activity_detail_listiklan.this);
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_iklan",id_iklan);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url+"unlike", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(activity_detail_listiklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                mDialog.dismiss();
                txt_unlike.setText(respon_server+" Kali");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                new AlertDialog.Builder(activity_detail_listiklan.this)
                        .setTitle("Gagal")
                        .setMessage("Gagal menyambung, silahkan coba kembali")
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                txt_unlike.setText("0 Kali");
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_laporkan){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog_laporkan, null);
            Button btn_kirim = (Button) dialogView.findViewById(R.id.btn_kirim);
            final EditText edt_nama=(EditText) dialogView.findViewById(R.id.edt_nama);
            final EditText edt_email=(EditText) dialogView.findViewById(R.id.edt_email);
            final EditText edt_pesan=(EditText) dialogView.findViewById(R.id.edt_pesan);
            btn_kirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_nama.getText().toString().equals("")) {
                        Toast.makeText(activity_detail_listiklan.this, "Masukkan nama anda", Toast.LENGTH_SHORT).show();
                        edt_nama.requestFocus();
                    } else if (edt_email.getText().toString().equals("")) {
                        Toast.makeText(activity_detail_listiklan.this, "Masukkan email anda", Toast.LENGTH_SHORT).show();
                        edt_email.requestFocus();
                    } else if (edt_pesan.getText().toString().equals("")) {
                        Toast.makeText(activity_detail_listiklan.this, "Masukkan pesan anda", Toast.LENGTH_SHORT).show();
                        edt_pesan.requestFocus();
                    } else {
                        laporkan_pesan(class_bantuan.base_url+"laporkan",
                                class_bantuan.api_key,
                                edt_nama.getText().toString(),
                                edt_email.getText().toString(),
                                edt_pesan.getText().toString());
                    }

                }
            });
            dialog.setView(dialogView);
            alertDialogObject = dialog.create();
            alertDialogObject.show();
        }else if(v.getId()==R.id.like_layout){
            proses_like(class_bantuan.base_url,class_bantuan.api_key,id_iklan.getText().toString());
        }else if(v.getId()==R.id.unlike_layout){
            proses_unlike(class_bantuan.base_url,class_bantuan.api_key,id_iklan.getText().toString());
        }
    }
    private void setClipboard(Context context,String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    @Override
    public void onTabSelected(int position) {
        if(position==0){
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(telp)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            } catch (Exception ex) {
                Toast.makeText(activity_detail_listiklan.this, "Error: Proses Panggilan", Toast.LENGTH_SHORT).show();
            }
        }
        if(position==2){
            setClipboard(activity_detail_listiklan.this,telp);
            Toast.makeText(activity_detail_listiklan.this, "Nomor telp sudah sudah di copy", Toast.LENGTH_LONG).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            startActivity(launchIntent);
        }
        else if(position==3){
            rankDialog = new Dialog(activity_detail_listiklan.this, R.style.FullHeightDialog);
            rankDialog.setContentView(R.layout.custom_rankdialog);
            rankDialog.setCancelable(true);
            TextView updateButton = (TextView) rankDialog.findViewById(R.id.rank_dialog_simpan);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rankDialog.dismiss();
                }
            });
            TextView batalButton=(TextView) rankDialog.findViewById(R.id.rank_dialog_batal);
            batalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rankDialog.dismiss();
                }
            });
            rankDialog.show();
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
