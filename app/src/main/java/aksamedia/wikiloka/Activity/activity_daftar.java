package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mrengineer13.snackbar.SnackBar;
import com.gregacucnik.EditTextView;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_bantuan;

public class activity_daftar extends AppCompatActivity {
    EditTextView dft_user,dft_email,dft_pass;
    Button btn_daftar,btn_tutup;
    String respon_server;
    private SimpleArcDialog mDialog;
    private SnackBar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        inisialisasi();
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proses_daftar(class_bantuan.base_url+"daftar", class_bantuan.api_key, dft_user.getText().toString(), dft_email.getText().toString(), dft_pass.getText().toString());
            }
        });
        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void inisialisasi(){
        dft_user=(EditTextView) findViewById(R.id.tv_username);
        dft_email =(EditTextView) findViewById(R.id.tv_user);
        dft_pass=(EditTextView) findViewById(R.id.tv_pass);
        btn_daftar=(Button) findViewById(R.id.btn_daftar);
        btn_tutup=(Button) findViewById(R.id.btn_tutup);
    }
    private void proses_daftar(String url,String api_key,String username,String email,String password){
        final String[] respon = {""};
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("username", username);
        params.put("email",email);
        params.put("password", password);
        client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        mDialog = new SimpleArcDialog(activity_daftar.this);
                        mDialog.setConfiguration(new ArcConfiguration(activity_daftar.this));
                        mDialog.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        respon_server = new String(responseBody);

                        if (respon_server.equals("failed")) {
                            mDialog.dismiss();
                            snackBar = new SnackBar.Builder(activity_daftar.this)
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
                        } else if (respon_server.equals("Email Sudah Terdaftar")) {
                            mDialog.dismiss();
                            snackBar = new SnackBar.Builder(activity_daftar.this)
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
                        } else if (respon_server.equals("Username Sudah Terdaftar")) {
                            mDialog.dismiss();
                            snackBar = new SnackBar.Builder(activity_daftar.this)
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
                            snackBar = new SnackBar.Builder(activity_daftar.this)
                                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                    @Override
                                    public void onMessageClick(Parcelable token) {
                                        String email_arr[]=dft_email.getText().split("@");
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+email_arr[1]));
                                        startActivity(browserIntent);
                                        snackBar.clear();
                                        finish();
                                    }
                                })
                                .withMessage("Proses daftar sukses. Silahkan cek email anda.")
                                .withActionMessage("Buka")
                                .withStyle(SnackBar.Style.INFO)
                                .withDuration(SnackBar.PERMANENT_SNACK)
                                .show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        //respon[0] = "gagal";
                        mDialog.dismiss();
                        Log.e("log daftar member",new String(responseBody));
                        Toast.makeText(activity_daftar.this, "Proses Daftar anda gagal", Toast.LENGTH_LONG).show();
                    }

                }
        );
    }
}
