package aksamedia.wikiloka.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class class_helpersqlite extends SQLiteOpenHelper{
	private static final String NAMA_DB = "db_wikiloka";
	public class_helpersqlite(Context context) {
	super(context, NAMA_DB, null, 1 );
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
	public void buat_tbl_member(SQLiteDatabase db){
	 	db.execSQL("CREATE TABLE if not exists tbl_member (id_member INTEGER PRIMARY KEY   AUTOINCREMENT,username TEXT,email TEXT,token TEXT);");
	}
	public void buat_tbl_image(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE if not exists tbl_image (id_image INTEGER PRIMARY KEY   AUTOINCREMENT,email TEXT,url_image TEXT);");
	}
	public void buat_tbl_iklan(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE if not exists tbl_iklan(id_iklan INTEGER PRIMARY KEY AUTOINCREMENT,status TEXT);");
	}
	public void buat_tabel_menu(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_menu(id_menu integer primary key autoincrement,menu text,url text);");
	}
	public void buat_tbl_profil(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists profil_akun(id_profil integer primary key autoincrement," +
				"username text," +
				"provinsi_member text,"+
				"kota_member text," +
				"nama_member text," +
				"telepon_member text," +
				"bbm_member text," +
				"wa_member text);");
	}
	public void buat_tbl_favorit(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_favorit(id_favorit integer primary key autoincrement," +
				"id_iklan text," +
				"username text);");
	}
	public void buat_tbl_filter_motor(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_motor(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"merk text," +
				"tipe_kendaraan text," +
				"harga_awal text," +
				"harga_akhir text," +
				"tahun text," +
				"username text);");
	}
	public void buat_tbl_filter_rumah_tangga(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_rumah_tangga(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
	public void buat_tbl_filter_travel(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_travel(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
	public void buat_tbl_filter_properti(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_properti(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"filter text," +
				"kamar_tidur text," +
				"kamar_mandi text," +
				"sertifikasi text," +
				"harga_awal text," +
				"harga_akhir text," +
				"lantai text," +
				"luas_bangunan_awal text," +
				"luas_bangunan_akhir text," +
				"luas_tanah_awal text," +
				"luas_tanah_akhir text," +
				"username text);");
	}
	public void buat_tbl_filter_mobil(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_mobil(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"merk text," +
				"tipe_kendaraan text," +
				"harga_awal text," +
				"harga_akhir text," +
				"tahun text," +
				"username text," +
				"transmisi text);");
	}
	public void buat_tbl_filter_olahraga(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_olahraga(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
	public void buat_tbl_filter_kuliner(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_kuliner(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
	public void buat_tbl_filter_fashion(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_fashion(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
	public void buat_tbl_filter_elektronik(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists tbl_filter_elektronik(id_history integer primary key autoincrement," +
				"urutan text," +
				"sub_kategori text," +
				"username text);");
	}
}
