package tkn.fidegar.claves;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Alfred on 06/03/2018.
 */

public class SmsFidegar {

    public static Cursor LeerSMS(ContentResolver cr)
    {
        //Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqColumns = new String[]{ "_id", "date", "body"};

        // Get Content Resolver object, wich will del with Content Provider
        //Método con argumento para evitar SQL Injection
       return cr.query(inboxURI,reqColumns,"body LIKE ?", new String[]{ "%Tu clave para retiro sin tarjeta%" }, "_id ASC"); // ORDER BY _id ASC
    }
}
