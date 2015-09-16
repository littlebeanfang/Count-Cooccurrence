package bottomLevel.formula;

import java.util.*;

public class SetOperation<T> {
	public SetOperation() {

	}

	public double jaccard(Set<T> set1, Set<T> set2) {
		Set<T> union = union(set1, set2);
		Set<T> intersect = intersect(set1, set2);
		if (union.isEmpty()) {
			return 0;
		}
		else{
			return ((double) intersect.size()) / union.size();
		}
	}
	
	public Set<T> union(Set<T> set1, Set<T> set2) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(set1);
		set.addAll(set2);
		return set;
	}
	
	public Set<T> intersect(Set<T> set1, Set<T> set2) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(set1);
		set.retainAll(set2);
		return set;
	}
}