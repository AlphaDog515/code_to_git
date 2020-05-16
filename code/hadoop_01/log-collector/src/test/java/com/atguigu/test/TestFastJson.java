package com.atguigu.test;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/*
 * 1. Gson:   new Gson()返回一个Gson对象！
 * 				Gson.toJson():  讲java对象转为Json
 * 				Gson.fromJson():  将JSON字符串转为Java对象
 * 
 * 2. FastJson：   
 * 				JSON： 最核心的类
 * 					toJSONString(Object)： 将java对象转为JSON
 * 					parseObject(String, Class)： 将JSON转为java对象
 * 
 * 				JSONObject: 代表一个JSON对象.本质使用Map实现！
 * 		
 * 						{属性名:属性值,...}
 * 						put:  向对象中添加属性
 * 						get:  查询指定的属性值
 * 				JSONArray：   代表一个JSON数组
 * 						[{},{},{}]
 * 						add： 向array中添加元素
 * 						get: 通过索引取出元素
 */
public class TestFastJson {

	@Test
	public void testFastJson() throws Exception {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("name", "jack");
		jsonObject.put("age", 18);
		jsonObject.put("gender", "male");

		// {"gender":"male","name":"jack","age":18}
		System.out.println(jsonObject);

	}

	@Test
	public void testFastJson1() throws Exception {

		String str = "{\"gender\":\"male\",\"name\":\"jack\",\"age\":18}";

		// Person p = new JSON() {}.parseObject(str, Person.class);

		Person p = JSON.parseObject(str, Person.class);

		System.out.println(p);

	}

	@Test
	public void testFastJson3() throws Exception {

		JSONObject jsonObject1 = new JSONObject();

		jsonObject1.put("name", "jack");
		jsonObject1.put("age", 18);
		jsonObject1.put("gender", "male");

		JSONObject jsonObject2 = new JSONObject();

		jsonObject2.put("name", "jack");
		jsonObject2.put("age", 19);
		jsonObject2.put("gender", "male");

		JSONArray jsonArray = new JSONArray();

		jsonArray.add(jsonObject1);
		jsonArray.add(jsonObject2);

		String str = jsonArray.toString();

		// [{"gender":"male","name":"jack","age":18},{"gender":"male","name":"jack","age":19}]
		System.out.println(str);

	}

	@Test
	public void testFastJson4() throws Exception {
		// 将json数组转化为list
		String str = "[{\"gender\":\"male\",\"name\":\"jack\",\"age\":18},{\"gender\":\"male\",\"name\":\"jack\",\"age\":19}]";

		List<Person> res = JSON.parseArray(str, Person.class);
		System.out.println(res);
	}

}
