package Test;

import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;

import BeanUtil.DoubleValueMapSorter;


public class DoubleValueMapSorterTest {
	public static void main(String args[]){
		HashMap<String, Double> str_douHashMap=new HashMap<String,Double>();
		str_douHashMap.put("1", 0.1);
		str_douHashMap.put("2", 0.1);
		str_douHashMap.put("3", 0.3);
		str_douHashMap.put("4", 0.4);
		DoubleValueMapSorter test=new DoubleValueMapSorter();
		List<Entry<String, Double>> list=test.SortCountMap(str_douHashMap);
		for(Entry<String, Double> entry:list){
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
	}
}
