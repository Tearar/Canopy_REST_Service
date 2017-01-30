package com.example;

public class Cluster {
	
	private final String ident;
	private final int dayOfWeek, weekOfYear;
	private final double startingTime, endingTime;
	private final double latitude, longitude;
	
	
	public Cluster(String ident, int dayOfWeek, int weekOfYear, double startingTime, double endingTime, double longitude, double latitude ) {
		
		this.ident = ident;
		this.dayOfWeek = dayOfWeek;
		this.weekOfYear = weekOfYear;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.latitude = longitude;
		this.longitude = latitude;
		}


	public String getIdent() {
		return ident;
	}


	public int getDayOfWeek() {
		return dayOfWeek;
	}


	public int getWeekOfYear() {
		return weekOfYear;
	}


	public double getStartingTime() {
		return startingTime;
	}


	public double getEndingTime() {
		return endingTime;
	}


	public double getLatitude() {
		return latitude;
	}


	public double getLongitude() {
		return longitude;
	}
	
	

}
