package _controls;

import java.util.Calendar;
import java.util.Date;

import businessLogic.DateTimePickersListner;
import android.os.Bundle;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

	private static String PARAM_NAME_TITLE = "DialogTitle";
	private static String PARAM_NAME_NEWVALUE = "ValueToDisplay";
	private static String PARAM_NAME_IS24H = "TimeIs24h";

	DateTimePickersListner ontimeSet;

	public void setCallBack(DateTimePickersListner onTime, int TargetID) {
		ontimeSet = onTime;
		ontimeSet.CallerID = TargetID;
	}

	public static TimePickerFragment newInstance(Date TimeToDisplay,
			boolean TimeIs24h, String DialogTitle) {

		Bundle args = new Bundle();
		args.putString(PARAM_NAME_TITLE, DialogTitle);
		args.putLong(PARAM_NAME_NEWVALUE, TimeToDisplay.getTime());
		args.putBoolean(PARAM_NAME_IS24H, TimeIs24h);

		TimePickerFragment NewDialog = new TimePickerFragment();
		NewDialog.setArguments(args);

		return NewDialog;
	}

	int hour;
	int minute;
	int targetID;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Calendar c = Calendar.getInstance();
		if (getArguments().containsKey(PARAM_NAME_NEWVALUE))
			c.setTimeInMillis(getArguments().getLong(PARAM_NAME_NEWVALUE));
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		boolean TimeIs24h = true;
		if (getArguments().containsKey(PARAM_NAME_IS24H))
			TimeIs24h = getArguments().getBoolean(PARAM_NAME_IS24H);

		// Create a new instance of TimePickerDialog for set passed parameters
		// and return it
		TimePickerDialog NewDialog = new TimePickerDialog(getActivity(),
				ontimeSet, hour, minute, TimeIs24h);

		if (getArguments().containsKey(PARAM_NAME_TITLE)) {
			String Title = getArguments().getString(PARAM_NAME_TITLE);
			if (!Title.isEmpty())
				NewDialog.setTitle(Title);
		}

		return NewDialog;
	}

	public TimePickerFragment() {
	}

}