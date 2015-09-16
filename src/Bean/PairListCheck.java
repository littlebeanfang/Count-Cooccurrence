package Bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import BeanUtil.BeanReader;
import BeanUtil.BeanWriter;

public class PairListCheck {
/**
 * check 100 cultural different pairs of Yizhong
 * @throws IOException 
 */
	private HashSet<String> yzzhtitle;
	private HashMap<String,String> zh_entitleinMatch;
	private HashMap<String, Integer> inlinkZhtitleRankMap;
	private HashMap<String, Integer> outlinkZhtitleRankMap;
	private HashMap<String, Integer> averageZhtitleRankMap;
	public PairListCheck(){
		yzzhtitle=new HashSet<String>();
		zh_entitleinMatch=new HashMap<String,String>();
		inlinkZhtitleRankMap=new HashMap<String, Integer>();
		outlinkZhtitleRankMap=new HashMap<String, Integer>();
		averageZhtitleRankMap=new HashMap<String,Integer>();
	}
	public void WriteYZPairRank(String inlinkmatchfile,String outlinkmatchfile,
			String averagematchfile, String yzlistfile, String writerankfile) throws IOException{
		this.FillRankMap(inlinkmatchfile, outlinkmatchfile, averagematchfile);
		this.GetZhTitleList(yzlistfile);
		BeanWriter rankWriter=new BeanWriter(writerankfile, "Jaccar rank of Yizhong pairs. Format:<zhtitle>\\t<entitle>\\t<inlinkRank>\\t<outlinkRank>\\t<averageRank>.");
		int count=0;
		String content;
		for(String zhtitle:this.yzzhtitle){
			if(zh_entitleinMatch.containsKey(zhtitle)){
				count++;
				content=zhtitle+"\t"+zh_entitleinMatch.get(zhtitle)+"\t"+inlinkZhtitleRankMap.get(zhtitle)+
						"\t"+outlinkZhtitleRankMap.get(zhtitle)+"\t"+averageZhtitleRankMap.get(zhtitle);
				rankWriter.writeln(content);
			}else{
				System.out.println(zhtitle+" is not in match file !");
			}
		}
		System.out.println(count+" Pairs is find in match file.");
		rankWriter.close();
	}
	private void FillRankMap(String inlinkmatchfile,String outlinkmatchfile,
			String averagematchfile) throws IOException{
		BeanReader inreader=new BeanReader(inlinkmatchfile);
		BeanReader outreader=new BeanReader(outlinkmatchfile);
		BeanReader avereader=new BeanReader(averagematchfile);
		String columns[];
		String zhtitle;
		String entitle;
		int rankcount=0;
		while((columns=inreader.readSplitLine())!=null){
			zhtitle=columns[0];
			entitle=columns[1];
			rankcount++;
			inlinkZhtitleRankMap.put(zhtitle, rankcount);
			zh_entitleinMatch.put(zhtitle,entitle);
		}
		System.out.println("inlink:map size"+inlinkZhtitleRankMap.size()+",rankcount"+rankcount);
		rankcount=0;
		while((columns=outreader.readSplitLine())!=null){
			zhtitle=columns[0];
			rankcount++;
			outlinkZhtitleRankMap.put(zhtitle, rankcount);
		}
		System.out.println("outlink:map size"+outlinkZhtitleRankMap.size()+",rankcount"+rankcount);
		rankcount=0;
		while((columns=avereader.readSplitLine())!=null){
			zhtitle=columns[0];
			rankcount++;
			averageZhtitleRankMap.put(zhtitle, rankcount);
		}
		System.out.println("average:map size"+averageZhtitleRankMap.size()+",rankcount"+rankcount);
		inreader.close();
		outreader.close();
		avereader.close();
	}
	private void GetZhTitleList(String yzlistfile) throws IOException{
		BeanReader listReader=new BeanReader(yzlistfile);
		String columns[];
		String zhtitle;
		while((columns=listReader.readSplitLine(","))!=null){
			zhtitle=columns[1];
			yzzhtitle.add(zhtitle.trim());
		}
		System.out.println("yzzhtitle size:"+yzzhtitle.size());
		for(String item:yzzhtitle){
			System.out.println(item);
		}
		listReader.close();
	}
	public static void main(String args[]) throws IOException{
		PairListCheck test=new PairListCheck();
		String inlinkmatchfile="disamgroup_jaccard_inlink_KENNY.txt";
		String outlinkmatchfile="disamgroup_jaccard_outlink_KENNY.txt";
		String averagematchfile="disamgroup_jaccard_average_KENNY.txt";
		String yzlistfile="list_of_culturally_different_pairs.txt";
		String writerankfile="yzPairJaccarRank.txt";
		test.WriteYZPairRank(inlinkmatchfile, outlinkmatchfile, 
				averagematchfile, yzlistfile, writerankfile);
	}
}
