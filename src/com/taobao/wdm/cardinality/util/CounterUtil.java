/**
 * 
 */
package com.taobao.wdm.cardinality.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zunyuan.jy
 * 
 * @since 2013-11-8
 */
public class CounterUtil {

	public static void putToBucket(Map<String, Integer> map, int hash, int k) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 32; i++) {
			list.add(hash >> i & 1);
		}
		Collections.reverse(list);
		String bucket = "";
		for (int i = 0; i < k; i++) {
			bucket += list.get(i);
		}
		int r = CounterUtil.row(list, k);

		if (map.get(bucket) != null) {
			int g = map.get(bucket);
			map.put(bucket, r > g ? r : g);
		} else {
			map.put(bucket, r);
		}
	}

	public static int row(List<Integer> list, int offset) {
		int r = 0;
		for (int i = offset; i < list.size(); i++) {
			if (list.get(i) == 0)
				r++;
			else
				break;
		}
		if (r != list.size() - offset)
			r = r + 1;
		return r;
	}

	public static void putToBucketFast(Map<Integer, Integer> map, int hash,
			int k) {
		int bucket = (hash >> (32 - k)) & Integer.MAX_VALUE;
		int r = 1;
		for (int i = 0; i < 32 - k; i++) {
			r |= (r << 1);
		}
		r = hash & r;
		for (int i = 0; i < 32 - k; i++) {
			r = r >> 1;
			if ((r & 1) == 1) {
				r = i+1;
				break;
			}
			if (i == 32 - k - 1)
				r = 0;
		}
		if (map.get(bucket) != null) {
			int g = map.get(bucket);
			map.put(bucket, r > g ? r : g);
		} else {
			map.put(bucket, r);
		}
	}
}
