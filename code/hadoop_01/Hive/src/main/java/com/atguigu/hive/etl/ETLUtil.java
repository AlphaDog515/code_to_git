package com.atguigu.hive.etl;

/*
    ①统一集合类型的分隔符为&，替换最后一个字段的分隔符由\t替换为&
	②去除category中每个类别多余的空格
	③每行数据至少有9个字段
 */
public class ETLUtil {

	public static String parseString(String source) {

		// 每行数据至少有9个字段
		String[] words = source.split("\t");

		// 过滤字段少于9个的数据
		if (words.length < 10) {
			return null;
		}

		// 去除category中每个类别多余的空格
		words[3] = words[3].replaceAll(" ", "");

		StringBuffer sb = new StringBuffer();

		// 替换最后一个字段的分隔符由\t替换为&
		for (int i = 0; i < words.length; i++) {
			// 前9个，每个字段之间拼接\t
			if (i < 9) {
				sb.append(words[i] + "\t");
			} else {
				sb.append(words[i] + "&");
			}
		}
		String result = sb.toString();
		// 取结果的第一个字符到倒数第二个字符，过滤掉多余的&
		return result.substring(0, result.length() - 1);
	}

	public static void main(String[] args) {
		String source = "SDNkMu	8ZT68	w00dy911	630	People & Blogs	186	10181	3.49	494	257";
		System.out.println(ETLUtil.parseString(source));
	}
}
