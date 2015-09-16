package BeanUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DoubleValueMapDecSorter {
	public List<Map.Entry<String,Double>> SortCountMap(HashMap<String, Double> popmap){
		List<Map.Entry<String,Double>> list=new ArrayList<>();
		list.addAll(popmap.entrySet());  
        ValueComparator vc=new ValueComparator();  
        Collections.sort(list,vc);
		return list;  
	}
	private class ValueComparator implements Comparator<Map.Entry<String, Double>>    
	{    
	    public int compare(Map.Entry<String, Double> mp1, Map.Entry<String, Double> mp2)     
	    {    double diff=mp2.getValue() - mp1.getValue();
//	        return ((mp2.getValue() - mp1.getValue())>0?1:-1);  
	    	if(diff==0){
	    		return 0;
	    	}else if(diff<0){
	    		return 1;
	    	}else{
	    		return -1;
	    	}
	    }    
	} 
}
