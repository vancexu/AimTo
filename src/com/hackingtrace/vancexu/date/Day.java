package com.hackingtrace.vancexu.date;
public class Day extends CalendarUnit {
	private Month m;

	public Day(int pDay, Month m) {
		setDay(pDay, m);
	}

	public boolean increment() {
		currentPos += 1;
		if (currentPos <= m.getMonthSize())
			return true;
		else
			return false;
	}

	public void setDay(int pDay, Month m) {
		setCurrentPos(pDay);
		this.m = m;
	}

	public int getDay() {
		return currentPos;
	}
	
	public int getDays() {
		return currentPos - 1;
	}
}
