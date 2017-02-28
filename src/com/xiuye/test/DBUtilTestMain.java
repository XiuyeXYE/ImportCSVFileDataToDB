package com.xiuye.test;

import java.io.File;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import com.xiuye.util.DBConfigUtil;

public class DBUtilTestMain {

	public static void main(String[] args) {

		System.out.println(DBConfigUtil.driverClass());
		System.out.println(DBConfigUtil.url());
		System.out.println(DBConfigUtil.user());
		System.out.println(DBConfigUtil.password());

		Collection<?> list = DBConfigUtil.allTablesAliases();
		for(Object s : list){
			System.out.println(s);
		}

		Set<Entry<Object, Object>> set = DBConfigUtil.tables();
		for(Entry<Object, Object> e : set){
			System.out.println(e);
			System.out.println(e.getKey());
			System.out.println(e.getValue());
		}

//		System.out.println(DBConfigUtil.columnsCount("HOUSINGFUND_PERSON"));
//		System.out.println(DBConfigUtil.columnsCount("HOUSINGFUND_LOAN"));
//		System.out.println(DBConfigUtil.generateInsertSQL("HOUSINGFUND_LOAN"));
//		System.out.println(DBConfigUtil.generateInsertSQL("HOUSINGFUND_PERSON"));
//		System.out.println(DBConfigUtil.columnsFormatConditions("HOUSINGFUND_PERSON"));
//		System.out.println(DBConfigUtil.columnsFormatConditions("HOUSINGFUND_LOAN"));
//		DBConfigUtil.columnsFormatConditions("HOUSINGFUND_PERSON");
//		System.out.println(DBUtil.columnsCount("TESTTable"));
		System.out.println(new File(".").getAbsolutePath());

	}
}
