package com.sound;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatingTime {
	Date date= new Date();
	public String date()
	{
		SimpleDateFormat ft=new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
		String d=ft.format(date).toString();
		return d;
	}
	public String time()
	{
		SimpleDateFormat ft1=new SimpleDateFormat("hh:mm a");
		String t=ft1.format(date);
		return t;
	}
}
