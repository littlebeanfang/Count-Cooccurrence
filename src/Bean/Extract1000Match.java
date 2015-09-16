package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import path.Path;

public class Extract1000Match {
	public void Extract(String old1000MatchFile, String new1000MatchFile, String matchTitleFile) throws IOException{
		HashMap<String,String> matchFileBuf=new HashMap<String,String>();
		BufferedReader matchReader=new BufferedReader(new FileReader(matchTitleFile));
		String line;
		//store match file
		while((line=matchReader.readLine())!=null){
			String columns[]=line.split("\t");
			String zh_en_title=columns[1]+"_"+columns[0];
			matchFileBuf.put(zh_en_title, line);
		}
		//read old1000Match, and write new1000Match
		BufferedReader old1000reader=new BufferedReader(new FileReader(old1000MatchFile));
		File new1000=new File(new1000MatchFile);
		if(!new1000.exists()){
			new1000.createNewFile();
		}
		FileWriter new1000Writer=new FileWriter(new1000);
		new1000Writer.write("zh_page\ten_page\tinScore\toutScore\toverallScore\tzh_outlinks\t"
				+ "en_outlinks\tco_outlinks\tzh_inlinks\ten_inlinks\tco_inlinks\tzh_pageRank\ten_pageRank\n");
		//skip first line
		line=old1000reader.readLine();
		while((line=old1000reader.readLine())!=null){
			String columns[]=line.split("\t");
			String zh_en_title=columns[0]+"_"+columns[1];
			String writeline=matchFileBuf.get(zh_en_title);
			if(writeline==null){
				System.out.println("NotFind!"+zh_en_title);
				continue;
			}
			new1000Writer.write(writeline+"\n");
		}
		matchReader.close();
		old1000reader.close();
		new1000Writer.close();
	}
	public static void main(String args[]) throws IOException{
		String old1000MatchFile=Path.BANRawDir+"OldMatch1000.txt";
		String new1000MatchFile=Path.BANRawDir+"NewMatch1000.txt";
		String matchTitleFile=Path.BANRawDir+"match_alllinks_title.txt";
		Extract1000Match test=new Extract1000Match();
		test.Extract(old1000MatchFile, new1000MatchFile, matchTitleFile);
	}
}
