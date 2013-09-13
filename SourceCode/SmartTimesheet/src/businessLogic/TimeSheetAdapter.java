package businessLogic;

import java.util.List;

import com.linkzon.smarttimesheet.R;

import dataLayer.EntityTimesheet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimeSheetAdapter extends ArrayAdapter<EntityTimesheet> {

	public TimeSheetAdapter(Context context, int resource,
			List<EntityTimesheet> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.timesheets_row, null);

		TextView tvSpentTotal;
		TextView tvSpent;
		TextView tvTotal;
		TextView tvPause;
		TextView tvPauseTotal;
		TextView tvSheetTotal;
		TextView tvReferenceDay;
		TextView tvSheet;

		EntityTimesheet myTimeSheet = (EntityTimesheet) getItem(position);

		tvReferenceDay = (TextView) convertView
				.findViewById(R.id.tvReferenceDay);
		tvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
		tvSheet = (TextView) convertView.findViewById(R.id.tvSheet);
		tvSheetTotal = (TextView) convertView.findViewById(R.id.tvSheetTotal);
		tvPause = (TextView) convertView.findViewById(R.id.tvPause);
		tvPauseTotal = (TextView) convertView.findViewById(R.id.tvPauseTotal);
		tvSpent = (TextView) convertView.findViewById(R.id.tvSpent);
		tvSpentTotal = (TextView) convertView.findViewById(R.id.tvSpentTotal);

		tvReferenceDay.setText(myTimeSheet.getReferenceDay().toString());
		tvSheet.setText(Utils.TimeToString(getContext(),
				myTimeSheet.getSheetStarted())
				+ " - "
				+ Utils.TimeToString(getContext(),
						myTimeSheet.getSheetFinished()));
		tvPause.setText(Utils.TimeToString(getContext(),
				myTimeSheet.getPauseStarted())
				+ " - "
				+ Utils.TimeToString(getContext(),
						myTimeSheet.getPauseFinished()));
		tvSpent.setText("" + myTimeSheet.getReferenceDayMoneySpent() + "-"
				+ myTimeSheet.getReferenceDayMoneyBack());

		tvTotal.setText(Utils.TimeToString(getContext(),
				myTimeSheet.getReferenceDayTotal()));
		tvSheetTotal.setText(Utils.TimeToString(getContext(),
				myTimeSheet.getSheetTotal()));
		tvPauseTotal.setText(Utils.TimeToString(getContext(),
				myTimeSheet.getPauseTotal()));
		tvSpentTotal.setText("" + myTimeSheet.getReferenceDayMoneyTotal());

		return convertView;
	}

}
