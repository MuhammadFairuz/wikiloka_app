package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Fragment.fragment_iklankuaktif;
import aksamedia.wikiloka.Fragment.fragment_iklankulaku;
import aksamedia.wikiloka.Fragment.fragment_iklankunonaktif;

public class activity_listiklanku extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listiklanku);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0) {
                    ((fragment_iklankuaktif) adapter.getItem(position)).isiData();
                }
                else if(position==1) {
                    ((fragment_iklankunonaktif) adapter.getItem(position)).isiData();
                }
                else if(position==2) {
                    ((fragment_iklankulaku) adapter.getItem(position)).isiData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setuptoolbar();
    }
    public void setuptoolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("IKLANKU");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabOne.setText("AKTIF");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanaktif), null, null);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabTwo.setText("NON-AKTIF");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklannonaktif), null, null);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_iklanku, null);
        tabThree.setText("LAKU");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(null, get_drawable(R.drawable.ic_iklanlaku), null, null);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new fragment_iklankuaktif(), "ONE");
        adapter.addFrag(new fragment_iklankunonaktif(), "TWO");
        adapter.addFrag(new fragment_iklankulaku(), "THREE");
        viewPager.setAdapter(adapter);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent=new Intent(activity_listiklanku.this,activity_main.class);
        startActivity(intent);
    }


}
