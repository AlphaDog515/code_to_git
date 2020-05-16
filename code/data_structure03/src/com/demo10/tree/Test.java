package com.demo10.tree;

import java.util.ArrayList;

public class Test {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		ArrayList arrayList = new ArrayList();
		// ArrayList 底层维护了数组transient Object[] elementData;
		// ArrayList 底层仍然使用数组扩容
	}
}
