package BeanUtil;

import java.util.HashSet;
import java.util.Iterator;

public class BeanHashSet extends HashSet{
	public void Tranverse(){
		Iterator iterator=this.iterator();
		while(iterator.hasNext()){
			iterator.next();
		}
	}
}
