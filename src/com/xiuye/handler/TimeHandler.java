package com.xiuye.handler;

import javax.swing.JLabel;

public class TimeHandler {

	private static long start = 0;
	private static long end = 0;

	public static void start() {
		start = System.currentTimeMillis();
	}

	public static String currentTime() {
		end = System.currentTimeMillis();
		long s, m, h;
		long l = end - start;
		l /= 1000;

		s = l%60;
		l /= 60;
		m = l%60;
		l /= 60;
		h = l;
		return h + ":" + m + ":" + s;
	}

	private static boolean on;

	public static void startTiming(JLabel time){
		start();
		on = true;
		Thread t = new Thread(){
			@Override
			public void run() {
				while(on)
					time.setText(currentTime());

			}
		};

		t.setName("timingThread");

		t.start();
	}

	public static void end(){
		on = false;
	}
}
