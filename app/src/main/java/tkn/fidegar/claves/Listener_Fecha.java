package tkn.fidegar.claves;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by Alfred on 14/03/2018.
 */

public class Listener_Fecha implements View.OnClickListener {

    private Context _context;
    private AppCompatActivity _activity;
    public Listener_Fecha(Context context) {
        _context = context;
        _activity = (AppCompatActivity)context;
    }

    @Override
    public void onClick(View v) {
        final EditText etDate = (EditText) _activity.findViewById(R.id.etDate);
        DataPickerFragment newFragment = DataPickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                etDate.setText(selectedDate);
            }
        });
        newFragment.show(_activity.getSupportFragmentManager(), "DatePicker");
    }

    private String twoDigits(int n){
        return (n<=9) ? "0" + n : String.valueOf(n);
    }
}
