package aksamedia.wikiloka.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_adapterlistitem;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_iklan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.Class.class_sub_kategori;
import aksamedia.wikiloka.Gajelas.tv_euphemia;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class activity_sub_kategori extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    private class_adapterlistitem mAdapter;
    private AsyncHttpClient client;
    private ArrayList<String> m_kota;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialogObject;
    private ArrayList<String> i_kota;
    private SimpleArcDialog mDialog;
    private ArrayList<class_iklan> class_barang;
    private Toolbar toolbar;
    private LinearLayout top_menu;
    int index_menuterakhir;
    private TextView[] tv,tv1;
    String []arrmenu={"Semua","Top Shop","Rekomendasi","Terlaris"};
    private MaterialSearchView searchView;
    private HorizontalScrollView hsv;
    private String respon_server;
    private ListView list;
    SnackBar snackBar;
    private tv_euphemia kateg_utama;
    private String[] item_kategori1,id_kat,status;
    RecyclerView mRecyclerView;
    private String url_image;
    ArrayList<class_iklan> iklan;
    FloatingActionButton fab;
    String detail_url, urlSubKategori ="";
    TextView tvFilter, tvSort, tvKategori1, tvKategori2;
    Context context = this;
    JSONArray jarrSubkategori, jarrSub2kategori, jarrHarga;
    ArrayAdapter<String> adapter;
    String[] subKategori, sub2Kategori, temp, harga, tipeKendaraan;
    Spinner sSort,sSubKategori,sMerk,sFilter,sTipeKendaraan,sTransmisi,sTahun,sKamarTidur,sKamarMandi,sSertifikasi,sLantai,sHargaAwal,sHargaAkhir,sLBAwal,sLBAkhir,sLTAwal,sLTAkhir;
    LinearLayout filterLuasTanah,filterLuasBangunan,filterHarga;
    TextView bUbah, bBatal;
    List<NameValuePair> paramsKategori;
    ImageView img;
    List<String> properti;
    class_bantuan cb;
    RelativeLayout navFilter;
    String device_id;
    TelephonyManager tMgr2;
    String[] filter_db;
    JSONArray jarrSubKategori1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_kategori);

        paramsKategori = new LinkedList<NameValuePair>();

        top_menu=(LinearLayout) findViewById(R.id.top_menu);
        navFilter= (RelativeLayout) findViewById(R.id.navFilter);
        hsv=(HorizontalScrollView) findViewById(R.id.hsv);
        list=(ListView) findViewById(R.id.list_sub);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        tv=new TextView[arrmenu.length];
        tv1=new TextView[arrmenu.length];
        mRecyclerView = (RecyclerView) findViewById(R.id.rvIklan);
        tvFilter = (TextView) findViewById(R.id.tv_filter);
        tvKategori1 = (TextView) findViewById(R.id.tv_kategori1);
        tvKategori2 = (TextView) findViewById(R.id.tv_kategori2);
        img = (ImageView) findViewById(R.id.img);

        //fab.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        navFilter.setVisibility(View.GONE);

        for (int i = 0; i < arrmenu.length; ++i) {
            View child1=getLayoutInflater().inflate(R.layout.custom_topmenu, null);
            tv[i]=(TextView)child1.findViewById(R.id.texttop2);
            tv[i].setTag(String.valueOf(arrmenu[i]));
            tv[i].setText("   " + arrmenu[i].toUpperCase() + "   ");
            tv[i].findViewWithTag(String.valueOf(arrmenu[i])).setOnClickListener(clicktabmenu);
            tv1[i]=(TextView)child1.findViewById(R.id.menutop2);
            tv1[i].setTag(String.valueOf(arrmenu[i]));
            tv1[i].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu));//.getColor(R.color.colorPrimary));
            tv1[i].setGravity(Gravity.BOTTOM);
            top_menu.addView(child1);
        }
        setuptoolbar();
        //tv1[0].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
        index_menuterakhir=0;
        kateg_utama=(tv_euphemia) findViewById(R.id.kategori_header);
        kateg_utama.setText(getIntent().getStringExtra("kategori"));

        url_image=getIntent().getStringExtra("url_kategori");
        sub_kategori1(class_bantuan.base_url + "sub_kategori_satu", class_bantuan.api_key, getIntent().getStringExtra("id_kategori"));
        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("posisi", String.valueOf(position));
                if (position == 0) {
                    bottom_filter.setAccentColor(Color.parseColor("#747474"));
                    ((fragment_iklanbekas) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(position)).ambil_data("bekas", getIntent().getStringExtra("kategori"), "semua", "semua", "kosong");
                    viewPager.getAdapter().notifyDataSetChanged();
                } else if (position == 1) {
                    bottom_filter.setAccentColor(Color.parseColor("#747474"));
                    ((fragment_iklanbaru) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(position)).ambil_data("baru", getIntent().getStringExtra("kategori"), "semua", "semua", "kosong");
                    viewPager.getAdapter().notifyDataSetChanged();
                } else if (position == 2) {
                    bottom_filter.setAccentColor(Color.parseColor("#747474"));
                    ((fragment_iklansemua) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(position)).ambil_data("semua", getIntent().getStringExtra("kategori"), "semua", "semua", "kosong");
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                setupTabIcons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });*/
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMgr2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String username = new class_prosessql(activity_sub_kategori.this).get_username();
                if (username.equals("kosong")){
                    device_id=String.valueOf(tMgr2.getDeviceId());
                } else{
                    device_id=username;
                }
                Log.e("device_id_check", device_id);
                filter_db = new class_prosessql(activity_sub_kategori.this).get_history("tbl_filter_"+getIntent().getStringExtra("seo_kategori").replaceAll("-", "_"), device_id);
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
                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, subKategori);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sSort.setAdapter(adapter);
                sSort.setVisibility(View.VISIBLE);
                if(filter_db[1]!=null && !filter_db[1].equals("-"))
                    sSort.setSelection(Integer.parseInt(filter_db[1]));

                //SUB KATEGORI 1
                subKategori = new String[jarrSubkategori.length()+1];
                subKategori[0] = "Semua kategori";

                try{
                    for (int i = 0; i < jarrSubkategori.length(); ++i) {
                        JSONObject jobj = jarrSubkategori.getJSONObject(i);
                        subKategori[i+1] = jobj.getString("sub1_kategori");
                    }
                    adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, subKategori);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sSubKategori.setAdapter(adapter);
                    sSubKategori.setVisibility(View.VISIBLE);
                    if(filter_db[2]!=null && !filter_db[2].equals("-"))
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


                        if(position!=0)
                            urlSubKategori = subKategori[position].toLowerCase().replaceAll("&","").replaceAll("-","").replaceAll(" ", "-");
                        detail_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("seo_kategori") + "/" + urlSubKategori + "/";

                        properti = Arrays.asList("rumah", "apartmen", "indekos", "bangunan-komersil", "tanah");

                        //MERK//
                        if (urlSubKategori.equals("merk")) {
                            if(getIntent().getStringExtra("seo_kategori").equals("mobil")){
                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, class_bantuan.transmisi);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sTransmisi.setAdapter(adapter);
                                sTransmisi.setVisibility(View.VISIBLE);
                                if(filter_db[9]!=null && !filter_db[9].equals("-"))
                                    sTransmisi.setSelection(Integer.parseInt(filter_db[9]));
                            }
                            client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("key", class_bantuan.api_key);
                            client.post(detail_url, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onStart() {
                                    mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                                    mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
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
                                        adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, sub2Kategori);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sFilter.setAdapter(adapter);
                                        sFilter.setVisibility(View.VISIBLE);

                                        if(filter_db[3]!=null && !filter_db[3].equals("-"))
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
                                                            mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                                                            mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
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
                                                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                sTipeKendaraan.setAdapter(adapter);
                                                                sTipeKendaraan.setVisibility(View.VISIBLE);
                                                                if(filter_db[4]!=null && !filter_db[4].equals("-"))
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
                                        adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sTahun.setAdapter(adapter);
                                        sTahun.setVisibility(View.VISIBLE);
                                        if(filter_db[7]!=null && !filter_db[7].equals("-"))
                                            sTahun.setSelection(Integer.parseInt(filter_db[7]));

                                        harga = new String[jarrHarga.length()+1];
                                        harga[0] = "Semua Harga";
                                        for (int i = 0; i < jarrHarga.length(); ++i) {
                                            harga[i + 1] = jarrHarga.getString(i);
                                        }
                                        adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, harga);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sHargaAwal.setAdapter(adapter);
                                        sHargaAwal.setVisibility(View.VISIBLE);
                                        filterHarga.setVisibility(View.VISIBLE);
                                        if(filter_db[5]!=null && !filter_db[5].equals("-"))
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
                                                    adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    sHargaAkhir.setAdapter(adapter);
                                                    sHargaAkhir.setEnabled(true);
                                                    if(filter_db[6]!=null && !filter_db[6].equals("-"))
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
                                    mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                                    mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
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
                                        adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, harga);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sHargaAwal.setAdapter(adapter);
                                        sHargaAwal.setVisibility(View.VISIBLE);
                                        filterHarga.setVisibility(View.VISIBLE);
                                        if(filter_db[7]!=null && !filter_db[7].equals("-"))
                                            sHargaAwal.setSelection(Integer.parseInt(filter_db[7]));
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
                                                    adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    sHargaAkhir.setAdapter(adapter);
                                                    sHargaAkhir.setEnabled(true);
                                                    if(filter_db[8]!=null && !filter_db[8].equals("-"))
                                                        sHargaAkhir.setSelection(Integer.parseInt(filter_db[8]));
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });

                                        if (urlSubKategori.equals("rumah") || urlSubKategori.equals("apartmen") || urlSubKategori.equals("tanah")) {
                                            adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, class_bantuan.sertifikasi);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sSertifikasi.setAdapter(adapter);
                                            sSertifikasi.setVisibility(View.VISIBLE);
                                            if(filter_db[6]!=null && !filter_db[6].equals("-"))
                                                sSertifikasi.setSelection(Integer.parseInt(filter_db[6]));

                                            if (!urlSubKategori.equals("tanah")) {
                                                sub2Kategori = new String[]{"Pilih " + urlSubKategori, "Dijual", "Disewakan"};
                                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, sub2Kategori);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                sFilter.setAdapter(adapter);
                                                sFilter.setVisibility(View.VISIBLE);
                                                if(filter_db[3]!=null && !filter_db[3].equals("-"))
                                                    sFilter.setSelection(Integer.parseInt(filter_db[3]));

                                                temp = new String[class_bantuan.jumlah.length+1];
                                                temp[0] = "Kamar Tidur";
                                                for (int i = 0; i < class_bantuan.jumlah.length; ++i) {
                                                    temp[i + 1] = class_bantuan.jumlah[i];
                                                }
                                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                sKamarTidur.setAdapter(adapter);
                                                sKamarTidur.setVisibility(View.VISIBLE);
                                                if(filter_db[4]!=null && !filter_db[4].equals("-"))
                                                    sKamarTidur.setSelection(Integer.parseInt(filter_db[4]));

                                                temp = new String[class_bantuan.jumlah.length+1];
                                                temp[0] = "Lantai";
                                                for (int i = 0; i < class_bantuan.jumlah.length; ++i) {
                                                    temp[i + 1] = class_bantuan.jumlah[i];
                                                }
                                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                sLantai.setAdapter(adapter);
                                                sLantai.setVisibility(View.VISIBLE);
                                                if(filter_db[9]!=null && !filter_db[9].equals("-"))
                                                    sLantai.setSelection(Integer.parseInt(filter_db[9]));

                                                temp = new String[class_bantuan.luas.length+1];
                                                temp[0] = "Luas Tanah Awal";
                                                for (int i = 0; i < class_bantuan.luas.length; ++i) {
                                                    temp[i + 1] = class_bantuan.luas[i];
                                                }
                                                adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                sLTAwal.setAdapter(adapter);
                                                sLTAwal.setVisibility(View.VISIBLE);
                                                filterLuasTanah.setVisibility(View.VISIBLE);
                                                if(filter_db[12]!=null && !filter_db[12].equals("-"))
                                                    sLTAwal.setSelection(Integer.parseInt(filter_db[12]));
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
                                                            adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            sLTAkhir.setAdapter(adapter);
                                                            sLTAkhir.setEnabled(true);
                                                            if(filter_db[13]!=null && !filter_db[13].equals("-"))
                                                                sLTAkhir.setSelection(Integer.parseInt(filter_db[13]));
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
                                            adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sLBAwal.setAdapter(adapter);
                                            sLBAwal.setVisibility(View.VISIBLE);
                                            if(filter_db[10]!=null && !filter_db[10].equals("-"))
                                                sLBAwal.setSelection(Integer.parseInt(filter_db[10]));
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
                                                        adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        sLBAkhir.setAdapter(adapter);
                                                        sLBAkhir.setEnabled(true);
                                                        if(filter_db[11]!=null && !filter_db[11].equals("-"))
                                                            sLBAkhir.setSelection(Integer.parseInt(filter_db[11]));
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
                                            adapter = new ArrayAdapter<String>(activity_sub_kategori.this, android.R.layout.simple_spinner_item, temp);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sKamarMandi.setAdapter(adapter);
                                            sKamarMandi.setVisibility(View.VISIBLE);
                                            if(filter_db[5]!=null && !filter_db[5].equals("-"))
                                                sKamarMandi.setSelection(Integer.parseInt(filter_db[5]));
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

                        sort= String.valueOf(sSort.getSelectedItemPosition());
                        if (sSort.getSelectedItemPosition() != 0)
                            paramsKategori.add(new BasicNameValuePair("sort", sSort.getSelectedItem().toString().toLowerCase()));


                        if(urlSubKategori.equals("merk") || properti.contains(urlSubKategori)){
                            hargaAwal= String.valueOf(sHargaAwal.getSelectedItemPosition());
                            if (sHargaAwal.getSelectedItemPosition() != 0)
                                paramsKategori.add(new BasicNameValuePair("start", sHargaAwal.getSelectedItem().toString()));

                            if (sHargaAkhir.isEnabled() ){
                                hargaAkhir = String.valueOf(sHargaAkhir.getSelectedItemPosition());
                                if (sHargaAkhir.getSelectedItemPosition() != 0)
                                    paramsKategori.add(new BasicNameValuePair("end", sHargaAkhir.getSelectedItem().toString()));
                            }

                        }

                        if (properti.contains(urlSubKategori)){
                            if (urlSubKategori.equals("rumah") || urlSubKategori.equals("apartmen") || urlSubKategori.equals("tanah")) {
                                sertifikasi= String.valueOf(sSertifikasi.getSelectedItemPosition());
                                if (sSertifikasi.getSelectedItemPosition() != 0)
                                    paramsKategori.add(new BasicNameValuePair("sertifikasi", String.valueOf(sSertifikasi.getSelectedItemPosition())));


                                if (!urlSubKategori.equals("tanah")) {
                                    if(sFilter.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("filter", sFilter.getSelectedItem().toString().toLowerCase()));
                                    filter = String.valueOf(sFilter.getSelectedItemPosition());

                                    if (sKamarTidur.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("kamar_tidur", String.valueOf(sKamarTidur.getSelectedItemPosition())));
                                    kamarTidur = String.valueOf(sKamarTidur.getSelectedItemPosition());

                                    if (sLantai.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("lantai", sLantai.getSelectedItem().toString().toLowerCase()));
                                    lantai = String.valueOf(sLantai.getSelectedItemPosition());

                                    if (sLTAwal.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("luas_tanah_awal", sLTAwal.getSelectedItem().toString()));
                                    luasTanahAwal = String.valueOf(sLTAwal.getSelectedItemPosition());

                                    if (sLTAkhir.isEnabled() ){
                                        if (sLTAkhir.getSelectedItemPosition() != 0)
                                            paramsKategori.add(new BasicNameValuePair("luas_tanah_akhir", sLTAkhir.getSelectedItem().toString()));
                                        luasTanahAkhir = String.valueOf(sLTAkhir.getSelectedItemPosition());
                                    }

                                }
                            }
                            if (!urlSubKategori.equals("tanah")) {
                                if (sLBAwal.getSelectedItemPosition() != 0)
                                    paramsKategori.add(new BasicNameValuePair("luas_bangunan_awal", sLBAwal.getSelectedItem().toString()));
                                luasBangunanAwal = String.valueOf(sLBAwal.getSelectedItemPosition());

                                if (sLBAkhir.isEnabled() ){
                                    if (sLBAkhir.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("luas_bangunan_akhir", sLBAkhir.getSelectedItem().toString()));
                                    luasBangunanAkhir = String.valueOf(sLBAkhir.getSelectedItemPosition());
                                }

                            }
                            if (urlSubKategori.equals("rumah") || urlSubKategori.equals("indekos") || urlSubKategori.equals("bangunan-komersil")) {
                                if (sKamarMandi.getVisibility() == View.VISIBLE ){
                                    if (sKamarMandi.getSelectedItemPosition() != 0)
                                        paramsKategori.add(new BasicNameValuePair("kamar_mandi", String.valueOf(sKamarMandi.getSelectedItemPosition())));
                                    kamarMandi = String.valueOf(sKamarMandi.getSelectedItemPosition());
                                }

                            }
                        }

                        if (urlSubKategori.equals("merk")) {
                            if (getIntent().getStringExtra("seo_kategori").equals("mobil")) {
                                if (sTransmisi.getSelectedItemPosition() != 0)
                                    paramsKategori.add(new BasicNameValuePair("transmisi", sTransmisi.getSelectedItem().toString().toLowerCase()));
                                transmisi = String.valueOf(sTransmisi.getSelectedItemPosition());
                            }

                            if (sFilter.getSelectedItemPosition() != 0)
                                paramsKategori.add(new BasicNameValuePair("filter", sFilter.getSelectedItem().toString().toLowerCase()));
                            filter = String.valueOf(sFilter.getSelectedItemPosition());

                            if (sTipeKendaraan.getSelectedItemPosition()!=0)
                                paramsKategori.add(new BasicNameValuePair("tipe", tipeKendaraan[((int) sTipeKendaraan.getSelectedItemId())]));
                            tipe = String.valueOf(sTipeKendaraan.getSelectedItemPosition());

                            if (sTahun.getSelectedItemPosition()!=0)
                                paramsKategori.add(new BasicNameValuePair("tahun", sTahun.getSelectedItem().toString().toLowerCase()));
                            tahun = String.valueOf(sTahun.getSelectedItemPosition());

                        }
                        cb = new class_bantuan();
                        detail_url= cb.merge(detail_url, paramsKategori);
                        baca_topmenu(detail_url, class_bantuan.api_key);
                        switch (getIntent().getStringExtra("seo_kategori").toLowerCase()){
                            case "motor":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_motor(sort, kategori2, filter, tipe, hargaAwal, hargaAkhir, tahun, device_id);
                                break;
                            case "mobil":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_mobil(sort, kategori2, filter, tipe, hargaAwal, hargaAkhir, tahun, device_id, transmisi);
                                break;
                            case "rumah-tangga":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_rumah_tangga(sort, kategori2, device_id);
                                break;
                            case "properti":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_properti(sort, kategori2, filter, kamarTidur, kamarMandi, sertifikasi, hargaAwal, hargaAkhir, lantai, luasBangunanAwal, luasBangunanAkhir,luasTanahAwal,luasTanahAkhir,device_id);
                                break;
                            case "olahraga":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_olahraga(sort,kategori2,device_id);
                                break;
                            case "elektronik":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_elektronik(sort,kategori2,device_id);
                                break;
                            case "travel":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_travel(sort,kategori2,device_id);
                                break;
                            case "kuliner":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_kuliner(sort,kategori2,device_id);
                                break;
                            case "fashion":
                                new class_prosessql(activity_sub_kategori.this).insert_filter_fashion(sort,kategori2,device_id);
                                break;
                            default:
                                break;
                        }
                        Log.e("device_id_submit", device_id);

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    /*private void setup_filter() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("TERBARU",R.drawable.ic_terbaru, R.color.color_ah_bot1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("TERMURAH",R.drawable.ic_termurah, R.color.color_ah_bot2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("FILTERING", R.drawable.ic_filter, R.color.color_ah_bot3);

        bottom_filter.addItem(item1);
        bottom_filter.addItem(item2);
        bottom_filter.addItem(item3);
        //bottom_filter.setColored(true);
        bottom_filter.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottom_filter.setTitleTextSize(16, 13);
        bottom_filter.setCurrentItem(0);
        bottom_filter.setAccentColor(Color.parseColor("#747474"));
        bottom_filter.setInactiveColor(Color.parseColor("#747474"));
        bottom_filter.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                String sort="";
                if(position==0){
                    sort="terbaru";
                }else if(position==1){
                    sort="termurah";
                }
                if(viewPager.getCurrentItem()==0){
                    bottom_filter.setAccentColor(Color.parseColor("#ff9800"));
                    ((fragment_iklanbekas) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(0)).ambil_data("bekas", getIntent().getStringExtra("kategori"), "semua", "semua", sort);
                    viewPager.getAdapter().notifyDataSetChanged();
                }else if(viewPager.getCurrentItem()==1){
                    bottom_filter.setAccentColor(Color.parseColor("#ff9800"));
                    ((fragment_iklanbaru) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(1)).ambil_data("baru", getIntent().getStringExtra("kategori"), "semua", "semua",sort);
                    viewPager.getAdapter().notifyDataSetChanged();
                    Log.e("v_pager",String.valueOf(viewPager.getCurrentItem()));
                }else if(viewPager.getCurrentItem()==2){
                    bottom_filter.setAccentColor(Color.parseColor("#ff9800"));
                    ((fragment_iklansemua) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(2)).ambil_data("semua", getIntent().getStringExtra("kategori"), "semua", "semua",sort);
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                setupTabIcons();
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
    private View.OnClickListener clicktabmenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            class_bantuan.menu_kategori = "topmenu";
            String main_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("seo_kategori") + "/";
            detail_url = main_url;
            set_warna();
            index_menuterakhir=getindex(arrmenu,String.valueOf(v.getTag()));
            tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));

            paramsKategori = new LinkedList<NameValuePair>();

            cb = new class_bantuan();
            if(String.valueOf(v.getTag()) == "Top Shop")
                paramsKategori.add(new BasicNameValuePair("premium", "top-shop"));
            else if(String.valueOf(v.getTag()) == "Rekomendasi")
                paramsKategori.add(new BasicNameValuePair("premium", "recommended"));
            else if(String.valueOf(v.getTag()) == "Terlaris")
                paramsKategori.add(new BasicNameValuePair("premium", "terlaris"));

            detail_url = cb.merge(detail_url, paramsKategori);
            //fab.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            baca_topmenu(detail_url, class_bantuan.api_key);
        }
    };
    public  void  set_warna(){
        for(int i=0;i<arrmenu.length;i++)
        {
            tv1[i].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu));
        }
    }
    public int getindex(String[] array, String value) {
        int j = 0;
        for(int i=0; i<array.length; i++)
        {
            if(array[i] == value)
            {
                j=i;
                break;
            }
        }

        return j;
    }


    @Override
    public void onBackPressed() {
        if(kateg_utama.getText().toString().indexOf('>') >= 0){
            class_bantuan.menu_kategori = "sub_kategori";
            String[]tes=kateg_utama.getText().toString().split(">>");
            kateg_utama.setText(tes[0]);
            sub_kategori1(class_bantuan.base_url + "sub_kategori_satu", class_bantuan.api_key, getIntent().getStringExtra("id_kategori"));
        } else if (class_bantuan.menu_kategori.equals("topmenu")){
            class_bantuan.menu_kategori = "";
            finish();
            Intent intent = new Intent(this, activity_sub_kategori.class);
            intent.putExtra("id_kategori",getIntent().getStringExtra("id_kategori"));
            intent.putExtra("kategori",getIntent().getStringExtra("kategori"));
            intent.putExtra("url_kategori",getIntent().getStringExtra("url_kategori"));
            intent.putExtra("seo_kategori",getIntent().getStringExtra("seo_kategori"));
            startActivity(intent);
        } else {
            finish();
            Intent intent = new Intent(activity_sub_kategori.this, activity_main.class);
            startActivity(intent);
        }
    }

    public void baca_topmenu(String url,String api_key) {
        Log.e("Respon URL", url);
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        iklan = new ArrayList<>();
        client = new AsyncHttpClient();
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("Respon Kategori", new String(responseBody));
                try {
                    JSONObject job = new JSONObject(new String(responseBody));
                    JSONArray jarr = new JSONArray(String.valueOf(job.getJSONArray("iklan")));
                    jarrSubkategori = new JSONArray(String.valueOf(job.getJSONArray("subkategori")));
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        iklan.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getJSONObject("kota").getString("nama_area"),
                                jobj.getString("foto").replace(" ","%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity_sub_kategori.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(activity_sub_kategori.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(iklan);
                    mRecyclerView.setAdapter(class_adapterlistitem);
                    navFilter.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("url_kategori")).into(img);

                    if (sub2Kategori != null){
                        tvKategori1.setText(sSubKategori.getSelectedItem().toString());
                        if (sFilter.getSelectedItemPosition() == 0)
                            tvKategori2.setText("Semua");
                        else
                            tvKategori2.setText(sFilter.getSelectedItem().toString());
                    }else { //topmenu
                        tvKategori1.setText(getIntent().getStringExtra("kategori"));
                        tvKategori2.setText("Semua");
                    }
                }
                catch (JSONException ex)
                {
                    mDialog.dismiss();
                    Log.e("JSONException", ex.toString());
                }
                catch (Exception ex)
                {
                    mDialog.dismiss();
                    Log.e("tes exception", ex.toString());
                    dialog_gagal();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal();
                //class_prosessql proses=new class_prosessql(getActivity());
                //proses.baca_menuutama();
            }
        });
    }
    public void dialog_gagal(){
        snackBar = new SnackBar.Builder(activity_sub_kategori.this)
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

    /*private Drawable get_drawable(int draw){
        Drawable drawable = getResources().getDrawable(draw);
        //Drawable drawable1 = new ScaleDrawable(drawable, 0, 2, 2).getDrawable();

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 32, 32, false);
        Drawable drawable1= new BitmapDrawable(getResources(), bitmapResized);

        drawable1.setBounds(0, 0, 32, 32);
        return drawable1;
    }
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabOne.setText("BEKAS");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabTwo.setText("BARU");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabThree.setText("SEMUA");
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        class_bantuan.search_word="";
        class_bantuan.search_kota="";
        class_bantuan.search_seo_kategori="";
        class_bantuan.menu_kategori=getIntent().getStringExtra("kategori");
        adapter.addFrag(new fragment_iklanbekas().instance_menu("bekas", getIntent().getStringExtra("kategori"), "semua", "semua", "kosong"), "ONE");
        adapter.addFrag(new fragment_iklanbaru().instance_menu("baru",getIntent().getStringExtra("kategori"), "semua", "semua","kosong"), "TWO");
        adapter.addFrag(new fragment_iklansemua().instance_menu("semua",getIntent().getStringExtra("kategori"),"semua","semua","kosong"), "THREE");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager_search(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        class_bantuan.search_word=getIntent().getStringExtra("word");
        class_bantuan.search_kota=getIntent().getStringExtra("kota");
        class_bantuan.search_seo_kategori=getIntent().getStringExtra("seo_kategori");
        class_bantuan.menu_kategori="";
        adapter.addFrag(new fragment_iklanbekas().newInstance(getIntent().getStringExtra("word"),getIntent().getStringExtra("kota"),getIntent().getStringExtra("seo_kategori")), "ONE");
        adapter.addFrag(new fragment_iklanbaru().newInstance(getIntent().getStringExtra("word"),getIntent().getStringExtra("kota"),getIntent().getStringExtra("seo_kategori")), "TWO");
        adapter.addFrag(new fragment_iklansemua().newInstance(getIntent().getStringExtra("word"), getIntent().getStringExtra("kota"),getIntent().getStringExtra("seo_kategori")), "THREE");
        viewPager.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
    void sub_kategori2(String url,String key,String id_kategori){
        Log.e("masuk",  "subkategori2");
        RequestParams params = new RequestParams();
        list.setAdapter(null);
        params.put("key", key);
        params.put("id_kategori", id_kategori);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("json sub", respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String[] item_kategori = new String[jarr.length() + 1];
                    String[] id_kategori = new String[jarr.length() + 1];
                    final String[] status = new String[jarr.length() + 1];
                    item_kategori[0] = "<b>Semua " + kateg_utama.getText().toString().split(">>")[1] + "</b>";
                    id_kategori[0] = "0";
                    status[0] = "kosong";
                    int j = 1;
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        item_kategori[j] = jobj.getString("sub2_kategori");
                        id_kategori[j] = jobj.getString("id_sub2_kategori");
                        status[j] = jobj.getString("ada");
                        j += 1;
                    }
                    mDialog.dismiss();
                    list.setAdapter(new class_sub_kategori(activity_sub_kategori.this, id_kategori, item_kategori, status));

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                String[] arr_sub = kateg_utama.getText().toString().split(">>");
                                Intent intent = new Intent(activity_sub_kategori.this, activity_listitem.class);
                                intent.putExtra("kategori", arr_sub[0]);
                                intent.putExtra("sub1_kategori", arr_sub[1]);
                                intent.putExtra("sub2_kategori", "semua");
                                intent.putExtra("subKategori1", item_kategori1);
                                intent.putExtra("url_kategori", url_image);
                                startActivity(intent);
                            } else {
                                TextView tv_list = (TextView) view.findViewById(R.id.txtlist);
                                String[] arr_sub = kateg_utama.getText().toString().split(">>");
                                Intent intent = new Intent(activity_sub_kategori.this, activity_listitem.class);
                                intent.putExtra("kategori", arr_sub[0]);
                                intent.putExtra("sub1_kategori", arr_sub[1]);
                                intent.putExtra("sub2_kategori", tv_list.getText().toString());
                                intent.putExtra("subKategori1", item_kategori1);
                                intent.putExtra("url_kategori", url_image);
                                Log.e("intent nya", "sub1 : " + arr_sub[1] + " sub2 : " + tv_list.getText().toString());
                                startActivity(intent);
                            }

                        }
                    });
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    dialog_gagal("Gagal ambil data");
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal("Gagal ambil data");
            }
        });
    }

    void sub_kategori1(String url,String key,String id_kategori){
        RequestParams params = new RequestParams();
        params.put("key", key);
        params.put("id_kategori", id_kategori);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_sub_kategori.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_sub_kategori.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("json sub", respon_server);
                try {
                    if(respon_server==null||respon_server.equals("")){
                        item_kategori1 = new String[1];
                        id_kat = new String[1];
                        status=new String[1];
                        item_kategori1[0] = "<b>Semua "+kateg_utama.getText().toString()+"</b>";
                        id_kat[0] ="0";
                        status[0]="kosong";
                    }else{
                        jarrSubKategori1 = new JSONArray(respon_server);

                        item_kategori1 = new String[jarrSubKategori1.length()+1];
                        id_kat = new String[jarrSubKategori1.length()+1];
                        status=new String[jarrSubKategori1.length()+1];
                        item_kategori1[0] = "<b>Semua "+kateg_utama.getText().toString()+"</b>";
                        id_kat[0] ="0";
                        status[0]="kosong";
                        int j=1;
                        for (int i = 0; i < jarrSubKategori1.length(); ++i) {
                            JSONObject jobj = jarrSubKategori1.getJSONObject(i);
                            item_kategori1[j] = jobj.getString("sub1_kategori");
                            id_kat[j] = jobj.getString("id_sub1_kategori");
                            status[j]=jobj.getString("ada");
                            j+=1;
                        }
                    }


                    mDialog.dismiss();
                    list.setAdapter(new class_sub_kategori(activity_sub_kategori.this, id_kat, item_kategori1,status));

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(position==0){
                                Intent intent = new Intent(activity_sub_kategori.this, activity_listitem.class);
                                intent.putExtra("kategori", kateg_utama.getText().toString());
                                intent.putExtra("sub1_kategori", "semua");
                                intent.putExtra("sub2_kategori", "semua");
                                intent.putExtra("subKategori1", item_kategori1);
                                intent.putExtra("url_kategori", url_image);
                                startActivity(intent);
                            }
                            else{
                                TextView tv_id = (TextView) view.findViewById(R.id.txtid);
                                TextView tv_list = (TextView) view.findViewById(R.id.txtlist);
                                ImageView img_ada = (ImageView) view.findViewById(R.id.ada);
                                if (img_ada.getVisibility() == View.VISIBLE) {
                                    kateg_utama.setText(kateg_utama.getText().toString() + ">>" + tv_list.getText().toString());
                                    sub_kategori2(class_bantuan.base_url + "sub_kategori_dua", class_bantuan.api_key, tv_id.getText().toString());
                                } else if (img_ada.getVisibility() == View.INVISIBLE) {
                                    Intent intent = new Intent(activity_sub_kategori.this, activity_listitem.class);
                                    intent.putExtra("kategori", kateg_utama.getText().toString());
                                    intent.putExtra("sub1_kategori", tv_list.getText().toString());
                                    intent.putExtra("sub2_kategori", "semua");
                                    intent.putExtra("subKategori1", item_kategori1);
                                    intent.putExtra("url_kategori", url_image);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                } catch (JSONException ex) {
                    Log.e("act_sub_kategori",ex.toString());
                    mDialog.dismiss();
                    dialog_gagal("Gagal ambil data");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("act_sub_kategori",String.valueOf(statusCode));
                mDialog.dismiss();
                dialog_gagal("Gagal ambil data");
            }
        });
    }
    public void setuptoolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WIKILOKA");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(activity_sub_kategori.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik","tutup");
                    }
                })
                .withMessage(pesan)
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }
}
