package tkn.fidegar.claves;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alfred on 06/03/2018.
 */

public class SmsFidegar {

    public static Cursor LeerSMS(ContentResolver cr, String fecha) {
        //Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


        long dateStart = 0;
        long dateEnd = 0;

        try {
            dateStart = formatter.parse(fecha).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Add one day
        dateEnd = dateStart  + (1000 * 60 * 60 * 24);


        // List required columns
        String[] reqColumns = new String[]{ "_id", "date", "body"};

        // Get Content Resolver object, wich will del with Content Provider
        //Método con argumento para evitar SQL Injection
       return cr.query(inboxURI,reqColumns,
               "body LIKE ? AND date > ? AND date < ?",
               new String[]{
                       "%Tu clave para retiro sin tarjeta%",
                       String.valueOf(dateStart),
                       String.valueOf(dateEnd)
                        }
               , "_id ASC"); // ORDER BY _id ASC
    }
}
