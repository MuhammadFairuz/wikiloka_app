package aksamedia.wikiloka.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import aksamedia.wikiloka.Class.class_allpoin;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.Class.class_menu_upgrade;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.Fragment.frag_favorit;
import aksamedia.wikiloka.Fragment.frag_gantiprofil;
import aksamedia.wikiloka.Fragment.frag_inbox;
import aksamedia.wikiloka.Fragment.frag_listiklanku;
import aksamedia.wikiloka.Fragment.frag_premium;
import cz.msebera.android.httpclient.Header;

public class activity_akun extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String[] menu = new String[]{"Ganti profil", "List Iklan", "Buat Website", "Beli Poin", "Beli Iklan Premium"};
    ListView lv;
    ImageView back;
    private AlertDialog alertDialogObject;
    private String respon_server;
    private SimpleArcDialog mDialog;
    private NavigationView navigation_view, nav_view_Filter;
    private Toolbar toolbar;
    private class_helpersqlite db_helper;
    private FragmentManager main;
    private AsyncHttpClient client;
    private SnackBar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        inisialisasi();
        setuptoolbar();
    }

    private void inisialisasi() {
        navigation_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view_Filter = (NavigationView) findViewById(R.id.nav_view_filter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigation_view.setNavigationItemSelectedListener(this);
        main = getFragmentManager();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frm_main, new frag_gantiprofil(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        navigation_view.getMenu().getItem(0).setChecked(true);
    }

    void baca_poin(String url, String api_key) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_akun.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_akun.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);

                    final String[] id_poin = new String[jarr.length()];
                    String[] nama_poin = new String[jarr.length()];
                    String[] url = new String[jarr.length()];
                    String[] harga_poin = new String[jarr.length()];
                    String[] jumlah_poin = new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        url[i] = jobj.getString("gambar_poin");
                        id_poin[i] = jobj.getString("id_poin");
                        nama_poin[i] = jobj.getString("nama_poin");
                        harga_poin[i] = jobj.getString("harga_poin");
                        jumlah_poin[i] = jobj.getString("jumlah_poin");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_akun.this);
                    LayoutInflater inflater = activity_akun.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_kategori, null);
                    ListView list_kategori = (ListView) dialogView.findViewById(R.id.list);
                    list_kategori.setAdapter(new class_allpoin(activity_akun.this, url, id_poin, nama_poin, harga_poin, jumlah_poin));

                    list_kategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            alertDialogObject.dismiss();
                            proses_beli_poin(class_bantuan.base_url + "beli_poin",
                                    class_bantuan.api_key,
                                    ((TextView) view.findViewById(R.id.id_poin)).getText().toString(),
                                    new class_prosessql(activity_akun.this).baca_profil()[1]);

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

    private void proses_beli_poin(String url, String key, String id_poin, String username) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final SimpleArcDialog mDialog = new SimpleArcDialog(activity_akun.this);
        RequestParams params = new RequestParams();
        params.put("key", key);
        params.put("id_poin", id_poin);
        params.put("username", username);
        client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        mDialog.setConfiguration(new ArcConfiguration(activity_akun.this));
                        mDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("Respon", new String(responseBody));
                        try {
                            respon_server = new String(responseBody);
                            if (respon_server.equals("failed") || respon_server.isEmpty()) {
                                mDialog.dismiss();
                                dialog_gagal("Proses beli poin anda gagal.");
                            } else if (respon_server.equals("Success")) {
                                mDialog.dismiss();
                                dialog_sukses("Terima Kasih telah membeli poin. Segera Cek Email Anda untuk melihat invoice pembayaran.");
                            }
                        } catch (Exception ex) {
                            mDialog.dismiss();
                            dialog_gagal("Terjadi kesalahan beli poin.");
                            Log.e("Exception", ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        mDialog.dismiss();
                        dialog_gagal("Gagal kirim data");
                        Log.e("statusCode", String.valueOf(statusCode));
                    }
                }
        );
    }

    public void setuptoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AKUN SAYA");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    public void dialog_gagal(String pesan) {
        snackBar = new SnackBar.Builder(activity_akun.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik", "tutup");
                    }
                })
                .withMessage(pesan)
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.ALERT)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }

    public void dialog_sukses(String pesan) {
        snackBar = new SnackBar.Builder(activity_akun.this)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer != null) {
                drawer.openDrawer(Gravity.LEFT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("Jumlah Stack", String.valueOf(fm.getBackStackEntryCount()));

            fm.popBackStack();
            Log.e("Jumlah Stack", String.valueOf(fm.getBackStackEntryCount()));
        } else {
            Log.e("Jumlah Stack", String.valueOf(fm.getBackStackEntryCount()));
            finish();
            Intent intent = new Intent(activity_akun.this, activity_main.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(Gravity.LEFT);
        }
        int id = item.getItemId();
        if (id == R.id.nav_menu1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, new frag_gantiprofil(), "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (id == R.id.nav_menu2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, new frag_listiklanku(), "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (id == R.id.nav_menu3) {
            //buat website
            finish();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.wikiloka.com/iklanku/buatwebsite"));
            startActivity(intent);
        } else if (id == R.id.nav_menu4) {
            baca_poin(class_bantuan.base_url + "get_poin", class_bantuan.api_key);
        } else if (id == R.id.nav_menu5) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, new frag_premium(), "fragment")
                    .addToBackStack(null)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (id == R.id.nav_menu6) {
            keluar_aplikasi();
            finish();
            Intent intent = new Intent(activity_akun.this, activity_main.class);
            startActivity(intent);
        } else if (id == R.id.nav_menu7) {
            //page favorit
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, new frag_favorit(), "fragment")
                    .addToBackStack(null)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (id == R.id.nav_menu8) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_layoutupgrade, null);
            ListView list = (ListView) dialogView.findViewById(R.id.list_upgrade);
            String[] keterangan = {"Pembaruan Otomatis", "+ 10 Iklan Upload",
                    "+ 25 Iklan Upload", "+ 50 Iklan Upload",
                    "Unlimited Iklan Upload", "Domain Sendiri (Bonus Multi Kategori)",
                    "Multi Kategori"};
            String[] sub_keterangan = {"Harga 25 Poin / Tahun", "Harga 50 Poin / Tahun",
                    "Harga 75 Poin / Tahun", "Harga 100 Poin / Tahun",
                    "Harga 150 Poin / Tahun", "Harga 600 Poin / Tahun",
                    "Harga 400 Poin / Tahun"};
            final String[] nama_kolom = {"pembaruan", "upload10", "upload25",
                    "upload50", "uploadunl", "domainsendiri",
                    "multikategori"};
            list.setAdapter(new class_menu_upgrade(this, keterangan, sub_keterangan, nama_kolom));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    upgrade(class_bantuan.base_url + "get_upgrade/" + nama_kolom[position].toString(), class_bantuan.api_key, new class_prosessql(activity_akun.this).baca_member()[0]);
                }
            });
            dialog.setView(dialogView);
            alertDialogObject = dialog.create();
            alertDialogObject.show();
        } else if (id == R.id.nav_menu9) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, new frag_inbox(), "fragment")
                    .addToBackStack(null)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        return true;
    }

    public void keluar_aplikasi() {
        db_helper = new class_helpersqlite(activity_akun.this);
        SQLiteDatabase db = db_helper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("delete from tbl_member");
        stmt.execute();

        stmt = db.compileStatement("delete from tbl_favorit");
        stmt.execute();

        stmt.close();
        db.close();
    }

    public void upgrade(String url, String api_key, String username) {
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("username", username);
        client = new AsyncHttpClient();
        Log.e("urlnya", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(activity_akun.this);
                mDialog.setConfiguration(new ArcConfiguration(activity_akun.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.e("Respon iklan off", new String(responseBody));
                String respon = "";
                respon = new String(responseBody);
                try {
                    if (respon.equals("sukses")) {
                        mDialog.dismiss();
                        alertDialogObject.dismiss();
                        dialog_sukses("Upgrade iklan sukses");
                    } else {
                        mDialog.dismiss();
                        alertDialogObject.dismiss();
                        dialog_sukses(respon);
                    }
                } catch (Exception ex) {
                    mDialog.dismiss();
                    alertDialogObject.dismiss();
                    dialog_sukses("Kesalahan :" + ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_sukses("Gagal koneksi");
            }
        });
    }
}
