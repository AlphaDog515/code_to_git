package com.demo13.huffmancode;

public class Test01 {
	public static void main(String[] args) {
//		String strByte = "10001010";
//		byte b = (byte)(Integer.parseInt(strByte,2));
//		System.out.println(b);//-118
//		
//		int b1 = Integer.parseInt(strByte,2);
//		System.out.println(b1); //138
		
		byte bb = -15;
		System.out.println((int)bb);//-15
		
		String str = Integer.toBinaryString(bb);
		System.out.println(str); //11111111111111111111111111110001
		
		String str1 = "01111111111111111111111111110001";
		int b1 = Integer.parseInt(str1,2);
		System.out.println(b1);
		
//		byte b2 = (byte)Integer.parseInt(str,2);
//		System.out.println(b2);
	}

}