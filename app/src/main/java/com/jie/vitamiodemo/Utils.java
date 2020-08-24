package com.jie.vitamiodemo;

public class Utils {

	/**
	 * 毫秒转换成h:m:s
	 */
	public static String formatTime(long duration) {
		long hours = (duration % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (duration % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (duration % (1000 * 60)) / 1000;

		StringBuilder stringBuilder = new StringBuilder();
		if (hours < 10) {
			stringBuilder.append("0");
		}
		stringBuilder.append(hours).append(":");

		if (minutes < 10) {
			stringBuilder.append("0");
		}
		stringBuilder.append(minutes).append(":");

		if (seconds < 10) {
			stringBuilder.append("0");
		}
		stringBuilder.append(seconds);

		return stringBuilder.toString();
	}
}