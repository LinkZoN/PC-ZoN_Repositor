package businessLogic;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.linkzon.smarttimesheet.R;

public class Utils {

	/**
	 * @param NewTime
	 * @return
	 */
	public static String TimeToString(Context context, Date NewTime) {

		String sNow = context.getString(R.string.Time_Default);

		if (NewTime != null) {
			Calendar NewDate = Calendar.getInstance();
			NewDate.set(Calendar.MINUTE, 0);
			NewDate.set(Calendar.HOUR_OF_DAY, 0);
			NewDate.setTime(NewTime);

			Calendar Today = Calendar.getInstance();

			NewDate.set(Calendar.YEAR, Today.get(Calendar.YEAR));
			NewDate.set(Calendar.MONTH, Today.get(Calendar.MONTH));
			NewDate.set(Calendar.DAY_OF_MONTH, Today.get(Calendar.DAY_OF_MONTH));

			NewDate.set(Calendar.SECOND, 0);
			NewDate.set(Calendar.MILLISECOND, 0);

			sNow = String.format("%tH", NewDate);
			sNow += ":" + String.format("%tM", NewDate);
		}

		return sNow;
	}

}
