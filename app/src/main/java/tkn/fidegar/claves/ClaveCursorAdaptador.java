package tkn.fidegar.claves;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alfred on 06/03/2018.
 */

public class ClaveCursorAdaptador extends CursorAdapter {

    private static final String REGEX_DATOS_SMS = ".*(\\d{4}-\\d{4}-\\d{4}).*(\\d{12}).*(\\d{2}-[ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC]{3}).*";


    public ClaveCursorAdaptador(Context context, String fecha) {
        super(context, SmsFidegar.LeerSMS(context.getContentResolver(), fecha), 0);
    }

    //The newView is used to infalte a new view and return it,
    //you don´t bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_clave,parent,false);
    }

    // The bind method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvCons = (TextView) view.findViewById(R.id.tvConsecutivo);
        TextView tvClave = (TextView) view.findViewById(R.id.tvClave);
        TextView tvFolio = (TextView) view.findViewById(R.id.tvFolio);
        TextView tvFechaVenc = (TextView) view.findViewById(R.id.tvFechaVenc);

        //Extract properties from cursor
        String smsId =      cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        Long smsDate =    cursor.getLong(cursor.getColumnIndexOrThrow("date"));
        String smsBody =    cursor.getString(cursor.getColumnIndexOrThrow("body"));

        Date date = new Date(smsDate); //"MM/dd/yyyy").format(date);

        boolean t = smsBody.matches(REGEX_DATOS_SMS);
        Pattern pattern = Pattern.compile(REGEX_DATOS_SMS);
        Matcher matcher = pattern.matcher(smsBody);
        matcher.find();

        String clave = matcher.group(1);
        String folio = matcher.group(2);
        String fechaVen = matcher.group(3);

        tvCons.setText(smsId);
        tvClave.setText(clave);
        tvFolio.setText(folio);
        tvFechaVenc.setText(fechaVen);
    }
}
