package Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import path.Path;


import bottomLevel.ds.RankedObject;
import bottomLevel.fileIO.CollectionFile;
import bottomLevel.fileIO.GraphFile;
import bottomLevel.fileIO.HashMapFile;
import bottomLevel.others.PageRanker;

public class WikiPageRank {
	final String BANDir = Path.BANDir;

	public WikiPageRank() throws IOException, Exception {
		HashMap<String, HashMap<String, Double>> linkGraph = new GraphFile(Path.BANDir + "zh_network.txt").read();
		PageRanker<String> pr = new PageRanker<String>(linkGraph, 0.01);
		HashMap<String, Double> pagesRank = pr.getPagesRank();
		ArrayList<RankedObject> orderedPagesRank = new ArrayList<RankedObject>();
		for (String page : pagesRank.keySet()) {
			orderedPagesRank.add(new RankedObject(page, pagesRank.get(page)));
		}
		Collections.sort(orderedPagesRank);
		new CollectionFile(Path.BANDir + "zh_pageRank.txt").writeFrom(orderedPagesRank);
	}
}
