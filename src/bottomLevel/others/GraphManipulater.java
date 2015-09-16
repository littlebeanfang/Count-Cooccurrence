package bottomLevel.others;

import java.util.*;
import java.io.*;

public class GraphManipulater {
	public HashMap<String, HashMap<String, Double>> reverse(HashMap<String, HashMap<String, Double>> graph) {
		HashMap<String, HashMap<String, Double>> Rgraph = new HashMap<String, HashMap<String, Double>>();
		for (String word1 : graph.keySet()) {
			if (!Rgraph.containsKey(word1)) {
				Rgraph.put(word1, new HashMap<String, Double>());
			}
			for (String word2 : graph.get(word1).keySet()) {
				if (!Rgraph.containsKey(word2)) {
					Rgraph.put(word2, new HashMap<String, Double>());
				}
				Rgraph.get(word2).put(word1, graph.get(word1).get(word2));
			}
		}
		return Rgraph;
	}
	
	public void normalize(HashMap<String, HashMap<String, Double>> graph) {
		for (String word1 : graph.keySet()) {
			double weightSum = 0;
			for (String word2 : graph.get(word1).keySet()) {
				weightSum += graph.get(word1).get(word2);
			}
			for (String word2 : graph.get(word1).keySet()) {
				graph.get(word1).put(word2, graph.get(word1).get(word2) / weightSum);
			}
		}
	}
	
	public void addVertices(HashMap<String, HashMap<String, Double>> graph, Collection<String> vertices) {
		for (String vertex : vertices) {
			if(!graph.containsKey(vertex)){
				graph.put(vertex, new HashMap<String, Double>());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<String, Double>> clone(HashMap<String, HashMap<String, Double>> graph){
		HashMap<String, HashMap<String, Double>> clonedGraph = new HashMap<String, HashMap<String, Double>>();
		for (String word : graph.keySet()) {
			clonedGraph.put(word, (HashMap<String, Double>) graph.get(word).clone());
		}
		return clonedGraph;
	}
	
	public void filter(HashMap<String, HashMap<String, Double>> graph, double thresh) {
		for (String word1 : graph.keySet()) {
			HashMap<String, Double> originalEdges = graph.get(word1);
			HashMap<String, Double> edges = new HashMap<String, Double>();
			double probSum = 0;
			for (String word2 : originalEdges.keySet()) {
				double prob = originalEdges.get(word2);
				if (prob > thresh) {
					edges.put(word2, prob);
					probSum += prob;
				}
			}
			for (String word2 : edges.keySet()) {
				edges.put(word2, edges.get(word2) / probSum);
			}
			graph.put(word1, edges);
		}
	}
}