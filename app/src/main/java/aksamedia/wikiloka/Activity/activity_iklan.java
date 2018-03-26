package aksamedia.wikiloka.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.Class.class_kategori;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.Class.class_provinsi;
import aksamedia.wikiloka.Class.class_sub_kategori;
import aksamedia.wikiloka.Gajelas.tv_ubuntu;
import cz.msebera.android.httpclient.Header;

public class activity_iklan extends AppCompatActivity {
    ImageView img1, img2, img3, img4, img5, img_takepicture;
    int[] arr_image;
    class_helpersqlite db_helper;
    SQLiteDatabase db;
    TextView simpan;
    EditText judul_iklan,deskripsi_iklan,harga_iklan,web_iklan;
    TextView owner,telpon,bbm,username,telpon2,line_id,email;
    CheckBox cb_email,cb_telpon1,cb_telpon2,cb_line,cb_bbm;
    int REQUEST_CAMERA=1;
    int SELECT_FILE=2,flag;
    String [] url_image;
    private SimpleArcDialog mDialog;
    private SnackBar snackBar;
    EditText kategori,kota,edt_id_kota,edt_id_kat,jns_brg,id_jenis;
    boolean isTwise = false ;
    boolean isEdit = true ;
    private AlertDialog alertDialogObject;
    private String respon_server="";
    private RelativeLayout layout_form;
    private View child;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan);
        inisialisasi();
        img_takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity_iklan.this, String.valueOf(url_image.length), Toast.LENGTH_LONG).show();
                try {
                    String[] formdata = new String[16];
                    formdata[0] = judul_iklan.getText().toString();
                    formdata[1] = deskripsi_iklan.getText().toString();
                    formdata[2] = harga_iklan.getText().toString();
                    formdata[3] = web_iklan.getText().toString();
                    formdata[4] = owner.getText().toString();
                    formdata[5] = username.getText().toString();
                    formdata[6] = edt_id_kota.getText().toString();
                    formdata[10] = id_jenis.getText().toString();
                    String [] kategori_arr=edt_id_kat.getText().toString().split(",");
                    for (int i=0;i<kategori_arr.length;i++){
                        if(i==0){
                            formdata[7]=kategori_arr[i];
                        }
                        else if(i==1){
                            formdata[8]=kategori_arr[i];
                        }
                        else if(i==2){
                            formdata[9]=kategori_arr[i];
                        }
                    }
                    formdata[11]=(cb_email.isChecked())?"1":null;
                    formdata[12]=(cb_telpon1.isChecked())?"1":null;
                    formdata[13]=(cb_telpon2.isChecked())?"1":null;
                    formdata[14]=(cb_line.isChecked())?"1":null;
                    formdata[15]=(cb_bbm.isChecked())?"1":null;
                    upload(class_bantuan.base_url+"pasangiklan", class_bantuan.api_key, url_image, formdata);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        onclick_image();

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori_utama(class_bantuan.base_url + "kategori_utama", class_bantuan.api_key);
            }
        });
        kota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provinsi(class_bantuan.base_url + "provinsi", class_bantuan.api_key);
            }
        });
        jns_brg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_dialog_jns();
            }
        });
    }

    void provinsi(String url, String api_key) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_iklan.this);
                    LayoutInflater inflater = activity_iklan.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl = (TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR PROVINSI");
                    list_kategori.setAdapter(new class_provinsi(activity_iklan.this, id_provinsi, nama_provinsi));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            alertDialogObject.dismiss();
                            kota(class_bantuan.base_url + "kota", class_bantuan.api_key, text_id.getText().toString());
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
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_iklan.this);
                    LayoutInflater inflater = activity_iklan.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl=(TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR KOTA");
                    list_kategori.setAdapter(new class_provinsi(activity_iklan.this, id_kota, nama_area));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            tv_ubuntu text_list = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            edt_id_kota.setText(text_id.getText().toString());
                            kota.setText(text_list.getText().toString());
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
    void sub_kategori_2(String url,String key,String id_kategori){
        RequestParams params = new RequestParams();
        params.put("key", key);
        params.put("id_kategori", id_kategori);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String[] item_kategori = new String[jarr.length()];
                    String[] id_kategori = new String[jarr.length()];
                    String[] status=new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        item_kategori[i] = jobj.getString("sub2_kategori");
                        id_kategori[i] = jobj.getString("id_sub2_kategori");
                        status[i]=jobj.getString("ada");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_iklan.this);
                    LayoutInflater inflater = activity_iklan.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    list_kategori.setAdapter(new class_sub_kategori(activity_iklan.this, id_kategori, item_kategori,status));

                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            tv_ubuntu text_id=(tv_ubuntu) view.findViewById(R.id.txtid);
                            kategori.setText(kategori.getText().toString() + ">>" + text.getText().toString());
                            edt_id_kat.setText(edt_id_kat.getText().toString() + text_id.getText().toString() + ",");

                            alertDialogObject.dismiss();
                            String [] kategori_arr=edt_id_kat.getText().toString().split(",");
                            layout_form.removeAllViews();
                            if(kategori_arr[2]!=null){
                                if(kategori_arr[1].equals("8")) {
                                    Log.e("sub2_kategori",String.valueOf(kategori_arr[2]));
                                    form_mobil(kategori_arr[2]);
                                }
                            }

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
                mDialog.dismiss();
            }

        });
    }
    //inisialisasi form layout
    void form_mobil(String id_merk){
        child = getLayoutInflater().inflate(R.layout.form_mobil, null);

        String [] transmisi={"Pilih Transmisi","Manual","Automatic","Triptonic"};
        ((Spinner) child.findViewById(R.id.form_mobil_transmisi)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transmisi));
        Calendar now = Calendar.getInstance();
        int jml_tahun=(now.get(Calendar.YEAR)-1990)+3;
        String [] tahun=new String[jml_tahun];
        tahun[0]="Pilih Tahun";
        int j=1;
        for(int i=now.get(Calendar.YEAR);i>=1990;i--){
            tahun[j]=String.valueOf(i);
            j++;
        }
        tahun[j]="< 1990";
        ((Spinner) child.findViewById(R.id.form_mobil_tahun)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tahun));

        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("id_merk", id_merk);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"get_merk", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("Respon Server",respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    List<StringDgID> list = new ArrayList<StringDgID>();
                    list.add(new StringDgID("Tipe Kendaraan", "0"));
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        list.add(new StringDgID(jobj.getString("tipe"), jobj.getString("id_tipe_mobil")));
                    }
                    ArrayAdapter<StringDgID> adapter = new ArrayAdapter<StringDgID> (activity_iklan.this, android.R.layout.simple_spinner_item, list);
                    ((Spinner) child.findViewById(R.id.form_mobil_tipe)).setAdapter(adapter);
                } catch (JSONException ex) {
                    List<StringDgID> list = new ArrayList<StringDgID>();
                    list.add(new StringDgID("Tipe Kendaraan", "0"));
                    ArrayAdapter<StringDgID> adapter = new ArrayAdapter<StringDgID> (activity_iklan.this, android.R.layout.simple_spinner_item, list);
                    ((Spinner) child.findViewById(R.id.form_mobil_tipe)).setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                List<StringDgID> list = new ArrayList<StringDgID>();
                list.add(new StringDgID("Tipe Kendaraan", "0"));
                ArrayAdapter<StringDgID> adapter = new ArrayAdapter<StringDgID> (activity_iklan.this, android.R.layout.simple_spinner_item, list);
                ((Spinner) child.findViewById(R.id.form_mobil_tipe)).setAdapter(adapter);
            }
        });
        layout_form.addView(child);
    }
    void sub_kategori_1(String url,String key,String id_kategori){
        RequestParams params = new RequestParams();
        params.put("key", key);
        params.put("id_kategori", id_kategori);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String[] item_kategori = new String[jarr.length()];
                    String[] id_kategori=new String[jarr.length()];
                    String[] status=new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        item_kategori[i] = jobj.getString("sub1_kategori");
                        id_kategori[i]=jobj.getString("id_sub1_kategori");
                        status[i]=jobj.getString("ada");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_iklan.this);
                    LayoutInflater inflater = activity_iklan.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    list_kategori.setAdapter(new class_sub_kategori(activity_iklan.this, id_kategori, item_kategori, status));

                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            kategori.setText(kategori.getText().toString() + ">>" + text.getText().toString());
                            edt_id_kat.setText(edt_id_kat.getText().toString() + text_id.getText().toString() + ",");
                            //set_tag();
                            alertDialogObject.dismiss();
                            sub_kategori_2(class_bantuan.base_url + "sub_kategori_dua", class_bantuan.api_key, text_id.getText().toString());

                        }
                    });
                    dialog.setView(dialogView);
                    alertDialogObject = dialog.create();
                    alertDialogObject.show();
                } catch (JSONException ex) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
            }
        });
    }
    void kategori_utama(String url,String key){

        RequestParams params = new RequestParams();
        params.put("key", key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String[] id_kategori = new String[jarr.length()];
                    String[] item_kategori = new String[jarr.length()];
                    String[] url_kategori = new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        id_kategori[i] = jobj.getString("id_kategori");
                        item_kategori[i] = jobj.getString("kategori");
                        url_kategori[i] = jobj.getString("icon");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_iklan.this);
                    LayoutInflater inflater = activity_iklan.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    list_kategori.setAdapter(new class_kategori(activity_iklan.this, id_kategori, item_kategori, url_kategori));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            kategori.setText(text.getText().toString());
                            edt_id_kat.setText(text_id.getText().toString() + ",");
                            alertDialogObject.dismiss();
                            sub_kategori_1(class_bantuan.base_url + "sub_kategori_satu", class_bantuan.api_key, text_id.getText().toString());
                        }
                    });
                    dialog.setView(dialogView);
                    alertDialogObject = dialog.create();
                    alertDialogObject.show();

                    layout_form.removeAllViews();

                } catch (JSONException ex) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
            }
        });
    }
    void netral_img(){
        img1.setBackgroundColor(Color.TRANSPARENT);
        img1.setPadding(0, 0, 0, 0);
        img2.setBackgroundColor(Color.TRANSPARENT);
        img2.setPadding(0, 0, 0, 0);
        img3.setBackgroundColor(Color.TRANSPARENT);
        img3.setPadding(0, 0, 0, 0);
        img4.setBackgroundColor(Color.TRANSPARENT);
        img4.setPadding(0, 0, 0, 0);
        img5.setBackgroundColor(Color.TRANSPARENT);
        img5.setPadding(0, 0, 0, 0);

    }
    void set_img_kosong(){
        img1.setImageDrawable(null);
        img2.setImageDrawable(null);
        img3.setImageDrawable(null);
        img4.setImageDrawable(null);
        img5.setImageDrawable(null);
    }
    void upload(String url,String api_key,String []url_image,String []form_data) throws FileNotFoundException{
        RequestParams params = new RequestParams();

        for(int i=0;i<url_image.length;i++) {
            File myfile = new File(url_image[i]);
            params.put("userfile["+String.valueOf(i)+"]", myfile);
        }
        params.put("key", api_key);
        params.put("judul_iklan", form_data[0]);
        params.put("deskripsi_iklan", form_data[1]);
        params.put("harga_iklan", form_data[2]);
        params.put("web", form_data[3]);
        params.put("username", form_data[5]);
        params.put("kota", form_data[6]);
        if(form_data[7]!=null){
            params.put("kategori", form_data[7]);
        }if(form_data[8]!=null){
            params.put("sub1_kategori", form_data[8]);
        }if(form_data[9]!=null){
            params.put("sub2_kategori", form_data[9]);
        }
        params.put("jenis_iklan", form_data[10]);

        if(form_data[11]!=null){
            params.put("hide_email", form_data[11]);
        }
        if(form_data[15]!=null){
            params.put("hide_bbm",form_data[15]);
        }
        if(form_data[12]!=null){
            params.put("wa_available",form_data[12]);
        }
        //cek jika layout mobil tampil
        if(layout_form.findViewById(R.id.form_mobil_transmisi)!=null){
            params.put("transmisi",String.valueOf(((Spinner) child.findViewById(R.id.form_mobil_transmisi)).getSelectedItem()));
            params.put("tipe", ((StringDgID) ((Spinner) child.findViewById(R.id.form_mobil_tipe)).getSelectedItem()).id);
            params.put("tahun", String.valueOf(((Spinner) child.findViewById(R.id.form_mobil_tahun)).getSelectedItem()));
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_iklan.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_iklan.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respon_server = new String(responseBody);
                if (respon_server.equals("failed") || respon_server.isEmpty()) {
                    mDialog.dismiss();
                    snackBar = new SnackBar.Builder(activity_iklan.this)
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
                    mDialog.dismiss();
                    snackBar = new SnackBar.Builder(activity_iklan.this)
                            .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                @Override
                                public void onMessageClick(Parcelable token) {
                                    snackBar.clear();
                                    refresh_form();
                                }
                            })
                            .withMessage("Proses kirim iklan sukses")
                            .withActionMessage("Tutup")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.PERMANENT_SNACK)
                            .show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Return Failed", String.valueOf(statusCode));
                mDialog.dismiss();
                snackBar = new SnackBar.Builder(activity_iklan.this)
                        .withOnClickListener(new SnackBar.OnMessageClickListener() {
                            @Override
                            public void onMessageClick(Parcelable token) {
                                snackBar.clear();
                            }
                        })
                        .withMessage("Proses kirim iklan gagal")
                        .withActionMessage("Tutup")
                        .withStyle(SnackBar.Style.INFO)
                        .withDuration(SnackBar.LONG_SNACK)
                        .show();
            }
        });
    }
    void refreshimageview(){
        set_img_kosong();
        class_helpersqlite db_helper=new class_helpersqlite(this);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from tbl_image", null);
        Log.e("jumlah gambar", String.valueOf(c.getCount()));
        url_image=new String[c.getCount()];
        if (c.moveToFirst()) {
            int i=0;
            do {
                AQuery aq=new AQuery(activity_iklan.this);
                aq.id(findViewById(arr_image[i])).image(c.getString(2));
                Log.e("url", c.getString(2));
                url_image[i]=c.getString(2);
                i++;
            } while (c.moveToNext());
        }
        c.close();
    }
    public void setuptoolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PASANG IKLAN");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void inisialisasi() {
        setuptoolbar();
        img1 = (ImageView) findViewById(R.id.img_cam1);
        img2 = (ImageView) findViewById(R.id.img_cam2);
        img3 = (ImageView) findViewById(R.id.img_cam3);
        img4 = (ImageView) findViewById(R.id.img_cam4);
        img5 = (ImageView) findViewById(R.id.img_cam5);
        cb_email=(CheckBox) findViewById(R.id.checkbox1);
        cb_telpon1=(CheckBox) findViewById(R.id.checkbox2);
        cb_telpon2=(CheckBox) findViewById(R.id.checkbox3);
        cb_line=(CheckBox) findViewById(R.id.checkbox4);
        cb_bbm=(CheckBox) findViewById(R.id.checkbox5);

        layout_form=(RelativeLayout) findViewById(R.id.form_kategori);
        img_takepicture = (ImageView) findViewById(R.id.img_ambilgambar);
        arr_image = new int[]{R.id.img_cam1, R.id.img_cam2,R.id.img_cam3, R.id.img_cam4,R.id.img_cam5};
        judul_iklan=(EditText) findViewById(R.id.editText9);
        deskripsi_iklan=(EditText) findViewById(R.id.editText11);
        harga_iklan=(EditText) findViewById(R.id.editText12);
        web_iklan=(EditText) findViewById(R.id.editText13);
        owner=(TextView) findViewById(R.id.editText15);
        telpon=(TextView) findViewById(R.id.editText17);
        telpon2=(TextView) findViewById(R.id.editText24);
        line_id=(TextView) findViewById(R.id.editText25);
        email=(TextView) findViewById(R.id.editText26);
        bbm=(TextView) findViewById(R.id.editText18);
        username=(TextView) findViewById(R.id.editText19);
        simpan=(TextView) findViewById(R.id.textView21);
        edt_id_kota=(EditText)findViewById(R.id.editText21);
        edt_id_kat=(EditText) findViewById(R.id.editText22);

        class_prosessql class_prosessql=new class_prosessql(activity_iklan.this);
        username.setText(class_prosessql.baca_profil()[1]);
        respon_server = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username", username.getText().toString());
        client.post(class_bantuan.base_url + "get_profil", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    respon_server = new String(responseBody);
                    JSONArray jarr = new JSONArray(respon_server);
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        owner.setText(jobj.getString("nama_member"));
                        bbm.setText(jobj.getString("bbm_member"));
                        telpon.setText(jobj.getString("telepon_member"));
                        telpon2.setText(jobj.getString("telepon_member2"));
                        line_id.setText(jobj.getString("id_line"));
                        email.setText(jobj.getString("email"));
                    }
                } catch (JSONException ex) {
                    Toast.makeText(activity_iklan.this,"Gagal parsing data profil",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity_iklan.this,"Gagal ambil data profil",Toast.LENGTH_LONG).show();
            }
        });
        jns_brg=(EditText) findViewById(R.id.editText23);
        id_jenis=(EditText) findViewById(R.id.id_jenis);
        kategori = (EditText) findViewById(R.id.editText16);
        kota=(EditText) findViewById(R.id.editText20);
    }

    void insertgambar_kedb(String email,String url_image){
        db_helper=new class_helpersqlite(activity_iklan.this);
        db=db_helper.getWritableDatabase();
        SQLiteStatement stmt=db.compileStatement("insert into tbl_image (email,url_image) values(?,?)");
        stmt.bindString(1, email);
        stmt.bindString(2, url_image);
        stmt.execute();
        stmt.close();
        db.close();
        refreshimageview();
    }
    void prosesinsertgambar_kedb(String url_image){
        class_helpersqlite db_helper=new class_helpersqlite(activity_iklan.this);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c=db.rawQuery("select * from tbl_member", null);
        if(c.moveToFirst())
        {
            insertgambar_kedb(c.getString(2), url_image);
        }
    }
    void selectImage() {
        final CharSequence[] items = {"Kamera", "Galeri", "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_iklan.this);
        builder.setTitle("Tambah Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Kamera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Galeri")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void click_dialog_jns() {
        final CharSequence[] items = {"Barang Lama", "Barang Baru"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_iklan.this);
        builder.setTitle("Jenis Barang");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Barang Lama")) {
                    id_jenis.setText("2");
                    jns_brg.setText(items[item]);
                } else if (items[item].equals("Barang Baru")) {
                    id_jenis.setText("1");
                    jns_brg.setText(items[item]);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void hapusimg(final int i){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity_iklan.this);
        alertDialogBuilder.setTitle("Hapus");
        alertDialogBuilder
                            .setMessage("Anda yakin ingin menghapus foto ini?")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    proseshapusgambar(url_image[i]);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    void proseshapusgambar(String url_image) {
        SQLiteDatabase db;
        class_helpersqlite db_helper=new class_helpersqlite(activity_iklan.this);
        db=db_helper.getWritableDatabase();
        SQLiteStatement stmt=db.compileStatement("delete from tbl_image where url_image=?");
        stmt.bindString(1, url_image);
        stmt.execute();
        stmt.close();
        db.close();
        refreshimageview();
        netral_img();
    }
    void refresh_form() {
        SQLiteDatabase db;
        class_helpersqlite db_helper=new class_helpersqlite(activity_iklan.this);
        db=db_helper.getWritableDatabase();
        SQLiteStatement stmt=db.compileStatement("delete from tbl_image");
        stmt.execute();
        stmt.close();
        db.close();
        refreshimageview();
        netral_img();
        judul_iklan.setText("");
        deskripsi_iklan.setText("");
        harga_iklan.setText("");
        web_iklan.setText("");
    }
    void onclick_image(){
        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (img1.getDrawable() == null) {
                } else {
                    netral_img();
                    img1.setBackgroundColor(Color.BLUE);
                    img1.setPadding(10, 10, 10, 10);
                    hapusimg(0);
                }
                return false;
            }
        });
        img2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (img2.getDrawable() == null) {
                } else {
                    netral_img();
                    img2.setBackgroundColor(Color.BLUE);
                    img2.setPadding(10, 10, 10, 10);
                    hapusimg(1);
                }
                return false;
            }
        });
        img3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (img3.getDrawable() == null) {
                } else {
                    netral_img();
                    img3.setBackgroundColor(Color.BLUE);
                    img3.setPadding(10, 10, 10, 10);
                    hapusimg(2);
                }
                return false;
            }
        });
        img4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (img4.getDrawable() == null) {
                } else {
                    netral_img();
                    img4.setBackgroundColor(Color.BLUE);
                    img4.setPadding(10, 10, 10, 10);
                    hapusimg(3);
                }
                return false;
            }
        });
        img5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (img5.getDrawable() == null) {
                } else {
                    netral_img();
                    img5.setBackgroundColor(Color.BLUE);
                    img5.setPadding(10, 10, 10, 10);
                    hapusimg(4);
                }
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            flag=flag+1;
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //((ImageView)findViewById(arr_image[flag-1])).setImageBitmap(thumbnail);
                prosesinsertgambar_kedb(destination.getAbsolutePath());

             }
            else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                //((ImageView)findViewById(arr_image[flag-1])).setImageBitmap(bm);
                prosesinsertgambar_kedb(selectedImagePath);

            }
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent=new Intent(activity_iklan.this,activity_main.class);
        startActivity(intent);
    }
    public class StringDgID {
        public String value;
        public String id;

        public StringDgID(String value,String id) {
            this.value = value;
            this.id = id;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
