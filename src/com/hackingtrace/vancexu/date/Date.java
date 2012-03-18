package com.hackingtrace.vancexu.date;
public class Date {
	private Day d;
	private Month m;
	private Year y;

	public Date(int pYear, int pMonth, int pDay) {
		y = new Year(pYear);
		m = new Month(pMonth, y);
		d = new Day(pDay, m);
	}

	public void increment() {
		if (!d.increment()) {
			if (!m.increment()) {
				y.increment();
				m.setMonth(1, y);
			}
			d.setDay(1, m);
		}
	}
	
	public int getdays() {
		return this.y.getdays() + this.m.getDays() + this.d.getDays();
	}
	
	public static int diff(Date d1, Date d2) { //d1 >= d2
		return d1.getdays() - d2.getdays();
	}

	public String toString() {
		return (y.getYear() + "-" + m.getMonth() + "-" + d.getDay());
	}
}
