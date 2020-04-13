package util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


import static android.content.Context.MODE_PRIVATE;


public class Time_picker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

public interface TimePickerListener {
    void onTimeSet(TimePicker timePicker, int hour, int minute);
}

    TimePickerListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (TimePickerListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " must implements TimePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        mListener.onTimeSet(timePicker, i, i1);
        SharedPreferences.Editor editors = getContext().getSharedPreferences("time", MODE_PRIVATE).edit();
        editors.putString("hours", String.valueOf(i));
        editors.putString("minutes", String.valueOf(i1));

    }
}

