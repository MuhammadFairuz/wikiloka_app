package aksamedia.wikiloka.Fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_kategori;
import aksamedia.wikiloka.Class.class_provinsi;
import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Gajelas.tv_ubuntu;
import me.relex.circleindicator.CircleIndicator;

/**
 * A placeholder fragment containing a simple view.
 */

public class fragment_beranda extends Fragment implements View.OnClickListener, LocationListener {
    View rootview;
    public static RelativeLayout filter;
    private LayoutInflater inflat;
    private LinearLayout top_menu;
    private FrameLayout frame_frag;
    private static HorizontalScrollView hsv;
    private static TextView[] tv;
    private static TextView[] tv1;
    private static String[] arrmenu = {"Kuliner", "Travel", "Top Website", "Berita"};
    private float x1, x2;
    private static FragmentActivity context;
    private EditText txt_word, txt_kota, txt_kategori;
    private TextView tv_kota, tv_kategori;
    public static RelativeLayout slide_layout;
    private SimpleArcDialog mDialog;
    private String respon_server;
    private AlertDialog alertDialogObject;
    private ImageView btn_search;
    private RelativeLayout btn_kota, btn_lokasi;
    private CircleIndicator indicator;
    private ViewPager mDemoSlider;
    private Runnable update;
    private int currentPage;
    private Handler handler;
    private frag_adapter m_frag_adapter;
    private ArrayList<String> id_slider = new ArrayList<String>();
    private ArrayList<String> url_slider = new ArrayList<String>();
    protected boolean isGPSEnabled = false, isNetworkEnabled = false;
    protected Location location;
    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    public fragment_beranda() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_beranda, container, false);
        filter = (RelativeLayout) rootview.findViewById(R.id.search);
        inflat = inflater;
        context = getActivity();
        top_menu = (LinearLayout) rootview.findViewById(R.id.top_menu);
        frame_frag = (FrameLayout) rootview.findViewById(R.id.frame1);
        hsv = (HorizontalScrollView) rootview.findViewById(R.id.hsv);
        btn_kota = (RelativeLayout) rootview.findViewById(R.id.btn_kota);
        btn_lokasi = (RelativeLayout) rootview.findViewById(R.id.btn_lokasi);
        txt_kota = (EditText) rootview.findViewById(R.id.txt_kota);
        tv_kota = (TextView) rootview.findViewById(R.id.tv_kota);
        tv = new TextView[arrmenu.length];
        tv1 = new TextView[arrmenu.length];
        indicator = (CircleIndicator) rootview.findViewById(R.id.indicator);
        mDemoSlider = (ViewPager) rootview.findViewById(R.id.slider);
        slide_layout = (RelativeLayout) rootview.findViewById(R.id.slide_layout);
        init_slider();
        //getLocation();
        for (int i = 0; i < arrmenu.length; ++i) {
            View child1 = inflat.inflate(R.layout.custom_topmenu, null);
            tv[i] = (TextView) child1.findViewById(R.id.texttop2);
            tv[i].setTag(String.valueOf(arrmenu[i]));
            tv[i].findViewWithTag(String.valueOf(arrmenu[i])).setOnClickListener(clicktabmenu);
            tv[i].setText("   " + arrmenu[i].toUpperCase() + "   ");
            tv1[i] = (TextView) child1.findViewById(R.id.menutop2);
            tv1[i].setTag(String.valueOf(arrmenu[i]));
            tv1[i].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu));//.getColor(R.color.colorPrimary));
            tv1[i].setGravity(Gravity.BOTTOM);
            top_menu.addView(child1);
        }
        loadawal();
        btn_lokasi.setOnClickListener(this);
        btn_kota.setOnClickListener(this);
        return rootview;

    }

    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(context, "Aktifkan mobile data / GPS Anda", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkEnabled) {
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
        }
        return location;
    }

    private void init_slider() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        client.post(class_bantuan.base_url + "getSlider/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        mDialog = new SimpleArcDialog(getContext());
                        mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                        mDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        Log.e("Respon", new String(responseBody));
                        try {
                            respon_server = new String(responseBody);
                            if (respon_server.equals("failed") || respon_server.isEmpty()) {
                                if (mDialog != null && mDialog.isShowing()) {
                                    try {
                                        mDialog.dismiss();
                                    } catch (Exception ex) {

                                    }
                                }
                            } else {
                                id_slider.clear();
                                url_slider.clear();
                                if (respon_server.equals("kosong")) {
                                    for (int i = 0; i < 3; ++i) {
                                        id_slider.add(String.valueOf(i));
                                        url_slider.add("http://webkul.com/blog/wp-content/uploads/2014/05/Marketplace-Seller-Slider-Blog-Banner.png");
                                    }
                                } else {
                                    JSONArray jarr = new JSONArray(respon_server);
                                    for (int i = 0; i < jarr.length(); ++i) {
                                        JSONObject jobj = jarr.getJSONObject(i);
                                        id_slider.add(jobj.getString("id_iklan"));
                                        url_slider.add(jobj.getString("foto"));
                                    }
                                }
                                final List<Fragment> fragments = buildFragments();
                                m_frag_adapter = new frag_adapter(getActivity(), getActivity().getSupportFragmentManager(), fragments, id_slider);
                                mDemoSlider.setAdapter(m_frag_adapter);
                                indicator.setViewPager(mDemoSlider);

                                handler = new Handler();

                                update = new Runnable() {
                                    public void run() {
                                        if (currentPage == fragments.size()) {
                                            currentPage = 0;
                                        }
                                        mDemoSlider.setCurrentItem(currentPage++, true);
                                    }
                                };


                                new Timer().schedule(new TimerTask() {

                                    @Override
                                    public void run() {
                                        handler.post(update);
                                    }
                                }, 100, 5000);

                                mDialog.dismiss();
                            }

                        } catch (JSONException ex) {
                            if (mDialog != null && mDialog.isShowing()) {
                                try {
                                    mDialog.dismiss();
                                } catch (Exception ex1) {

                                }

                            }
                            Log.e("JSON", ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        if (mDialog != null && mDialog.isShowing()) {
                            try {
                                mDialog.dismiss();
                            } catch (Exception ex) {

                            }

                        }
                    }
                }
        );
    }

    private List<Fragment> buildFragments() {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < id_slider.size(); i++) {
            Bundle b = new Bundle();
            b.putString("id", id_slider.get(i));
            b.putString("image", url_slider.get(i));
            fragments.add(Fragment.instantiate(getContext(), frag_slider.class.getName(), b));
        }
        return fragments;
    }

    void kategori_utama(String url, String key) {
        RequestParams params = new RequestParams();
        params.put("key", key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    list_kategori.setAdapter(new class_kategori(getActivity(), id_kategori, item_kategori, url_kategori));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            tv_kategori.setText(text.getText().toString());
                            txt_kategori.setText(text_id.getText().toString());
                            alertDialogObject.dismiss();
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

    void provinsi(String url, String api_key) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl = (TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR PROVINSI");
                    list_kategori.setAdapter(new class_provinsi(getActivity(), id_provinsi, nama_provinsi));
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

    void kota(String url, String api_key, String id_prov) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("id_prov", id_prov);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    TextView jdl = (TextView) dialogView.findViewById(R.id.jdl_list);
                    jdl.setText("DAFTAR KOTA");
                    list_kategori.setAdapter(new class_provinsi(getActivity(), id_kota, nama_area));
                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_ubuntu text_id = (tv_ubuntu) view.findViewById(R.id.txtid);
                            tv_ubuntu text_list = (tv_ubuntu) view.findViewById(R.id.txtlist);
                            txt_kota.setText(text_id.getText().toString());
                            tv_kota.setText(text_list.getText().toString());
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
                mDialog.dismiss();
            }
        });
    }

    private static int index_menuterakhir;

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

    private View.OnClickListener clicktabmenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //slide_layout.setVisibility(View.GONE);
            set_warna();
            index_menuterakhir = getindex(arrmenu, String.valueOf(v.getTag()));
            tv1[index_menuterakhir].setBackground(getResources().getDrawable(R.drawable.shadow_topmenu_aktif));

            String kategori = "";
            Bundle bundle = new Bundle();
            bundle.putString("status_menu", "topmenu");

            if (String.valueOf(v.getTag()) == "Travel")
                kategori = "getIklanKategori/travel";
            else if (String.valueOf(v.getTag()) == "Kuliner")
                kategori = "getIklanKategori/kuliner";
            else if (String.valueOf(v.getTag()) == "Top Website")
                kategori = "getIklanTopWebsite";
            else
                kategori = "getBerita";
            bundle.putString("kategori_url", kategori);
            bundle.putString("kategori_menu", String.valueOf(v.getTag()));
            bundle.putString("tabmenu", "aktif");
            slide_layout.setVisibility(View.GONE);
            fragment_menu frag = new fragment_menu();
            frag.setArguments(bundle);
            FragmentManager fragment_man = getActivity().getSupportFragmentManager();
            FragmentTransaction fragment_trans = fragment_man.beginTransaction();
            fragment_trans.replace(R.id.frame1, frag).addToBackStack("tabmenu").commit();
        }
    };

    static void set_warna() {
        for (int i = 0; i < arrmenu.length; i++) {
            tv1[i].setBackground(context.getResources().getDrawable(R.drawable.shadow_topmenu));
        }
    }

    static void set_topmenu(String direction) {
        if (direction.equals("kanan")) {
            if (index_menuterakhir >= (arrmenu.length - 2)) {
                set_warna();
                index_menuterakhir = (arrmenu.length - 1);
                tv1[index_menuterakhir].setBackground(context.getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                int pos = 0;
                for (int i = 0; i < index_menuterakhir; i++) {
                    pos += tv[i].getWidth();
                }
                hsv.scrollTo(pos, 0);

            } else {
                set_warna();
                index_menuterakhir++;

                tv1[index_menuterakhir].setBackground(context.getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                int pos = 0;
                for (int i = 0; i < index_menuterakhir; i++) {
                    pos += tv1[i].getWidth();
                }
                hsv.scrollTo(pos, 0);
            }
        } else if (direction.equals("kiri")) {
            if (index_menuterakhir <= 0) {
                set_warna();
                index_menuterakhir = 0;
                tv1[index_menuterakhir].setBackground(context.getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                int pos = 0;
                for (int i = 0; i < index_menuterakhir; i++) {
                    pos += tv[i].getWidth();
                }
                hsv.scrollTo(pos, 0);
            } else {
                set_warna();
                index_menuterakhir--;
                tv1[index_menuterakhir].setBackground(context.getResources().getDrawable(R.drawable.shadow_topmenu_aktif));
                int pos = 0;
                for (int i = 0; i < index_menuterakhir; i++) {
                    pos += tv[i].getWidth();
                }
                hsv.scrollTo(pos, 0);
            }
        }
    }

    public void loadawal() {
        Bundle bundle = new Bundle();
        bundle.putString("status_menu", "utama");
        fragment_menu frag = new fragment_menu();
        frag.setArguments(bundle);
        FragmentManager fragment_man = getActivity().getSupportFragmentManager();
        FragmentTransaction fragment_trans = fragment_man.beginTransaction();
        fragment_trans.replace(R.id.frame1, frag).commit();
    }

    @Override
    public void onClick(View v) {
        /*if(v.getId()==R.id.tv_kota){
            tv_kota.setText("");
            txt_kota.setText("");
            provinsi(class_bantuan.base_url + "provinsi", class_bantuan.api_key);
        }else if(v.getId()==R.id.tv_kategori){
            tv_kategori.setText("");
            txt_kategori.setText("");
            kategori_utama(class_bantuan.base_url + "kategori_utama", class_bantuan.api_key);
        }else  if(v.getId()==R.id.btn_search){
            getActivity().finish();
            Intent intent = new Intent(getActivity(), activity_listitem.class);
            intent.putExtra("word",txt_word.getText().toString());
            intent.putExtra("kota",txt_kota.getText().toString());
            intent.putExtra("seo_kategori",txt_kategori.getText().toString());
            startActivity(intent);
        }*/
        if (v.getId() == R.id.btn_kota) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((ImageView) btn_kota.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_tdk1, getActivity().getTheme()));
                ((ImageView) btn_kota.getChildAt(4)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
                ((ImageView) btn_lokasi.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_near1, getActivity().getTheme()));
                ((ImageView) btn_lokasi.getChildAt(3)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorBottom, getActivity().getTheme()));
            } else {
                ((ImageView) btn_kota.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_tdk1));
                ((ImageView) btn_kota.getChildAt(4)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                ((ImageView) btn_lokasi.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_near1));
                ((ImageView) btn_lokasi.getChildAt(3)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorBottom));
            }
        } else if (v.getId() == R.id.btn_lokasi) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((ImageView) btn_lokasi.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_tdk2, getActivity().getTheme()));
                ((ImageView) btn_lokasi.getChildAt(3)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary, getActivity().getTheme()));
                ((ImageView) btn_kota.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_near2, getActivity().getTheme()));
                ((ImageView) btn_kota.getChildAt(4)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorBottom, getActivity().getTheme()));
            } else {
                ((ImageView) btn_lokasi.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_tdk2));
                ((ImageView) btn_lokasi.getChildAt(3)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                ((ImageView) btn_kota.getChildAt(0)).setBackground(getActivity().getResources().getDrawable(R.drawable.bg_button_near2));
                ((ImageView) btn_kota.getChildAt(4)).setBackgroundColor(getActivity().getResources().getColor(R.color.colorBottom));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

