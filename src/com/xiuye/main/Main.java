package com.xiuye.main;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.xiuye.frame.LoginFrame;

public class Main {

	private static void initLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		initLookAndFeel();

		String s = "1.本”软件”的一切知识产权，\n" + "以及与”软件”相关的所有信息内容，\n" + "包括但不限于：文字表述及其组合、\n" + "图标、图饰、图像、图表、色彩、\n"
				+ "界面设计、版面框架、有关数据、附加程序、\n" + "印刷材料或电子文档等均为作者所有，\n" + "受著作权法和国际著作权条约以及其他知识产\n" + "权法律法规的保护。\n"
				+ "2.禁止反向工程、反向编译和反向汇编：\n" + "用户不得对本软件产品进行反向工程(Reverse Engineer)、\n"
				+ "反向编译(Decompile)或反向汇编(Disassemble)，\n" + "同时不得改动编译在程序文件内部的任何资源。\n" + "除法律、法规明文规定允许上述活动外，\n"
				+ "用户必须遵守此协议限制。\n" + "3.授权: 如需进行商业性的销售、复制、分发，\n" + "包括但不限于软件销售、预装、捆绑等，必须获得\n" + "作者的授权和许可。\n"
				+ "4.保留权利：本协议未明示授权的其他一切权利仍归作者所有，\n" + "用户使用其他权利时必须获得作者的书面同意。\n" + "5.修改和升级：不升级和维护!\n"
				+ "6.禁止用于非法活动,如触犯任何法律,由当事人自己承担!\n";

		if (checkFirstTime()) {

			if (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(null, s, "协议", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, new String[] { "接收", "不接受" }, null)) {
				try {
					new File("acception").createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				LoginFrame login = new LoginFrame();
			} else {
				System.exit(0);
			}
		}else{
			LoginFrame login = new LoginFrame();
		}

		// OpenFileFrame app = new OpenFileFrame();
		// app.display();

		// LoginFrame login = new LoginFrame();
	}

	private static boolean checkFirstTime() {

		boolean f = new File("acception").exists();
		if(f){
			return false;
		}
		else{
			return true;
		}
	}

}
