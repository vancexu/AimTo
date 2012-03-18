package com.hackingtrace.vancexu.date;

public class Year extends CalendarUnit {
	public Year(int pYear) {
		setCurrentPos(pYear);
	}

	public int getYear() {
		return currentPos;
	}

	public boolean increment() {
		currentPos = currentPos + 1;
		return true;
	}

	public boolean isLeap() {
		if (((currentPos % 4 == 0) && (currentPos % 100 != 0))
				|| (currentPos % 400 == 0))
			return true;
		else
			return false;
	}

	public int getdays() {
		int yearRemain = currentPos - 2000; // assume input is bigger than 2000
		int sum = 0;
		if (yearRemain > 0) {
			for (int i = 0; i < yearRemain; ++i) {
				Year tmp = new Year(2000+i);
				if (tmp.isLeap()) {
					sum += 366;
				} else {
					sum += 365;
				}
			}
		}
		return sum;
	}
}
