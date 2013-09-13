package com.linkzon.smarttimesheet;

import java.util.Date;
import java.util.List;

import businessLogic.Utils;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import dataLayer.DatabaseHelper;
import dataLayer.EntityTimesheet;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.support.v4.app.ListFragment;
import android.content.Context;

public class TimeSheets extends ListFragment {

	private Context CurrentContext;
	private LayoutInflater CurrentInflater;

	public String TimeSheet_Category;

	private List<EntityTimesheet> lstTimeSheets;
	private RuntimeExceptionDao<EntityTimesheet, Long> CurrentRuntimeDao = null;
	private DatabaseHelper databaseHelper = null;

	private TextView tvSpentTotal;
	private TextView tvSpent;
	private TextView tvTotal;
	private TextView tvPause;
	private TextView tvPauseTotal;
	private TextView tvSheetTotal;
	private TextView tvReferenceDay;
	private TextView tvSheet;

	public TimeSheets() {
	}

	public static TimeSheets newInstance(Date DateFrom, Date DateTo) {

		Bundle args = new Bundle();

		TimeSheets NewIstance = new TimeSheets();
		NewIstance.setArguments(args);

		return NewIstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initParams();
		lstTimeSheets = CurrentRuntimeDao.queryForAll();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		CurrentInflater = inflater;
		CurrentContext = inflater.getContext();

		/** Setting the list adapter for the ListFragment */
		setListAdapter(new TimeSheetAdapter());

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	public void initParams() {

		initCurrentDao();

		if (TimeSheet_Category == null) {
			TimeSheet_Category = getString(R.string.Category_Default);
		}
	}

	private void initCurrentDao() {
		if (CurrentRuntimeDao == null)
			CurrentRuntimeDao = getHelper().getTimesheetEntity_DataDao();
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(CurrentContext,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	private final class TimeSheetAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = CurrentInflater.inflate(
						android.R.layout.simple_list_item_2, null);
			}
			EntityTimesheet myTimeSheet = (EntityTimesheet) getItem(position);

			tvReferenceDay = (TextView) convertView
					.findViewById(R.id.tvReferenceDay);
			tvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
			tvSheet = (TextView) convertView.findViewById(R.id.tvSheet);
			tvSheetTotal = (TextView) convertView
					.findViewById(R.id.tvSheetTotal);
			tvPause = (TextView) convertView.findViewById(R.id.tvPause);
			tvPauseTotal = (TextView) convertView
					.findViewById(R.id.tvPauseTotal);
			tvSpent = (TextView) convertView.findViewById(R.id.tvSpent);
			tvSpentTotal = (TextView) convertView
					.findViewById(R.id.tvSpentTotal);

			tvReferenceDay.setText(myTimeSheet.getReferenceDay().toString());
			tvSheet.setText(Utils.TimeToString(CurrentContext,
					myTimeSheet.getSheetStarted())
					+ " - "
					+ Utils.TimeToString(CurrentContext,
							myTimeSheet.getSheetFinished()));
			tvPause.setText(Utils.TimeToString(CurrentContext,
					myTimeSheet.getPauseStarted())
					+ " - "
					+ Utils.TimeToString(CurrentContext,
							myTimeSheet.getPauseFinished()));
			tvSpent.setText("" + myTimeSheet.getReferenceDayMoneySpent() + "-"
					+ myTimeSheet.getReferenceDayMoneyBack());

			tvTotal.setText(Utils.TimeToString(CurrentContext,
					myTimeSheet.getReferenceDayTotal()));
			tvSheetTotal.setText(Utils.TimeToString(CurrentContext,
					myTimeSheet.getSheetTotal()));
			tvPauseTotal.setText(Utils.TimeToString(CurrentContext,
					myTimeSheet.getPauseTotal()));
			tvSpentTotal.setText("" + myTimeSheet.getReferenceDayMoneyTotal());

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return lstTimeSheets.get(position);
		}

		@Override
		public int getCount() {
			return lstTimeSheets.size();
		}
	}
}
