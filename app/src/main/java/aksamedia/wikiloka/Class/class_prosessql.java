package aksamedia.wikiloka.Class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class class_prosessql {
    Context ctx1;
    String [] text_menu;
    String [] url_menu;
    public class_prosessql(Context ctx){
        this.ctx1=ctx;
    }
    public void insertdatamember(String username,String email,String token){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement stmt=db.compileStatement("insert into tbl_member (id_member,username,email,token) values(?,?,?,?)");
        stmt.bindString(1,"1");
        stmt.bindString(2, username);
        stmt.bindString(3, email);
        stmt.bindString(4, token);
        stmt.execute();
        stmt.close();
        db.close();
    }
    public String cek_session(){
        String ada="";
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c=db.rawQuery("select * from tbl_member",null);
        if(c.moveToFirst())
        {
            ada="ada";
        }
        else
        {
            ada="kosong";
        }
        c.close();
        db.close();
        return ada;
    }
    public void insert_menuutama(String menu,String url){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        //db.execSQL("delete from tbl_menu;");
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_menu (menu,url) values (?,?)");
        sql_insert.bindString(1,menu);
        sql_insert.bindString(2, url);
        sql_insert.execute();
    }
    public void baca_menuutama(){
        try {
            class_helpersqlite db_helper=new class_helpersqlite(ctx1);
            SQLiteDatabase db=db_helper.getWritableDatabase();
            Cursor c = db.rawQuery("select * from tbl_menu", null);
            Log.e("data menu", String.valueOf(c.getCount()));
            this.text_menu=new String[c.getCount()];
            this.url_menu=new String[c.getCount()];
            if (c.moveToFirst()) {
                int i=0;
                do {
                    text_menu[i]=c.getString(1);
                    url_menu[i]=c.getString(2);
                    i++;
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insert_profil(String username,String provinsi,String kota_member,String nama_member,String telepon_member,String bbm_member,String wa_member){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into profil_akun (" +
                "username," +
                "provinsi_member," +
                "kota_member," +
                "nama_member," +
                "telepon_member," +
                "bbm_member," +
                "wa_member) values (?,?,?,?,?,?,?)");
        sql_insert.bindString(1,username);
        sql_insert.bindString(2,provinsi);
        sql_insert.bindString(3,kota_member);
        sql_insert.bindString(4,nama_member);
        sql_insert.bindString(5, telepon_member);
        sql_insert.bindString(6,bbm_member);
        sql_insert.bindString(7,wa_member);
        sql_insert.execute();
    }
    public String[] baca_profil(){
        String []output=new String[7];
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        try{
            Cursor c = db.rawQuery("select * from profil_akun", null);

            if (c.moveToFirst()) {
                int i=0;
                do {
                    output[0]=c.getString(0);
                    output[1]=c.getString(1);
                    output[2]=c.getString(2);
                    output[3]=c.getString(3);
                    output[4]=c.getString(4);
                    output[5]=c.getString(5);
                    output[6]=c.getString(6);
                    i++;
                } while (c.moveToNext());
            }
            c.close();
            return output;
        }catch (SQLiteException ex){
            output[0]="kosong";output[1]="kosong";output[2]="kosong";output[3]="kosong";output[4]="kosong";output[5]="kosong";output[6]="kosong";
            return output;
        }

    }
    public String[] baca_member(){
        String []output=new String[3];
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from tbl_member", null);

        if (c.moveToFirst()) {
            int i=0;
            do {
                output[0]=c.getString(1);
                output[1]=c.getString(2);
                output[2]=c.getString(3);
                i++;
            } while (c.moveToNext());
        }
        c.close();
        return output;
    }
    public void insert_favorit(String id_iklan,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_favorit (" +
                "id_iklan," +
                "username) values (?,?)");
        sql_insert.bindString(1,id_iklan);
        sql_insert.bindString(2,username);
        sql_insert.execute();
        Log.e("insert favorit", id_iklan);
    }
    public void hapus_favorit(String id_iklan,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_hapus=db.compileStatement("delete from tbl_favorit where id_iklan=? and username=?");
        sql_hapus.bindString(1,id_iklan);
        sql_hapus.bindString(2,username);
        sql_hapus.execute();
    }
    public String cek_favorit(String id_iklan,String username){
        String ada="";
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        try{
            Cursor c = db.rawQuery("select * from tbl_favorit where id_iklan='"+id_iklan+"' and username='"+username+"'", null);
            if (c.moveToFirst()) {
                ada="true";
            }
            else{
                ada="false";
            }
        }catch (SQLiteException ex){
            ada="false";
        }
        return ada;
    }
    public void insert_filter_motor(String urutan,String sub_kategori,String merk,String tipe_kendaraan,String harga_awal,String harga_akhir,String tahun,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_motor (" +
                "urutan," +
                "sub_kategori," +
                "merk," +
                "tipe_kendaraan," +
                "harga_awal," +
                "harga_akhir," +
                "tahun," +
                "username) values (?,?,?,?,?,?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,merk);
        sql_insert.bindString(4,tipe_kendaraan);
        sql_insert.bindString(5,harga_awal);
        sql_insert.bindString(6,harga_akhir);
        sql_insert.bindString(7,tahun);
        sql_insert.bindString(8, username);
        sql_insert.execute();
    }
    public void insert_filter_rumah_tangga(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_rumah_tangga (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public void insert_filter_travel(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_travel (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public void insert_filter_properti(String urutan,String sub_kategori,String filter,String kamar_tidur,String kamar_mandi,String sertifikasi,String harga_awal,String harga_akhir,String lantai,String luas_bangunan_awal,String luas_bangunan_akhir,String luas_tanah_awal,String luas_tanah_akhir,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_properti (" +
                "urutan," +
                "sub_kategori," +
                "filter," +
                "kamar_tidur," +
                "kamar_mandi," +
                "sertifikasi," +
                "harga_awal," +
                "harga_akhir," +
                "lantai," +
                "luas_bangunan_awal," +
                "luas_bangunan_akhir," +
                "luas_tanah_awal," +
                "luas_tanah_akhir," +
                "username) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,filter);
        sql_insert.bindString(4,kamar_tidur);
        sql_insert.bindString(5,kamar_mandi);
        sql_insert.bindString(6,sertifikasi);
        sql_insert.bindString(7,harga_awal);
        sql_insert.bindString(8,harga_akhir);
        sql_insert.bindString(9,lantai);
        sql_insert.bindString(10,luas_bangunan_awal);
        sql_insert.bindString(11,luas_bangunan_akhir);
        sql_insert.bindString(12,luas_tanah_awal);
        sql_insert.bindString(13,luas_tanah_akhir);
        sql_insert.bindString(14,username);
        sql_insert.execute();
    }
    public void insert_filter_mobil(String urutan,String sub_kategori,String merk,String tipe_kendaraan,String harga_awal,String harga_akhir,String tahun,String username,String transmisi){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_mobil (" +
                "urutan," +
                "sub_kategori," +
                "merk," +
                "tipe_kendaraan," +
                "harga_awal," +
                "harga_akhir," +
                "tahun," +
                "username," +
                "transmisi) values (?,?,?,?,?,?,?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,merk);
        sql_insert.bindString(4,tipe_kendaraan);
        sql_insert.bindString(5,harga_awal);
        sql_insert.bindString(6,harga_akhir);
        sql_insert.bindString(7,tahun);
        sql_insert.bindString(8,username);
        sql_insert.bindString(9,transmisi);
        sql_insert.execute();
    }
    public void insert_filter_olahraga(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_olahraga (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public void insert_filter_kuliner(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_kuliner (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public void insert_filter_fashion(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_fashion (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public void insert_filter_elektronik(String urutan,String sub_kategori,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        SQLiteStatement sql_insert=db.compileStatement("insert into tbl_filter_elektronik (" +
                "urutan," +
                "sub_kategori," +
                "username) values (?,?,?)");
        sql_insert.bindString(1,urutan);
        sql_insert.bindString(2,sub_kategori);
        sql_insert.bindString(3,username);
        sql_insert.execute();
    }
    public String[] get_history(String tabel,String username){
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + tabel + " where username = '" + username + "' order by id_history desc limit 1", null);
        String []output=new String[c.getColumnCount()];

        if (c.moveToFirst()) {
            for (int i=0; i < c.getColumnCount(); i++){
                output[i]=c.getString(i);
            }
        }
        c.close();
        return output;
    }
    public String get_username(){
        String ada="";
        class_helpersqlite db_helper=new class_helpersqlite(ctx1);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Cursor c=db.rawQuery("select * from tbl_member",null);
        if(c.moveToFirst())
        {
            ada=c.getString(1);
        }
        else
        {
            ada="kosong";
        }
        c.close();
        db.close();
        return ada;
    }
}
