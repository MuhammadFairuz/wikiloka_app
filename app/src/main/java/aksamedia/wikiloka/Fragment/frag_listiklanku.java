package aksamedia.wikiloka.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;

public class frag_listiklanku extends Fragment implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    TextView tvPoinUser;
    private SimpleArcDialog mDialog;
    private String respon_server;
    private SnackBar snackBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_listiklanku, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tvPoinUser = (TextView) view.findViewById(R.id.tvPoinUser);
        baca_poin_byusername();

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((fragment_iklankuaktif) adapter.getItem(position)).isiData();
                } else if (position == 1) {
                    ((fragment_iklankunonaktif) adapter.getItem(position)).isiData();
                } else if (position == 2) {
                    ((fragment_iklankulaku) adapter.getItem(position)).isiData();
                }else if (position == 3) {
                    ((fragment_iklankuditolak) adapter.getItem(position)).isiData();
                }else if (position == 4) {
                    ((fragment_iklankupremium) adapter.getItem(position)).isiData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        return view;
    }


    private Drawable get_drawable(int draw){
        Drawable drawable = getResources().getDrawable(draw);
        //Drawable drawable1 = new ScaleDrawable(drawable, 0, 2, 2).getDrawable();

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 32, 32, false);
        Drawable drawable1= new BitmapDrawable(getResources(), bitmapResized);

        drawable1.setBounds(0, 0, 32, 32);
        return drawable1;
    }
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_iklanku, null);
        tabOne.setText("Iklan Aktif");
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanaktif), null, null);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_iklanku, null);
        tabTwo.setText("Iklan Tidak Aktif");
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklannonaktif), null, null);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_iklanku, null);
        tabThree.setText("Iklan Laku");
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanlaku), null, null);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_iklanku, null);
        tabfour.setText("Iklan Ditolak");
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanlaku), null, null);
        tabLayout.getTabAt(3).setCustomView(tabfour);

        TextView tabfive = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_iklanku, null);
        tabfive.setText("Iklan Premium");
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanlaku), null, null);
        tabLayout.getTabAt(4).setCustomView(tabfive);
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new fragment_iklankuaktif(), "ONE");
        adapter.addFrag(new fragment_iklankunonaktif(), "TWO");
        adapter.addFrag(new fragment_iklankulaku(), "THREE");
        adapter.addFrag(new fragment_iklankuditolak(), "FOUR");
        adapter.addFrag(new fragment_iklankupremium(), "FIVE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

    public void baca_poin_byusername(){
        RequestParams params = new RequestParams();
        params.put("key", class_bantuan.api_key);
        params.put("username", new class_prosessql(getActivity()).baca_member()[0]);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(class_bantuan.base_url + "get_profil", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                mDialog = new SimpleArcDialog(getActivity());
                mDialog.setConfiguration(new ArcConfiguration(getActivity()));
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                respon_server = new String(responseBody);
                Log.e("Poin", respon_server);
                try {
                    JSONArray jarr = new JSONArray(respon_server);
                    String poin = "";
                    for (int i = 0; i < jarr.length(); ++i) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        poin = jobj.getString("poin_member");
                    }
                    tvPoinUser.setText("Sisa Poin : " + poin);
                    mDialog.dismiss();
                } catch (JSONException ex) {
                    mDialog.dismiss();
                    dialog_gagal("Gagal baca data poin");
                    tvPoinUser.setText("Sisa Poin : 0");
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                dialog_gagal("Gagal koneksi ke server");
                tvPoinUser.setText("Sisa Poin : 0");
            }
        });
    }

    public void dialog_gagal(String pesan){
        snackBar = new SnackBar.Builder(getActivity())
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
}
