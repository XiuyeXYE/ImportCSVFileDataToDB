package com.xiuye.frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.xiuye.security.MD5;

public class LoginFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -2372313134920073396L;

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;

	public LoginFrame() {
		this.init();
		this.initLookAndFeel();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("app.png");
		this.setIconImage(image);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		// this.setResizable(false);
		this.setVisible(true);

	}

	private JTextField user;
	private JPasswordField password;

	private void init() {

		JLabel userL = new JLabel("用户名:", JLabel.CENTER);
		this.defaultFont(userL);
		userL.setForeground(Color.RED);
		JLabel passwordL = new JLabel("密码:", JLabel.CENTER);
		passwordL.setForeground(Color.RED);
		this.defaultFont(passwordL);
		this.user = new JTextField();
		this.user.setColumns(11);
		this.user.setForeground(Color.MAGENTA);
		this.defaultFont(this.user);
		this.password = new JPasswordField();
		this.defaultFont(this.password);
		this.password.setForeground(Color.MAGENTA);

		Container c = this.getContentPane();
		JPanel panel = new JPanel();
		c.setLayout(new FlowLayout());
		c.add(panel);
		panel.setLayout(new GridLayout(3, 2));

		panel.add(userL);
		panel.add(this.user);
		panel.add(passwordL);
		panel.add(this.password);

		JButton login = new JButton("登录");
		login.setForeground(Color.BLUE);
		this.defaultFont(login);
		JButton cancel = new JButton("取消");
		cancel.setForeground(Color.GREEN);
		this.defaultFont(cancel);
		panel.add(login);
		panel.add(cancel);

		this.buttonAddListener(login, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MD5 md5 = new MD5();

				String admin = user.getText();
				String pass = password.getText();
				try {
					if (admin != null && !admin.isEmpty() && pass != null && !pass.isEmpty()) {
						if ("f3341aeb7ce82fc782ac5481fd45eb94".equalsIgnoreCase(md5.doFinal(admin.trim()))
								&& "da4c1d063d7d7cf17eced6cf3bb63b90".equalsIgnoreCase(md5.doFinal(pass.trim()))) {
							JOptionPane.showMessageDialog(LoginFrame.this, "登录成功!", "成功",
									JOptionPane.INFORMATION_MESSAGE);
							call();
						} else {
							JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误!", "错误",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else{
						JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码为空!", "警告",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		this.buttonAddListener(cancel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(LoginFrame.this, "确定退出?", "退出",
						JOptionPane.OK_CANCEL_OPTION))
					System.exit(0);

			}
		});

	}

	private void buttonAddListener(JButton button, ActionListener listener) {
		button.addActionListener(listener);
	}

	private void defaultFont(JComponent comp) {
		comp.setFont(new Font(comp.getFont().getFontName(), Font.BOLD, 25));
	}

	private void call() {

		OpenFileFrame app = new OpenFileFrame();
		app.display();
		this.dispose();
	}

	private void initLookAndFeel() {

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

}
