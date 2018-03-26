package aksamedia.wikiloka.Class;

/**
 * Created by Dinar on 9/21/2016.
 */
public class class_berita {
    String id_berita;
    String judul_berita;
    String seo_berita;
    String url;
    String sumber_berita;

    public class_berita(String idberita, String judulberita, String seoberita, String sumberberita, String urlberita) {
        this.id_berita=idberita;
        this.judul_berita=judulberita;
        this.seo_berita=seoberita;
        this.sumber_berita=sumberberita;
        this.url = urlberita;
    }
}
