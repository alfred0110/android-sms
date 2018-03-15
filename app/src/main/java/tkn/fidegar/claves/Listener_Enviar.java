package tkn.fidegar.claves;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tkn.fidegar.models.SmsModel;

/**
 * Created by Alfred on 14/03/2018.
 */

public class Listener_Enviar implements View.OnClickListener {

    private Context _context;
    private String _tag;
    RequestQueue _requestQueue;
    private static final String REGEX_DATOS_SMS = ".*(\\d{4}-\\d{4}-\\d{4}).*(\\d{12}).*(\\d{2}-[ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC]{3}).*";

    public Listener_Enviar(Context context, RequestQueue requestQueue, String tag) {
        _context = context;
        _tag = tag;
        _requestQueue = requestQueue;
    }

    @Override
    public void onClick(View v) {
        JSONArray datosClaves = new JSONArray();
        final EditText etDate = (EditText)((AppCompatActivity)_context).findViewById(R.id.etDate);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        String pref_celular = pref.getString("datoCelular", "5544332211");
        String pref_url = pref.getString("datoUrl", "http://www.tkinova.com/fidegarsms/sms/save");

        Cursor c = SmsFidegar.LeerSMS(_context.getContentResolver(), etDate.getText().toString() );
        while(c.moveToNext())
        {
            Long smsDate =    c.getLong(c.getColumnIndexOrThrow("date"));
            String smsBody =    c.getString(c.getColumnIndexOrThrow("body"));
            Date fechaEnvio = new Date(smsDate); //"MM/dd/yyyy").format(date);

            Pattern pattern = Pattern.compile(REGEX_DATOS_SMS);
            Matcher matcher = pattern.matcher(smsBody);
            matcher.find();

            String clave = matcher.group(1);
            String folio = matcher.group(2);
            String fechaVen = matcher.group(3);

            SmsModel model =new SmsModel(clave, folio, fechaVen, fechaEnvio);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            JSONObject obj = new JSONObject();
            try {
                obj.put("Clave",model.getClave());
                obj.put("Folio",model.getFolio());
                obj.put("FechaVigencia",model.getFechaVigencia());
                obj.put("FechaEnvio", formatter.format(model.getFechaEnvio()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            datosClaves.put(obj);
        }
        c.close();

        JSONObject jsonBody = new JSONObject();
        try {


            jsonBody.put("Celular", pref_celular);
            jsonBody.put("Claves", datosClaves);
        }
        catch (JSONException ex)
        {

        }
        Log.i(_tag,jsonBody.toString());

        JsonObjectRequest jsonObjReq = new  JsonObjectRequest(pref_url,jsonBody,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String desc = response.getString("Description");
                            Toast.makeText(_context, desc, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(_tag,response.toString());
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(_context, error.toString(), Toast.LENGTH_LONG).show();
                Log.e(_tag,error.toString());
            }
        });
        _requestQueue.add(jsonObjReq);
    }
}

