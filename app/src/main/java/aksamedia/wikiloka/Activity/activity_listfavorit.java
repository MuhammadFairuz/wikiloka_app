package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aksamedia.wikiloka.Class.class_adapterfavorit;
import aksamedia.wikiloka.Class.class_adapterlistitem;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_favorit;
import cz.msebera.android.httpclient.Header;

public class activity_listfavorit extends AppCompatActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private class_adapterlistitem mAdapter;
    private AsyncHttpClient client;
    private ArrayList<String> m_kota;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialogObject;
    String wilayah="";
    private ArrayList<String> i_kota;
    private SimpleArcDialog mDialog;
    private ArrayList<class_favorit> class_barang;
    private Toolbar toolbar;
    private LinearLayout top_menu;
    private Spinner spinner;
    int index_menuterakhir;
    private TextView[] tv,tv1;
    String []arrmenu={"Semua","Top 25","Terbaru","Rekomendasi"};
    private MaterialSearchView searchView;
    private SnackBar snackbar;
    private HorizontalScrollView hsv;
    private String respon_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfavorit);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
       /* top_menu=(LinearLayout) findViewById(R.id.top_menu);
        hsv=(HorizontalScrollView) findViewById(R.id.hsv);
        tv=new TextView[arrmenu.length];
        tv1=new TextView[arrmenu.length];
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
        }*/
        isiData();
        /*class_swipe.detect(mRecyclerView, new class_swipe.SwipeCallback() {
            @Override
            public void onSwipeTop() {
                Log.e("d", "atas");
            }

            @Override
            public void onSwipeRight() {
                index_menuterakhir--;
                if (index_menuterakhir < 0) {
                    set_warna();
                    index_menuterakhir = arrmenu.length - 1;
                    tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                    int pos=0;
                    for (int i=0;i<index_menuterakhir;i++){
                        pos+=tv1[i].getWidth();
                    }
                    hsv.scrollTo(pos, 0);
                } else {
                    set_warna();
                    tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                    int pos=0;
                    for (int i=0;i<index_menuterakhir;i++){
                        pos+=tv1[i].getWidth();
                    }
                    hsv.scrollTo(pos, 0);
                }
            }

            @Override
            public void onSwipeBottom() {
                Log.e("d","bawah");
            }

            @Override
            public void onSwipeLeft() {
                index_menuterakhir++;
                if (index_menuterakhir >= arrmenu.length) {
                    set_warna();
                    index_menuterakhir = 0;
                    tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                    int pos=0;
                    for (int i=0;i<index_menuterakhir;i++){
                        pos+=tv[i].getWidth();
                    }
                    hsv.scrollTo(pos, 0);
                } else {
                    set_warna();
                    tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                    int pos=0;
                    for (int i=0;i<index_menuterakhir;i++){
                        pos+=tv[i].getWidth();
                    }
                    hsv.scrollTo(pos, 0);
                }

            }
        });
        tv1[0].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
        index_menuterakhir=0;*/
        setuptoolbar();
    }
    public void setuptoolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAVORIT");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /*private View.OnClickListener clicktabmenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            set_warna();
            index_menuterakhir=getindex(arrmenu,String.valueOf(v.getTag()));
            tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));

        }
    };*/
    private void isiData() {
        class_barang = new ArrayList<class_favorit>();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(activity_listfavorit.this).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"bacafavorit", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_listfavorit.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_listfavorit.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                respon_server= new String(responseBody);
                try
                {
                    JSONArray jarr = new JSONArray(respon_server);
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_favorit(
                            jobj.getString("id_iklan"),
                            "FAVORIT",
                            jobj.getString("judul_iklan"),
                            jobj.getString("harga_iklan"),
                            jobj.getJSONObject("kota").getString("nama_area"),
                            jobj.getString("gambar").replace(" ","%20"),
                            jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity_listfavorit.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(activity_listfavorit.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterfavorit class_adapterlistitem = new class_adapterfavorit(class_barang);
                    class_adapterlistitem.notifyDataSetChanged();
                    mRecyclerView.setAdapter(class_adapterlistitem);
                }
                catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException",ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                Log.e("Failure",String.valueOf(statusCode));
            }
        });
    }
    /*public  void  set_warna(){
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
    public void listkota(final String kota) {
        m_kota = new ArrayList<String>();
        i_kota = new ArrayList<String>();
        client=new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(1, 5000);
        String url = "";
        if(kota.equals(""))
        {
            client.addHeader("key", "9b9824312841e45f6c10e955830acc55");
            url="http://api.rajaongkir.com/starter/province";
        }
        else
        {
            client.addHeader("key", "9b9824312841e45f6c10e955830acc55");
            url="http://api.rajaongkir.com/starter/city?province="+kota;
        }
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e("start", "tes");
                mDialog = new SimpleArcDialog(activity_listfavorit.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_listfavorit.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jo = new JSONObject(new String(responseBody));
                    JSONArray jarr = jo.getJSONObject("rajaongkir").getJSONArray("results");
                    if(jarr.length()<1){
                        wilayah=wilayah.substring(0,wilayah.length()-2);
                        cari(wilayah);
                        mDialog.dismiss();
                        alertDialogObject.dismiss();
                    }
                    else {
                        Log.e("jumlah kota", String.valueOf(jarr.length()));
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            if (kota.equals("")) {
                                m_kota.add(jobj.getString("province"));
                                i_kota.add(jobj.getString("province_id"));
                            } else {
                                m_kota.add(jobj.getString("city_name"));
                                i_kota.add(jobj.getString("city_id"));
                            }
                            Log.e(String.valueOf(i), m_kota.get(i));
                        }
                        mDialog.dismiss();
                        listdialog();

                    }
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    dialog_gagal();
                } catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_gagal();
                }
                Log.e("respon raja ongkir", new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
            }
        });
    }
    public  void dialog_gagal(){
        snackbar = new SnackBar.Builder(activity_listfavorit.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik", "tutup");
                    }
                })
                .withMessage("Gagal mengambil data")
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }
    private void listdialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pencarian");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialoglist, null);

        ListView dialog_ListView = (ListView) dialogView.findViewById(R.id.dialoglist);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, m_kota);
        dialog_ListView.setAdapter(adapter);
        dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogObject.dismiss();
                listkota(i_kota.get(position));
                wilayah+=parent.getItemAtPosition(position).toString()+", ";
                cari(wilayah);
                Toast.makeText(activity_listfavorit.this, parent.getItemAtPosition(position).toString() + " clicked", Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setView(dialogView);
        alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }
    public void cari(String tekscari) {
        ArrayAdapter<String> list_adapter;
        List<String> list;
        list = new ArrayList<String>();
        list.add(tekscari);
        list_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list_adapter);
    }*/
    @Override
    public void onBackPressed() {
        finish();
        Intent intent=new Intent(activity_listfavorit.this,activity_main.class);
        startActivity(intent);
    }
}
