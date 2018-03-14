package tkn.fidegar.claves;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Alfred on 06/03/2018.
 */

public class DataPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private  DatePickerDialog.OnDateSetListener  listener;

    public static DataPickerFragment newInstance(DatePickerDialog.OnDateSetListener listener){

        DataPickerFragment fragment = new DataPickerFragment();
        fragment.setListener(listener);
        return  fragment;

    }

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new instance of DAtePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener ,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
