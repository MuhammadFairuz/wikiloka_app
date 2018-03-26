package aksamedia.wikiloka.Fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import aksamedia.wikiloka.Class.class_adapterlistpremium;
import aksamedia.wikiloka.Class.class_allpremium;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_listpremium;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Gajelas.tv_ubuntu;
import cz.msebera.android.httpclient.Header;

public class frag_premium extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RelativeLayout pilih_premium;
    private TextView tv_poin;
    private TextView ket_pilihpremium;
    private tv_ubuntu btn_beli;
    private RelativeLayout layout_kosong;
    private SnackBar snackBar;
    private SimpleArcDialog mDialog;
    private String respon_server;
    private AlertDialog alertDialogObject;
    private ArrayList<class_listpremium> class_barang;
    private ArrayList<Object> currentSelectedItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_premium, container, false);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rcv);
        pilih_premium=(RelativeLayout) view.findViewById(R.id.pilih_premium);
        tv_poin=(TextView) view.findViewById(R.id.status_poin);
        ket_pilihpremium=(TextView) view.findViewById(R.id.ket_pilihpremium);
        layout_kosong=(RelativeLayout) view.findViewById(R.id.layout_kosong);
        btn_beli=(tv_ubuntu) view.findViewById(R.id.btn_belipremium);
        pilih_premium.setOnClickListener(this);
        btn_beli.setOnClickListener(this);
        btn_beli.setVisibility(View.GONE);
        baca_poin_byusername();
        return view;
    }

    

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_belipremium){
            beli_premium(class_bantuan.base_url+"daftar_premium",class_bantuan.api_key, class_adapterlistpremium.id_premium.toString(),new class_prosessql(getActivity()).baca_member()[0]);
        }
        else if(v.getId()==R.id.pilih_premium) {
            tampil_premium(class_bantuan.base_url + "get_premium", class_bantuan.api_key, new class_prosessql(getActivity()).baca_profil()[1]);
        }
    }
    void tampil_premium(String url, String api_key,String username){
        RequestParams params = new RequestParams();
        params.put("key", api_key);
        params.put("username", username);
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
                    final String[] id_premium = new String[jarr.length()];
                    String[] nama_premium = new String[jarr.length()];
                    String[] status_web = new String[jarr.length()];
                    String[] gambar_premium = new String[jarr.length()];
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        gambar_premium[i] = jobj.getString("gambar_premium");
                        id_premium[i] = jobj.getString("id_premium");
                        nama_premium[i] = jobj.getString("nama_premium");
                        status_web[i] = jobj.getString("status_web");
                    }
                    mDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_layoutbelipremium, null);
                    ListView list_premium = (ListView) dialogView.findViewById(R.id.list);
                    list_premium.setAdapter(new class_allpremium(getActivity(), gambar_premium, id_premium, nama_premium, status_web));

                    list_premium.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            btn_beli.setVisibility(View.VISIBLE);
                            if (((TextView) view.findViewById(R.id.tv_statusweb)).getText().toString().equals("ada")) {
                                alertDialogObject.dismiss();
                                ket_pilihpremium.setText(((TextView) view.findViewById(R.id.tv_namapremium)).getText().toString());
                                class_adapterlistpremium.id_premium = ((TextView) view.findViewById(R.id.tv_idpremium)).getText().toString();

                                baca_iklan_byusername();
                            } else {
                                Toast.makeText(getActivity(), "Anda belum punya website.", Toast.LENGTH_SHORT).show();
                            }
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
    public void baca_iklan_byusername(){
        class_barang = new ArrayList<class_listpremium>();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(getActivity()).baca_member()[0]);
        params.put("no_premium",class_adapterlistpremium.id_premium);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"all_iklan_nopremium", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server= new String(responseBody);
                try
                {
                    JSONArray jarr = new JSONArray(respon_server);
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_listpremium(
                                jobj.getString("seo_iklan"),
                                jobj.getString("judul_iklan"),
                                jobj.getString("tanggal_post"),
                                jobj.getString("harga_iklan"),
                                jobj.getString("photo").replace(" ","%20")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    currentSelectedItems = new ArrayList<>();
                    class_adapterlistpremium class_adapterlistitem = new class_adapterlistpremium(class_barang, new class_adapterlistpremium.OnItemCheckListener() {
                        @Override
                        public void onItemCheck(class_listpremium item) {
                            currentSelectedItems.add(item);
                        }

                        @Override
                        public void onItemUncheck(class_listpremium item) {
                            currentSelectedItems.remove(item);
                        }
                    });
                    mRecyclerView.setAdapter(class_adapterlistitem);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    layout_kosong.setVisibility(View.INVISIBLE);
                }
                catch (JSONException ex) {
                    mDialog.dismiss();
                    Log.e("JSONException",ex.toString());
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    layout_kosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                Log.e("Failure",String.valueOf(statusCode));
                mRecyclerView.setVisibility(View.INVISIBLE);
                layout_kosong.setVisibility(View.VISIBLE);
            }
        });
    }
    public void baca_poin_byusername(){
        class_barang = new ArrayList<class_listpremium>();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(getActivity()).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"get_profil", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server= new String(responseBody);
                try
                {
                    JSONArray jarr = new JSONArray(respon_server);
                    String poin = "";
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        poin=jobj.getString("poin_member");
                    }
                    tv_poin.setText("Sisa Poin :" + poin);
                    mDialog.dismiss();
                    mRecyclerView.setAdapter(null);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    layout_kosong.setVisibility(View.VISIBLE);
                    ket_pilihpremium.setText("Pilih Iklan Premium");
                }
                catch (JSONException ex) {
                    mDialog.dismiss();
                    dialog_gagal("Gagal baca data poin");
                    tv_poin.setText("Sisa Poin :0");
                    mRecyclerView.setAdapter(null);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    layout_kosong.setVisibility(View.VISIBLE);
                    ket_pilihpremium.setText("Pilih Iklan Premium");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal("Gagal koneksi ke server");
                tv_poin.setText("Sisa Poin :0");
                mRecyclerView.setAdapter(null);
                mRecyclerView.setVisibility(View.INVISIBLE);
                layout_kosong.setVisibility(View.VISIBLE);
                ket_pilihpremium.setText("Pilih Iklan Premium");
            }
        });
    }
    public void beli_premium(String url,String api_key,String id_premium,String username){
        RequestParams params = new RequestParams();
        params.put("key",api_key);
        for(int i=0;i<currentSelectedItems.size();i++) {
            class_listpremium list_item = (class_listpremium)currentSelectedItems.get(i);
            params.put("seo_iklan["+String.valueOf(i)+"]", list_item.seo_iklan);
            Log.e("seo_iklan", String.valueOf(i));
        }
        params.put("id_premium", id_premium);
        params.put("username", username);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    mDialog.dismiss();
                    respon_server = new String(responseBody);
                    if (respon_server.equals("success")) {
                        dialog_gagal("Iklan anda telah terdaftar sebagai iklan pemium");
                        baca_poin_byusername();

                    }
                    else if (respon_server.equals("poin_kurang")) {
                        dialog_gagal("Gagal mendaftar iklan premium, poin Anda kurang");
                        baca_poin_byusername();

                    }
                }
                catch (Exception ex) {
                    mDialog.dismiss();
                    dialog_gagal("Gagal beli iklan premium");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal("Terjadi masalah sambungan");
            }
        });
    }

    public void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(getActivity())
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
