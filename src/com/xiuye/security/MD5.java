package com.xiuye.security;

import java.util.Arrays;

public class MD5 {

	public MD5() {
	}

	/**
	 * 对MD5算法简要的叙述可以为：MD5以512位分组来处理输入的信息，且每一分组又被划分为16个32位子分组，经过了一系列的处理后，
	 * 算法的输出由四个32位分组组成，将这四个32位分组级联后将生成一个128位散列值。
	 *
	 * 第一步、填充：如果输入信息的长度(bit)对512求余的结果不等于448，就需要填充使得对512求余的结果等于448。
	 * 填充的方法是填充一个1和n个0。填充完后，信息的长度就为N*512+448(bit)；
	 *
	 * 第二步、记录信息长度：用64位来存储填充前信息长度。这64位加在第一步结果的后面，这样信息长度就变为N*512+448+64=(N+1)*512位
	 * 。
	 *
	 * 第三步、装入标准的幻数（四个整数）：标准的幻数（物理顺序）是（A=(01234567)16，B=(89ABCDEF)16，C=(FEDCBA98)
	 * 16
	 * ，D=(76543210)16）。如果在程序中定义应该是（A=0X67452301L，B=0XEFCDAB89L，C=0X98BADCFEL，D
	 * =0X10325476L）。有点晕哈，其实想一想就明白了。
	 *
	 * 第四步、四轮循环运算：循环的次数是分组的个数（N+1）
	 *
	 * @param args
	 */

	/**
	 * 填充数据
	 *
	 * @param s
	 * @return
	 */
	public byte[] fillInData(String s) {
		byte[] data = s.getBytes();
		int length = data.length;
		int rest = length % 64;
		int additionalLength = 0;
		if (rest > 56) {
			additionalLength = 56 + 64 - rest;
		} else if (rest < 56) {
			additionalLength = 56 - rest;
		}
		int newLength = additionalLength + length;
		data = Arrays.copyOf(data, newLength + 8);
		byte[] dataLength = new byte[8];
		dataLength[0] = (byte) (length & 0xFF);
		dataLength[1] = (byte) (length >>> 8 & 0xFF);
		dataLength[2] = (byte) (length >>> 16 & 0xFF);
		dataLength[3] = (byte) (length >>> 24 & 0xFF);
		System.arraycopy(dataLength, 0, data, data.length - 1 - 8, 8);

		return data;
	}

	/**
	 * 以512分组
	 *
	 * @param data
	 * @return
	 */
	public byte[][] fromTotalLengthTo512ByteGroups(byte[] data) {

		int i = data.length / 64;
		byte[][] groups = new byte[i][64];

		for (i = 0; i < groups.length; i++) {

			for (int j = 0; j < groups[i].length; j++) {
				groups[i][j] = data[i * groups[i].length + j];
			}

		}

		return groups;

	}

	/**
	 * 512 位数据分组 data 64 byte <-> 512 bit 512分组16组
	 *
	 * @param args
	 * @throws Exception
	 */
	public byte[][] from512BitTo16GroupsPer32Bit(byte[] data) throws Exception {

		if (data.length != 64) {
			throw new Exception("data's length is not 64 byte");
		}
		byte[][] a = new byte[16][4];
		// System.out.println(a.length);//16
		// System.out.println(a[0].length);//4
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = data[i * a[i].length + j];
			}
		}

		return a;
	}

	/**
	 * 16组32位数据转化为 16组整型数据
	 *
	 * @param a
	 * @return
	 * @throws Exception
	 */
	public int[] from16GroupsPer32BitTo16Ints(byte[][] a) throws Exception {
		if (a.length != 16) {
			throw new Exception(
					"data's length is not 16 groups");
		}
		int[] b = new int[a.length];

		for (int i = 0; i < a.length; i++) {
			b[i] = 0;
			for (int j = 0; j < a[i].length; j++) {
				b[i] = b[i] << 8 | a[i][j];
			}

		}

		return b;

	}

	/**
	 * operand priority ~ -> & -> ^ -> |
	 *
	 * F(X,Y,Z)=(X&Y)|((~X)&Z) G(X,Y,Z)=(X&Z)|(Y&(~Z)) H(X,Y,Z)=X^Y^Z
	 * I(X,Y,Z)=Y^(X|(~Z))
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int F(int x, int y, int z) {
		return x & y | ~x & z;
	}

	public int G(int x, int y, int z) {
		return x & y | y & ~z;
	}

	public int H(int x, int y, int z) {
		return x ^ y ^ z;
	}

	public int I(int x, int y, int z) {
		return y ^ (x | ~z);
	}

	private int A = 0X67452301;
	private int B = 0XEFCDAB89;
	private int C = 0X98BADCFE;
	private int D = 0X10325476;

	/**
	 * FF(a,b,c,d,Mj,s,ti)表示a=b+((a+F(b,c,d)+Mj+ti)<<<s)
	 * GG(a,b,c,d,Mj,s,ti)表示a=b+((a+G(b,c,d)+Mj+ti)<<<s)
	 * HH(a,b,c,d,Mj,s,ti)表示a=b+((a+H(b,c,d)+Mj+ti)<<<s)
	 * II(a,b,c,d,Mj,s,ti)表示a=b+((a+I(b,c,d)+Mj+ti)<<<s) 循环移动实现如下: a>>>m|a<<n-m
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param mj
	 * @param ti
	 * @return
	 */
	public int FF(int a, int b, int c, int d, int mj, int s, int ti) {
		a = a + F(b, c, d) + mj + ti;
		return b + (a << s | a >>> (32 - s));
	}

	public int GG(int a, int b, int c, int d, int mj, int s, int ti) {
		a = (a + G(b, c, d) + mj + ti);
		return b + (a << s | a >>> (32 - s));
	}

	public int HH(int a, int b, int c, int d, int mj, int s, int ti) {
		a = (a + H(b, c, d) + mj + ti);
		return b + (a << s | a >>> (32 - s));
	}

	public int II(int a, int b, int c, int d, int mj, int s, int ti) {
		a = (a + I(b, c, d) + mj + ti);
		return b + (a << s | a >>> (32 - s));
	}

	public void perGroupsCalculate(int[] _16Groups) {

		int a = A;
		int b = B;
		int c = C;
		int d = D;

		/**
		 * 第一轮
		 */
		a = FF(a, b, c, d, _16Groups[0], 7, 0xd76aa478);
		b = FF(d, a, b, c, _16Groups[1], 12, 0xe8c7b756);
		c = FF(c, d, a, b, _16Groups[2], 17, 0x242070db);
		d = FF(b, c, d, a, _16Groups[3], 22, 0xc1bdceee);
		a = FF(a, b, c, d, _16Groups[4], 7, 0xf57c0faf);
		b = FF(d, a, b, c, _16Groups[5], 12, 0x4787c62a);
		c = FF(c, d, a, b, _16Groups[6], 17, 0xa8304613);
		d = FF(b, c, d, a, _16Groups[7], 22, 0xfd469501);
		a = FF(a, b, c, d, _16Groups[8], 7, 0x698098d8);
		b = FF(d, a, b, c, _16Groups[9], 12, 0x8b44f7af);
		c = FF(c, d, a, b, _16Groups[10], 17, 0xffff5bb1);
		d = FF(b, c, d, a, _16Groups[11], 22, 0x895cd7be);
		a = FF(a, b, c, d, _16Groups[12], 7, 0x6b901122);
		b = FF(d, a, b, c, _16Groups[13], 12, 0xfd987193);
		c = FF(c, d, a, b, _16Groups[14], 17, 0xa679438e);
		d = FF(b, c, d, a, _16Groups[15], 22, 0x49b40821);

		// 第二轮
		a = GG(a, b, c, d, _16Groups[1], 5, 0xf61e2562);
		b = GG(d, a, b, c, _16Groups[6], 9, 0xc040b340);
		c = GG(c, d, a, b, _16Groups[11], 14, 0x265e5a51);
		d = GG(b, c, d, a, _16Groups[0], 20, 0xe9b6c7aa);
		a = GG(a, b, c, d, _16Groups[5], 5, 0xd62f105d);
		b = GG(d, a, b, c, _16Groups[10], 9, 0x02441453);
		c = GG(c, d, a, b, _16Groups[15], 14, 0xd8a1e681);
		d = GG(b, c, d, a, _16Groups[4], 20, 0xe7d3fbc8);
		a = GG(a, b, c, d, _16Groups[9], 5, 0x21e1cde6);
		b = GG(d, a, b, c, _16Groups[14], 9, 0xc33707d6);
		c = GG(c, d, a, b, _16Groups[3], 14, 0xf4d50d87);
		d = GG(b, c, d, a, _16Groups[8], 20, 0x455a14ed);
		a = GG(a, b, c, d, _16Groups[13], 5, 0xa9e3e905);
		b = GG(d, a, b, c, _16Groups[2], 9, 0xfcefa3f8);
		c = GG(c, d, a, b, _16Groups[7], 14, 0x676f02d9);
		d = GG(b, c, d, a, _16Groups[12], 20, 0x8d2a4c8a);

		// 第三轮
		a = HH(a, b, c, d, _16Groups[5], 4, 0xfffa3942);
		b = HH(d, a, b, c, _16Groups[8], 11, 0x8771f681);
		c = HH(c, d, a, b, _16Groups[11], 16, 0x6d9d6122);
		d = HH(b, c, d, a, _16Groups[14], 23, 0xfde5380c);
		a = HH(a, b, c, d, _16Groups[1], 4, 0xa4beea44);
		b = HH(d, a, b, c, _16Groups[4], 11, 0x4bdecfa9);
		c = HH(c, d, a, b, _16Groups[7], 16, 0xf6bb4b60);
		d = HH(b, c, d, a, _16Groups[10], 23, 0xbebfbc70);
		a = HH(a, b, c, d, _16Groups[13], 4, 0x289b7ec6);
		b = HH(d, a, b, c, _16Groups[0], 11, 0xeaa127fa);
		c = HH(c, d, a, b, _16Groups[3], 16, 0xd4ef3085);
		d = HH(b, c, d, a, _16Groups[6], 23, 0x04881d05);
		a = HH(a, b, c, d, _16Groups[9], 4, 0xd9d4d039);
		b = HH(d, a, b, c, _16Groups[12], 11, 0xe6db99e5);
		c = HH(c, d, a, b, _16Groups[15], 16, 0x1fa27cf8);
		d = HH(b, c, d, a, _16Groups[2], 23, 0xc4ac5665);

		// 第四轮
		a = II(a, b, c, d, _16Groups[0], 6, 0xf4292244);
		b = II(d, a, b, c, _16Groups[7], 10, 0x432aff97);
		c = II(c, d, a, b, _16Groups[14], 15, 0xab9423a7);
		d = II(b, c, d, a, _16Groups[5], 21, 0xfc93a039);
		a = II(a, b, c, d, _16Groups[12], 6, 0x655b59c3);
		b = II(d, a, b, c, _16Groups[3], 10, 0x8f0ccc92);
		c = II(c, d, a, b, _16Groups[10], 15, 0xffeff47d);
		d = II(b, c, d, a, _16Groups[1], 21, 0x85845dd1);
		a = II(a, b, c, d, _16Groups[8], 6, 0x6fa87e4f);
		b = II(d, a, b, c, _16Groups[15], 10, 0xfe2ce6e0);
		c = II(c, d, a, b, _16Groups[6], 15, 0xa3014314);
		d = II(b, c, d, a, _16Groups[13], 21, 0x4e0811a1);
		a = II(a, b, c, d, _16Groups[4], 6, 0xf7537e82);
		b = II(d, a, b, c, _16Groups[11], 10, 0xbd3af235);
		c = II(c, d, a, b, _16Groups[2], 15, 0x2ad7d2bb);
		d = II(b, c, d, a, _16Groups[9], 21, 0xeb86d391);

		A += a;
		B += b;
		C += c;
		D += d;

	}

	public void resetMagicNumbers() {
		A = 0X67452301;
		B = 0XEFCDAB89;
		C = 0X98BADCFE;
		D = 0X10325476;
	}

	public String doFinal(String s) throws Exception {

		this.resetMagicNumbers();
		byte[] data = this.fillInData(s);
		byte[][] ds = this.fromTotalLengthTo512ByteGroups(data);

		for (int i = 0; i < ds.length; i++) {



			byte[][] temp512 = this.from512BitTo16GroupsPer32Bit(ds[i]);
			int[] temp16 = this.from16GroupsPer32BitTo16Ints(temp512);
			this.perGroupsCalculate(temp16);


		}
		String ret = Integer.toHexString(A) + Integer.toHexString(B)
				+ Integer.toHexString(C) + Integer.toHexString(D);

		return ret;
	}


}

