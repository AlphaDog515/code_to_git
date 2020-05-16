package com.demo00;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Test05_BloomFilter {
	public static void main(String[] args) {
		BloomFilter fileter = new BloomFilter(7);
		System.out.println(fileter.addIfNotExist("1111111111111"));
		System.out.println(fileter.addIfNotExist("2222222222222222"));
		System.out.println(fileter.addIfNotExist("3333333333333333"));
		System.out.println(fileter.addIfNotExist("444444444444444"));
		System.out.println(fileter.addIfNotExist("5555555555555"));
		System.out.println(fileter.addIfNotExist("6666666666666"));
		System.out.println(fileter.addIfNotExist("1111111111111"));
		// fileter.saveFilterToFile("C:\\Users\\john\\Desktop\\1111\\11.obj");
		// fileter = readFilterFromFile("C:\\Users\\john\\Desktop\\111\\11.obj");
		System.out.println(fileter.getUseRate());
		System.out.println(fileter.addIfNotExist("1111111111111"));
	}
}

class BloomFilter implements Serializable {

	private final int[] seeds;
	private final int size;
	private final BitSet notebook;
	private final MisjudgmentRate rate;
	private final AtomicInteger useCount = new AtomicInteger();
	private final Double autoClearRate;

	// dataCount��Ԥ�ڴ�������ݹ�ģ
	public BloomFilter(int dataCount) {
		this(MisjudgmentRate.MIDDLE, dataCount, null);
	}

	// �Զ���չ������ڲ���Ϣ��ʹ�ñ��ʣ���null���ʾ�����Զ�����;
	// ��������ʹ���ʴﵽ100%ʱ�������۴���ʲô���ݣ�������Ϊ�������Ѿ�������;
	// ��ϣ��������ʹ���ʴﵽ80%ʱ�Զ��������ʹ�ã�����0.8
	public BloomFilter(MisjudgmentRate rate, int dataCount, Double autoClearRate) {
		// ÿ���ַ�����Ҫ��bitλ��*��������
		long bitSize = rate.seeds.length * dataCount;
		if (bitSize < 0 || bitSize > Integer.MAX_VALUE) {
			throw new RuntimeException("λ��̫������ˣ��뽵�������ʻ��߽������ݴ�С");
		}
		this.rate = rate;
		seeds = rate.seeds;
		size = (int) bitSize;
		// ����һ��BitSetλ����
		notebook = new BitSet(size);
		this.autoClearRate = autoClearRate;
	}

	// ������ڷ���true,�����ڷ���false
	public boolean addIfNotExist(String data) {
		// �Ƿ���Ҫ����
		checkNeedClear();
		// seeds.length����ÿһ��string��Ӧ���ٸ�bitλ��ÿһλ����һ������ֵ
		// ����data�����data�ַ����ĵ�һ������ֵindex�������һ��indexֵ��Ӧ��bit=false˵������dataֵ�����ڣ���ֱ�ӽ����ж�Ӧbitλ��Ϊtrue����;
		// �����һ��indexֵ��Ӧbit=true����indexֵ���棬����ʱ������˵��data�Ѿ����ڣ�
		// ��������ڶ���indexֵ��������indexֵ����������˵����dataֵ�����ڣ���֮ǰ�����index�����Ӧ��bitλ��Ϊtrue
		int[] indexs = new int[seeds.length];
		// �ٶ�data�Ѿ�����
		boolean exist = true;
		int index;
		for (int i = 0; i < seeds.length; i++) {
			// ����λhashֵ
			indexs[i] = index = hash(data, seeds[i]);
			if (exist) {
				// ���ĳһλbit�����ڣ���˵����data������
				if (!notebook.get(index)) {
					exist = false;
					// ��֮ǰ��bitλ��Ϊtrue
					for (int j = 0; j <= i; j++) {
						setTrue(indexs[j]);
					}
				}
			} else {
				// �����������ֱ����Ϊtrue
				setTrue(index);
			}
		}

		return exist;
	}

	private int hash(String data, int seeds) {
		char[] value = data.toCharArray();
		int hash = 0;
		if (value.length > 0) {
			for (int i = 0; i < value.length; i++) {
				hash = i * hash + value[i];
			}
		}
		hash = hash * seeds % size;
		return Math.abs(hash);
	}

	private void setTrue(int index) {
		useCount.incrementAndGet();
		notebook.set(index, true);
	}

	// ���BitSetʹ�ñ��ʳ�����ֵ����BitSet����
	private void checkNeedClear() {
		if (autoClearRate != null) {
			if (getUseRate() >= autoClearRate) {
				synchronized (this) {
					if (getUseRate() >= autoClearRate) {
						notebook.clear();
						useCount.set(0);
					}
				}
			}
		}
	}

	public Double getUseRate() {
		return (double) useCount.intValue() / (double) size;
	}

	public void saveFilterToFile(String path) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
			oos.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static BloomFilter readFilterFromFile(String path) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
			return (BloomFilter) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ��չ������еļ�¼��Ϣ
	 */
	public void clear() {
		useCount.set(0);
		notebook.clear();
	}

	public MisjudgmentRate getRate() {
		return rate;
	}

	/**
	 * �����λ��Խ�࣬������Խ�͵���Խռ�ڴ�
	 * 
	 * 4��λ�����ʴ����0.14689159766308
	 * 
	 * 8��λ�����ʴ����0.02157714146322
	 * 
	 * 16��λ�����ʴ����0.00046557303372
	 * 
	 * 32��λ�����ʴ����0.00000021167340
	 *
	 */
	public enum MisjudgmentRate {
		// ����Ҫѡȡ�������ܺܺõĽ��ʹ�����
		/**
		 * ÿ���ַ�������4��λ
		 */
		VERY_SMALL(new int[] { 2, 3, 5, 7 }),
		/**
		 * ÿ���ַ�������8��λ
		 */
		SMALL(new int[] { 2, 3, 5, 7, 11, 13, 17, 19 }), //
		/**
		 * ÿ���ַ�������16��λ
		 */
		MIDDLE(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53 }), //
		/**
		 * ÿ���ַ�������32��λ
		 */
		HIGH(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
				101, 103, 107, 109, 113, 127, 131 });

		private int[] seeds;

		// ö������MIDDLE���캯����seeds�����ʼ��
		private MisjudgmentRate(int[] seeds) {
			this.seeds = seeds;
		}

		public int[] getSeeds() {
			return seeds;
		}

		public void setSeeds(int[] seeds) {
			this.seeds = seeds;
		}
	}

}