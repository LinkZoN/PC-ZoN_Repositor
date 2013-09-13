package dataLayer;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Timesheets.db")
public class EntityTimesheet {

	@DatabaseField(generatedId=true)
	Integer _Id;

	@DatabaseField(index = true)
	String TypeId;

	@DatabaseField(index = true)
	Date ReferenceDay;

	@DatabaseField
	Date ReferenceDayDefault;
	@DatabaseField
	Date ReferenceDayReal;
	@DatabaseField
	Date ReferenceDayTotal;
	@DatabaseField
	float ReferenceDayMoneySpent;
	@DatabaseField
	float ReferenceDayMoneyBack;
	@DatabaseField
	float ReferenceDayMoneyTotal;
	@DatabaseField
	Date SheetStarted;
	@DatabaseField
	Date SheetFinished;
	@DatabaseField
	Date SheetTotal;
	@DatabaseField
	Date PauseStarted;
	@DatabaseField
	Date PauseFinished;
	@DatabaseField
	Date PauseTotal;
	@DatabaseField
	float MoneySpent1;
	@DatabaseField
	float MoneyBack1;
	@DatabaseField
	float MoneyTotal1;
	@DatabaseField
	float MoneySpent2;
	@DatabaseField
	float MoneyBack2;
	@DatabaseField
	float MoneyTotal2;
	@DatabaseField
	float MoneySpent3;
	@DatabaseField
	float MoneyBack3;
	@DatabaseField
	float MoneyTotal3;

	public EntityTimesheet() {
		// TODO Auto-generated constructor stub

	}

	public EntityTimesheet(String typeId, Date referenceDay) {
		super();
		TypeId = typeId;
		ReferenceDay = referenceDay;
	}

	public EntityTimesheet(Integer _Id, String typeId, Date referenceDay) {
		super();
		this._Id = _Id;
		TypeId = typeId;
		ReferenceDay = referenceDay;
	}

	public EntityTimesheet(Integer _Id, String typeId, Date referenceDay,
			Date referenceDayDefault, Date referenceDayReal,
			Date referenceDayTotal, float referenceDayMoneySpent,
			float referenceDayMoneyBack, float referenceDayMoneyTotal,
			Date sheetStarted, Date sheetFinished, Date sheetTotal,
			Date pauseStarted, Date pauseFinished, Date pauseTotal,
			float moneySpent1, float moneyBack1, float moneyTotal1,
			float moneySpent2, float moneyBack2, float moneyTotal2,
			float moneySpent3, float moneyBack3, float moneyTotal3) {
		super();
		this._Id = _Id;
		TypeId = typeId;
		ReferenceDay = referenceDay;
		ReferenceDayDefault = referenceDayDefault;
		ReferenceDayReal = referenceDayReal;
		ReferenceDayTotal = referenceDayTotal;
		ReferenceDayMoneySpent = referenceDayMoneySpent;
		ReferenceDayMoneyBack = referenceDayMoneyBack;
		ReferenceDayMoneyTotal = referenceDayMoneyTotal;
		SheetStarted = sheetStarted;
		SheetFinished = sheetFinished;
		SheetTotal = sheetTotal;
		PauseStarted = pauseStarted;
		PauseFinished = pauseFinished;
		PauseTotal = pauseTotal;
		MoneySpent1 = moneySpent1;
		MoneyBack1 = moneyBack1;
		MoneyTotal1 = moneyTotal1;
		MoneySpent2 = moneySpent2;
		MoneyBack2 = moneyBack2;
		MoneyTotal2 = moneyTotal2;
		MoneySpent3 = moneySpent3;
		MoneyBack3 = moneyBack3;
		MoneyTotal3 = moneyTotal3;
	}

	public Integer get_Id() {
		return _Id;
	}

	public String getTypeId() {
		return TypeId;
	}

	public Date getReferenceDay() {
		return ReferenceDay;
	}

	public Date getReferenceDayDefault() {
		return ReferenceDayDefault;
	}

	public Date getReferenceDayReal() {
		return ReferenceDayReal;
	}

	public Date getReferenceDayTotal() {
		return ReferenceDayTotal;
	}

	public float getReferenceDayMoneySpent() {
		return ReferenceDayMoneySpent;
	}

	public float getReferenceDayMoneyBack() {
		return ReferenceDayMoneyBack;
	}

	public float getReferenceDayMoneyTotal() {
		return ReferenceDayMoneyTotal;
	}

	public Date getSheetStarted() {
		return SheetStarted;
	}

	public Date getSheetFinished() {
		return SheetFinished;
	}

	public Date getSheetTotal() {
		return SheetTotal;
	}

	public Date getPauseStarted() {
		return PauseStarted;
	}

	public Date getPauseFinished() {
		return PauseFinished;
	}

	public Date getPauseTotal() {
		return PauseTotal;
	}

	public float getMoneySpent1() {
		return MoneySpent1;
	}

	public float getMoneyBack1() {
		return MoneyBack1;
	}

	public float getMoneyTotal1() {
		return MoneyTotal1;
	}

	public float getMoneySpent2() {
		return MoneySpent2;
	}

	public float getMoneyBack2() {
		return MoneyBack2;
	}

	public float getMoneyTotal2() {
		return MoneyTotal2;
	}

	public float getMoneySpent3() {
		return MoneySpent3;
	}

	public float getMoneyBack3() {
		return MoneyBack3;
	}

	public float getMoneyTotal3() {
		return MoneyTotal3;
	}

	public void set_Id(Integer _Id) {
		this._Id = _Id;
	}

	public void setTypeId(String typeId) {
		TypeId = typeId;
	}

	public void setReferenceDay(Date referenceDay) {
		ReferenceDay = referenceDay;
	}

	public void setReferenceDayDefault(Date referenceDayDefault) {
		ReferenceDayDefault = referenceDayDefault;
	}

	public void setReferenceDayReal(Date referenceDayReal) {
		ReferenceDayReal = referenceDayReal;
	}

	public void setReferenceDayTotal(Date referenceDayTotal) {
		ReferenceDayTotal = referenceDayTotal;
	}

	public void setReferenceDayMoneySpent(float referenceDayMoneySpent) {
		ReferenceDayMoneySpent = referenceDayMoneySpent;
	}

	public void setReferenceDayMoneyBack(float referenceDayMoneyBack) {
		ReferenceDayMoneyBack = referenceDayMoneyBack;
	}

	public void setReferenceDayMoneyTotal(float referenceDayMoneyTotal) {
		ReferenceDayMoneyTotal = referenceDayMoneyTotal;
	}

	public void setSheetStarted(Date sheetStarted) {
		SheetStarted = sheetStarted;
	}

	public void setSheetFinished(Date sheetFinished) {
		SheetFinished = sheetFinished;
	}

	public void setSheetTotal(Date sheetTotal) {
		SheetTotal = sheetTotal;
	}

	public void setPauseStarted(Date pauseStarted) {
		PauseStarted = pauseStarted;
	}

	public void setPauseFinished(Date pauseFinished) {
		PauseFinished = pauseFinished;
	}

	public void setPauseTotal(Date pauseTotal) {
		PauseTotal = pauseTotal;
	}

	public void setMoneySpent1(float moneySpent1) {
		MoneySpent1 = moneySpent1;
	}

	public void setMoneyBack1(float moneyBack1) {
		MoneyBack1 = moneyBack1;
	}

	public void setMoneyTotal1(float moneyTotal1) {
		MoneyTotal1 = moneyTotal1;
	}

	public void setMoneySpent2(float moneySpent2) {
		MoneySpent2 = moneySpent2;
	}

	public void setMoneyBack2(float moneyBack2) {
		MoneyBack2 = moneyBack2;
	}

	public void setMoneyTotal2(float moneyTotal2) {
		MoneyTotal2 = moneyTotal2;
	}

	public void setMoneySpent3(float moneySpent3) {
		MoneySpent3 = moneySpent3;
	}

	public void setMoneyBack3(float moneyBack3) {
		MoneyBack3 = moneyBack3;
	}

	public void setMoneyTotal3(float moneyTotal3) {
		MoneyTotal3 = moneyTotal3;
	}

}
