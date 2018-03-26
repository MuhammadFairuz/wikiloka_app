package aksamedia.wikiloka.Activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.Class.class_provinsi;
import aksamedia.wikiloka.Gajelas.tv_ubuntu;
import cz.msebera.android.httpclient.Header;

public class activity_gantiprofil extends AppCompatActivity implements View.OnClickListener{
    ImageView back;
    EditText[]edt=new EditText[8];
    EditText txt_prov,txt_kota;
    CheckBox cb;
    TextView simpan;
    private SimpleArcDialog mDialog;
    private String respon_server;
    private SnackBar snackBar;
    private AlertDialog alertDialogObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantiprofil);
        inisialisasi();
        setuptoolbar();
        baca_online_profil(class_bantuan.base_url, class_bantuan.api_key, new class_prosessql(activity_gantiprofil.this).baca_member()[0]);
        simpan.setOnClickListener(this);
        txt_prov.setOnClickListener(this);
        txt_kota.setOnClickListener(this);
    }
    public void setuptoolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GANTI PROFIL");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void update_profil(String url, String api_key, String[] form_data) {
        RequestParams params = new RequestParams();

        params.put("key", api_key);
        params.put("provinsi_member", form_data[0]);
        //Log.e("prov ubah id", form_data[0]);
        params.put("kota_member", form_data[1]);
        //Log.e("kota ubah id", form_data[1]);
        params.put("nama", form_data[2]);
        params.put("pinbb", form_data[3]);
        params.put("telepon", form_data[4]);
        params.put("telepon_member2", form_data[5]);
        params.put("id_line", form_data[6]);
        params.put("alamat", form_data[7]);
        params.put("username", new class_prosessql(activity_gantiprofil.this).baca_profil()[1]);


        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_gantiprofil.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_gantiprofil.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respon_server = new String(responseBody);
                if (respon_server.equals("failed")) {
                    mDialog.dismiss();
                    snackBar = new SnackBar.Builder(activity_gantiprofil.this)
                            .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                @Override
                                public void onMessageClick(Parcelable token) {
                                    snackBar.clear();
                                }
                            })
                            .withMessage(respon_server)
                            .withActionMessage("Tutup")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.LONG_SNACK)
                            .show();
                } else if (respon_server.equals("Success")) {
                   /* new class_prosessql(activity_gantiprofil.this).insert_profil(
                            new class_prosessql(activity_gantiprofil.this).baca_profil()[1],
                            edt[0].getText().toString(),
                            edt[1].getText().toString(),
                            edt[2].getText().toString(),
                            edt[3].getText().toString(),
                            edt[4].getText().toString(),
                            "");*/
                    mDialog.dismiss();
                    snackBar = new SnackBar.Builder(activity_gantiprofil.this)
                            .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                @Override
                                public void onMessageClick(Parcelable token) {
                                    snackBar.clear();
                                }
                            })
                            .withMessage("Proses ganti profil sukses")
                            .withActionMessage("Tutup")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.PERMANENT_SNACK)
                            .show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Return Failed", String.valueOf(statusCode));
            }
        });
    }
    private void inisialisasi(){
        back=(ImageView) findViewById(R.id.tombol_kembali);
        edt[0]=(EditText) findViewById(R.id.editText3);
        edt[1] = (EditText) findViewById(R.id.editText7);
        edt[2] = (EditText) findViewById(R.id.editText4);
        edt[3] = (EditText) findViewById(R.id.editText5);
        edt[4] = (EditText) findViewById(R.id.editText6);
        edt[5] = (EditText) findViewById(R.id.editText8);
        edt[6] = (EditText) findViewById(R.id.editText9);
        edt[7] = (EditText) findViewById(R.id.editText10);
        simpan = (TextView) findViewById(R.id.textView8);
        txt_prov=(EditText) findViewById(R.id.txt_provinsi);
        txt_kota=(EditText) findViewById(R.id.txt_kota);
    }

    private void baca_txt_provinsi(String url, String token, String id_prov) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("id_prov", id_prov);
        client.post(url + "txt_prov", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                txt_prov.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                txt_prov.setText("");
            }
        });
    }
    private void baca_txt_kota(String url, String token, String id_kota) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("id_kota", id_kota);
        client.post(url + "txt_kota", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                txt_kota.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                txt_kota.setText("");
            }
        });
    }
    private void baca_online_profil(String url, String token, String username) {
        respon_server = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", token);
        params.put("username", username);
        client.post(url + "get_profil", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    respon_server = new String(responseBody);
                    if (respon_server.equals("failed") || respon_server.isEmpty()) {
                        dialog_gagal("Gagal ambil data profil");
                    } else {
                        JSONArray jarr = new JSONArray(respon_server);
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            edt[0].setText(jobj.getString("provinsi_member"));
                            if(Integer.parseInt(jobj.getString("provinsi_member"))==0){
                                txt_prov.setText("");
                            } else {
                                baca_txt_provinsi(class_bantuan.base_url, class_bantuan.api_key, jobj.getString("provinsi_member"));
                            }

                            edt[1].setText(jobj.getString("kota_member"));
                            if(Integer.parseInt(jobj.getString("kota_member"))==0){
                                txt_kota.setText("");
                            }else {
                                baca_txt_kota(class_bantuan.base_url, class_bantuan.api_key,jobj.getString("kota_member"));
                            }
                            edt[2].setText(jobj.getString("nama_member"));
                            edt[3].setText(jobj.getString("bbm_member"));
                            edt[4].setText(jobj.getString("telepon_member"));
                            edt[5].setText(jobj.getString("telepon_member2"));
                            edt[6].setText(jobj.getString("id_line"));
                            edt[7].setText(jobj.getString("alamat"));
                           /* new class_prosessql(activity_gantiprofil.this).insert_profil(
                                    jobj.getString("username"),
                                    jobj.getString("provinsi_member"),
                                    jobj.getString("kota_member"),
                                    jobj.getString("nama_member"),
                                    jobj.getString("telepon_member"),
                                    jobj.getString("bbm_member"),
                                    "");*/
                            //cb.setChecked(jobj.getBoolean("wa_member"));
                        }
                    }

                } catch (JSONException ex) {
                    dialog_gagal("Silahkan login terlebih dahulu");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                dialog_gagal("Gagal mengakses data profil");
            }
        });
    }
    public void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(activity_gantiprofil.this)
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
    public void onClick(View v) {
        if(v.getId()==R.id.textView8){
            String[] formdata = new String[8];
            formdata[0] = edt[0].getText().toString();
            formdata[1] = edt[1].getText().toString();
            formdata[2] = edt[2].getText().toString();
            formdata[3] = edt[3].getText().toString();
            formdata[4] = edt[4].getText().toString();
            formdata[5] = edt[5].getText().toString();
            formdata[6] = edt[6].getText().toString();
            formdata[7] = edt[7].getText().toString();
            update_profil(class_bantuan.base_url + "updateprofil", class_bantuan.api_key, formdata);
        }
        else if(v.getId()==R.id.txt_provinsi){
            provinsi(class_bantuan.base_url+"provinsi", class_bantuan.api_key);
        }
        else if(v.getId()==R.id.txt_kota){
            kota(class_bantuan.base_url+"kota", class_bantuan.api_key,edt[0].getText().toString());
        }
    }
    void provinsi(String url, String api_key) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_gantiprofil.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_gantiprofil.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String[] id_provinsi = new String[jarr.length()];
                    String[] nama_provinsi = new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        id_provinsi[i] = jobj.getString("id_provinsi");
                        nama_provinsi[i] = jobj.getString("nama_provinsi");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_gantiprofil.this);
                    LayoutInflater inflater = activity_gantiprofil.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl = (TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR PROVINSI");
                    list_kategori.setAdapter(new class_provinsi(activity_gantiprofil.this, id_provinsi, nama_provinsi));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            tv_ubuntu text_list=(tv_ubuntu) view.findViewById(R.id.txtlist);
                            alertDialogObject.dismiss();
                            edt[0].setText(text_id.getText().toString());
                            txt_prov.setText(text_list.getText().toString());
                            //kota(class_bantuan.base_url + "kota", class_bantuan.api_key, text_id.getText().toString());
                        }
                    });
                    dialog.setView(dialogView);
                    alertDialogObject = dialog.create();
                    alertDialogObject.show();
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    void kota(String url, String api_key, String id_prov){
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_prov",id_prov);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_gantiprofil.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_gantiprofil.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    final String[] id_kota = new String[jarr.length()];
                    String[] nama_area = new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        id_kota[i] = jobj.getString("id_kota");
                        nama_area[i] = jobj.getString("nama_area");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_gantiprofil.this);
                    LayoutInflater inflater = activity_gantiprofil.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl=(TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR KOTA");
                    list_kategori.setAdapter(new class_provinsi(activity_gantiprofil.this, id_kota, nama_area));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            tv_ubuntu text_list = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            edt[1].setText(text_id.getText().toString());
                            txt_kota.setText(text_list.getText().toString());
                            alertDialogObject.dismiss();
                        }
                    });
                    dialog.setView(dialogView);
                    alertDialogObject = dialog.create();
                    alertDialogObject.show();
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
