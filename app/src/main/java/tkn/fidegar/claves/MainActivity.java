package tkn.fidegar.claves;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tkn.fidegar.models.SmsModel;

public class MainActivity extends AppCompatActivity {

    private static final String REGEX_DATOS_SMS = ".*(\\d{4}-\\d{4}-\\d{4}).*(\\d{12}).*(\\d{2}-[ENE,FEB,MAR,ABR,MAY,JUN,JUL,AGO,SEP,OCT,NOV,DIC]{3}).*";

    ClaveCursorAdaptador adapter;
    ListView lvSms;
    Button btnConsultar;
    EditText etDate;
    String _tag = "__SMS__";
    RequestQueue peticionesHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        peticionesHttp =  Volley.newRequestQueue(this);
        lvSms = (ListView) findViewById(R.id.lvClaves);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                JSONArray datosClaves = new JSONArray();

                Cursor c = SmsFidegar.LeerSMS(getContentResolver());
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

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("Clave",model.getClave());
                        obj.put("Folio",model.getFolio());
                        obj.put("FechaVigencia",model.getFechaVigencia());
                        obj.put("FechaEnvio",model.getFechaEnvio().toLocaleString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    datosClaves.put(obj);
                }


                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("Celular", "1234567891");
                    jsonBody.put("Claves", datosClaves);
                }
                catch (JSONException ex)
                {

                }
                Log.i(_tag,jsonBody.toString());

                String url = "http://www.tkinova.com/fidegarsms/sms/save";
                //RequestFuture<JSONObject> future = RequestFuture.newFuture();

                JsonObjectRequest jsonObjReq = new  JsonObjectRequest(url,jsonBody,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String desc = response.getString("Description");
                                    Toast.makeText(view.getContext(), desc, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i(_tag,response.toString());
                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                Log.e(_tag,error.toString());
                            }
                });
                peticionesHttp.add(jsonObjReq);
            }
        });


        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etDate = (EditText) findViewById(R.id.etDate);
                DataPickerFragment newFragment = DataPickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                        etDate.setText(selectedDate);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });


        Cursor c = SmsFidegar.LeerSMS(getContentResolver());
        adapter = new ClaveCursorAdaptador(this,c);
        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 lvSms.setAdapter(adapter);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String twoDigits(int n){
        return (n<=9) ? "0" + n : String.valueOf(n);
    }

}
