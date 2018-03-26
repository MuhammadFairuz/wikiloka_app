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

import aksamedia.wikiloka.Class.class_adapteriklanku_laku;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_iklanku_laku;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;

public class fragment_iklankupremium extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView mRecyclerView;


    String respon_server="";
    LinearLayoutManager mLayoutManager;


    public fragment_iklankupremium() {

    }
    public static fragment_iklankupremium newInstance() {
        fragment_iklankupremium fragment = new fragment_iklankupremium();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_iklankulaku, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        isiData();
        return view;
    }
    public void isiData() {
        final ArrayList<class_iklanku_laku> class_barang = new ArrayList<class_iklanku_laku>();
        final SimpleArcDialog[] mDialog = new SimpleArcDialog[1];
        mRecyclerView.setAdapter(null);
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(getActivity()).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"all_iklan_username_premium", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog[0] = new SimpleArcDialog(getActivity());
                mDialog[0].setConfiguration(new ArcConfiguration(getActivity()));
                mDialog[0].show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    Log.e("respon server", respon_server);

                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        class_barang.add(new class_iklanku_laku(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                "Surabaya",
                                jobj.getString("photo").replace(" ", "%20")));
                    }
                    mDialog[0].dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapteriklanku_laku class_adapterlistitem = new class_adapteriklanku_laku(class_barang);
                    mRecyclerView.setAdapter(class_adapterlistitem);
                } catch (JSONException ex) {
                    mDialog[0].dismiss();
                    Log.e("JSONException", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog[0].dismiss();
                Log.e("Failure", String.valueOf(statusCode));
            }
        });
    }
}
