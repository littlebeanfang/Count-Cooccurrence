package Bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnAnchorExtractor {
	/**
	 * get English anchor label-title pairs from a href xml file.
	 * the xml file is extract from WikiExtractor with link option.
	 */
	public void en_Anchor(String link_plaintxt,String anchor1levelfile,
			String anchor2levelfile) throws IOException {
		AnchorMapOp anchormaps=new AnchorMapOp();
		BufferedReader reader = new BufferedReader(
				new FileReader(link_plaintxt));
		String line;
		int count = 0;
		while ((line = reader.readLine()) != null) {
			count++;
			if (count % 5000000 == 0) {
				System.out.print(count + "\t");
			}
			this.ExtractAnchorPairFromHref(line,anchormaps);
		}
		anchormaps.WriteSortMap(anchor1levelfile);
		anchormaps.Write2levelSortMap(anchor2levelfile);
		reader.close();
	}

	public void ExtractAnchorPairFromHref(String targetsen,AnchorMapOp anchormaps){
		// e.g <a href="Russia">Russian</a>
		Pattern pattern = Pattern.compile("<a href=\"([^>]*)\">([^<]*)</a>");
		Matcher matcher = pattern.matcher(targetsen);
		while (matcher.find()) {
			String title = matcher.group(1).replace(" ", "_");
			String label = matcher.group(2);
			// System.out.println("title:"+title+",label:"+label);
			anchormaps.AddTo1levelMap(title, label);
			anchormaps.AddTo2LevelMap(title, label);
		}
	}
	
	public static void main(String args[]) throws IOException{
		EnAnchorExtractor test=new EnAnchorExtractor();
		String dir="/home/bean/wiki/BAN/";
		String link_plaintxt=dir+"text_withlink.xml";
		String anchor1levelfile=dir+"en_anchorlabeltitle_count.map";
		String anchor2levelfile=dir+"en_anchorlabel_title_count.map";
		test.en_Anchor(link_plaintxt, anchor1levelfile, anchor2levelfile);
	}
}
