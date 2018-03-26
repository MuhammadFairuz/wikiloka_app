package aksamedia.wikiloka.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aksamedia.wikiloka.Class.class_adapterlistitem;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_iklan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;

public class fragment_iklanbaru extends Fragment {
    private static final String ARG_PARAM1 = "word";
    private static final String ARG_PARAM2 = "kota";
    private static final String ARG_PARAM3 = "seo_kategori";

    private static final String ARG_PARAM1a = "jenis";
    private static final String ARG_PARAM2a = "kategori";
    private static final String ARG_PARAM3a = "sub1_kategori";
    private static final String ARG_PARAM4a = "kota";
    private static final String ARG_PARAM5a = "sort";

    String mParam1,mParam2,mParam3;
    String mParam1a,mParam2a,mParam3a,mParam4a,mParam5a;
    RecyclerView mRecyclerView;
    ArrayList<class_iklan> class_barang;
    SimpleArcDialog mDialog;
    String respon_server="";
    LinearLayoutManager mLayoutManager;


    public fragment_iklanbaru() {

    }
    @Override
    public void onStart(){
        super.onStart();
        if (getArguments().getString(ARG_PARAM1) != null) {
            search_data(mParam1,mParam2,mParam3);
        }else if (getArguments().getString(ARG_PARAM1a) != null) {
            ambil_data(mParam1a, mParam2a, mParam3a, mParam4a,mParam5a);
        }
    }
    public static fragment_iklanbaru newInstance(String word,String kota,String seo_kategori) {
        fragment_iklanbaru fragment = new fragment_iklanbaru();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, word);
        args.putString(ARG_PARAM2, kota);
        args.putString(ARG_PARAM3, seo_kategori);
        fragment.setArguments(args);
        return fragment;
    }
    public static fragment_iklanbaru instance_menu(String jenis,String kategori,String sub1_kategori,String kota,String sort) {
        fragment_iklanbaru fragment = new fragment_iklanbaru();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1a, jenis);
        args.putString(ARG_PARAM2a, kategori);
        args.putString(ARG_PARAM3a, sub1_kategori);
        args.putString(ARG_PARAM4a, kota);
        args.putString(ARG_PARAM5a, sort);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString(ARG_PARAM1) != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }if (getArguments().getString(ARG_PARAM1a) != null) {
            mParam1a = getArguments().getString(ARG_PARAM1a);
            mParam2a = getArguments().getString(ARG_PARAM2a);
            mParam3a = getArguments().getString(ARG_PARAM3a);
            mParam4a = getArguments().getString(ARG_PARAM4a);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_iklankunonaktif, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        return view;
    }
    public void isiData() {
        class_barang = new ArrayList<class_iklan>();
        mRecyclerView.setAdapter(null);
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(getActivity()).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"all_iklan/baru", params, new AsyncHttpResponseHandler() {
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
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                "Surabaya",
                                jobj.getString("photo").replace(" ","%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(class_barang);
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
    public void search_data(String word,String kota,String seo_kategori) {
        class_barang = new ArrayList<class_iklan>();
        mRecyclerView.setAdapter(null);
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("word",word);
        params.put("kota", kota);
        params.put("seo_kategori",seo_kategori);
        params.put("jenis","baru");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"search", params, new AsyncHttpResponseHandler() {
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
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                "Surabaya",
                                jobj.getString("photo").replace(" ","%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(class_barang);
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
    public void ambil_data(String jenis,String kategori,String sub1_kategori,String kota,String sort) {
        final ArrayList<class_iklan> class_barang = new ArrayList<class_iklan>();
        final SimpleArcDialog mDialog = new SimpleArcDialog(getActivity());

        class_barang.clear();
        //mRecyclerView.setAdapter(null);
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("jenis",jenis);
        params.put("kategori",kategori);
        params.put("sub1_kategori",sub1_kategori);
        params.put("kota",kota);
        if(sort!="kosong"){
            params.put("sort",sort);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"kategori", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    Log.e("respon baru", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                "Surabaya",
                                jobj.getString("photo").replace(" ","%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog.dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(class_barang);
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
}
