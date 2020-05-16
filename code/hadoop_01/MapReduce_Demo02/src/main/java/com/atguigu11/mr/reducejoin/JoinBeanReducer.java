package com.atguigu11.mr.reducejoin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * 
 *  order.txt: 1001	01	1
 * 	 pd.txt :  01 小米
 *          orderid,pid,amount,source,pname
 * 1. (null,1001，01，1,order.txt,nodata)
 * (null,nodata，01，nodata,pd.txt,小米)
 * 
 * 2. 在输出之前，需要把数据按照source属性分类
 * 		只能在reduce中分类:clean up 与set up 里面只有context
 *
 */
public class JoinBeanReducer extends Reducer<NullWritable, JoinBean, NullWritable, JoinBean> {

	// 分类的集合
	private List<JoinBean> orderDatas = new ArrayList<>();
	private Map<String, String> pdDatas = new HashMap<>();

	// 根据source分类
	@Override
	protected void reduce(NullWritable key, Iterable<JoinBean> values,
			Reducer<NullWritable, JoinBean, NullWritable, JoinBean>.Context arg2)
			throws IOException, InterruptedException {

		for (JoinBean value : values) {
			if (value.getSource().equals("order.txt")) {
				// value自始至终都是同一个对象只是属性不断迭代
				// 将value对象的属性数据取出，封装到一个新的JoinBean中
				// 因为value至始至终都是同一个对象，只不过每次迭代，属性会随之变化
				JoinBean joinBean = new JoinBean();

				try {
					BeanUtils.copyProperties(joinBean, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				orderDatas.add(joinBean);
			} else {
				// 数据来源于pd.txt
				pdDatas.put(value.getPid(), value.getPname());
			}
		}
	}

	// Join数据，写出
	@Override
	protected void cleanup(Reducer<NullWritable, JoinBean, NullWritable, JoinBean>.Context context)
			throws IOException, InterruptedException {
		// 只输出来自orderDatas的数据
		for (JoinBean joinBean : orderDatas) {
			// 从Map中根据pid取出pname，设置到bean的pname属性中
			joinBean.setPname(pdDatas.get(joinBean.getPid()));
			context.write(NullWritable.get(), joinBean);
		}
	}

}
