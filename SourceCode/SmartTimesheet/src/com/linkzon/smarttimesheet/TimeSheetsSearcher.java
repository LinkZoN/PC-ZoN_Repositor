package com.linkzon.smarttimesheet;

import java.util.Date;
import java.util.List;

import businessLogic.TimeSheetAdapter;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import dataLayer.DatabaseHelper;
import dataLayer.EntityTimesheet;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class TimeSheetsSearcher extends FragmentActivity {

	public final static String PARAM_NAME_Category = "TimeSheet_Category";
	public final static String PARAM_NAME_DateFrom = "TimeSheets_DateFrom";
	public final static String PARAM_NAME_DateTo = "TimeSheets_DateTo";

	public interface ITimeSheetsSearcher {

		public void onSearchItemClick(int ID, String Category, Date ReferenceDay);

	}

	public String TimeSheet_Category;
	public Date TimeSheets_DateFrom;
	public Date TimeSheets_DateTo;

	private List<EntityTimesheet> lstTimeSheets;
	private RuntimeExceptionDao<EntityTimesheet, Long> CurrentRuntimeDao = null;
	private DatabaseHelper databaseHelper = null;

	public TimeSheetsSearcher() {
	}

	public static TimeSheetsSearcher newInstance(String Category,
			Date DateFrom, Date DateTo) {

		TimeSheetsSearcher NewIstance = new TimeSheetsSearcher();
		NewIstance.TimeSheet_Category = Category;
		NewIstance.TimeSheets_DateFrom = DateFrom;
		NewIstance.TimeSheets_DateTo = DateTo;

		// Bundle args = new Bundle();
		// NewIstance.setArguments(args);

		return NewIstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_sheets_searcher);
		// Show the Up button in the action bar.
		setupActionBar();

		// if (this.getArguments().containsKey(PARAM_NAME_Category)) {
		// String Category = getArguments().getString(PARAM_NAME_Category);
		// if (!Category.isEmpty())
		// TimeSheet_Category = Category;
		// }

		initParams();

		timeSheets_Search();

		TimeSheetAdapter adapter = new TimeSheetAdapter(this,
				R.layout.timesheets_row, lstTimeSheets);

		ListView listView = (ListView) findViewById(R.id.lvTimeSheets);
		listView.setOnItemClickListener(ListClick);
		listView.setAdapter(adapter);

	}

	private List<EntityTimesheet> timeSheets_Search() {

		return timeSheets_Search(TimeSheet_Category, TimeSheets_DateFrom,
				TimeSheets_DateTo);
	}

	private List<EntityTimesheet> timeSheets_Search(String Category,
			Date DateFrom, Date DateTo) {
		lstTimeSheets = CurrentRuntimeDao.queryForAll();
		return lstTimeSheets;
	}

	OnItemClickListener ListClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long id) {

			EntityTimesheet MyTimeSheet = (EntityTimesheet) adapter
					.getItemAtPosition(position);

			Intent returnIntent = new Intent();

			int TimeSheet_ID = MyTimeSheet.get_Id();
			TimeSheet_Category = MyTimeSheet.getTypeId();
			Date ReferenceDay = MyTimeSheet.getReferenceDay();

			returnIntent.putExtra("TimeSheet_ID", TimeSheet_ID);
			returnIntent.putExtra("TimeSheet_Category", TimeSheet_Category);
			if (ReferenceDay != null)
				returnIntent
						.putExtra("TimeSheet_Today", ReferenceDay.getTime());
			setResult(RESULT_OK, returnIntent);

			if (getParent() instanceof ITimeSheetsSearcher) {
				ITimeSheetsSearcher MyActivity = (ITimeSheetsSearcher) getParent();
				MyActivity.onSearchItemClick(TimeSheet_ID, TimeSheet_Category,
						ReferenceDay);
			}

			finish();
		}
	};

	public void initParams() {

		initCurrentDao();

		Intent sender = getIntent();

		String Category = sender.getExtras().getString(PARAM_NAME_Category);
		if (!Category.trim().isEmpty())
			TimeSheet_Category = Category;

		long lDate = sender.getExtras().getLong(PARAM_NAME_DateFrom);
		if (lDate > 0L)
			TimeSheets_DateFrom = new Date(lDate);

		lDate = sender.getExtras().getLong(PARAM_NAME_DateTo);
		if (lDate > 0L)
			TimeSheets_DateTo = new Date(lDate);

		// if (TimeSheet_Category == null) {
		// TimeSheet_Category = getString(R.string.Category_Default);
		// }
	}

	private void initCurrentDao() {
		if (CurrentRuntimeDao == null)
			CurrentRuntimeDao = getHelper().getTimesheetEntity_DataDao();
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_sheets_searcher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
