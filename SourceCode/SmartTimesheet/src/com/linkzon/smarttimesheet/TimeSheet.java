package com.linkzon.smarttimesheet;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import businessLogic.DateTimePickersListner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import _controls.DatePickerFragment;
import _controls.TimePickerFragment;
import businessLogic.*;
import dataLayer.DatabaseHelper;
import dataLayer.EntityTimesheet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeSheet extends FragmentActivity {
	/** OBSOLETE: implements OnClickListener { */

	private final String LOG_TAG = getClass().getSimpleName();
	private final static int TIMESHEETS_LIST = 1;

	public int TimeSheet_ID;
	public Date TimeSheet_Today;
	public String TimeSheet_Category;

	public String SelectedTime = null;

	private DatabaseHelper databaseHelper = null;
	private RuntimeExceptionDao<EntityTimesheet, Long> CurrentRuntimeDao = null;
	private EntityTimesheet CurrentEntity;

	private Spinner spCategory;
	private Button btDayStart;
	private Button btPauseStart;
	private Button btPauseEnd;
	private Button btDayEnd;
	private Button btShowList;
	private TextView tvReferenceDay;
	private TextView tvDayStart;
	private TextView tvPauseStart;
	private TextView tvPauseEnd;
	private TextView tvDayEnd;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.time_sheet, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_sheet);

		initParams();

		initControls();

		loadTimeSheet();

	}

	public void initParams() {

		initCurrentDao();

		if (TimeSheet_Today == null) {
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			TimeSheet_Today = now.getTime();
		}
		if (TimeSheet_Category == null) {
			TimeSheet_Category = getString(R.string.Category_Default);
		}
	}

	private void initControls() {

		tvReferenceDay = (TextView) findViewById(R.id.tvReferenceDay);
		tvReferenceDay.setOnClickListener(Click);

		spCategory = (Spinner) findViewById(R.id.spCategory);
		spCategory.setOnItemSelectedListener(SpinnerClick);

		tvDayStart = (TextView) findViewById(R.id.tvDayStart);
		tvDayStart.setOnClickListener(Click);
		btDayStart = (Button) findViewById(R.id.btDayStart);
		btDayStart.setOnClickListener(Click);

		tvPauseStart = (TextView) findViewById(R.id.tvPauseStart);
		tvPauseStart.setOnClickListener(Click);
		btPauseStart = (Button) findViewById(R.id.btPauseStart);
		btPauseStart.setOnClickListener(Click);

		btPauseEnd = (Button) findViewById(R.id.btPauseEnd);
		btPauseEnd.setOnClickListener(Click);
		tvPauseEnd = (TextView) findViewById(R.id.tvPauseEnd);
		btPauseEnd.setOnClickListener(Click);

		tvDayEnd = (TextView) findViewById(R.id.tvDayEnd);
		tvDayEnd.setOnClickListener(Click);
		btDayEnd = (Button) findViewById(R.id.btDayEnd);
		btDayEnd.setOnClickListener(Click);

		btShowList = (Button) findViewById(R.id.btShowList);
		btShowList.setOnClickListener(Click);

	}

	OnItemSelectedListener SpinnerClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long arg3) {

			String SelectedValue = parent.getItemAtPosition(pos).toString();
			if (TimeSheet_Category != SelectedValue) {
				TimeSheet_ID = 0;
				TimeSheet_Category = SelectedValue;
				loadTimeSheet();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	OnClickListener Click = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Calendar NewTime = Calendar.getInstance();
			String sNow = Utils.TimeToString(TimeSheet.this, NewTime.getTime());

			switch (v.getId()) {
			case R.id.btDayStart:
				tvDayStart.setText(sNow);
				InsetTimesheet();
				break;
			case R.id.btDayEnd:
				tvDayEnd.setText(sNow);
				InsetTimesheet();
				break;
			case R.id.btPauseStart:
				tvPauseStart.setText(sNow);
				InsetTimesheet();
				break;
			case R.id.btPauseEnd:
				tvPauseEnd.setText(sNow);
				InsetTimesheet();
				break;

			case R.id.tvDayStart:
				showTimePickerDialog(TimeFromString(tvDayStart.getText()
						.toString()), v.getId(), btDayStart.getText()
						.toString());
				break;
			case R.id.tvDayEnd:
				showTimePickerDialog(TimeFromString(tvDayEnd.getText()
						.toString()), v.getId(), btDayEnd.getText().toString());
				break;
			case R.id.tvPauseStart:
				showTimePickerDialog(TimeFromString(tvPauseStart.getText()
						.toString()), v.getId(), btPauseStart.getText()
						.toString());
				break;
			case R.id.tvPauseEnd:
				showTimePickerDialog(TimeFromString(tvPauseEnd.getText()
						.toString()), v.getId(), btPauseEnd.getText()
						.toString());
				break;

			case R.id.btShowList:
				showTimeSheetsList();
				break;

			case R.id.tvReferenceDay:
				showDatePickerDialog(TimeSheet_Today, R.id.tvReferenceDay, "");
				break;

			}

		}
	};

	private void showDatePickerDialog(Date NewDate, int TargetID,
			String DialogTitle) {

		String FRAGMENT_URI = "DatePickerFragmentDialog";

		FragmentTransaction ft = fragmentTransaction_CleanAndGet(FRAGMENT_URI);

		// Create and show the dialog.
		Date Now = NewDate;

		if (Now == null)
			Now = new Date();

		DatePickerFragment date = DatePickerFragment.newInstance(Now,
				"Date Picker");

		// Set Call back to capture selected date
		date.setCallBack(onDateSelected, TargetID);
		date.show(ft, FRAGMENT_URI);
	}

	/**
	 * Capture selected date ACTENTION: the monthOfYear start from zero
	 */
	DateTimePickersListner onDateSelected = new DateTimePickersListner() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			switch (CallerID) {
			case R.id.tvReferenceDay:
				Calendar NewDate = Calendar.getInstance();
				NewDate.set(year, monthOfYear, dayOfMonth);
				Date RefDate = NewDate.getTime();
				if (TimeSheet_Today != RefDate) {
					TimeSheet_ID = 0;
					TimeSheet_Today = RefDate;
					loadTimeSheet();
				}
				break;
			}
		}
	};

	private void showTimeSheetsList() {

		Calendar NewDate = Calendar.getInstance();
		NewDate.set(Calendar.DAY_OF_MONTH, 1);
		NewDate.set(Calendar.HOUR_OF_DAY, 0);
		NewDate.set(Calendar.MINUTE, 0);
		NewDate.set(Calendar.SECOND, 0);
		NewDate.set(Calendar.MILLISECOND, 0);

		Date DateFrom = NewDate.getTime();

		NewDate.add(Calendar.MONTH, 1);
		NewDate.add(Calendar.MILLISECOND, -1);

		Date DateTo = NewDate.getTime();

		Intent TimeSheetsSearchIntent = new Intent(this,// getApplicationContext(),
				TimeSheetsSearcher.class);
		TimeSheetsSearchIntent.putExtra(TimeSheetsSearcher.PARAM_NAME_Category,
				TimeSheet_Category);
		TimeSheetsSearchIntent.putExtra(TimeSheetsSearcher.PARAM_NAME_DateFrom,
				DateFrom.getTime());
		TimeSheetsSearchIntent.putExtra(TimeSheetsSearcher.PARAM_NAME_DateTo,
				DateTo.getTime());

		startActivityForResult(TimeSheetsSearchIntent, TIMESHEETS_LIST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == TIMESHEETS_LIST) {

			if (resultCode == RESULT_OK) {
				TimeSheet_ID = data.getIntExtra("TimeSheet_ID", -1);
				TimeSheet_Category = data.getStringExtra("TimeSheet_Category");
				TimeSheet_Today = new Date(data.getLongExtra("TimeSheet_Today",
						TimeSheet_Today.getTime()));
				loadTimeSheet();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	/**
	 * Show The TimePicker Fragment Dialog
	 */
	public void showTimePickerDialog(Date NewTime, int TargetID,
			String DialogTitle) {

		String FRAGMENT_URI = "TimePickerFragmentDialog";

		FragmentTransaction ft = fragmentTransaction_CleanAndGet(FRAGMENT_URI);

		// Create and show the dialog.
		Date Now = NewTime;

		if (Now == null)
			Now = new Date();

		TimePickerFragment newFragment = TimePickerFragment.newInstance(Now,
				true, DialogTitle);

		newFragment.setCallBack(onTimeSetted, TargetID);
		newFragment.show(ft, FRAGMENT_URI);
	}

	/**
	 * Capture selected Time
	 */
	DateTimePickersListner onTimeSetted = new DateTimePickersListner() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			String sNow = pad(hourOfDay) + ":" + pad(minute);

			switch (CallerID) {
			case R.id.tvDayStart:
				tvDayStart.setText(sNow);
				break;
			case R.id.tvDayEnd:
				tvDayEnd.setText(sNow);
				break;
			case R.id.tvPauseStart:
				tvPauseStart.setText(sNow);
				break;
			case R.id.tvPauseEnd:
				tvPauseEnd.setText(sNow);
				break;
			}

			InsetTimesheet();

		}
	};

	private String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public void loadTimeSheet() {

		getEntity();

		setControls();
	}

	public boolean getEntity() {

		initCurrentDao();

		List<EntityTimesheet> lstResult = null;

		PreparedQuery<EntityTimesheet> preparedQuery = null;

		boolean Result = true;
		try {
			if (TimeSheet_ID > 0) {

				QueryBuilder<EntityTimesheet, Long> queryBuilder = CurrentRuntimeDao
						.queryBuilder();

				Where<EntityTimesheet, Long> where = queryBuilder.where();
				where.eq("_Id", TimeSheet_ID);

				preparedQuery = where.prepare();

			} else if (TimeSheet_Today != null
					&& TimeSheet_Category.trim() != "") {

				QueryBuilder<EntityTimesheet, Long> queryBuilder = CurrentRuntimeDao
						.queryBuilder();

				Where<EntityTimesheet, Long> where = queryBuilder.where();
				where.eq("ReferenceDay", TimeSheet_Today);
				where.and();
				where.eq("TypeId", TimeSheet_Category);

				preparedQuery = where.prepare();

			}

			lstResult = CurrentRuntimeDao.query(preparedQuery);

			if (lstResult != null && !lstResult.isEmpty()) {
				CurrentEntity = lstResult.get(0);
				TimeSheet_ID = CurrentEntity.get_Id();
			} else {
				CurrentEntity = new EntityTimesheet();

				CurrentEntity.setReferenceDay(TimeSheet_Today);
				CurrentEntity.setTypeId(TimeSheet_Category);
			}

			TimeSheet_Today = CurrentEntity.getReferenceDay();
			TimeSheet_Category = CurrentEntity.getTypeId();

		} catch (SQLException e) {
			Result = false;

			e.printStackTrace();
			ShowError(e.getMessage());
		} finally {
		}

		return Result;
	}

	private void setControls() {

		LoadCategory();

		Date RefDate = CurrentEntity.getReferenceDay();

		String formattedDate = formatReferenceDay(RefDate);

		tvReferenceDay.setText(formattedDate);
		tvDayStart.setText(Utils.TimeToString(this,
				CurrentEntity.getSheetStarted()));
		tvPauseStart.setText(Utils.TimeToString(this,
				CurrentEntity.getPauseStarted()));
		tvPauseEnd.setText(Utils.TimeToString(this,
				CurrentEntity.getPauseFinished()));
		tvDayEnd.setText(Utils.TimeToString(this,
				CurrentEntity.getSheetFinished()));
	}

	private String formatReferenceDay(Date RefDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy MMM dd");
		String formattedDate = df.format(RefDate);
		df = new SimpleDateFormat("EEEE");
		formattedDate += "\n" + df.format(RefDate);
		return formattedDate;
	}

	public void LoadCategory() {

		initCurrentDao();

		int iIndex = 0;
		List<String> list = new ArrayList<String>();

		// TODO: Load Values Missing

		//

		if (!list.contains(TimeSheet_Category)) {
			list.add(TimeSheet_Category);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spCategory.setAdapter(dataAdapter);

		spCategory.setSelection(iIndex, true);
	}

	private boolean InsetTimesheet() {

		boolean Result = true;
		String OutputMessage = getString(R.string.SaveOk);

		try {

			getEntity();

			getCategoryInputed();

			CurrentEntity.setTypeId(TimeSheet_Category);

			CurrentEntity.setSheetStarted(TimeFromString(tvDayStart.getText()
					.toString()));
			CurrentEntity.setPauseStarted(TimeFromString(tvPauseStart.getText()
					.toString()));
			CurrentEntity.setPauseFinished(TimeFromString(tvPauseEnd.getText()
					.toString()));
			CurrentEntity.setSheetFinished(TimeFromString(tvDayEnd.getText()
					.toString()));

			CreateOrUpdateStatus Status = CurrentRuntimeDao
					.createOrUpdate(CurrentEntity);

			Result = Status.isUpdated() || Status.isCreated();

			/**
			 * OBSOLETE:
			 */
			// if (CurrentEntity.get_Id() > 0) {
			// CurrentRuntimeDao.update(CurrentEntity);
			// } else {
			// TimeSheet_ID = CurrentRuntimeDao.countOf() + 1;
			// CurrentEntity.set_Id(TimeSheet_ID);
			// CurrentRuntimeDao.create(CurrentEntity);
			// }
			// Result = true;

			ShowMessage(OutputMessage);

		} catch (Exception e) {

			Result = false;

			OutputMessage = getString(R.string.SaveKo) + '\n' + e.getMessage();
			ShowError(OutputMessage);
			Log.e(LOG_TAG, OutputMessage);

		} finally {

			loadTimeSheet();

		}

		return Result;
	}

	/**
	 * DialogFragment.show() will take care of adding the fragment in a
	 * transaction. We also want to remove any currently showing dialog, so make
	 * our own transaction and take care of that here.
	 * 
	 * @param NewFragmentUri
	 * @return
	 */
	private FragmentTransaction fragmentTransaction_CleanAndGet(
			String NewFragmentUri) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(
				NewFragmentUri);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		return ft;
	}

	/**
	 * 
	 */
	public void getCategoryInputed() {
		String SelectedCategory = String.valueOf(spCategory.getSelectedItem());

		if (!SelectedCategory.trim().isEmpty())
			TimeSheet_Category = SelectedCategory;
	}

	/**
	 * @param sNewTime
	 * @return
	 * @throws NumberFormatException
	 */
	private Date TimeFromString(String sNewTime) throws NumberFormatException {

		Date ResultDate = null;

		if (sNewTime == getString(R.string.Time_Default))
			return ResultDate;

		// Default Time
		String sTime = "";

		if (!sNewTime.isEmpty() && sNewTime.length() == 5) {

			sTime = sNewTime;
			Calendar NewDate = Calendar.getInstance();
			NewDate.setTime(TimeSheet_Today);

			if (sNewTime != getString(R.string.Time_Default)) {
				NewDate.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(sTime.substring(0, 2)));
				NewDate.set(Calendar.MINUTE,
						Integer.parseInt(sTime.substring(3)));
			}
			// else {
			// Calendar Now = Calendar.getInstance();
			// NewDate.set(Calendar.HOUR_OF_DAY, Now.get(Calendar.HOUR_OF_DAY));
			// NewDate.set(Calendar.MINUTE, Now.get(Calendar.MINUTE));
			// }

			NewDate.set(Calendar.SECOND, 0);
			NewDate.set(Calendar.MILLISECOND, 0);

			ResultDate = NewDate.getTime();
		}

		return ResultDate;
	}

	/**
	 * 
	 */
	private void initCurrentDao() {
		if (CurrentRuntimeDao == null)
			CurrentRuntimeDao = getHelper().getTimesheetEntity_DataDao();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	public void ShowError(String Message) {
		ShowMessage(Message, MessageTypes.ERROR);
	}

	public void ShowMessage(String Message) {
		ShowMessage(Message, MessageTypes.DEFAULT);
	}

	public enum MessageTypes {
		ERROR(-1), DEFAULT(0);

		private int emunID;

		MessageTypes(int EnumID) {
			this.emunID = EnumID;
		}

		public int getEnumID() {
			return emunID;
		}
	}

	public void ShowMessage(String Message, MessageTypes MessageType) {

		int iPeriod = Toast.LENGTH_SHORT;

		switch (MessageType) {
		case ERROR:
			Message = "ERROR:\n" + Message;
			iPeriod = Toast.LENGTH_LONG;
			break;
		default:
			break;
		}

		Toast.makeText(getApplicationContext(), Message, iPeriod).show();
	}

	/**
	 * Obsolete
	 * 
	 * @Override protected Dialog onCreateDialog(int id) {
	 * 
	 *           Dialog ResultDialog = null;
	 * 
	 *           switch (id) { case TIME_DIALOG_ID: ResultDialog =
	 *           openTimePickerDialog(); break; } return ResultDialog; }
	 * 
	 * 
	 *           // Time Picker Manager public TimePickerDialog
	 *           openTimePickerDialog() { return openTimePickerDialog(null,
	 *           true, "", ""); }
	 * 
	 *           public TimePickerDialog openTimePickerDialog(String Title,
	 *           String Message) { return openTimePickerDialog(null, true,
	 *           Title, Message); }
	 * 
	 *           public TimePickerDialog openTimePickerDialog(boolean is24r) {
	 *           return openTimePickerDialog(null, is24r, "", ""); }
	 * 
	 *           public TimePickerDialog openTimePickerDialog(boolean is24r,
	 *           String Title, String Message) { return
	 *           openTimePickerDialog(null, is24r, Title, Message); }
	 * 
	 *           public TimePickerDialog openTimePickerDialog(Calendar
	 *           TimeToView, boolean is24r, String Title, String Message) {
	 * 
	 *           if (TimeToView == null) TimeToView = Calendar.getInstance();
	 * 
	 *           TimePickerDialog timePickerDialog = new TimePickerDialog(this,
	 *           onTimeSetListener, TimeToView.get(Calendar.HOUR_OF_DAY),
	 *           TimeToView.get(Calendar.MINUTE), is24r);
	 * 
	 *           timePickerDialog.setTitle(Title);
	 *           timePickerDialog.setMessage(Message);
	 * 
	 *           timePickerDialog.setOnShowListener(new OnShowListener() {
	 * 
	 * @Override public void onShow(DialogInterface arg0) {
	 * 
	 *           } }); timePickerDialog.setOnCancelListener(new
	 *           OnCancelListener() {
	 * 
	 * @Override public void onCancel(DialogInterface dialog) { SelectedTime =
	 *           null; SelectedHour = null; SelectedMinute = null; } });
	 *           timePickerDialog.setOnDismissListener(new OnDismissListener() {
	 * 
	 * @Override public void onDismiss(DialogInterface arg0) {
	 * 
	 *           } });
	 * 
	 *           timePickerDialog.show();
	 * 
	 *           return timePickerDialog; }
	 * 
	 *           public String SelectedTime_GetString() {
	 * 
	 *           SelectedTime = pad(SelectedHour) + ":" + pad(SelectedMinute);
	 *           return SelectedTime;
	 * 
	 *           }
	 * 
	 *           private String pad(int c) { if (c >= 10) return
	 *           String.valueOf(c); else return "0" + String.valueOf(c); }
	 * 
	 *           OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 *           minute) { SelectedHour = hourOfDay; SelectedMinute = minute;
	 *           SelectedTime_GetString(); }
	 * 
	 *           };
	 */
}
