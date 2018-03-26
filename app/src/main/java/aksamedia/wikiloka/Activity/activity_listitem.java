package aksamedia.wikiloka.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.paginate.Paginate;
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
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class activity_listitem extends AppCompatActivity implements Paginate.Callbacks {
    private LinearLayoutManager mLayoutManager;
    //private RecyclerView mRecyclerView;
    private aksamedia.wikiloka.Class.class_adapterlistitem mAdapter;
    private AsyncHttpClient client;
    private ArrayList<String> m_kota;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialogObject;
    String wilayah = "";
    private ArrayList<String> i_kota;
    private SimpleArcDialog mDialog;
    private ArrayList<class_iklan> class_barang;
    private Toolbar toolbar;
    private LinearLayout top_menu;
    private Spinner spinner;
    int index_menuterakhir;
    private TextView[] tv, tv1;
    String[] arrmenu = {"Semua", "Top Shop", "Rekomendasi", "Terlaris"};
    private MaterialSearchView searchView;
    private SnackBar snackbar;
    private HorizontalScrollView hsv;
    private String respon_server;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RecyclerView mRecyclerView;
    private ImageView img;
    private TextView tv_kategori1, tv_kategori2, tv_filter;
    private SwipeRefreshLayout swipe_refresh;
    private LinearLayoutManager llm;
    private boolean mLoading = false;
    class_adapterlistitem class_adapterlistitem;
    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;
    private String status_paging="ada";
    private int start;
    private ProgressBar p_bar;
    class_bantuan cb;
    List<NameValuePair> paramsKategori;
    ArrayList<class_iklan> iklan;
    SnackBar snackBar;
    String[] subKategori, sub2Kategori, seoKategori, temp, harga, tipeKendaraan;
    Spinner sSort,sSubKategori,sMerk,sFilter,sTipeKendaraan,sTransmisi,sTahun,sKamarTidur,sKamarMandi,sSertifikasi,sLantai,sHargaAwal,sHargaAkhir,sLBAwal,sLBAkhir,sLTAwal,sLTAkhir;
    LinearLayout filterLuasTanah,filterLuasBangunan,filterHarga;
    TextView bUbah, bBatal;

    Spinner sSort2,sSubKategori2,sMerk2,sFilter2,sTipeKendaraan2,sTransmisi2,sTahun2,sKamarTidur2,sKamarMandi2,sSertifikasi2,sLantai2,sHargaAwal2,sHargaAkhir2,sLBAwal2,sLBAkhir2,sLTAwal2,sLTAkhir2;
    LinearLayout filterLuasTanah2,filterLuasBangunan2,filterHarga2;
    TextView bUbah2, bBatal2;

    ArrayAdapter<String> adapter;
    JSONArray jarrSubkategori, jarrSub2kategori, jarrHarga;
    String detail_url, urlSubKategori="";
    List<String> properti;
    Context context = this;
    String device_id;
    TelephonyManager tMgr2;
    String[] filter_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        top_menu = (LinearLayout) findViewById(R.id.top_menu);
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        img = (ImageView) findViewById(R.id.img);
        tv_kategori1 = (TextView) findViewById(R.id.tv_kategori1);
        tv_kategori2 = (TextView) findViewById(R.id.tv_kategori2);
        tv_filter = (TextView) findViewById(R.id.tv_filter);
        p_bar=(ProgressBar) findViewById(R.id.progressBar);

        paramsKategori = new LinkedList<NameValuePair>();

        tv = new TextView[arrmenu.length];
        tv1 = new TextView[arrmenu.length];
        for (int i = 0; i < arrmenu.length; ++i) {
            View child1 = getLayoutInflater().inflate(R.layout.custom_topmenu, null);
            tv[i] = (TextView) child1.findViewById(R.id.texttop2);
            tv[i].setTag(String.valueOf(arrmenu[i]));
            tv[i].setText("   " + arrmenu[i].toUpperCase() + "   ");
            tv[i].findViewWithTag(String.valueOf(arrmenu[i])).setOnClickListener(clicktabmenu);
            tv1[i] = (TextView) child1.findViewById(R.id.menutop2);
            tv1[i].setTag(String.valueOf(arrmenu[i]));
            tv1[i].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu));//.getColor(R.color.colorPrimary));
            tv1[i].setGravity(Gravity.BOTTOM);

            top_menu.addView(child1);
        }
        setuptoolbar();
        tv1[0].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
        index_menuterakhir = 0;

        Log.e("act list", getIntent().getStringExtra("sub2_kategori"));
        if (getIntent().getStringExtra("sub2_kategori").equals("semua")) {
            tv_kategori1.setText(getIntent().getStringExtra("kategori"));
            tv_kategori2.setText(getIntent().getStringExtra("sub1_kategori"));
        } else {
            tv_kategori1.setText(getIntent().getStringExtra("sub1_kategori"));
            tv_kategori2.setText(getIntent().getStringExtra("sub2_kategori"));
        }
        Picasso.with(this).load(getIntent().getStringExtra("url_kategori")).into(img);
        ambil_data("semua",
                getIntent().getStringExtra("kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                getIntent().getStringExtra("sub1_kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                getIntent().getStringExtra("sub2_kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                "semua", "kosong", "3", "0");
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
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
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0;
                status_paging = "ada";
                refresh_data("semua",
                        getIntent().getStringExtra("kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                        getIntent().getStringExtra("sub1_kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                        getIntent().getStringExtra("sub2_kategori").toLowerCase().replace(" ", "-").replace("&", ""),
                        "semua", "kosong", "3", "0");
            }
        });
        start=0;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                start +=3;
                if(status_paging.equals("ada")) {
                    //paging_data("semua", getIntent().getStringExtra("kategori").toLowerCase().replace(" ", "-").replace("&", ""), getIntent().getStringExtra("sub1_kategori").toLowerCase().replace(" ", "-").replace("&", ""), getIntent().getStringExtra("sub2_kategori").toLowerCase().replace(" ", "-").replace("&", ""), "semua", "kosong", "3", String.valueOf(start));
                }else{

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
            }
        });

        setfilter();

        tv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] subKategori1 = getIntent().getStringArrayExtra("subKategori1");

                tMgr2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String username = new class_prosessql(activity_listitem.this).get_username();
                if (username.equals("kosong")){
                    device_id=String.valueOf(tMgr2.getDeviceId());
                } else{
                    device_id=username;
                }

                filter_db = new class_prosessql(activity_listitem.this).get_history("tbl_filter_" + getIntent().getStringExtra("kategori").toLowerCase().replaceAll(" ", "_"), device_id);
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
                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, subKategori);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sSort.setAdapter(adapter);
                sSort.setVisibility(View.VISIBLE);
                if(filter_db[1]!=null && !filter_db[1].equals("-"))
                    sSort.setSelection(Integer.parseInt(filter_db[1]));

                //SUB KATEGORI 1
                subKategori = new String[subKategori1.length];
                subKategori[0] = "Semua kategori";

                for (int i = 1; i < subKategori1.length; i++) {
                    subKategori[i] = subKategori1[i];
                }
                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, subKategori);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sSubKategori.setAdapter(adapter);
                sSubKategori.setVisibility(View.VISIBLE);
                if(filter_db[2]!=null && !filter_db[2].equals("-"))
                    sSubKategori.setSelection(Integer.parseInt(filter_db[2]));
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
                        detail_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("kategori").toLowerCase() + "/" + urlSubKategori + "/";

                        properti = Arrays.asList("rumah", "apartmen", "indekos", "bangunan-komersil", "tanah");

                        //MERK//
                        if (urlSubKategori.equals("merk")) {
                            if(getIntent().getStringExtra("kategori").toLowerCase().equals("mobil")){
                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, class_bantuan.transmisi);
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
                                    mDialog = new SimpleArcDialog(activity_listitem.this);
                                    mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, sub2Kategori);
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
                                                            mDialog = new SimpleArcDialog(activity_listitem.this);
                                                            mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
                                                            mDialog.show();
                                                        }

                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                            String key_kategori = "";
                                                            JSONObject job = null;
                                                            try {
                                                                job = new JSONObject(new String(responseBody));
                                                                if (getIntent().getStringExtra("kategori").toLowerCase().equals("mobil"))
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
                                                                    tipeKendaraan[i + 1] = jobj.getString("id_tipe_" + getIntent().getStringExtra("kategori").toLowerCase());
                                                                }

                                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, harga);
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
                                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                    mDialog = new SimpleArcDialog(activity_listitem.this);
                                    mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, harga);
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
                                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, class_bantuan.sertifikasi);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sSertifikasi.setAdapter(adapter);
                                            sSertifikasi.setVisibility(View.VISIBLE);
                                            if(filter_db[6]!=null && !filter_db[6].equals("-"))
                                                sSertifikasi.setSelection(Integer.parseInt(filter_db[6]));

                                            if (!urlSubKategori.equals("tanah")) {
                                                sub2Kategori = new String[]{"Pilih " + urlSubKategori, "Dijual", "Disewakan"};
                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, sub2Kategori);
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
                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                            if (getIntent().getStringExtra("kategori").toLowerCase().equals("mobil")) {
                                if (sTransmisi.getSelectedItemPosition() != 0)
                                    paramsKategori.add(new BasicNameValuePair("transmisi", sTransmisi.getSelectedItem().toString().toLowerCase()));
                                transmisi = String.valueOf(sTransmisi.getSelectedItemPosition());
                            }

                            if (sFilter.getSelectedItemPosition() != 0)
                                paramsKategori.add(new BasicNameValuePair("filter", sFilter.getSelectedItem().toString().toLowerCase()));
                            filter = String.valueOf(sFilter.getSelectedItemPosition());

                            if (sTipeKendaraan.getSelectedItemPosition() != 0)
                                paramsKategori.add(new BasicNameValuePair("tipe", tipeKendaraan[((int) sTipeKendaraan.getSelectedItemId())]));
                            tipe = String.valueOf(sTipeKendaraan.getSelectedItemPosition());

                            if (sTahun.getSelectedItemPosition() != 0)
                                paramsKategori.add(new BasicNameValuePair("tahun", sTahun.getSelectedItem().toString().toLowerCase()));
                            tahun = String.valueOf(sTahun.getSelectedItemPosition());
                        }
                        cb = new class_bantuan();
                        detail_url= cb.merge(detail_url, paramsKategori);
                        baca_topmenu(detail_url, class_bantuan.api_key);
                        switch (getIntent().getStringExtra("kategori").toLowerCase()){
                            case "motor":
                                new class_prosessql(activity_listitem.this).insert_filter_motor(sort, kategori2, filter, tipe, hargaAwal, hargaAkhir, tahun, device_id);
                                break;
                            case "mobil":
                                new class_prosessql(activity_listitem.this).insert_filter_mobil(sort, kategori2, filter, tipe, hargaAwal, hargaAkhir, tahun, device_id, transmisi);
                                break;
                            case "rumah-tangga":
                                new class_prosessql(activity_listitem.this).insert_filter_rumah_tangga(sort, kategori2, device_id);
                                break;
                            case "properti":
                                new class_prosessql(activity_listitem.this).insert_filter_properti(sort, kategori2, filter, kamarTidur, kamarMandi, sertifikasi, hargaAwal, hargaAkhir, lantai, luasBangunanAwal, luasBangunanAkhir,luasTanahAwal,luasTanahAkhir,device_id);
                                break;
                            case "olahraga":
                                new class_prosessql(activity_listitem.this).insert_filter_olahraga(sort,kategori2,device_id);
                                break;
                            case "elektronik":
                                new class_prosessql(activity_listitem.this).insert_filter_elektronik(sort,kategori2,device_id);
                                break;
                            case "travel":
                                new class_prosessql(activity_listitem.this).insert_filter_travel(sort,kategori2,device_id);
                                break;
                            case "kuliner":
                                new class_prosessql(activity_listitem.this).insert_filter_kuliner(sort,kategori2,device_id);
                                break;
                            case "fashion":
                                new class_prosessql(activity_listitem.this).insert_filter_fashion(sort,kategori2,device_id);
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

    private void setfilter() {
        /////
        String[] subKategori1 = getIntent().getStringArrayExtra("subKategori1");

        tMgr2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String username = new class_prosessql(activity_listitem.this).get_username();
        if (username.equals("kosong")){
            device_id=String.valueOf(tMgr2.getDeviceId());
        } else{
            device_id=username;
        }

        filter_db = new class_prosessql(activity_listitem.this).get_history("tbl_filter_" + getIntent().getStringExtra("kategori").toLowerCase().replaceAll(" ", "_"), device_id);

        sSort= (Spinner) findViewById(R.id.sSort);
        sSubKategori = (Spinner) findViewById(R.id.sSubKategori);
        sMerk = (Spinner) findViewById(R.id.sMerk);
        sFilter = (Spinner) findViewById(R.id.sFilter);
        sTipeKendaraan = (Spinner) findViewById(R.id.sTipeKendaraan);
        sTransmisi = (Spinner) findViewById(R.id.sTransmisi);
        sTahun= (Spinner) findViewById(R.id.sTahun);
        sKamarTidur = (Spinner) findViewById(R.id.sKamarTidur);
        sKamarMandi= (Spinner) findViewById(R.id.sKamarMandi);
        sSertifikasi= (Spinner) findViewById(R.id.sSertifikasi);
        sLantai= (Spinner) findViewById(R.id.sLantai);
        filterLuasTanah= (LinearLayout) findViewById(R.id.filterLuasTanah);
        filterLuasBangunan= (LinearLayout) findViewById(R.id.filterLuasBangunan);
        filterHarga= (LinearLayout) findViewById(R.id.filterHarga);
        sHargaAwal= (Spinner) findViewById(R.id.sHargaAwal);
        sHargaAkhir= (Spinner) findViewById(R.id.sHargaAkhir);
        sLTAwal= (Spinner) findViewById(R.id.sLTAwal);
        sLTAkhir= (Spinner) findViewById(R.id.sLTAkhir);
        sLBAwal= (Spinner) findViewById(R.id.sLBAwal);
        sLBAkhir= (Spinner) findViewById(R.id.sLBAkhir);

        bBatal = (TextView) findViewById(R.id.bBatal);
        bUbah = (TextView) findViewById(R.id.bUbah);

        paramsKategori = new LinkedList<NameValuePair>();

        //SORT HARGA
        subKategori = new String[]{"Semua urutan", "Termurah", "Terbaru"};
        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, subKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSort.setAdapter(adapter);
        sSort.setVisibility(View.VISIBLE);
        if(filter_db[1]!=null && !filter_db[1].equals("-"))
            sSort.setSelection(Integer.parseInt(filter_db[1]));

        //SUB KATEGORI 1
        subKategori = new String[subKategori1.length];
        subKategori[0] = "Semua kategori";

        for (int i = 1; i < subKategori1.length; i++) {
            subKategori[i] = subKategori1[i];
        }
        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, subKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSubKategori.setAdapter(adapter);
        sSubKategori.setVisibility(View.VISIBLE);
        if(filter_db[2]!=null && !filter_db[2].equals("-"))
            sSubKategori.setSelection(Integer.parseInt(filter_db[2]));
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
                detail_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("kategori").toLowerCase() + "/" + urlSubKategori + "/";

                properti = Arrays.asList("rumah", "apartmen", "indekos", "bangunan-komersil", "tanah");

                //MERK//
                if (urlSubKategori.equals("merk")) {
                    if(getIntent().getStringExtra("kategori").toLowerCase().equals("mobil")){
                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, class_bantuan.transmisi);
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
                            mDialog = new SimpleArcDialog(activity_listitem.this);
                            mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
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
                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, sub2Kategori);
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
                                                    mDialog = new SimpleArcDialog(activity_listitem.this);
                                                    mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
                                                    mDialog.show();
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    String key_kategori = "";
                                                    JSONObject job = null;
                                                    try {
                                                        job = new JSONObject(new String(responseBody));
                                                        if (getIntent().getStringExtra("kategori").toLowerCase().equals("mobil"))
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
                                                            tipeKendaraan[i + 1] = jobj.getString("id_tipe_" + getIntent().getStringExtra("kategori").toLowerCase());
                                                        }

                                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, harga);
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
                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                            mDialog = new SimpleArcDialog(activity_listitem.this);
                            mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
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
                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, harga);
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
                                            adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, class_bantuan.sertifikasi);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sSertifikasi.setAdapter(adapter);
                                    sSertifikasi.setVisibility(View.VISIBLE);
                                    if(filter_db[6]!=null && !filter_db[6].equals("-"))
                                        sSertifikasi.setSelection(Integer.parseInt(filter_db[6]));

                                    if (!urlSubKategori.equals("tanah")) {
                                        sub2Kategori = new String[]{"Pilih " + urlSubKategori, "Dijual", "Disewakan"};
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, sub2Kategori);
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                        adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                                adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
                                    adapter = new ArrayAdapter<String>(activity_listitem.this, android.R.layout.simple_spinner_item, temp);
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
        /////
    }

    public void setuptoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WIKILOKA");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
            String main_url = class_bantuan.base_url + "getIklanKategori/" + getIntent().getStringExtra("kategori").toLowerCase() + "/";
            detail_url = main_url;
            set_warna();
            index_menuterakhir = getindex(arrmenu, String.valueOf(v.getTag()));
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
            mRecyclerView.setVisibility(View.VISIBLE);
            baca_topmenu(detail_url, class_bantuan.api_key);
        }
    };

    public void set_warna() {
        for (int i = 0; i < arrmenu.length; i++) {
            tv1[i].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu));
        }
    }

    public int getindex(String[] array, String value) {
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                j = i;
                break;
            }
        }

        return j;
    }
    int i=0;
    @Override
    public synchronized void onLoadMore() {
        loading = true;
    }

    @Override
    public synchronized boolean isLoading() {
        return loading; // Return boolean weather data is already loading or not
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == 3; // If all pages are loaded return true
    }
    public void paging_data(String jenis, String kategori, String sub1_kategori, String sub2_kategori, String kota, String sort,String limit1,String limit2) {
        //class_barang = new ArrayList<class_iklan>();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        if (sort != "kosong") {
            params.put("sort", sort);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("jenis", jenis);
        params.put("kategori", kategori);
        params.put("sub1_kategori", sub1_kategori);
        params.put("sub2_kategori", sub2_kategori);
        params.put("kota", kota);
        params.put("limit1",limit1);
        params.put("limit2",limit2);
        client.post(class_bantuan.base_url + "kategori", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                p_bar.setVisibility(View.VISIBLE);
                p_bar.setIndeterminate(true);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("respon semua", respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    if(jarr.length()<1){
                        status_paging="kosong";
                        p_bar.setVisibility(View.INVISIBLE);
                    }else{

                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            class_barang.add(new class_iklan(
                                    jobj.getString("id_iklan"),
                                    "FAVORIT",
                                    jobj.getString("judul_iklan"),
                                    jobj.getString("harga_iklan"),
                                    jobj.getJSONObject("kota").getString("nama_area"),
                                    jobj.getString("photo").replace(" ", "%20"),
                                    jobj.getString("seo_iklan")));
                        }
                        class_adapterlistitem.notifyDataSetChanged();
                        p_bar.setVisibility(View.INVISIBLE);
                    }
                    
                } catch (JSONException ex) {
                    loading=false;
                    Log.e("JSONException", ex.toString());
                    p_bar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                loading=false;
                Log.e("Failure", String.valueOf(statusCode));
                p_bar.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void ambil_data(String jenis, String kategori, String sub1_kategori, String sub2_kategori, String kota, String sort,String limit1,String limit2) {
        class_barang = new ArrayList<class_iklan>();
        mDialog = new SimpleArcDialog(activity_listitem.this);
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        if (sort != "kosong") {
            params.put("sort", sort);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("jenis", jenis);
        params.put("kategori", kategori);
        params.put("sub1_kategori", sub1_kategori);
        params.put("sub2_kategori", sub2_kategori);
        params.put("kota", kota);
        params.put("limit1",limit1);
        params.put("limit2",limit2);
        Log.e("ambil_data", "ambil_data");
        client.post(class_bantuan.base_url + "kategori", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("respon semua", respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);


                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getJSONObject("kota").getString("nama_area"),
                                jobj.getString("photo").replace(" ", "%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity_listitem.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    llm = new LinearLayoutManager(activity_listitem.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem = new class_adapterlistitem(class_barang);
                    mRecyclerView.setAdapter(class_adapterlistitem);
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                Log.e("Failure", String.valueOf(statusCode));
            }
        });
    }
    public void refresh_data(String jenis, String kategori, String sub1_kategori, String sub2_kategori, String kota, String sort,String limit1,String limit2) {
        class_barang.clear();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        if (sort != "kosong") {
            params.put("sort", sort);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("jenis", jenis);
        params.put("kategori", kategori);
        params.put("sub1_kategori", sub1_kategori);
        params.put("sub2_kategori", sub2_kategori);
        params.put("kota", kota);
        params.put("limit1",limit1);
        params.put("limit2", limit2);
        client.post(class_bantuan.base_url + "kategori", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("respon semua", respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    if(jarr.length()<1){
                        status_paging="kosong";
                    }else{
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            class_barang.add(new class_iklan(
                                    jobj.getString("id_iklan"),
                                    "FAVORIT",
                                    jobj.getString("judul_iklan"),
                                    jobj.getString("harga_iklan"),
                                    jobj.getJSONObject("kota").getString("nama_area"),
                                    jobj.getString("photo").replace(" ", "%20"),
                                    jobj.getString("seo_iklan")));
                        }
                    }
                    class_adapterlistitem.notifyDataSetChanged();
                    swipe_refresh.setRefreshing(false);
                } catch (JSONException ex) {
                    swipe_refresh.setRefreshing(false);
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                swipe_refresh.setRefreshing(false);
                Log.e("Failure", String.valueOf(statusCode));
            }
        });
    }

    /*@Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(activity_listitem.this, activity_main.class);
        startActivity(intent);
    }*/

    public void baca_topmenu(String url,String api_key) {
        Log.e("Respon URL", url);
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        iklan = new ArrayList<>();
        client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_listitem.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_listitem.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("Respon Kategori", new String(responseBody));
                try {
                    JSONObject job = new JSONObject(new String(responseBody));
                    JSONArray jarr = new JSONArray(String.valueOf(job.getJSONArray("iklan")));
                    jarrSubkategori = new JSONArray(String.valueOf(job.getJSONArray("subkategori")));
                    //jarrSub2kategori = new JSONArray(String.valueOf(job.getJSONArray("sub2kategori")));
                    //JSONArray jarr = new JSONArray(new String(responseBody));
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        iklan.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getJSONObject("kota").getString("nama_area"),
                                jobj.getString("foto").replace(" ", "%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity_listitem.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(activity_listitem.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(iklan);
                    mRecyclerView.setAdapter(class_adapterlistitem);
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException", ex.toString());
                } catch (Exception ex) {
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
        snackBar = new SnackBar.Builder(activity_listitem.this)
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