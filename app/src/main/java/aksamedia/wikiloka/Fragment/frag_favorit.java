package aksamedia.wikiloka.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import java.util.List;

import aksamedia.wikiloka.Class.class_adapterfavorit;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_favorit;
import aksamedia.wikiloka.Class.class_iklan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Dinar on 11/3/2016.
 */
public class frag_favorit extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<class_iklan> iklan;
    private ArrayList<class_favorit> class_barang;

    String respon_server="";
    LinearLayoutManager mLayoutManager;

    public frag_favorit(){
    }

    public static frag_favorit newInstance(){
        frag_favorit fragment = new frag_favorit();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        isiData();
    }

    private void isiData() {
        class_barang = new ArrayList<class_favorit>();
        mRecyclerView.setAdapter(null);
        final SimpleArcDialog[] mDialog = new SimpleArcDialog[1];
        //final ArrayList<class_iklanku_on> class_barang = new ArrayList<class_iklanku_on>();
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username",new class_prosessql(getActivity()).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();


        iklan = new ArrayList<>();
        client = new AsyncHttpClient();
        client.post(class_bantuan.base_url+"bacafavorit", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog[0] = new SimpleArcDialog(getActivity());
                mDialog[0].setConfiguration(new ArcConfiguration(getActivity()));
                mDialog[0].show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("Respon Kategori", new String(responseBody));
                try {
                    JSONArray jarr = new JSONArray(new String(responseBody));
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        /*iklan.add(new class_iklan(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getJSONObject("kota").getString("nama_area"),
                                jobj.getString("gambar").replace(" ", "%20"),
                                jobj.getString("seo_iklan")));*/
                        class_barang.add(new class_favorit(
                                jobj.getString("id_iklan"),
                                "FAVORIT",
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getJSONObject("kota").getString("nama_area"),
                                jobj.getString("gambar").replace(" ", "%20"),
                                jobj.getString("seo_iklan")));
                    }
                    mDialog[0].dismiss();

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapterfavorit class_adapterlistitem = new class_adapterfavorit(class_barang);
                    class_adapterlistitem.notifyDataSetChanged();
                    mRecyclerView.setAdapter(class_adapterlistitem);
                    /*class_adapterlistitem class_adapterlistitem = new class_adapterlistitem(iklan);
                    mRecyclerView.setAdapter(class_adapterlistitem);*/

                } catch (JSONException ex) {
                    mDialog[0].dismiss();
                    Log.e("JSONException", ex.toString());
                } catch (Exception ex) {
                    mDialog[0].dismiss();
                    Log.e("tes exception", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog[0].dismiss();
                Log.e("Failure", String.valueOf(statusCode));
            }
        });



        /*client.post(class_bantuan.base_url+"bacafavorit", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog[0] = new SimpleArcDialog(getActivity());
                mDialog[0].setConfiguration(new ArcConfiguration(getActivity()));
                mDialog[0].show();
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
                        class_barang.add(new class_iklanku_on(
                                jobj.getString("id_iklan"),
                                jobj.getString("judul_iklan"),
                                jobj.getString("harga_iklan"),
                                jobj.getString("kota"),
                                jobj.getString("gambar").replace(" ","%20")));
                    }
                    mDialog[0].dismiss();
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    class_adapteriklanku_on class_adapterlistitem = new class_adapteriklanku_on(class_barang);
                    mRecyclerView.setAdapter(class_adapterlistitem);
                }
                catch (JSONException ex) {
                    mDialog[0].dismiss();
                    Log.e("JSONException",ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog[0].dismiss();
                Log.e("Failure",String.valueOf(statusCode));
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_favorit, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
