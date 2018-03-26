package aksamedia.wikiloka.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aksamedia.wikiloka.Activity.activity_iklan;
import aksamedia.wikiloka.Activity.activity_login;
import aksamedia.wikiloka.Activity.activity_sub_kategori;
import aksamedia.wikiloka.Class.class_adapterberita;
import aksamedia.wikiloka.Class.class_adapterlistitem;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_berita;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.Class.class_iklan;
import aksamedia.wikiloka.Class.class_menu;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;
import cz.msebera.android.httpclient.Header;

public class fragment_menu extends Fragment implements View.OnClickListener {
    ListView lv1;
    String [] url_menu,item_menu, seo_menu;
    SnackBar snackBar;
    AsyncHttpClient client;
    View rv;
    ArrayList<class_iklan> iklan;
    ArrayList<class_berita> berita;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private SimpleArcDialog mDialog;
    private String[] id_menu;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rv= inflater.inflate(R.layout.fragment_menu, container, false);
        lv1=(ListView) rv.findViewById(R.id.list_kategori);
        mRecyclerView = (RecyclerView) rv.findViewById(R.id.rvKategori);
        fab=(FloatingActionButton) rv.findViewById(R.id.fab);
        String menu_sekarang = this.getArguments().getString("status_menu");
        class_helpersqlite db_helper=new class_helpersqlite(getActivity());
        SQLiteDatabase db=db_helper.getWritableDatabase();
        db_helper.buat_tbl_filter_motor(db);
        db_helper.buat_tbl_filter_rumah_tangga(db);
        db_helper.buat_tbl_filter_travel(db);
        db_helper.buat_tbl_filter_mobil(db);
        db_helper.buat_tbl_filter_properti(db);
        db_helper.buat_tbl_filter_olahraga(db);
        db_helper.buat_tbl_filter_kuliner(db);
        db_helper.buat_tbl_filter_fashion(db);
        db_helper.buat_tbl_filter_elektronik(db);
        db_helper.buat_tbl_member(db);
        if(menu_sekarang.equals("utama"))
        {
           baca_menuutama(class_bantuan.base_url+"kategori_utama", class_bantuan.api_key);
            fragment_beranda.filter.setVisibility(View.VISIBLE);
            //fab.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            fragment_beranda.slide_layout.setVisibility(View.VISIBLE);
        }
        else if(menu_sekarang.equals("topmenu"))
        {
            baca_topmenu(class_bantuan.base_url + this.getArguments().getString("kategori_url"), class_bantuan.api_key, this.getArguments().getString("kategori_menu"));
            fragment_beranda.filter.setVisibility(View.GONE);
            //fab.setVisibility(View.GONE);
            lv1.setVisibility(View.GONE);

            mRecyclerView.setVisibility(View.VISIBLE);
            class_bantuan.menu_kategori = "topmenu";
            fragment_beranda.slide_layout.setVisibility(View.GONE);
        }
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), activity_sub_kategori.class);
                intent.putExtra("id_kategori",id_menu[position]);
                intent.putExtra("kategori",item_menu[position]);
                intent.putExtra("url_kategori",url_menu[position]);
                intent.putExtra("seo_kategori",seo_menu[position]);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(this);
        return rv;
    }
    public void baca_menuutama(String url,String api_key){
        RequestParams params = new RequestParams();
        params.put("key",api_key);

        client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.e("Respon Kategori", new String(responseBody));
                try {
                    JSONArray jarr = new JSONArray(new String(responseBody));
                    id_menu = new String[jarr.length()];
                    url_menu = new String[jarr.length()];
                    item_menu = new String[jarr.length()];
                    seo_menu = new String[jarr.length()];
                    //class_helpersqlite db_helper=new class_helpersqlite(getActivity());
                    //SQLiteDatabase db=db_helper.getWritableDatabase();
                    //db.execSQL("delete from tbl_menu;");
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        item_menu[i] = jobj.getString("kategori");
                        url_menu[i] = jobj.getString("icon");
                        id_menu[i] = jobj.getString("id_kategori");
                        seo_menu[i] = jobj.getString("seo_kategori");
                        //new class_prosessql(getActivity()).insert_menuutama(item_menu[i], url_menu[i]);
                    }
                    mDialog.dismiss();
                    lv1.setAdapter(new class_menu(getActivity(), item_menu, url_menu));

                } catch (JSONException ex) {
                    mDialog.dismiss();
                    dialog_gagal();
                    class_prosessql proses = new class_prosessql(getActivity());
                    try {
                        proses.baca_menuutama();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lv1.setAdapter(null);
                } catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_gagal();
                    //instan class_prosessql.java
                    //class_prosessql proses=new class_prosessql(getActivity());
                    //proses.baca_menuutama();
                    lv1.setAdapter(null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal();
                //class_prosessql proses=new class_prosessql(getActivity());
                //proses.baca_menuutama();
                lv1.setAdapter(null);
            }
        });
    }
    public void baca_topmenu(String url,String api_key, final String kategori){
        Log.e("Respon URL", url);
        RequestParams params = new RequestParams();
        params.put("key",api_key);
        iklan=new ArrayList<>();
        berita=new ArrayList<>();
        client = new AsyncHttpClient();
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.e("Respon Kategori", new String(responseBody));
                try
                {
                    JSONObject job = new JSONObject(new String(responseBody));
                    JSONArray jarr = new JSONArray(String.valueOf(job.getJSONArray("iklan")));
                    //JSONArray jarr = new JSONArray(new String(responseBody));
                    if(kategori == "Berita"){
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            berita.add(new class_berita(
                                    jobj.getString("id_berita"),
                                    jobj.getString("judul_berita"),
                                    jobj.getString("seo_berita"),
                                    jobj.getString("sumber_berita"),
                                    jobj.getString("gambar").replace(" ", "%20")));
                        }
                        mDialog.dismiss();

                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        class_adapterberita class_adapterberita = new class_adapterberita(berita);
                        mRecyclerView.setAdapter(class_adapterberita);
                    }
                    //TOP SHOP masih belum jelas
                    /*else if (kategori == "Top Shop"){
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
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(iklan);
                        mRecyclerView.setAdapter(class_adapterlistitem);
                    }*/
                    else{
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
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(iklan);
                        mRecyclerView.setAdapter(class_adapterlistitem);
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
                    Log.e("tes exception",ex.toString());
                    dialog_gagal();
                    //instan class_prosessql.java
                    //class_prosessql proses=new class_prosessql(getActivity());
                    //proses.baca_menuutama();
                    lv1.setAdapter(null);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                mDialog.dismiss();
                dialog_gagal();
                //class_prosessql proses=new class_prosessql(getActivity());
                //proses.baca_menuutama();
                lv1.setAdapter(null);
            }
        });
    }
    public void dialog_gagal(){
        snackBar = new SnackBar.Builder(getActivity())
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab){
            getActivity().finish();
            if (new class_prosessql(getActivity()).baca_profil()[1] == null){
                Intent intent = new Intent(getActivity(), activity_login.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getActivity(), activity_iklan.class);
                startActivity(intent);
            }
        }
    }

}