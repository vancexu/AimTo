package com.hackingtrace.vancexu.date;

public class Month extends CalendarUnit {
	private Year y;
	private int[] sizeIndex = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public Month(int pMonth, Year y) {
		setMonth(pMonth, y);
	}

	public void setMonth(int pMonth, Year y) {
		setCurrentPos(pMonth);
		this.y = y;
	}

	public int getMonth() {
		return currentPos;
	}

	public int getMonthSize() {
		if (y.isLeap())
			sizeIndex[1] = 29;
		else
			sizeIndex[1] = 28;
		return sizeIndex[currentPos - 1];
	}

	public boolean increment() {
		currentPos += 1;
		if (currentPos > 12)
			return false;
		else
			return true;
	}

	public int getDays() {
		int monthRemain = currentPos - 1;
		int sum = 0;
		if (monthRemain > 0) {
			for (int i = 1; i <= monthRemain; ++i) {
				Month tmp = new Month(i, this.y);
				sum += tmp.getMonthSize();
			}
		}
		return sum;
	}
}
