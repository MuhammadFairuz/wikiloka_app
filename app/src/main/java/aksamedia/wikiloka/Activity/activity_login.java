package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mrengineer13.snackbar.SnackBar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gregacucnik.EditTextView;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aksamedia.wikiloka.Master.Token;
import aksamedia.wikiloka.R;
import aksamedia.wikiloka.WebRequest.Request;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.Class.class_prosessql;
import cz.msebera.android.httpclient.Header;

public class activity_login extends AppCompatActivity {
    EditTextView txt_user, txt_pass;
    Button btn_masuk, btn_daftar;
    class_helpersqlite db_helper;
    SQLiteDatabase db;
    String respon_server;
    SnackBar snackBar;
    String form_tujuan;
    private TextView tv_lupa;
    private AlertDialog alertDialogObject;
    private EditText gnt_email;
    private EditText gnt_pass;
    private EditText gnt_pass1;
    private SimpleArcDialog mDialog;
    private String pass_edt;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        form_tujuan = getIntent().getStringExtra("aksesform");
        db_helper = new class_helpersqlite(this);
        db = db_helper.getWritableDatabase();
        db_helper.buat_tbl_member(db);
        db_helper.buat_tbl_profil(db);
        if (new class_prosessql(activity_login.this).cek_session().equals("ada")) {
            finish();
            if (form_tujuan.equals("form_pengaturan")) {
                Intent intent = new Intent(activity_login.this, activity_akun.class);
                startActivity(intent);
            } else if (form_tujuan.equals("form_iklan")) {
                Intent intent = new Intent(activity_login.this, activity_iklan.class);
                startActivity(intent);
            } else if (form_tujuan.equals("form_favorit")) {
                Intent intent = new Intent(activity_login.this, activity_listfavorit.class);
                startActivity(intent);
            }
        }

        setContentView(R.layout.activity_login);
        inisialisasi();
        tv_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_form();
            }
        });
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proses_login1();
            }
        });
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_daftar.class);
                startActivity(intent);
            }
        });
    }

    private void reset_form() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_layoutforgot, null);
        Button btn_tutup = (Button) dialogView.findViewById(R.id.btn_tutup);
        Button btn_ganti = (Button) dialogView.findViewById(R.id.btn_ganti);
        gnt_email = (EditText) dialogView.findViewById(R.id.gnt_email);
        gnt_pass = (EditText) dialogView.findViewById(R.id.gnt_pass);
        gnt_pass1 = (EditText) dialogView.findViewById(R.id.gnt_pass1);
        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogObject.dismiss();
            }
        });
        btn_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gnt_email.getText().toString().equals("")) {
                    snackBar = new SnackBar.Builder(activity_login.this)
                            .withMessage("Masukkan email anda.")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.LONG_SNACK)
                            .show();
                } else if (gnt_pass.getText().toString().equals("") || gnt_pass1.getText().toString().equals("")) {
                    snackBar = new SnackBar.Builder(activity_login.this)
                            .withMessage("Masukkan password anda.")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.LONG_SNACK)
                            .show();
                } else if (!gnt_pass.getText().toString().equals(gnt_pass1.getText().toString())) {
                    snackBar = new SnackBar.Builder(activity_login.this)
                            .withMessage("Konfirmasi password anda salah.")
                            .withStyle(SnackBar.Style.INFO)
                            .withDuration(SnackBar.LONG_SNACK)
                            .show();
                } else {
                    prosesganti(class_bantuan.alamat_server, gnt_email.getText().toString(), gnt_pass.getText().toString());
                }

            }
        });
        dialog.setView(dialogView);
        alertDialogObject = dialog.create();
        alertDialogObject.show();
    }

    private void prosesganti(String url, String email, String password) {
        final String[] respon = {""};
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
    }

    private void inisialisasi() {
        txt_user = (EditTextView) findViewById(R.id.tv_user);
        txt_pass = (EditTextView) findViewById(R.id.tv_pass);
        btn_masuk = (Button) findViewById(R.id.btn_masuk);
        btn_daftar = (Button) findViewById(R.id.btn_daftar);
        tv_lupa = (TextView) findViewById(R.id.tv_forgot);
        txt_pass.setEditTextViewListener(new EditTextView.EditTextViewListener() {
            @Override
            public void onEditTextViewEditModeStart() {
            }

            @Override
            public void onEditTextViewEditModeFinish(String text) {
                /*pass_edt = text;
                Log.e("selesai", "selesai password");
                String enc = "";
                for (int i = 0; i < text.length(); i++) {
                    enc += "*";
                }
                txt_pass.setText(enc);*/
            }


        });
    }

    private void proses_login1() {
        RequestParams params = new RequestParams();
        params.put("key", "482a5b91a13cb49d6ca5399013b08c1c");
        params.put("username", txt_user.getText().toString());
        params.put("password", txt_pass.getText().toString());
        Request.post("login", params, new AsyncHttpResponseHandler() {
            SimpleArcDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                dialog = new SimpleArcDialog(activity_login.this);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("sukses", "onSuccess: " + new String(responseBody));
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    JSONObject data = obj.getJSONObject("data");
                    Token.updateToken(getApplicationContext(), data.getString("token"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity_login.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isi_favorit() {
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username", new class_prosessql(activity_login.this).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url + "bacafavorit", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        new class_prosessql(activity_login.this).insert_favorit(jobj.getString("id_iklan"), new class_prosessql(activity_login.this).baca_profil()[1]);
                    }
                } catch (JSONException ex) {
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Failure", String.valueOf(statusCode));
            }
        });
    }

    public void dialog_sukses() {
        Intent intent = new Intent(getApplicationContext(), activity_main.class);
        startActivity(intent);
        /*snackBar = new SnackBar.Builder(activity_login.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), activity_main.class);
                        startActivity(intent);
                    }
                })
                .withMessage("Proses login sukses")
                .withActionMessage("Lanjut")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();*/
    }

    public void dialog_gagal(String pesan) {
        snackBar = new SnackBar.Builder(activity_login.this)
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

    public void dialog_forgot_gagal() {
        snackBar = new SnackBar.Builder(activity_login.this)
                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Log.e("klik", "tutup");
                    }
                })
                .withMessage("Proses ganti password gagal")
                .withActionMessage("Tutup")
                .withStyle(SnackBar.Style.INFO)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(activity_login.this, activity_main.class);
        startActivity(intent);
    }
}
