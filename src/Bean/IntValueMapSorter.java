package Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntValueMapSorter{
	public List<Map.Entry<String,Integer>> SortCountMap(HashMap<String, Integer> popmap){
		List<Map.Entry<String,Integer>> list=new ArrayList<>();
		list.addAll(popmap.entrySet());  
        ValueComparator vc=new ValueComparator();  
        Collections.sort(list,vc);
		return list;  
	}
	private class ValueComparator implements Comparator<Map.Entry<String, Integer>>    
	{    
	    public int compare(Map.Entry<String, Integer> mp1, Map.Entry<String, Integer> mp2)     
	    {    
	        return mp2.getValue() - mp1.getValue();  
	    }    
	} 
}
