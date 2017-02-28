package com.xiuye.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class XYProperties {

	public static class Entry<K, V> {
		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Entry [key=" + key + ", value=" + value + "]";
		}

	}

	private List<Entry<String, String>> entrys;

	public XYProperties() {
		this.entrys = new ArrayList<>();
	}

	public void load(InputStream is) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
				//	System.out.println("line := " + line);
					String[] kv = line.split("=");
					Entry<String, String> entry = null;
					//System.out.println(kv.length);
					if(kv.length==2){
						entry = new Entry<String, String>(kv[0], kv[1]);
					}
					else{
						entry = new Entry<String, String>(kv[0], "");
					}
					this.entrys.add(entry);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load(String fileName) {

		try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr);) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] kv = line.split("=");
					Entry<String, String> entry = new Entry<String, String>(kv[0], kv[1]);
					this.entrys.add(entry);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<Entry<String, String>> entryList() {
		return this.entrys;
	}

	public int size() {
		return this.entrys.size();
	}

}
