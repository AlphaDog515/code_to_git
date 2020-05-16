package com.demo03;

public class Num14_IsPalindrome {

}

/*
	判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。	
	示例 1:	
	输入: 121
	输出: true
	示例 2:	
	输入: -121
	输出: false
	解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
	示例 3:	
	输入: 10
	输出: false
	解释: 从右向左读, 为 01 。因此它不是一个回文数。
 */


//	简单粗暴，看看就行
class Solution_14_1 {
	public boolean isPalindrome(int x) {
		String reversedStr = (new StringBuilder(x + "")).reverse().toString();
		return (x + "").equals(reversedStr);
	}
}

/*
	通过取整和取余操作获取整数中对应的数字进行比较。	
	举个例子：1221 这个数字。	
	通过计算 1221 / 1000， 得首位1
	通过计算 1221 % 10， 可得末位 1
	进行比较
	再将 22 取出来继续比较 
 */


class Solution_14_2 {
	public boolean isPalindrome(int x) {
		// 边界判断
		if (x < 0)
			return false;
		int div = 1;
		while (x / div >= 10)
			div *= 10;
		while (x > 0) {
			int left = x / div;
			int right = x % 10;
			if (left != right)
				return false;
			x = (x % div) / 10;
			div /= 100;
		}
		return true;
	}
}

/*
	每次进行取余操作 （ %10），取出最低的数字：y = x % 10
	将最低的数字加到取出数的末尾：revertNum = revertNum * 10 + y
	每取一个最低位数字，x 都要自除以 10
	判断 x 是不是小于 revertNum ，当它小于的时候，说明数字已经对半或者过半了
	最后，判断奇偶数情况：如果是偶数的话，revertNum 和 x 相等；
		如果是奇数的话，最中间的数字就在revertNum 的最低位上，将它除以 10 以后应该和 x 相等。
 */
class Solution_14_3 {
	public boolean isPalindrome(int x) {
		// 思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
		if (x < 0 || (x % 10 == 0 && x != 0))
			return false;
		int revertedNumber = 0;
		while (x > revertedNumber) {
			revertedNumber = revertedNumber * 10 + x % 10;
			x /= 10;
		}
		return x == revertedNumber || x == revertedNumber / 10;
	}
}

class Solution_14_4 {
//	对于数字的末位，直接取余就可以了，对于数字的首位，我们可以这么算。
//	首先用一个变量记录数字的最高位，
//	比如 1232112321，可以标记 help 为 1000010000，
//	第一个末位为 11，第一个首位为 12321/10000=1，
//	接下来我们需要计算 232232 是否为回文，怎么计算呢？
//	我们需要去掉首位和末位。
//	可以采用 x % help / 10 的方式
//	12321%10000==2321 可以将最高位去掉，然后 2321/10==232 可以将最低为去掉。
//	最后不要忘记将 help/100。

	public boolean isPalindrome(int x) {
		if (x < 0) {
			return false;
		}
		int help = 1;
		int tmp = x;
		while (tmp >= 10) {
			help *= 10;
			tmp /= 10;
		}
		while (x != 0) {
			if (x % 10 != x / help) {
				return false;
			}
			x = x % help / 10;
			help /= 100;
		}
		return true;
	}
}




