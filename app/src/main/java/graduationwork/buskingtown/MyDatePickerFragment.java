package graduationwork.buskingtown;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, year,month,day);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return datePickerDialog;
    }
}