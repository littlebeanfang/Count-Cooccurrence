package Graph;


import java.util.*;

public class PageRanker<T> {
	final double teleportFactor = 0.15;
	double convergenceError;

	private HashMap<T, HashMap<T, Double>> linkGraph = new HashMap<T, HashMap<T, Double>>();
	private HashMap<T, Double> pagesRank = new HashMap<T, Double>();

	public PageRanker(HashMap<T, HashMap<T, Double>> linkGraph, double convergenceError) throws Exception {
		this.linkGraph = normalizeGraph(linkGraph);
		this.convergenceError = convergenceError;
		calcuPageRank();
	}

	public HashMap<T, Double> getPagesRank() {
		return pagesRank;
	}

	private HashMap<T, HashMap<T, Double>> normalizeGraph(HashMap<T, HashMap<T, Double>> graph) {
		HashMap<T, HashMap<T, Double>> normalizedGraph = new HashMap<T, HashMap<T, Double>>();
		for (T page1 : graph.keySet()) {
			normalizedGraph.put(page1, new HashMap<T, Double>());
			double totalWeight = 0;
			for (T page2 : graph.get(page1).keySet()) {
				totalWeight += graph.get(page1).get(page2);
			}
			for (T page2 : graph.get(page1).keySet()) {
				normalizedGraph.get(page1).put(page2, (double) graph.get(page1).get(page2) / totalWeight);
			}
		}
		return normalizedGraph;
	}

	private void calcuPageRank() throws Exception {
		for (T page1 : linkGraph.keySet()) {
			pagesRank.put(page1, (double) 0);
			for (T page2 : linkGraph.get(page1).keySet()) {
				pagesRank.put(page2, (double) 0);
			}
		}
		final int N = pagesRank.size();
		for (T page : pagesRank.keySet()) {
			pagesRank.put(page, 1.0 / N);
		}
		for (int iteration = 1; iteration <= 1000; ++iteration) {
			HashMap<T, Double> newPagesRank = new HashMap<T, Double>();
			for (T page : pagesRank.keySet()) {
				newPagesRank.put(page, (double) 0);
			}
			for (T thisPage : linkGraph.keySet()) {
				for (T nextPage : linkGraph.get(thisPage).keySet()) {
					double increment = pagesRank.get(thisPage) * linkGraph.get(thisPage).get(nextPage) * (1 - teleportFactor);
					newPagesRank.put(nextPage, newPagesRank.get(nextPage) + increment);
				}
			}
			double leak = 1;
			for (T page : newPagesRank.keySet()) {
				leak -= newPagesRank.get(page);
			}
			for (T page : newPagesRank.keySet()) {
				newPagesRank.put(page, newPagesRank.get(page) + (leak / N));
			}
			boolean isConverge = true;
			for (T page : pagesRank.keySet()) {
				double diff = Math.abs(newPagesRank.get(page) - pagesRank.get(page));
				if (diff > convergenceError / pagesRank.size()) {
					isConverge = false;
					break;
				}
			}
			System.out.println("iteration " + iteration + " is done!");
			pagesRank = newPagesRank;
			if (isConverge) {
				break;
			}
		}
	}
}