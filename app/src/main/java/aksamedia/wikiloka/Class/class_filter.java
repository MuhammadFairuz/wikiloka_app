package aksamedia.wikiloka.Class;

import android.app.Dialog;
import android.content.Context;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import aksamedia.wikiloka.Activity.activity_listitem;
import aksamedia.wikiloka.Activity.activity_sub_kategori;
import aksamedia.wikiloka.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Dinar on 10/15/2016.
 */
public class class_filter extends AppCompatActivity implements View.OnClickListener {
    private SimpleArcDialog mDialog;
    private AsyncHttpClient client;
    String device_id, urlSubKategori, detail_url;
    TelephonyManager tMgr2;
    String[] filter_db, subKategori, sub2Kategori, temp, tipeKendaraan, harga;
    Context context = this;
    Spinner sSort,sSubKategori,sMerk,sFilter,sTipeKendaraan,sTransmisi,sTahun,sKamarTidur,sKamarMandi,sSertifikasi,sLantai,sHargaAwal,sHargaAkhir,sLBAwal,sLBAkhir,sLTAwal,sLTAkhir;
    LinearLayout filterLuasTanah,filterLuasBangunan,filterHarga;
    TextView bUbah, bBatal;
    List<NameValuePair> paramsKategori;
    ArrayAdapter<String> adapter;
    JSONArray jarrSubkategori, jarrSub2kategori, jarrHarga;
    List<String> properti;
    SnackBar snackBar;
    class_bantuan cb;
    activity_sub_kategori ask;
    activity_listitem ali;


    @Override
    public void onClick(View v) {
        tMgr2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id=String.valueOf(tMgr2.getDeviceId());
        filter_db = new class_prosessql(class_filter.this).get_history("tbl_filter_"+getIntent().getStringExtra("seo_kategori").replaceAll("-", "_"), device_id);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_filter_kategori);

        sSort= (Spinner) dialog.findViewById(R.id.sSort);
        sSubKategori = (Spinner) dialog.findViewById(R.id.sSubKategori);
        sMerk = (Spinner) dialog.findViewById(R.id.sMerk);
        sFilter = (Spinner) dialog.findViewById(R.id.sFilter);
        sTipeKendaraan = (Spinner) dialog.findViewById(R.id.sTipeKendaraan);
        sTransmisi = (Spinner) dialog.findViewById(R.id.sTransmisi);
        sTahun= (Spinner) dialog.findViewById(R.id.sTahun);
        sKamarTidur = (Spinner) dialog.findViewById(R.id.sKamarTidur);
        sKamarMandi= (Spinner) dialog.findViewById(R.id.sKamarMandi);
        sSertifikasi= (Spinner) dialog.findViewById(R.id.sSertifikasi);
        sLantai= (Spinner) dialog.findViewById(R.id.sLantai);
        filterLuasTanah= (LinearLayout) dialog.findViewById(R.id.filterLuasTanah);
        filterLuasBangunan= (LinearLayout) dialog.findViewById(R.id.filterLuasBangunan);
        filterHarga= (LinearLayout) dialog.findViewById(R.id.filterHarga);
        sHargaAwal= (Spinner) dialog.findViewById(R.id.sHargaAwal);
        sHargaAkhir= (Spinner) dialog.findViewById(R.id.sHargaAkhir);
        sLTAwal= (Spinner) dialog.findViewById(R.id.sLTAwal);
        sLTAkhir= (Spinner) dialog.findViewById(R.id.sLTAkhir);
        sLBAwal= (Spinner) dialog.findViewById(R.id.sLBAwal);
        sLBAkhir= (Spinner) dialog.findViewById(R.id.sLBAkhir);

        bBatal = (TextView) dialog.findViewById(R.id.bBatal);
        bUbah = (TextView) dialog.findViewById(R.id.bUbah);

        paramsKategori = new LinkedList<NameValuePair>();

        //SORT HARGA
        subKategori = new String[]{"Semua urutan", "Termurah", "Terbaru"};
        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, subKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSort.setAdapter(adapter);
        sSort.setVisibility(View.VISIBLE);
        if (filter_db[1] != "-" && filter_db[1] != null)
            sSort.setSelection(Integer.parseInt(filter_db[1]));

        //SUB KATEGORI 1
        subKategori = new String[jarrSubkategori.length()+1];
        subKategori[0] = "Semua kategori";

        try{
            for (int i = 0; i < jarrSubkategori.length(); ++i) {
                JSONObject jobj = jarrSubkategori.getJSONObject(i);
                subKategori[i+1] = jobj.getString("sub1_kategori");
            }
            adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, subKategori);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sSubKategori.setAdapter(adapter);
            sSubKategori.setVisibility(View.VISIBLE);
            if (filter_db[2] != "-" && filter_db[2] != null)
                sSubKategori.setSelection(Integer.parseInt(filter_db[2]));
        } catch (JSONException ex)
        {
            Log.e("JSONException", ex.toString());
        }

        sSubKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //initialize
                sMerk.setVisibility(View.GONE);
                sFilter.setVisibility(View.GONE);
                sTipeKendaraan.setVisibility(View.GONE);
                sTransmisi.setVisibility(View.GONE);
                sTahun.setVisibility(View.GONE);
                sKamarTidur.setVisibility(View.GONE);
                sKamarMandi.setVisibility(View.GONE);
                sSertifikasi.setVisibility(View.GONE);
                sLantai.setVisibility(View.GONE);

                sHargaAkhir.setEnabled(false);
                sHargaAkhir.setAdapter(null);
                sLBAkhir.setEnabled(false);
                sLBAkhir.setAdapter(null);
                sLTAkhir.setEnabled(false);
                sLTAkhir.setAdapter(null);
                filterLuasTanah.setVisibility(View.GONE);
                filterLuasBangunan.setVisibility(View.GONE);
                filterHarga.setVisibility(View.GONE);


                urlSubKategori = subKategori[position].toLowerCase().replaceAll("&","").replaceAll("-","").replaceAll(" ", "-");
                detail_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("seo_kategori") + "/" + urlSubKategori + "/";

                properti = Arrays.asList("rumah", "apartmen", "indekos", "bangunan-komersil", "tanah");

                //MERK//
                if (urlSubKategori.equals("merk")) {
                    if(getIntent().getStringExtra("seo_kategori").equals("mobil")){
                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, class_bantuan.transmisi);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sTransmisi.setAdapter(adapter);
                        sTransmisi.setVisibility(View.VISIBLE);
                    }
                    client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("key", class_bantuan.api_key);
                    client.post(detail_url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            mDialog = new SimpleArcDialog(class_filter.this);
                            mDialog.setConfiguration(new ArcConfiguration(class_filter.this));
                            mDialog.show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            JSONObject job = null;
                            try {
                                job = new JSONObject(new String(responseBody));
                                jarrSub2kategori = new JSONArray(String.valueOf(job.getJSONArray("sub2kategori")));
                                jarrHarga = new JSONArray(String.valueOf(job.getJSONArray("harga")));
                                sub2Kategori = new String[jarrSub2kategori.length() + 1];
                                sub2Kategori[0] = "Semua Merk";
                                for (int i = 0; i < jarrSub2kategori.length(); ++i) {
                                    JSONObject jobj = jarrSub2kategori.getJSONObject(i);
                                    sub2Kategori[i + 1] = jobj.getString("sub2_kategori");
                                }
                                adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, sub2Kategori);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sFilter.setAdapter(adapter);
                                sFilter.setVisibility(View.VISIBLE);

                                if (filter_db[3] != "-" && filter_db[3] != null)
                                    sFilter.setSelection(Integer.parseInt(filter_db[3]));

                                sFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != 0) {
                                            String filter_url = detail_url + "?filter=" + sFilter.getSelectedItem().toString().replaceAll("-", "").replaceAll(" ", "-");
                                            Log.e("filter url", filter_url);
                                            client = new AsyncHttpClient();
                                            RequestParams params = new RequestParams();
                                            params.put("key", class_bantuan.api_key);
                                            client.post(filter_url, params, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onStart() {
                                                    mDialog = new SimpleArcDialog(class_filter.this);
                                                    mDialog.setConfiguration(new ArcConfiguration(class_filter.this));
                                                    mDialog.show();
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    String key_kategori = "";
                                                    JSONObject job = null;
                                                    try {
                                                        job = new JSONObject(new String(responseBody));
                                                        if (getIntent().getStringExtra("seo_kategori").equals("mobil"))
                                                            key_kategori = "tipe";
                                                        else
                                                            key_kategori = "tipe_motor";
                                                        jarrSub2kategori = new JSONArray(String.valueOf(job.getJSONArray(key_kategori)));
                                                        temp = new String[jarrSub2kategori.length() + 1];
                                                        tipeKendaraan = new String[jarrSub2kategori.length() + 1];
                                                        temp[0] = "Semua Tipe Kendaraan";
                                                        tipeKendaraan[0] = "0";
                                                        for (int i = 0; i < jarrSub2kategori.length(); ++i) {
                                                            JSONObject jobj = jarrSub2kategori.getJSONObject(i);
                                                            temp[i + 1] = jobj.getString("tipe");
                                                            tipeKendaraan[i + 1] = jobj.getString("id_tipe_" + getIntent().getStringExtra("seo_kategori").toLowerCase());
                                                        }
                                                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        sTipeKendaraan.setAdapter(adapter);
                                                        sTipeKendaraan.setVisibility(View.VISIBLE);
                                                        if (filter_db[4] != "-" && filter_db[4] != null)
                                                            sTipeKendaraan.setSelection(Integer.parseInt(filter_db[4]));
                                                        mDialog.dismiss();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                                temp = new String[Calendar.getInstance().get(Calendar.YEAR)-1990+1];
                                for (int i = 1; i < temp.length; ++i)
                                    temp[i] = String.valueOf(i + 1990);
                                temp[0] = "Semua Tahun";
                                adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sTahun.setAdapter(adapter);
                                sTahun.setVisibility(View.VISIBLE);
                                if (filter_db[7] != "-" && filter_db[7] != null)
                                    sTahun.setSelection(Integer.parseInt(filter_db[7]));

                                harga = new String[jarrHarga.length()+1];
                                harga[0] = "Semua Harga";
                                for (int i = 0; i < jarrHarga.length(); ++i) {
                                    harga[i + 1] = jarrHarga.getString(i);
                                }
                                adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, harga);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sHargaAwal.setAdapter(adapter);
                                sHargaAwal.setVisibility(View.VISIBLE);
                                filterHarga.setVisibility(View.VISIBLE);
                                if (filter_db[5] != "-" && filter_db[5] != null)
                                    sHargaAwal.setSelection(Integer.parseInt(filter_db[5]));

                                sHargaAwal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != 0) {
                                            temp = new String[harga.length - position + 1];
                                            temp[0] = "Harga Akhir";
                                            int j = 1;
                                            for (int i = position; i < harga.length; ++i) {
                                                temp[j++] = harga[i];
                                            }
                                            adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sHargaAkhir.setAdapter(adapter);
                                            sHargaAkhir.setEnabled(true);
                                            if (filter_db[6] != "-" && filter_db[6] != null)
                                                sHargaAkhir.setSelection(Integer.parseInt(filter_db[6]));
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                mDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            mDialog.dismiss();
                            dialog_gagal();
                        }
                    });
                    //PROPERTI
                } else if (properti.contains(urlSubKategori)) {
                    client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("key", class_bantuan.api_key);
                    client.post(detail_url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            mDialog = new SimpleArcDialog(class_filter.this);
                            mDialog.setConfiguration(new ArcConfiguration(class_filter.this));
                            mDialog.show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            JSONObject job = null;
                            try {
                                job = new JSONObject(new String(responseBody));
                                jarrHarga = new JSONArray(String.valueOf(job.getJSONArray("harga")));

                                harga = new String[jarrHarga.length()+1];
                                harga[0] = "Semua Harga";
                                for (int i = 0; i < jarrHarga.length(); ++i) {
                                    harga[i + 1] = jarrHarga.getString(i);
                                }
                                adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, harga);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sHargaAwal.setAdapter(adapter);
                                sHargaAwal.setVisibility(View.VISIBLE);
                                filterHarga.setVisibility(View.VISIBLE);

                                sHargaAwal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != 0) {
                                            temp = new String[harga.length - position + 1];
                                            temp[0] = "Harga Akhir";
                                            int j = 1;
                                            for (int i = position; i < harga.length; ++i) {
                                                temp[j++] = harga[i];
                                            }
                                            adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sHargaAkhir.setAdapter(adapter);
                                            sHargaAkhir.setEnabled(true);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                if (urlSubKategori.equals("rumah") || urlSubKategori.equals("apartmen") || urlSubKategori.equals("tanah")) {
                                    adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, class_bantuan.sertifikasi);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sSertifikasi.setAdapter(adapter);
                                    sSertifikasi.setVisibility(View.VISIBLE);

                                    if (!urlSubKategori.equals("tanah")) {
                                        sub2Kategori = new String[]{"Pilih " + urlSubKategori, "Dijual", "Disewakan"};
                                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, sub2Kategori);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sFilter.setAdapter(adapter);
                                        sFilter.setVisibility(View.VISIBLE);

                                        temp = new String[class_bantuan.jumlah.length+1];
                                        temp[0] = "Kamar Tidur";
                                        for (int i = 0; i < class_bantuan.jumlah.length; ++i) {
                                            temp[i + 1] = class_bantuan.jumlah[i];
                                        }
                                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sKamarTidur.setAdapter(adapter);
                                        sKamarTidur.setVisibility(View.VISIBLE);

                                        temp = new String[class_bantuan.jumlah.length+1];
                                        temp[0] = "Lantai";
                                        for (int i = 0; i < class_bantuan.jumlah.length; ++i) {
                                            temp[i + 1] = class_bantuan.jumlah[i];
                                        }
                                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sLantai.setAdapter(adapter);
                                        sLantai.setVisibility(View.VISIBLE);

                                        temp = new String[class_bantuan.luas.length+1];
                                        temp[0] = "Luas Tanah Awal";
                                        for (int i = 0; i < class_bantuan.luas.length; ++i) {
                                            temp[i + 1] = class_bantuan.luas[i];
                                        }
                                        adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sLTAwal.setAdapter(adapter);
                                        sLTAwal.setVisibility(View.VISIBLE);
                                        filterLuasTanah.setVisibility(View.VISIBLE);
                                        sLTAwal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if (position != 0) {
                                                    temp = new String[class_bantuan.luas.length - position + 2];
                                                    temp[0] = "Luas Tanah Akhir";
                                                    int j=1;
                                                    for (int i = position-1; i < class_bantuan.luas.length; ++i) {
                                                        temp[j++] = class_bantuan.luas[i];
                                                    }
                                                    adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    sLTAkhir.setAdapter(adapter);
                                                    sLTAkhir.setEnabled(true);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }

                                }
                                if (!urlSubKategori.equals("tanah")) {
                                    temp = new String[class_bantuan.luas.length+1];
                                    temp[0] = "Luas Bangunan Awal";
                                    for (int i = 0; i < class_bantuan.luas.length; ++i) {
                                        temp[i + 1] = class_bantuan.luas[i];
                                    }
                                    adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sLBAwal.setAdapter(adapter);
                                    sLBAwal.setVisibility(View.VISIBLE);
                                    filterLuasBangunan.setVisibility(View.VISIBLE);

                                    sLBAwal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position != 0){
                                                temp = new String[class_bantuan.luas.length - position + 2];
                                                temp[0] = "Luas Bangunan Akhir";
                                                int j=1;
                                                for (int i = position-1; i < class_bantuan.luas.length; ++i) {
                                                    temp[j++] = class_bantuan.luas[i];
                                                }
                                                adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                sLBAkhir.setAdapter(adapter);
                                                sLBAkhir.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }

                                if (urlSubKategori.equals("rumah") || urlSubKategori.equals("indekos") || urlSubKategori.equals("bangunan-komersil")) {
                                    temp = new String[class_bantuan.jumlah.length+1];
                                    temp[0] = "Kamar Mandi";
                                    for (int i = 0; i < class_bantuan.jumlah.length; ++i) {
                                        temp[i + 1] = class_bantuan.jumlah[i];
                                    }
                                    adapter = new ArrayAdapter<String>(class_filter.this, android.R.layout.simple_spinner_item, temp);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sKamarMandi.setAdapter(adapter);
                                    sKamarMandi.setVisibility(View.VISIBLE);
                                }

                                mDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sort="-", hargaAwal="-", hargaAkhir="-", kategori2="-", sertifikasi="-",
                        filter="-", kamarTidur="-", kamarMandi="-", lantai="-", luasTanahAwal="-",
                        luasTanahAkhir="-", luasBangunanAwal="-", luasBangunanAkhir="-", transmisi="-",
                        tipe="-", tahun="-";
                kategori2 = String.valueOf(sSubKategori.getSelectedItemPosition());

                paramsKategori.add(new BasicNameValuePair("sort", sSort.getSelectedItem().toString().toLowerCase()));
                sort= String.valueOf(sSort.getSelectedItemPosition());

                if(urlSubKategori.equals("merk") || properti.contains(urlSubKategori)){
                    hargaAwal= String.valueOf(sHargaAwal.getSelectedItemPosition());
                    paramsKategori.add(new BasicNameValuePair("start", sHargaAwal.getSelectedItem().toString()));

                    if (sHargaAkhir.isEnabled() ){
                        paramsKategori.add(new BasicNameValuePair("end", sHargaAkhir.getSelectedItem().toString()));
                        hargaAkhir = String.valueOf(sHargaAkhir.getSelectedItemPosition());
                    }

                }

                if (properti.contains(urlSubKategori)){
                    if (urlSubKategori.equals("rumah") || urlSubKategori.equals("apartmen") || urlSubKategori.equals("tanah")) {
                        paramsKategori.add(new BasicNameValuePair("sertifikasi", String.valueOf(sSertifikasi.getSelectedItemPosition())));
                        sertifikasi= String.valueOf(sSertifikasi.getSelectedItemPosition());

                        if (!urlSubKategori.equals("tanah")) {
                            paramsKategori.add(new BasicNameValuePair("filter", sFilter.getSelectedItem().toString().toLowerCase()));
                            filter = String.valueOf(sFilter.getSelectedItemPosition());
                            paramsKategori.add(new BasicNameValuePair("kamar_tidur", String.valueOf(sKamarTidur.getSelectedItemPosition())));
                            kamarTidur = String.valueOf(sKamarTidur.getSelectedItemPosition());

                            paramsKategori.add(new BasicNameValuePair("lantai", sLantai.getSelectedItem().toString().toLowerCase()));
                            lantai = String.valueOf(sLantai.getSelectedItemPosition());

                            paramsKategori.add(new BasicNameValuePair("luas_tanah_awal", sLTAwal.getSelectedItem().toString()));
                            luasTanahAwal = String.valueOf(sLTAwal.getSelectedItemPosition());

                            if (sLTAkhir.isEnabled() ){
                                paramsKategori.add(new BasicNameValuePair("luas_tanah_akhir", sLTAkhir.getSelectedItem().toString()));
                                luasTanahAkhir = String.valueOf(sLTAkhir.getSelectedItemPosition());
                            }

                        }
                    }
                    if (!urlSubKategori.equals("tanah")) {
                        paramsKategori.add(new BasicNameValuePair("luas_bangunan_awal", sLBAwal.getSelectedItem().toString()));
                        luasBangunanAwal = String.valueOf(sLBAwal.getSelectedItemPosition());

                        if (sLBAkhir.isEnabled() ){
                            paramsKategori.add(new BasicNameValuePair("luas_bangunan_akhir", sLBAkhir.getSelectedItem().toString()));
                            luasBangunanAkhir = String.valueOf(sLBAkhir.getSelectedItemPosition());
                        }

                    }
                    if (urlSubKategori.equals("rumah") || urlSubKategori.equals("indekos") || urlSubKategori.equals("bangunan-komersil")) {
                        if (sKamarMandi.getVisibility() == View.VISIBLE ){
                            paramsKategori.add(new BasicNameValuePair("kamar_mandi", String.valueOf(sKamarMandi.getSelectedItemPosition())));
                            kamarMandi = String.valueOf(sKamarMandi.getSelectedItemPosition());
                        }

                    }
                }

                if (urlSubKategori.equals("merk")) {
                    if (getIntent().getStringExtra("seo_kategori").equals("mobil")) {
                        paramsKategori.add(new BasicNameValuePair("transmisi", sTransmisi.getSelectedItem().toString().toLowerCase()));
                        transmisi = String.valueOf(sTransmisi.getSelectedItemPosition());
                    }

                    paramsKategori.add(new BasicNameValuePair("filter", sFilter.getSelectedItem().toString().toLowerCase()));
                    filter = String.valueOf(sFilter.getSelectedItemPosition());

                    paramsKategori.add(new BasicNameValuePair("tipe", tipeKendaraan[((int) sTipeKendaraan.getSelectedItemId())]));
                    tipe = String.valueOf(sTipeKendaraan.getSelectedItemPosition());

                    paramsKategori.add(new BasicNameValuePair("tahun", sTahun.getSelectedItem().toString().toLowerCase()));
                    tahun = String.valueOf(sTahun.getSelectedItemPosition());

                }

                detail_url= cb.merge(detail_url, paramsKategori);
                switch (getIntent().getStringExtra("seo_kategori").toLowerCase()){
                    case "motor":
                        new class_prosessql(class_filter.this).insert_filter_motor(sort, kategori2, filter, tipe, hargaAwal, hargaAkhir, tahun, device_id);
                        break;
                    case "mobil":
                        new class_prosessql(class_filter.this).insert_filter_mobil(sort, kategori2, filter, tipe, transmisi, hargaAwal, hargaAkhir, tahun, device_id);
                        break;
                    case "rumah-tangga":
                        new class_prosessql(class_filter.this).insert_filter_rumah_tangga(sort, kategori2, device_id);
                        break;
                    case "properti":
                        new class_prosessql(class_filter.this).insert_filter_properti(sort, kategori2, filter, kamarTidur, kamarMandi, sertifikasi, hargaAwal, hargaAkhir, lantai, luasBangunanAwal, luasBangunanAkhir,luasTanahAwal,luasTanahAkhir,device_id);
                        break;
                    case "olahraga":
                        new class_prosessql(class_filter.this).insert_filter_olahraga(sort,kategori2,device_id);
                        break;
                    case "elektronik":
                        new class_prosessql(class_filter.this).insert_filter_elektronik(sort,kategori2,device_id);
                        break;
                    case "travel":
                        new class_prosessql(class_filter.this).insert_filter_travel(sort,kategori2,device_id);
                        break;
                    case "kuliner":
                        new class_prosessql(class_filter.this).insert_filter_kuliner(sort,kategori2,device_id);
                        break;
                    case "fashion":
                        new class_prosessql(class_filter.this).insert_filter_fashion(sort,kategori2,device_id);
                        break;
                    default:
                        break;
                }
                //baca_topmenu(detail_url, class_bantuan.api_key);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialog_gagal(){
        snackBar = new SnackBar.Builder(class_filter.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik","tutup");
                    }
                })
                .withMessage("Gagal mengambil data")
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }
}
