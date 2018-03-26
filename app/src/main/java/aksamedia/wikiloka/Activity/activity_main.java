package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import aksamedia.wikiloka.Adapter.custom_navview_member;
import aksamedia.wikiloka.Adapter.pagerAdapter;
import aksamedia.wikiloka.Class.class_bantuan;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.Class.class_menumember;
import aksamedia.wikiloka.Master.Token;
import aksamedia.wikiloka.R;
import layout.artikel;
import layout.berita;
import layout.home;
import layout.liputan;
import layout.top_50;
import layout.top_kuliner;
import layout.top_promo;
import layout.top_website;

public class activity_main extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    FragmentManager fragment_man;
    FragmentTransaction fragment_trans;
    TextView tv;
    ImageButton img;
    TextView menu1, menu2, menu3, menu4, menu5;
    LinearLayout menu_1, menu_2, menu_3, menu_4, menu_5;
    ImageView im_menu1, im_menu2, im_menu3, im_menu4, im_menu5;
    //class_helpersqlite db_helper;
    //SQLiteDatabase db;
    custom_navview_member navview_member;
    private MaterialSearchView searchView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static void set_navmember(custom_navview_member navigationView, AppCompatActivity ctx) {
        View view = ctx.getLayoutInflater().inflate(R.layout.navigation_view, null);

        navigationView.setHeaderView(view, 10);
        int[] icon = {
                R.drawable.ic_homee,
                R.drawable.ic_fitur_premium,
                R.drawable.ic_profile,
                R.drawable.ic_management_iklan,
                R.drawable.ic_free_website,
                R.drawable.ic_management_premium,
                R.drawable.ic_logout,
                R.drawable.blank,
                R.drawable.blank,
                R.drawable.blank,
                R.drawable.blank,};
        String[] expand = {"", "", "", "", "", "", "", "ada", "", "", ""};
        navigationView.setAdapter(new class_menumember(ctx,
                icon,
                ctx.getResources().getStringArray(R.array.array_text_menumember), expand), ctx, ctx.getResources().getStringArray(R.array.kategory));
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter adapter = new pagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new home(), "BERANDA");
        adapter.addFragment(new top_50(), "TOP 50");
        adapter.addFragment(new top_kuliner(), "TOP KULINER");
        adapter.addFragment(new top_website(), "TOP WEBSITE");
        adapter.addFragment(new top_promo(), "TOP PROMO");
        adapter.addFragment(new artikel(), "ARTIKEL");
        adapter.addFragment(new liputan(), "LIPUTAN");
        adapter.addFragment(new berita(), "BERITA");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setuptoolbar();
        setupmenulayout();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        navview_member = (custom_navview_member) findViewById(R.id.navbar);
        set_navmember(navview_member, this);
        Log.d("Token", "onCreate: " + Token.getToken(this));
        menu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadawal();
            }
        });
        menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styledipilih("menu2");
                finish();
                Intent intent = new Intent(activity_main.this, activity_login.class);
                intent.putExtra("aksesform", "form_pengaturan");
                startActivity(intent);
            }
        });
        menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styledipilih("menu3");
                class_helpersqlite db_helper = new class_helpersqlite(activity_main.this);
                SQLiteDatabase db = db_helper.getWritableDatabase();
                db.execSQL("DROP TABLE if exists tbl_iklan;");
                db_helper.buat_tbl_iklan(db);
                db.execSQL("DROP TABLE if exists tbl_image;");
                db_helper.buat_tbl_image(db);
                db.close();
                finish();
                Intent intent = new Intent(activity_main.this, activity_login.class);
                intent.putExtra("aksesform", "form_iklan");
                startActivity(intent);
            }
        });
        menu_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styledipilih("menu4");
                finish();
                Intent intent = new Intent(activity_main.this, activity_login.class);
                intent.putExtra("aksesform", "form_favorit");
                startActivity(intent);
            }
        });
        menu_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(activity_main.this)
                        .setSheet(R.menu.menu_bottomsheet)
                        .setTitle("Menu")
                        .setListener(new BottomSheetListener() {
                            @Override
                            public void onSheetShown() {

                            }

                            @Override
                            public void onSheetItemSelected(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.petunjuk:
                                        Toast.makeText(activity_main.this, "petunjuk", Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.tentang:
                                        Toast.makeText(activity_main.this, "tentang", Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.keluar:
                                        keluar_aplikasi();
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(activity_main.this, "default", Toast.LENGTH_SHORT).show();
                                        ;
                                }
                            }

                            @Override
                            public void onSheetDismissed(int i) {

                            }
                        })
                        .show();
            }
        });
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        loadawal();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void resetcolor() {
        im_menu1.setColorFilter(null);
        menu1.setTextColor(getResources().getColor(R.color.colorFtBot));
        im_menu2.setColorFilter(null);
        menu2.setTextColor(getResources().getColor(R.color.colorFtBot));
        im_menu3.setColorFilter(null);
        menu3.setTextColor(getResources().getColor(R.color.colorFtBot));
        im_menu4.setColorFilter(null);
        menu4.setTextColor(getResources().getColor(R.color.colorFtBot));
        im_menu5.setColorFilter(null);
        menu5.setTextColor(getResources().getColor(R.color.colorFtBot));
    }

    public void styledipilih(String dipilih) {
        if (dipilih.equals("menu1")) {
            resetcolor();
            im_menu1.setColorFilter(Color.argb(255, 255, 255, 255));
            menu1.setTextColor(Color.WHITE);
        } else if (dipilih.equals("menu2")) {
            resetcolor();
            im_menu2.setColorFilter(Color.argb(255, 255, 255, 255));
            menu2.setTextColor(Color.WHITE);
        } else if (dipilih.equals("menu3")) {
            resetcolor();
            im_menu3.setColorFilter(Color.argb(255, 255, 255, 255));
            menu3.setTextColor(Color.WHITE);
        } else if (dipilih.equals("menu4")) {
            resetcolor();
            im_menu4.setColorFilter(Color.argb(255, 255, 255, 255));
            menu4.setTextColor(Color.WHITE);
        } else if (dipilih.equals("menu5")) {
            resetcolor();
            im_menu5.setColorFilter(Color.argb(255, 255, 255, 255));
            menu5.setTextColor(Color.WHITE);
        }
    }

    public void setuptoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WIKILOKA");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupmenulayout() {

        menu_1 = (LinearLayout) findViewById(R.id.menu_1);
        menu_2 = (LinearLayout) findViewById(R.id.menu_2);
        menu_3 = (LinearLayout) findViewById(R.id.menu_3);
        menu_4 = (LinearLayout) findViewById(R.id.menu_4);
        menu_5 = (LinearLayout) findViewById(R.id.menu_5);
        menu1 = (TextView) findViewById(R.id.menu1);
        menu1.setTypeface(Typeface.createFromAsset(getAssets(), "font_oxygen.ttf"));
        menu2 = (TextView) findViewById(R.id.menu2);
        menu2.setTypeface(Typeface.createFromAsset(getAssets(), "font_oxygen.ttf"));
        menu3 = (TextView) findViewById(R.id.menu3);
        menu3.setTypeface(Typeface.createFromAsset(getAssets(), "font_oxygen.ttf"));
        menu4 = (TextView) findViewById(R.id.menu4);
        menu4.setTypeface(Typeface.createFromAsset(getAssets(), "font_oxygen.ttf"));
        menu5 = (TextView) findViewById(R.id.menu5);
        menu5.setTypeface(Typeface.createFromAsset(getAssets(), "font_oxygen.ttf"));
        im_menu1 = (ImageView) findViewById(R.id.imageView1);
        im_menu2 = (ImageView) findViewById(R.id.imageView2);
        im_menu3 = (ImageView) findViewById(R.id.imageView3);
        im_menu4 = (ImageView) findViewById(R.id.imageView4);
        im_menu5 = (ImageView) findViewById(R.id.imageView5);
    }

    public void loadawal() {
//        fragment_man = getSupportFragmentManager();
//        fragment_trans = fragment_man.beginTransaction();
//        fragment_trans.replace(R.id.fragment_utama, new fragment_beranda()).commit();
//        styledipilih("menu1");
    }

    public void keluar_aplikasi() {
//        db_helper = new class_helpersqlite(activity_main.this);
//        db = db_helper.getWritableDatabase();
//        SQLiteStatement stmt = db.compileStatement("delete from tbl_member");
//        stmt.execute();
//        stmt.close();
//        db.close();
    }

    @Override
    public void onBackPressed() {
        if (class_bantuan.menu_kategori.equals("topmenu")) {
            loadawal();
            class_bantuan.menu_kategori = "";
        } else {
            Toast.makeText(this, "Tekan BACK lagi untuk keluar", Toast.LENGTH_SHORT).show();
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
