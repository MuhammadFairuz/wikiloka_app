package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.github.mrengineer13.snackbar.SnackBar;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_bantuan;

/**
 * Created by Dinar on 9/21/2016.
 */
public class activity_detailberita extends AppCompatActivity implements View.OnClickListener, BottomNavigationBar.OnTabSelectedListener{

    TextView tv_judul, tv_isi, tv_read_more;
    ImageView gambar;
    Intent intent;
    private SimpleArcDialog mDialog;
    private SnackBar snackBar;
    private String respon_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);
        initialize();
        get_data(class_bantuan.base_url + "detailBerita", class_bantuan.api_key, getIntent().getStringExtra("seo_berita"));
        tv_read_more.setOnClickListener(goToSource);
    }

    private View.OnClickListener goToSource = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("sumber_berita")));
            startActivity(intent);
        }
    };

    void initialize() {
        tv_judul = (TextView) findViewById(R.id.detail_judul_berita);
        tv_isi = (TextView) findViewById(R.id.detail_isi_berita);
        tv_read_more = (TextView) findViewById(R.id.tv_read_more);
        gambar = (ImageView) findViewById(R.id.detail_foto_berita);
    }

    private void get_data(String url, String api_key, String seo_berita) {
        respon_server = "";
        mDialog = new SimpleArcDialog(activity_detailberita.this);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("seo_berita", seo_berita);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(activity_detailberita.this));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    respon_server = new String(responseBody);
                    mDialog.dismiss();
                    if(respon_server.equals("failed") || respon_server.isEmpty()){
                        dialog_gagal("Gagal ambil detail berita");
                    }
                    else{
                        tv_read_more.setVisibility(View.VISIBLE);
                        JSONArray jarr = new JSONArray(respon_server);
                        for (int i = 0; i < jarr.length(); ++i) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            tv_judul.setText(jobj.getString("judul_berita"));
                            tv_isi.setText(jobj.getString("isi_berita"));
                            Picasso.with(getApplicationContext())
                                    .load(jobj.getString("gambar").replace(" ", "%20")).into(gambar);
                        }
                    }
                } catch (JSONException ex) {
                    dialog_gagal("Gagal parsing data detail.");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                dialog_gagal("Gagal parsing data detail.");
            }
        });
    }

    void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(activity_detailberita.this)
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

    }

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /*@Override
    public void onBackPressed() {
        finish();
            intent = new Intent(activity_detailberita.this, activity_main.class);
            class_bantuan.menu_kategori = "";
        startActivity(intent);
    }*/
}
