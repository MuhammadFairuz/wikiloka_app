package aksamedia.wikiloka.Class;

/**
 * Created by ANONYMOUS on poin_10/01/2016.
 */
public class class_iklanku_laku {
    String label;
    String judul;
    String harga;
    String deskripsi;
    String url;
    String id_iklan;

    public class_iklanku_laku(String id_iklan, String label, String judul, String harga, String deskripsi, String url) {
        this.id_iklan=id_iklan;
        this.label=label;
        this.judul = judul;
        this.harga =harga;
        this.deskripsi = deskripsi;
        this.url=url;
    }
}


