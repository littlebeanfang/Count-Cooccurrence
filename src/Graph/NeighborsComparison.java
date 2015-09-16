package Graph;

import java.io.IOException;
import java.util.*;
import java.io.*;

import path.Path;


import bottomLevel.ds.RankedObject;
import bottomLevel.fileIO.CollectionFile;
import bottomLevel.fileIO.GraphFile;
import bottomLevel.fileIO.HashMapFile;
import bottomLevel.formula.SetOperation;
import bottomLevel.others.GraphManipulater;
import bottomLevel.others.PageRanker;

public class NeighborsComparison {
	final String BANDir = Path.BANDir;

	public NeighborsComparison() throws IOException, Exception {
		HashMap<String, String> zh2en = new HashMapFile(BANDir + "en2zh.txt").read(1, String.class, String.class);
		HashMap<String, HashMap<String, Double>> en_network = new GraphFile(BANDir + "en_network.txt").read();
		HashMap<String, HashMap<String, Double>> zh_network = new GraphFile(BANDir + "zh_network.txt").read();

		new GraphManipulater().addVertices(zh_network, zh2en.keySet());
		new GraphManipulater().addVertices(en_network, zh2en.values());
		HashMap<String, HashMap<String, Double>> zh_Rnetwork = new GraphManipulater().reverse(zh_network);
		HashMap<String, HashMap<String, Double>> en_Rnetwork = new GraphManipulater().reverse(en_network);

		PageRanker<String> zh_pr = new PageRanker<String>(zh_network, 0.01);
		HashMap<String, Double> zh_pagesRank = zh_pr.getPagesRank();
		PageRanker<String> en_pr = new PageRanker<String>(en_network, 0.01);
		HashMap<String, Double> en_pagesRank = en_pr.getPagesRank();

		HashSet<String> printed = new HashSet<String>();
		SetOperation<String> so = new SetOperation<String>();
		for (String zh_page : zh2en.keySet()) {
			String en_page = zh2en.get(zh_page);
			double zh_pageRank = zh_pagesRank.get(zh_page);
			double en_pageRank = en_pagesRank.get(en_page);
			
			Set<String> en_outlinks = en_network.get(en_page).keySet();
			Set<String> en_inlinks = en_Rnetwork.get(en_page).keySet();
			Set<String> zh_outlinks = new HashSet<String>();
			for(String page:zh_network.get(zh_page).keySet()){
				zh_outlinks.add(zh2en.get(page));
			}
			Set<String> zh_inlinks = new HashSet<String>();
			for(String page:zh_Rnetwork.get(zh_page).keySet()){
				zh_inlinks.add(zh2en.get(page));
			}
			
			Set<String> co_outlinks = so.intersect(zh_outlinks, en_outlinks);
			Set<String> co_inlinks = so.intersect(zh_inlinks, en_inlinks);
			double outScore = so.jaccard(en_outlinks, zh_outlinks);
			double inScore = so.jaccard(en_inlinks, zh_inlinks);
			double score = outScore + inScore;
			printed.add(zh_page + "\t" + en_page + "\t" + inScore + "\t" + outScore + "\t" + score + "\t" + zh_outlinks.size() + "\t" + en_outlinks.size()
					+ "\t" + co_outlinks.size() + "\t" + zh_inlinks.size() + "\t" + en_inlinks.size() + "\t" + co_inlinks.size() + "\t" + zh_pageRank + "\t"
					+ en_pageRank);
		}

		new CollectionFile(BANDir + "matches.txt").writeFrom(printed);
	}
}
