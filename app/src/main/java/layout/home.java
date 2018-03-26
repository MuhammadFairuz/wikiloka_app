package layout;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.leo.simplearcloader.SimpleArcDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import aksamedia.wikiloka.Adapter.CustomPagerAdapter;
import aksamedia.wikiloka.R;
import aksamedia.wikiloka.WebRequest.Request;
import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    View view;
    ArrayList<String> url_slider = new ArrayList<>();
    ListView listmenu;

    public home() {
        // Required empty public constructor
    }

    public ArrayList<String> getUrl_slider() {
        return url_slider;
    }

    public void setUrl_slider() {
        if (getUrl_slider().isEmpty())
            Request.get("slider", null, new AsyncHttpResponseHandler() {
                SimpleArcDialog dialog;

                @Override
                public void onStart() {
                    super.onStart();
                    dialog = new SimpleArcDialog(getContext());
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

                    try {
                        JSONObject object = new JSONObject(new String(responseBody));
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject temp = data.getJSONObject(i);
                            String urlimage = "http://wikiloka.com/images/slider/" + temp.getString("gambar_slider");
                            Log.d("slider", "onSuccess: " + urlimage);
                            url_slider.add(urlimage);
                            pager();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        listmenu = (ListView) view.findViewById(R.id.list_menu);
        if (getUrl_slider().isEmpty())
            setUrl_slider();
        else
            pager();
        return view;
    }

    private void pager() {
        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.vPagerHome);
        final String[] url = new String[url_slider.size()];
        for (int i = 0; i < getUrl_slider().size(); i++) {
            url[i] = getUrl_slider().get(i);
        }
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), url);
        mViewPager.setAdapter(mCustomPagerAdapter);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicatorHome);
        indicator.setViewPager(mViewPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            int currentPage = 0;

            public void run() {
                if (currentPage == url.length) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 5000);
    }

}
