package com.demo02;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*去除重复的数组： 
 * 方法一：需要一个存储去重后元素的数组，然后两层循环，
 * 外层遍历原数组，内容逐个判断是否和之后的元素重复，然后再提出来存入新的数组。
 * 方法System.arraycopy(被复制的数组，起始下标，接收的新数组，起始下标，复制的个数);
 * 可以直接复制数组，既然这样，我就没必要纠结新数组的长度了，
 * 直接声明一个跟原数组长度一样的临时数组，只要记录下去重后的元素个数，
 * 然后就可以确定去重后数组长度再去将临时数组拷贝到新数组就行了。 
 * 
 * 方法二:只需要创建一个集合，然后遍历数组逐一放入集合，
 * 只要在放入之前用contains()方法判断一下集合中是否已经存在这个元素就行了，
 * 然后用toArray转成数组一切搞定。 
 * 
 * 方法三：最简单的方法就是利用Set集合无序不可重复的特性进行元素过滤。 
 * 
 * 方法四：链表的哈希集合：有顺序，不重复。 
 */

public class BaseQuestion03 {

	public static void main(String[] args) {		
		String[] strs = { "2007-1-01", "2007-1-06", "2007-1-01", "2007-1-08", "2007-1-08" };
		printArr(strs);
		System.out.println("------------");

		Object[] newArr = ifRepeat(strs);
		printArr(newArr);
		System.out.println("------------");

		ifRepeat2(strs);
		System.out.println("------------");

		Object[] newArr3 = ifRepeat3(strs);
		printArr(newArr3);
		System.out.println("------------");

		ifRepeat4(strs);

	}

	public static void printArr(Object[] arr) {
		for (Object object : arr) {
			System.out.println(object);
		}
	}

	// 方法一：需要传入一个Object数组，然后返回去重后的数组
	public static Object[] ifRepeat(Object[] arr) {
		// 用来记录去除重复之后的数组长度和给临时数组作为下标索引
		int t = 0;
		// 临时数组
		Object[] tempArr = new Object[arr.length];
		// 遍历原数组
		for (int i = 0; i < arr.length; i++) {
			// 声明一个标记，并每次重置
			boolean isTrue = true;
			// 内层循环将原数组的元素逐个对比
			for (int j = i + 1; j < arr.length; j++) {
				// 如果发现有重复元素，改变标记状态并结束当次内层循环
				if (arr[i] == arr[j]) {
					isTrue = false;
					break;
				}
			}
			// 判断标记是否被改变，如果没被改变就是没有重复元素
			if (isTrue) {
				// 没有元素就将原数组的元素赋给临时数组
				tempArr[t] = arr[i];
				// 走到这里证明当前元素没有重复，那么记录自增
				t++;
			}
		}
		// 声明需要返回的数组，这个才是去重后的数组
		Object[] newArr = new Object[t];
		// 用arraycopy方法将刚才去重的数组拷贝到新数组并返回
		System.arraycopy(tempArr, 0, newArr, 0, t);
		return newArr;
	}

	/*
	 * 方法二：只需要创建一个集合，然后遍历数组逐一放入集合，
	 * 只要在放入之前用contains()方法判断一下集合中是否已经存在这个元素就行了，
	 * 然后用toArray转成数组一切搞定。
	 */
	public static void ifRepeat2(Object[] arr) {
		// 创建一个集合
		List list = new ArrayList();
		// 遍历数组往集合里存元素
		for (int i = 0; i < arr.length; i++) {
			// 如果集合里面没有相同的元素才往里存
			if (!list.contains(arr[i])) {
				list.add(arr[i]);
			}
		}

		// toArray()方法会返回一个包含集合所有元素的Object类型数组
		Object[] newArr = list.toArray();
		// 遍历输出一下测试是否有效
		for (int i = 0; i < newArr.length; i++) {
			System.out.println(" " + newArr[i]);
		}

	}

	/*
	 * 方法三：最简单的方法就是利用Set集合无序不可重复的特性进行元素过滤。
	 */
	public static Object[] ifRepeat3(Object[] arr) {
		// 实例化一个set集合
		Set set = new HashSet();
		// 遍历数组并存入集合,如果元素已存在则不会重复存入
		for (int i = 0; i < arr.length; i++) {
			set.add(arr[i]);
		}
		// 返回Set集合的数组形式
		return set.toArray();
	}

	/*
	 * 方法四：链表的哈希集合：有顺序，不重复。
	 */
	public static void ifRepeat4(Object[] arr) {
		LinkedHashSet<Object> haoma = new LinkedHashSet<Object>();
		for (int i = 0; i < arr.length; i++) {
			haoma.add(arr[i]);
		}

		// 创建迭代器
		Iterator<Object> iterator = haoma.iterator();
		int a = 0;
		// 迭代集合
		while (iterator.hasNext()) { // true
			Object c = iterator.next();
			System.out.println(c);
		}

	}

}