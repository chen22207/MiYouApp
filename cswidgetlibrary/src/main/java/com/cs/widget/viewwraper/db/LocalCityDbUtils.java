package com.cs.widget.viewwraper.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by cs on 16/4/26.
 */
public class LocalCityDbUtils {
	private static final String DB_NAME = "china_city.db";
	private static LocalCityDbUtils instance;
	private Context context;
	private SQLiteDatabase sqLiteDatabase;
	private CityData cityData;

	private LocalCityDbUtils() {
	}

	public static LocalCityDbUtils getInstance() {
		if (instance == null) {
			instance = new LocalCityDbUtils();
		}
		return instance;
	}

	public void initDb(Context context) {
		this.context = context;
		File file = context.getDatabasePath(DB_NAME);
		if (file.exists()) {
			sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
		} else {
			AssetManager manager = context.getAssets();
			try {
				InputStream is = manager.open(DB_NAME);
				FileOutputStream fileOutputStream = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int c = 0;
				while ((c = is.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, c);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
	}

	public void initCityData() {
		cityData = new CityData();
		sqLiteDatabase.beginTransaction();
		Cursor c = sqLiteDatabase.query("T_Province", new String[]{"ProName", "ProSort"}, null, null, null, null, null, null);
		ArrayList<String> province = new ArrayList<>();
		ArrayList<ArrayList<String>> cities = new ArrayList<>();
		ArrayList<ArrayList<ArrayList<String>>> zones = new ArrayList<>();
		while (c.moveToNext()) {
			String proName = c.getString(0);
			String proSort = c.getString(1);
			province.add(proName);
			Cursor c1 = sqLiteDatabase.query("T_City", new String[]{"CityName", "ProID", "CitySort"}, "ProID=?", new String[]{proSort}, null, null, null);
			ArrayList<String> city = new ArrayList<>();
			ArrayList<ArrayList<String>> zone = new ArrayList<>();
			while (c1.moveToNext()) {
				String cityName = c1.getString(0);
				String proID = c1.getString(1);
				String citySort = c1.getString(2);
				city.add(cityName);
				Cursor c2 = sqLiteDatabase.query("T_Zone", new String[]{"ZoneId", "ZoneName"}, "CityId=?", new String[]{citySort}, null, null, null);
				ArrayList<String> zone1 = new ArrayList<>();
				while (c2.moveToNext()) {
					String zoneId = c2.getString(0);
					String zoneName = c2.getString(1);
					zone1.add(zoneName);
				}
				zone.add(zone1);
				c2.close();
			}
			cities.add(city);
			zones.add(zone);
			c1.close();
		}
		c.close();
		sqLiteDatabase.endTransaction();
		cityData.setProvince(province);
		cityData.setCity(cities);
		cityData.setZone(zones);
	}

	public CityData getCityData() {
		return cityData;
	}

	public class CityData {
		private ArrayList<String> province;
		private ArrayList<ArrayList<String>> city;
		private ArrayList<ArrayList<ArrayList<String>>> zone;

		public ArrayList<String> getProvince() {
			return province;
		}

		public void setProvince(ArrayList<String> province) {
			this.province = province;
		}

		public ArrayList<ArrayList<String>> getCity() {
			return city;
		}

		public void setCity(ArrayList<ArrayList<String>> city) {
			this.city = city;
		}

		public ArrayList<ArrayList<ArrayList<String>>> getZone() {
			return zone;
		}

		public void setZone(ArrayList<ArrayList<ArrayList<String>>> zone) {
			this.zone = zone;
		}
	}
}
