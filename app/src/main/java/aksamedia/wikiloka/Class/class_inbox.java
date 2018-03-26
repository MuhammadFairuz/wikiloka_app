package aksamedia.wikiloka.Class;

/**
 * Created by Dinar on 11/19/2016.
 */

public class class_inbox {
    String title, picture, last_message, last_sender, key;
    long last_time;
    public class_inbox(String title, String picture, String last_message, String last_sender, long last_time, String key){
        this.title = title;
        this.picture = picture;
        this.last_message = last_message;
        this.last_sender = last_sender;
        this.last_time = last_time;
        this.key = key;
    }
}
