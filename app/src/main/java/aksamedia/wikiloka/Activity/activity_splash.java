package aksamedia.wikiloka.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_helpersqlite;
import aksamedia.wikiloka.progressbar.CircleProgressBar;
import aksamedia.wikiloka.progressbar.utils.OnProgressViewListener;


public class activity_splash extends AppCompatActivity {
    Button btn;
    private CircleProgressBar circleProgressBar;
    private class_helpersqlite db_helper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db_helper = new class_helpersqlite(this);
        db = db_helper.getWritableDatabase();
        db_helper.buat_tbl_member(db);
        db_helper.buat_tbl_profil(db);
        db_helper.buat_tbl_favorit(db);
        db_helper.buat_tbl_iklan(db);
        db_helper.buat_tbl_image(db);
        db_helper.buat_tabel_menu(db);
         circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progress);
        circleProgressBar.setProgressIndeterminateAnimation(10000);
        circleProgressBar.setOnProgressViewListener(new OnProgressViewListener() {
           @Override
            public void onFinish() {
               finish();
               Intent intent=new Intent(activity_splash.this,activity_main.class);
               startActivity(intent);
            }

            @Override
            public void onProgressUpdate(float progress) {
                circleProgressBar.setText("" + (int) progress + " %");
                circleProgressBar.setTextColor(R.color.colorFtBot);
            }
        });
    }
}
