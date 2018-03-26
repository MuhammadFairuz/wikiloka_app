package aksamedia.wikiloka.Class;

import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;

/**
 * Created by Sony Vaio on 21/01/2016.
 */
public class class_bantuan {
    public static String alamat_server="http://wikiloka.com";
    public static String base_url="http://wikiloka.com/Api/";

    public static String api_key="482a5b91a13cb49d6ca5399013b08c1c";
    public static String menu_kategori="";
    public static String search_word="";
    public static String search_kota="";
    public static String search_seo_kategori="";
    //wikiloka_aksamedia

    public static String[] transmisi = new String[]{"Transmisi", "Manual", "Automatic", "Triptonic"};
    public static String[] jumlah = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", ">10"};
    public static String[] sertifikasi = new String[]{"Sertifikasi", "SHM - Sertifikat Hak Milik", "HGB - Hak Guna Bangunan", "Lainnya (PPJB,Girik, Adat, dll)"};
    public static String[] luas = new String[]{"30", "60", "90", "125", "150", "175", "225", "250", "500"};

    //merge_url
    /*public String merge_url(String url, String key, String value){
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(key, value));
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        return url;
    }*/

    public String merge(String url, List<NameValuePair> params){
        if(!url.contains("?"))
            url+="?";
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        return url;
    }
}
