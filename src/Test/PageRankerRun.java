package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import path.CommonPath;
import Graph.GraphFile;
import Graph.PageRanker;

public class PageRankerRun {
	public static void main(String args[]) throws Exception{
		HashMap<String, HashMap<String, Double>> linkGraph=new HashMap<String, HashMap<String, Double>>();
		double convergenceError=0.01;
		GraphFile gf=new GraphFile(CommonPath.pageLinkPath);
		gf.readTo(linkGraph,1);
		/*
		for(String fromNode:linkGraph.keySet()){
			HashMap<String, Integer> end_weightHashMap=linkGraph.get(fromNode);
			for(Map.Entry<String, Integer> entry:end_weightHashMap.entrySet()){
				System.out.println(fromNode+"->"+entry.getKey()+":"+entry.getValue());
			}
		}
		*/
		PageRanker pr=new PageRanker<String>(linkGraph, convergenceError);
		File writeToPageRankFile=new File(CommonPath.PageWeightPath);
		if(!writeToPageRankFile.exists()){
			writeToPageRankFile.createNewFile();
		}
		FileWriter pageRankWriter=new FileWriter(writeToPageRankFile);
		HashMap<String, Double> pageRankHashMap=pr.getPagesRank();
		
		for(Map.Entry<String,Double> entry:pageRankHashMap.entrySet()){
			pageRankWriter.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		System.out.println("Writing File:done.");
		pageRankWriter.close();
	}
}
