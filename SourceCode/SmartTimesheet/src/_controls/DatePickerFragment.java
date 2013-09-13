package _controls;

import java.util.Calendar;
import java.util.Date;

import businessLogic.DateTimePickersListner;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	private static String PARAM_NAME_TITLE = "DialogTitle";
	private static String PARAM_NAME_NEWVALUE = "ValueToDisplay";

	DateTimePickersListner ondateSet;
	
	
	public void setCallBack(DateTimePickersListner ondate, int TargetID) {
		ondateSet = ondate;
		ondateSet.CallerID = TargetID;
	}

	public static DatePickerFragment newInstance(Date TimeToDisplay, String DialogTitle) {
		Bundle args = new Bundle();
		args.putString(PARAM_NAME_TITLE, DialogTitle);
		args.putLong(PARAM_NAME_NEWVALUE, TimeToDisplay.getTime());

		DatePickerFragment NewDialog = new DatePickerFragment();
		NewDialog.setArguments(args);

		return NewDialog;
	}

	int targetID;
	Calendar selectedDate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		selectedDate = Calendar.getInstance();
		if (getArguments().containsKey(PARAM_NAME_NEWVALUE))
			selectedDate.setTimeInMillis(getArguments().getLong(
					PARAM_NAME_NEWVALUE));

		DatePickerDialog NewDialog = new DatePickerDialog(getActivity(),
				ondateSet, selectedDate.get(Calendar.YEAR),
				selectedDate.get(Calendar.MONTH),
				selectedDate.get(Calendar.DAY_OF_MONTH));

		if (getArguments().containsKey(PARAM_NAME_TITLE)) {
			String Title = getArguments().getString(PARAM_NAME_TITLE);
			if (!Title.isEmpty())
				NewDialog.setTitle(Title);
		}

		return NewDialog;
	}

	public DatePickerFragment() {
	}

}
