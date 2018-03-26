package aksamedia.wikiloka.Class;

/**
 * Created by ANONYMOUS on poin_10/01/2016.
 */
public class class_favorit {
    String label;
    String judul;
    String harga;
    String deskripsi;
    String url;
    String id_iklan;
    String seo_iklan;

    public class_favorit(String id_iklan, String label, String judul, String harga, String deskripsi, String url, String seo_iklan) {
        this.id_iklan=id_iklan;
        this.label=label;
        this.judul = judul;
        this.harga =harga;
        this.deskripsi = deskripsi;
        this.url=url;
        this.seo_iklan=seo_iklan;
    }
}


