package aksamedia.wikiloka.Class;

/**
 * Created by Dinar on 11/20/2016.
 */

public class class_message {
    String message, name, usernames;
    long sendtime;
    public class_message(String message, String name, String usernames, long sendtime){
        this.message= message;
        this.name = name;
        this.usernames= usernames;
        this.sendtime = sendtime;
    }
}
