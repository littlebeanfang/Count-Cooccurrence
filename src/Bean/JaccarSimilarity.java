package Bean;

import java.io.FileReader;
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

public class JaccarSimilarity {
	private boolean split;
	private String en2zhwholeFile=Path.BANRawDir+"disamgroup_lang_idlinks.txt";
	private String en2zhsplitfile=Path.BANRawDir+"disamgroup_lang_idlinks.txt";
	private String en_networkSplitFile=Path.BANRawDir+"disamgroup_en_links.txt";
	private String zh_networkSplitFile=Path.BANRawDir+"disamgroup_zh_links.txt";
	private String jarsimilarityOutFile=Path.BANRawDir+"disamgroup_match.txt";
	public JaccarSimilarity(String langlinkfile, String en_linkfile,String zh_linkfile,String jaccarfile){
		en2zhsplitfile=langlinkfile;
		en_networkSplitFile=en_linkfile;
		zh_networkSplitFile=zh_linkfile;
		jarsimilarityOutFile=jaccarfile;
		this.split=false;
	}
	public JaccarSimilarity(String langlink_wholefile, String langlink_splitfile, String en_splitlinkfile,String zh_splitlinkfile,String jaccarfile){
		en2zhwholeFile=langlink_wholefile;
		en2zhsplitfile=langlink_splitfile;
		en_networkSplitFile=en_splitlinkfile;
		zh_networkSplitFile=zh_splitlinkfile;
		jarsimilarityOutFile=jaccarfile;
		this.split=true;
	}
	public void ComputeJaccard() throws IOException, Exception{
		if(split){
			NeighborsComparisonSplit();
		}else{
			NeighborsComparisonNoSplit();
		}
	}
	private void NeighborsComparisonSplit()
			throws IOException, Exception {
		HashMap<String, String> zh2enwhole = new HashMapFile(en2zhwholeFile).read(
				1, String.class, String.class);
		HashMap<String, String> zh2ensplit = new HashMapFile(en2zhsplitfile).read(
				1, String.class, String.class);
		// inner network of english title to title
		HashMap<String, HashMap<String, Double>> en_network = new GraphFile(
				en_networkSplitFile).read();
		// inner network of chinese title to title
		HashMap<String, HashMap<String, Double>> zh_network = new GraphFile(
				zh_networkSplitFile).read();

		new GraphManipulater().addVertices(zh_network, zh2ensplit.keySet());
		new GraphManipulater().addVertices(en_network, zh2ensplit.values());
		HashMap<String, HashMap<String, Double>> zh_Rnetwork = new GraphManipulater()
				.reverse(zh_network);
		HashMap<String, HashMap<String, Double>> en_Rnetwork = new GraphManipulater()
				.reverse(en_network);

//		PageRanker<String> zh_pr = new PageRanker<String>(zh_network, 0.01);
//		HashMap<String, Double> zh_pagesRank = zh_pr.getPagesRank();
//		PageRanker<String> en_pr = new PageRanker<String>(en_network, 0.01);
//		HashMap<String, Double> en_pagesRank = en_pr.getPagesRank();

		HashSet<String> printed = new HashSet<String>();
		SetOperation<String> so = new SetOperation<String>();
		for (String zh_page : zh2ensplit.keySet()) {
			String en_page = zh2ensplit.get(zh_page);
//			double zh_pageRank = zh_pagesRank.get(zh_page);
//			double en_pageRank = en_pagesRank.get(en_page);

			Set<String> en_outlinks = en_network.get(en_page).keySet();
			Set<String> en_inlinks = en_Rnetwork.get(en_page).keySet();
			Set<String> zh_outlinks = new HashSet<String>();
			for (String page : zh_network.get(zh_page).keySet()) {
				zh_outlinks.add(zh2enwhole.get(page));
			}
			Set<String> zh_inlinks = new HashSet<String>();
			for (String page : zh_Rnetwork.get(zh_page).keySet()) {
				zh_inlinks.add(zh2enwhole.get(page));
			}

			Set<String> co_outlinks = so.intersect(zh_outlinks, en_outlinks);
			Set<String> co_inlinks = so.intersect(zh_inlinks, en_inlinks);
			double outScore = so.jaccard(en_outlinks, zh_outlinks);
			double inScore = so.jaccard(en_inlinks, zh_inlinks);
			double score = outScore + inScore;
			printed.add(zh_page + "\t" + en_page + "\t" + inScore + "\t"
					+ outScore + "\t" + score + "\t" + zh_outlinks.size()
					+ "\t" + en_outlinks.size() + "\t" + co_outlinks.size()
					+ "\t" + zh_inlinks.size() + "\t" + en_inlinks.size()
					+ "\t" + co_inlinks.size() + "\t" 
					//+ zh_pageRank + "\t" + en_pageRank
					);
		}

		new CollectionFile(jarsimilarityOutFile).writeFrom(printed);
	}
	
	private void NeighborsComparisonNoSplit()
			throws IOException, Exception {
		HashMap<String, String> zh2en = new HashMapFile(en2zhsplitfile).read(
				1, String.class, String.class);
		// inner network of english title to title
		HashMap<String, HashMap<String, Double>> en_network = new GraphFile(
				en_networkSplitFile).read();
		// inner network of chinese title to title
		HashMap<String, HashMap<String, Double>> zh_network = new GraphFile(
				zh_networkSplitFile).read();

		new GraphManipulater().addVertices(zh_network, zh2en.keySet());
		new GraphManipulater().addVertices(en_network, zh2en.values());
		HashMap<String, HashMap<String, Double>> zh_Rnetwork = new GraphManipulater()
				.reverse(zh_network);
		HashMap<String, HashMap<String, Double>> en_Rnetwork = new GraphManipulater()
				.reverse(en_network);

//		PageRanker<String> zh_pr = new PageRanker<String>(zh_network, 0.01);
//		HashMap<String, Double> zh_pagesRank = zh_pr.getPagesRank();
//		PageRanker<String> en_pr = new PageRanker<String>(en_network, 0.01);
//		HashMap<String, Double> en_pagesRank = en_pr.getPagesRank();

		HashSet<String> printed = new HashSet<String>();
		SetOperation<String> so = new SetOperation<String>();
		for (String zh_page : zh2en.keySet()) {
			String en_page = zh2en.get(zh_page);
//			double zh_pageRank = zh_pagesRank.get(zh_page);
//			double en_pageRank = en_pagesRank.get(en_page);

			Set<String> en_outlinks = en_network.get(en_page).keySet();
			Set<String> en_inlinks = en_Rnetwork.get(en_page).keySet();
			Set<String> zh_outlinks = new HashSet<String>();
			for (String page : zh_network.get(zh_page).keySet()) {
				zh_outlinks.add(zh2en.get(page));
			}
			Set<String> zh_inlinks = new HashSet<String>();
			for (String page : zh_Rnetwork.get(zh_page).keySet()) {
				zh_inlinks.add(zh2en.get(page));
			}

			Set<String> co_outlinks = so.intersect(zh_outlinks, en_outlinks);
			Set<String> co_inlinks = so.intersect(zh_inlinks, en_inlinks);
			double outScore = so.jaccard(en_outlinks, zh_outlinks);
			double inScore = so.jaccard(en_inlinks, zh_inlinks);
			double score = outScore + inScore;
			printed.add(zh_page + "\t" + en_page + "\t" + inScore + "\t"
					+ outScore + "\t" + score + "\t" + zh_outlinks.size()
					+ "\t" + en_outlinks.size() + "\t" + co_outlinks.size()
					+ "\t" + zh_inlinks.size() + "\t" + en_inlinks.size()
					+ "\t" + co_inlinks.size() + "\t" 
					//+ zh_pageRank + "\t" + en_pageRank
					);
		}

		new CollectionFile(jarsimilarityOutFile).writeFrom(printed);
	}
	public void PageRankerCall(String languageprefix) throws IOException, Exception{
		String en_networkFile=Path.BANRawDir+languageprefix+"_links.txt";
		String en_pagerankFile=Path.BANRawDir+languageprefix+"_pagerank.txt";
		HashMap<String, HashMap<String, Double>> en_network = new GraphFile(
				en_networkFile).read();
		PageRanker<String> en_pr = new PageRanker<String>(en_network, 0.01);
		HashMap<String, Double> en_pagesRank = en_pr.getPagesRank();
		
		File out=new File(en_pagerankFile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		for(Map.Entry<String, Double> entry:en_pagesRank.entrySet()){
			writer.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		writer.close();
	}
	public void WritePageRankFile() throws IOException{
		String matchFile=Path.JarcarSimilarityOutFile;
		String zh_PageRankFile=Path.BANRawDir+"zh_pagerank.txt";
		String en_PageRankFile=Path.BANRawDir+"en_pagerank.txt";
		BufferedReader matchReader=new BufferedReader(new FileReader(matchFile));
		File zh_outFile=new File(zh_PageRankFile);
		if(!zh_outFile.exists()){
			zh_outFile.createNewFile();
		}
		FileWriter zh_writer=new FileWriter(zh_outFile);
		File en_outFile=new File(en_PageRankFile);
		if(!en_outFile.exists()){
			en_outFile.createNewFile();
		}
		FileWriter en_writer=new FileWriter(en_outFile);
		String line;
		while((line=matchReader.readLine())!=null){
			String columns[]=line.split("\t");
			String zhtitle=columns[0];
			String entitle=columns[1];
			String zhpr=columns[11];
			String enpr=columns[12];
			zh_writer.write(zhtitle+"\t"+zhpr+"\n");
			en_writer.write(entitle+"\t"+enpr+"\n");
		}
		matchReader.close();
		zh_writer.close();
		en_writer.close();
	}
	
	public void RewriteMatchFile() throws Exception{
		String zh_PageRankFile=Path.BANRawDir+"zh_pagerank.txt";
		String en_PageRankFile=Path.BANRawDir+"en_pagerank.txt";
		String zh_id2titleFile=Path.BANRawDir+"zh_id2title.txt";
		String en_id2titleFile=Path.BANRawDir+"en_id2title.txt";
		String MatchIdFile=Path.BANRawDir+"match_alllinks";
		String MatchTitleMergeFile=Path.BANRawDir+"match_alllinks_title.txt";
		HashMap<String, Double> zh_pr = new HashMapFile(zh_PageRankFile).read(
				0, String.class, Double.class);
		HashMap<String, Double> en_pr = new HashMapFile(en_PageRankFile).read(
				0, String.class, Double.class);
		HashMap<String, String> enid2title = new HashMapFile(en_id2titleFile).read(0,
				String.class, String.class);
		HashMap<String, String> zhid2title = new HashMapFile(zh_id2titleFile).read(0,
				String.class, String.class);
		BufferedReader matchReader=new BufferedReader(new FileReader(MatchIdFile));
		File out=new File(MatchTitleMergeFile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		String line;
		while((line=matchReader.readLine())!=null){
			String columns[]=line.split("\t");
			String zhid=columns[0];
			String enid=columns[1];
			String entitle=enid2title.get(enid);
			String zhtitle=zhid2title.get(zhid);
			if(!en_pr.containsKey(entitle)||!zh_pr.containsKey(zhtitle)){
				System.out.println("Not contain title:"+enid+"\t"+entitle+"\t"+zhid+"\t"+zhtitle);
				continue;
			}
			double enprscore=en_pr.get(entitle);
			double zhprscore=zh_pr.get(zhtitle);
			
			writer.write(entitle+"\t"+zhtitle);
			for(int i=2;i<columns.length;i++){
				writer.write("\t"+columns[i]);
			}
			writer.write("\t"+enprscore+"\t"+zhprscore+"\n");
			writer.flush();
		}
		matchReader.close();
		writer.close();
	}
	public static void main(String args[]) throws IOException, Exception{
		String en2zhwholeFile=Path.BANRawDir+"disamgroup_lang_idlinks.txt";
		String en_networkSplitFile=Path.BANRawDir+"disamgroup_en_links.txt";
		String zh_networkSplitFile=Path.BANRawDir+"disamgroup_zh_links.txt";
		String jarsimilarityOutFile=Path.BANRawDir+"disamgroup_match.txt";
		
		long start=System.currentTimeMillis();
		JaccarSimilarity test0=new JaccarSimilarity(en2zhwholeFile, en_networkSplitFile, zh_networkSplitFile, jarsimilarityOutFile);
		test0.ComputeJaccard();
		long end=System.currentTimeMillis();
		System.out.println("Time take:"+(end-start)/1000+" s.");
		
//		NeighborsComparison test0=new NeighborsComparison();
//		test0.PageRankerCall("en");
//		test0.PageRankerCall("zh");
//		test0.WritePageRankFile();
//		System.out.println("pagerank writing: done...");
//		test0.RewriteMatchFile();
	}
}
