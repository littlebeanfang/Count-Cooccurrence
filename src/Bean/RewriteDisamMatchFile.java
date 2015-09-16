package Bean;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import BeanUtil.BeanReader;
import BeanUtil.BeanWriter;
import BeanUtil.DoubleValueMapDecSorter;
import BeanUtil.DoubleValueMapSorter;

public class RewriteDisamMatchFile {
	/**
	 * this class is used to
	 * 
	 * @throws IOException
	 */
	public void Rewrite(String matchfile, String en_id2titlefile,
			String zh_id2titlefile, String en_disamfiltergroup,
			String zh_disamfiltergroup, String jaccaroutfile,
			String en_writecheckgroupfile, String zh_writecheckgroupfile)
			throws IOException {
		/*
		 * match file format 0 zh_pageid + "\t" + 1 en_pageid + "\t" + 2 inScore
		 * + "\t" + 3 outScore + "\t" + 4 score + "\t" + 5 zh_outlinks.size() +
		 * "\t" + 6 en_outlinks.size() + "\t" + 7 co_outlinks.size() + "\t" + 8
		 * zh_inlinks.size() + "\t" + 9 en_inlinks.size() + "\t" + 10
		 * co_inlinks.size() + "\t" //+ 11 zh_pageRank + "\t" + 12 en_pageRank
		 */
		/*
		 * jaccaroutfile format, ordered by jaccard zh_pagetitle+ "\t" +
		 * en_pagetitle+ "\t" + jaccardscore + "\t" + zh_id(y/n flag of if a
		 * group)+ "\t" + en_id(y/n..)
		 */
		BeanReader matchidReader = new BeanReader(matchfile);
		BeanReader en_id2titleReader = new BeanReader(en_id2titlefile);
		BeanReader zh_id2titleReader = new BeanReader(zh_id2titlefile);
		BeanReader en_disamfilterReader = new BeanReader(en_disamfiltergroup);
		BeanReader zh_disamfilterReader = new BeanReader(zh_disamfiltergroup);
		BeanWriter jaccarWriter = new BeanWriter(
				jaccaroutfile,
				"format:"
						+ "<zh_title> \\t <en_title> \\t <jaccardscore> \\t <zh_id>(* groupflag)"
						+ "\\t <en_id>(* groupflag).");
		// Init map
		HashMap<String, String> en_id2title_map = new HashMap<String, String>();
		HashMap<String, String> zh_id2title_map = new HashMap<String, String>();
		HashMap<String, HashSet<String>> en_groupcenter_subset = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> zh_groupcenter_subset = new HashMap<String, HashSet<String>>();
		HashMap<String, Double> printmap = new HashMap<String, Double>();
		Set<String> en_groupid;
		Set<String> zh_groupid;
		// read id2title and group
		this.ReadId2titleMap(en_id2titleReader, en_id2title_map);
		this.ReadId2titleMap(zh_id2titleReader, zh_id2title_map);
		this.ReadGroupMap(en_disamfilterReader, en_groupcenter_subset);
		this.ReadGroupMap(zh_disamfilterReader, zh_groupcenter_subset);
		en_groupid = en_groupcenter_subset.keySet();
		zh_groupid = zh_groupcenter_subset.keySet();
		// rewrite group file
		this.RewriteGroupFile(en_groupcenter_subset, en_id2title_map,
				en_disamfiltergroup, en_writecheckgroupfile);
		this.RewriteGroupFile(zh_groupcenter_subset, zh_id2title_map,
				zh_disamfiltergroup, zh_writecheckgroupfile);

		String columns[];
		String zh_id;
		String en_id;
		String gzh_id;
		String gen_id;
		String zh_title;
		String en_title;
		String writeline;
		while ((columns = matchidReader.readSplitLine()) != null) {
			zh_id = columns[0];
			en_id = columns[1];
			double jaccar = Double.parseDouble(columns[4]);
			zh_title = zh_id2title_map.get(zh_id);
			en_title = en_id2title_map.get(en_id);
			gzh_id = zh_groupid.contains(zh_id) ? zh_id + "*" : zh_id;
			gen_id = en_groupid.contains(en_id) ? en_id + "*" : en_id;
			writeline = zh_title + "\t" + en_title + "\t" + jaccar + "\t"
					+ gzh_id + "\t" + gen_id;
			printmap.put(writeline, jaccar);
		}
		DoubleValueMapDecSorter sorter = new DoubleValueMapDecSorter();
		List<Entry<String, Double>> printlist = sorter.SortCountMap(printmap);
		for (Map.Entry<String, Double> entry : printlist) {
			jaccarWriter.writeln(entry.getKey());
		}
		matchidReader.close();
		en_id2titleReader.close();
		zh_id2titleReader.close();
		en_disamfilterReader.close();
		zh_disamfilterReader.close();
		jaccarWriter.close();
	}

	public void GetJaccarRankFiles(int numofitem, String matchfile, String en_id2titlefile,
			String zh_id2titlefile, String en_disamfiltergroup,
			String zh_disamfiltergroup, String jaccar_inlink_outfile,
			String jaccar_outlink_outfile,String jaccar_average_outfile)
			throws IOException {
		/*
		 * match file format 0 zh_pageid + "\t" + 1 en_pageid + "\t" + 2 inScore
		 * + "\t" + 3 outScore + "\t" + 4 score + "\t" + 5 zh_outlinks.size() +
		 * "\t" + 6 en_outlinks.size() + "\t" + 7 co_outlinks.size() + "\t" + 8
		 * zh_inlinks.size() + "\t" + 9 en_inlinks.size() + "\t" + 10
		 * co_inlinks.size() + "\t" //+ 11 zh_pageRank + "\t" + 12 en_pageRank
		 */
		/*
		 * jaccaroutfile format, ordered by jaccard zh_pagetitle+ "\t" +
		 * en_pagetitle+ "\t" + jaccardscore + "\t" + zh_id(y/n flag of if a
		 * group)+ "\t" + en_id(y/n..)
		 */
		BeanReader matchidReader = new BeanReader(matchfile);
		BeanReader en_id2titleReader = new BeanReader(en_id2titlefile);
		BeanReader zh_id2titleReader = new BeanReader(zh_id2titlefile);
		BeanReader en_disamfilterReader = new BeanReader(en_disamfiltergroup);
		BeanReader zh_disamfilterReader = new BeanReader(zh_disamfiltergroup);
		BeanWriter jaccar_inlink_Writer = new BeanWriter(
				jaccar_inlink_outfile,
				"format:"
						+ "<zh_title> \\t <en_title> \\t <jaccard_inlink_score> \\t <zh_id>(* groupflag)"
						+ "\\t <en_id>(* groupflag).");
		BeanWriter jaccar_outlink_Writer = new BeanWriter(
				jaccar_outlink_outfile,
				"format:"
						+ "<zh_title> \\t <en_title> \\t <jaccard_outlink_score> \\t <zh_id>(* groupflag)"
						+ "\\t <en_id>(* groupflag).");
		BeanWriter jaccar_average_Writer = new BeanWriter(
				jaccar_average_outfile,
				"format:"
						+ "<zh_title> \\t <en_title> \\t <jaccard_average_score> \\t <zh_id>(* groupflag)"
						+ "\\t <en_id>(* groupflag).");
		// Init map
		HashMap<String, String> en_id2title_map = new HashMap<String, String>();
		HashMap<String, String> zh_id2title_map = new HashMap<String, String>();
		HashMap<String, HashSet<String>> en_groupcenter_subset = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> zh_groupcenter_subset = new HashMap<String, HashSet<String>>();
		HashMap<String, Double> inlinkprintmap = new HashMap<String, Double>();
		HashMap<String, Double> outlinkprintmap = new HashMap<String, Double>();
		HashMap<String, Double> averageprintmap = new HashMap<String, Double>();
		Set<String> en_groupid;
		Set<String> zh_groupid;
		// read id2title and group
		this.ReadId2titleMap(en_id2titleReader, en_id2title_map);
		this.ReadId2titleMap(zh_id2titleReader, zh_id2title_map);
		this.ReadGroupMap(en_disamfilterReader, en_groupcenter_subset);
		this.ReadGroupMap(zh_disamfilterReader, zh_groupcenter_subset);
		en_groupid = en_groupcenter_subset.keySet();
		zh_groupid = zh_groupcenter_subset.keySet();

		String columns[];
		String zh_id;
		String en_id;
		String gzh_id;
		String gen_id;
		String zh_title;
		String en_title;
		String writeinlinkline;
		String writeoutlinkline;
		String writeaverageline;
		while ((columns = matchidReader.readSplitLine()) != null) {
			zh_id = columns[0];
			en_id = columns[1];
			double injaccar = Double.parseDouble(columns[2]);
			double outjaccar = Double.parseDouble(columns[3]);
			double averjaccar=Double.parseDouble(columns[4]);
			zh_title = zh_id2title_map.get(zh_id);
			en_title = en_id2title_map.get(en_id);
			gzh_id = zh_groupid.contains(zh_id) ? zh_id + "*" : zh_id;
			gen_id = en_groupid.contains(en_id) ? en_id + "*" : en_id;
			writeinlinkline = zh_title + "\t" + en_title + "\t" + injaccar + "\t"
					+ gzh_id + "\t" + gen_id;
			writeoutlinkline = zh_title + "\t" + en_title + "\t" + outjaccar + "\t"
					+ gzh_id + "\t" + gen_id;
			writeaverageline = zh_title + "\t" + en_title + "\t" + averjaccar + "\t"
					+ gzh_id + "\t" + gen_id;
			inlinkprintmap.put(writeinlinkline, injaccar);
			outlinkprintmap.put(writeoutlinkline, outjaccar);
			averageprintmap.put(writeaverageline, averjaccar);
		}
		System.out.println("inlinkprintmap size:"+inlinkprintmap.size());
		System.out.println("outlinkprintmap size:"+outlinkprintmap.size());
		System.out.println("averageprintmap size:"+averageprintmap.size());
		DoubleValueMapDecSorter sorter = new DoubleValueMapDecSorter();
		List<Entry<String, Double>> printinlinklist = sorter.SortCountMap(inlinkprintmap);
		List<Entry<String, Double>> printoutlinklist = sorter.SortCountMap(outlinkprintmap);
		List<Entry<String, Double>> printaveragelist = sorter.SortCountMap(averageprintmap);
		System.out.println("printinlinklist size:"+printinlinklist.size());
		System.out.println("printoutlinklist size:"+printoutlinklist.size());
		System.out.println("printaveragelist size:"+printaveragelist.size());
		int count=0;
		for (Map.Entry<String, Double> entry : printinlinklist) {
			if(entry.getValue()==0){
				continue;
			}
			if(count++>numofitem){
				break;
			}
			jaccar_inlink_Writer.writeln(entry.getKey());
		}
		count=0;
		for (Map.Entry<String, Double> entry : printoutlinklist) {
			if(entry.getValue()==0){
				continue;
			}
			if(count++>numofitem){
				break;
			}
			jaccar_outlink_Writer.writeln(entry.getKey());
		}
		count=0;
		for (Map.Entry<String, Double> entry : printaveragelist) {
			if(entry.getValue()==0){
				continue;
			}
			if(count++>numofitem){
				break;
			}
			jaccar_average_Writer.writeln(entry.getKey());
		}
		matchidReader.close();
		en_id2titleReader.close();
		zh_id2titleReader.close();
		en_disamfilterReader.close();
		zh_disamfilterReader.close();
		jaccar_inlink_Writer.close();
		jaccar_outlink_Writer.close();
		jaccar_average_Writer.close();
	}
	private void ReadId2titleMap(BeanReader id2titleReader,
			HashMap<String, String> id2titlemap) throws IOException {
		String columns[];
		while ((columns = id2titleReader.readSplitLine()) != null) {
			String id = columns[0];
			String title = columns[1];
			id2titlemap.put(id, title);
		}
	}

	private void ReadGroupMap(BeanReader group_submainReader,
			HashMap<String, HashSet<String>> groupmap) throws IOException {
		String columns[];
		while ((columns = group_submainReader.readSplitLine()) != null) {
			String sub = columns[0];
			String main = columns[1];
			this.AddToOne2MultiMap(groupmap, main, sub);
		}
	}

	private void RewriteGroupFile(HashMap<String, HashSet<String>> groupmap,
			HashMap<String, String> id2titlemap, String groupfile,
			String writefile) throws IOException {
		/*
		 * group file format: groupcenterid+ "\t" + groupcentertitle+ "||" +
		 * subnodetitlelist
		 */
		System.out.println("rewrite: " + groupfile);
		// read sub-main map
		HashMap<String, String> sub_main_map = new HashMap<String, String>();
		BeanReader reader = new BeanReader(groupfile);
		this.ReadId2titleMap(reader, sub_main_map);
		reader.close();

		BeanWriter groupwriter = new BeanWriter(writefile,
				"GroupCheckFile format:<mainid>\\t<maintitle>\\t<subtitlelist>");
		String maintitle;
		String subtitle;
		int groupcount = 0;
		int singlecount = 0;
		for (String mainid : id2titlemap.keySet()) {
			maintitle = id2titlemap.get(mainid);
			if (groupmap.containsKey(mainid)) {
				groupcount++;
				groupwriter.write(mainid + "\t" + maintitle + "\t||");
				HashSet<String> subids = groupmap.get(mainid);
				for (String subid : subids) {
					subtitle = id2titlemap.get(subid);
					groupwriter.write("\t" + subtitle);
				}
				groupwriter.writeln("");
			} else {
				// single page as group
				singlecount++;
				groupwriter.writeln(mainid + "\t" + maintitle);
			}
		}
		System.out.println("group number:" + groupcount + ", single number:"
				+ singlecount);
		System.out
				.println("========================================================");
		groupwriter.close();
	}

	private void AddToOne2MultiMap(HashMap<String, HashSet<String>> map,
			String key, String value) {
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			HashSet<String> valueSet = new HashSet<String>();
			valueSet.add(value);
			map.put(key, valueSet);
		}
	}

	public static void main(String args[]) throws IOException {
		RewriteDisamMatchFile test = new RewriteDisamMatchFile();
		String BANdir = "/home/bean/wiki/BAN/";
		String en_disamfiltergroup = BANdir + "en_filter_disam_id.txt";
		String zh_disamfiltergroup = BANdir + "zh_filter_disam_id.txt";
		String matchfile = BANdir + "disamgroup_jaccard.txt";
		String jaccaroutfile = BANdir + "disamgroup_jaccard_KENNY.txt";
		String jaccar_inlink_outfile = BANdir + "disamgroup_jaccard_inlink_KENNY1000_no0.txt";
		String jaccar_outlink_outfile = BANdir + "disamgroup_jaccard_outlink_KENNY1000_no0.txt";
		String jaccar_average_outfile = BANdir + "disamgroup_jaccard_average_KENNY1000_no0.txt";
		String en_writecheckgroupfile = BANdir + "en_group_KENNY.txt";
		String zh_writecheckgroupfile = BANdir + "zh_group_KENNY.txt";
		String rawdir = "/home/bean/wiki/BAN/raw/";
		String en_id2titlefile = rawdir + "en_id2title.txt";
		String zh_id2titlefile = rawdir + "zh_id2title.txt";
//		test.Rewrite(matchfile, en_id2titlefile, zh_id2titlefile,
//				en_disamfiltergroup, zh_disamfiltergroup, jaccaroutfile,
//				en_writecheckgroupfile, zh_writecheckgroupfile);
		test.GetJaccarRankFiles(1000, matchfile, en_id2titlefile, zh_id2titlefile, en_disamfiltergroup, zh_disamfiltergroup, jaccar_inlink_outfile, jaccar_outlink_outfile,jaccar_average_outfile);
	}
}
