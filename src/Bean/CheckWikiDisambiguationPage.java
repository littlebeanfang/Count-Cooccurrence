package Bean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckWikiDisambiguationPage {
	public void PageTxtCheck(String pagetxtfile, String pageid)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(pagetxtfile));
		String line;
		while ((line = reader.readLine()) != null) {
			String columns[] = line.split("\t");
			if (columns[0].equals(pageid)) {
				System.out.println(line);
			}
		}
		reader.close();
	}

	public void OutLinkCheck(String page_outlinkFile, String pageid)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				page_outlinkFile));
		String line;
		while ((line = reader.readLine()) != null) {
			String columns[] = line.split("\t");
			if (columns[0].equals(pageid)) {
				System.out.println(columns[1]);
			}
		}
		reader.close();
	}

	public void InLinkCheck(String page_inlinkFile, String pageid)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				page_inlinkFile));
		String line;
		while ((line = reader.readLine()) != null) {
			String columns[] = line.split("\t");
			if (columns[0].equals(pageid)) {
				System.out.println(columns[1]);
			}
		}
		reader.close();
	}

	public void DisambiguationPageGet() {
		/**
		 * 1. title contains "_(disambiguation)", is disambiguation page 2. in
		 * inlink txt, second column Disambiguation page
		 */
		
	}

	public void DisambiguationPageFromPagetxt(String pagetxtpath, String disambiguationtxtpath,boolean english)
			throws IOException {
		BufferedReader pagetxtreader = new BufferedReader(new FileReader(
				pagetxtpath));
		File disam=new File(disambiguationtxtpath);
		if(!disam.exists()){
			disam.createNewFile();
		}
		FileWriter disamwriter=new FileWriter(disam);
		disamwriter.write("subpage\tmainpage\n");
		String line;
		String disampageflagstr=english?"_(disambiguation)":"_(消歧义)";
		while ((line = pagetxtreader.readLine()) != null) {
			String columns[] = line.split("\t");
			if (columns.length < 4) {
				//extra special line end
				System.out.println("error in split length < 4!" +columns[2]);
				continue;
			}
//			if (columns.length == 5) {
//				//extra special line end
//				System.out.println("error in split length = 5!" +columns.length+columns[2]+"\t4:"+columns[4]);
//				continue;
//			}
			if (columns.length > 5) {
				//extra special line end
				System.out.println("error in split length > 5!" +columns.length+columns[2]);
				continue;
			}
			if (!columns[0].equals(columns[1])) {
				System.out.println("error in id not equal" );
			}

			String ID = columns[0];
			String title = columns[2];
			if(!title.contains(disampageflagstr)){
				continue;
			}
			String content = columns[3];
//			System.out.println("ID:"+ID);
			System.out.print("title:"+title);
//			System.out.println("content:"+content);
			ArrayList<String> disamtitles=this.TestPatternRecog(content);
			for(String disamtitle:disamtitles){
				disamwriter.write(disamtitle+"\t"+title+"\n");
				disamwriter.flush();
			}
		}
		pagetxtreader.close();
		disamwriter.close();
	}

	public ArrayList<String> TestPatternRecog(String targetsen) throws IOException {
//		System.out.println("targetsen1:"+targetsen);
		ArrayList<String> titles = new ArrayList<String>();
		//1. Store target String in String param
//		Pattern pattern = Pattern.compile("\\n\\*\\[\\[(.*)[|.*\\]]\\]");
		//2. Store target String in file
		Pattern pattern = Pattern.compile("\\\\n\\*[\\s]*\\[\\[([^\\]]*)\\]\\]");
		Pattern wikipatn=Pattern.compile("wiktionary\\|([^}]*)");
//		  targetsen =
//		 "Austin_(disambiguation) {{wiktionary|Austin}}\n\'\'\'Austin\'\'\' is an English language contracted form of [[Augustine (given name)|Augustine]].\n\'\'\'Austin\'\'\' may refer to:\n{{TOC right}}\n\n==Geographical locations==\n\n===In the United States===\n*[[Austin, Arkansas]]\n*[[Austin, Colorado]]\n* Austin, Illinois:\n** [[Austin Township,銆�Macon County, Illinois]]\n**[[Austin, Chicago]], Cook County, Illinois\n*[[Austin, Indiana]]\n*[[Austin, Kentucky]]\n*[[Austin, Minnesota]]\n*[[Austin, Nevada]]\n*[[Austin, Oregon]]\n*[[Austin, Texas]], the capital of the U.S. state of Texas.\n*[[Austin County, Texas]] (note that the city of Austin is located in Travis County)\n*[[Austin, Washington]]\n\n===In Canada===\n*[[Austin, Manitoba]]\n*[[Austin, Ontario]]\n*[[Austin, Quebec]]\n*[[Austin Island]], Nunavut\n\n===In Australia===\n*[[Austin, Western Australia]]\n\n==People==\n*[[Austin (name)]]\n\n==Schools==\n*[[Austin College]], Sherman, Texas\n*[[University of Texas at Austin]], flagship institution of the University of Texas System\n*[[Austin Peay State University]], Clarksville, Tennessee\n\n==Religion==\n*[[Augustine of Hippo]] or [[Augustine of Canterbury]]\n*An adjective for the [[Augustinians]]\n\n==Business==\n*[[Austin Automobile Company]], short-lived American automobile company\n*[[Austin (brand)]], a brand owned by the Kellogg Company\n*[[Austin Motor Company]], British car manufacturer\n*[[American Austin Car Company]], short-lived American automobile maker\n\n==Entertainment==\n*[[Austin (song)|\"Austin\" (song)]], a single by Blake Shelton\n*Austin, a kangaroo [[Beanie Baby]] produced by Ty, Inc.\n*Austin the kangaroo from the children\'s television series \'\'[[The Backyardigans]]\'\'\n\n==Other uses==\n*[[USS Austin|USS \'\'Austin\'\']], three ships\n*[[Austin Station (disambiguation)]], various public transportation stations\n\n==See also==\n*[[Austen (disambiguation)]]\n*[[Augustine (disambiguation)]]\n*[[Special:Prefixindex/Austin|All pages beginning with Austin]]\n\n{{disambiguation|geo}}";
//		 System.out.println("targetsen2:"+targetsen);
		Matcher matcher = pattern.matcher(targetsen);
		Matcher wikimatcher=wikipatn.matcher(targetsen);
		if(wikimatcher.find()){
			String wikistr=wikimatcher.group(1);
//			System.out.println("wikistr:"+wikistr+",groupnum:"+wikimatcher.groupCount());
			titles.add(wikistr);
		}
		while (matcher.find()) {
			String ans = matcher.group(1);
			//System.out.println("ans:"+ans);
			int index1 = ans.indexOf("]]");
			int index2 = ans.indexOf("|");
			if (index1 != -1 && index2 != -1) {
				System.out.println("Both situation!");
			}
			if (index2 != -1) {
				index1 = index2;
			}
			int lastindex = (index1 == -1) ? ans.length() : index1;
			// System.out.println(index+" "+lastindex);
			// System.out.println(ans.substring(0,lastindex).replace(" ", "_"));
			String title=ans.substring(0, lastindex).replace(" ", "_");
			titles.add(title);
			System.out.println("title:"+title);
		}
//		if(titles.size()==0){
//			System.out.println();
//			System.in.read();
//		}
		System.out.println("titlesize:"+titles.size());
		return titles;
	}
	
	
	public static void main(String args[]) throws IOException {
		// String ensqltablepath = "/home/keyang/AN/experiment/tables/";
		// String pagetxtfile = ensqltablepath + "Page.txt";
		// String page_outlinkFile = ensqltablepath + "page_outlinks.txt";
		// String pageid = "36071326";// title: Apple_(disambiguation)
		CheckWikiDisambiguationPage test = new CheckWikiDisambiguationPage();
		// test.PageTxtCheck(pagetxtfile, pageid);
		// System.out
		// .println("===========================================================");
		// test.OutLinkCheck(page_outlinkFile, pageid);
//		BufferedReader reader=new BufferedReader(new FileReader("pat.txt"));
//		String line=reader.readLine();
//		test.TestPatternRecog(line);
		 //test.TestPatternRecog("Austin_(disambiguation) {{wiktionary|Austin}}\n\'\'\'Austin\'\'\' is an English language contracted form of [[Augustine (given name)|Augustine]].\n\'\'\'Austin\'\'\' may refer to:\n{{TOC right}}\n\n==Geographical locations==\n\n===In the United States===\n*[[Austin, Arkansas]]\n*[[Austin, Colorado]]\n* Austin, Illinois:\n** [[Austin Township,銆�Macon County, Illinois]]\n**[[Austin, Chicago]], Cook County, Illinois\n*[[Austin, Indiana]]\n*[[Austin, Kentucky]]\n*[[Austin, Minnesota]]\n*[[Austin, Nevada]]\n*[[Austin, Oregon]]\n*[[Austin, Texas]], the capital of the U.S. state of Texas.\n*[[Austin County, Texas]] (note that the city of Austin is located in Travis County)\n*[[Austin, Washington]]\n\n===In Canada===\n*[[Austin, Manitoba]]\n*[[Austin, Ontario]]\n*[[Austin, Quebec]]\n*[[Austin Island]], Nunavut\n\n===In Australia===\n*[[Austin, Western Australia]]\n\n==People==\n*[[Austin (name)]]\n\n==Schools==\n*[[Austin College]], Sherman, Texas\n*[[University of Texas at Austin]], flagship institution of the University of Texas System\n*[[Austin Peay State University]], Clarksville, Tennessee\n\n==Religion==\n*[[Augustine of Hippo]] or [[Augustine of Canterbury]]\n*An adjective for the [[Augustinians]]\n\n==Business==\n*[[Austin Automobile Company]], short-lived American automobile company\n*[[Austin (brand)]], a brand owned by the Kellogg Company\n*[[Austin Motor Company]], British car manufacturer\n*[[American Austin Car Company]], short-lived American automobile maker\n\n==Entertainment==\n*[[Austin (song)|\"Austin\" (song)]], a single by Blake Shelton\n*Austin, a kangaroo [[Beanie Baby]] produced by Ty, Inc.\n*Austin the kangaroo from the children\'s television series \'\'[[The Backyardigans]]\'\'\n\n==Other uses==\n*[[USS Austin|USS \'\'Austin\'\']], three ships\n*[[Austin Station (disambiguation)]], various public transportation stations\n\n==See also==\n*[[Austen (disambiguation)]]\n*[[Augustine (disambiguation)]]\n*[[Special:Prefixindex/Austin|All pages beginning with Austin]]\n\n{{disambiguation|geo}}");
		//run on galaxy
//		test.DisambiguationPageFromPagetxt("/home/keyang/AN/experiment/tables/Page.txt","/home/bean/wiki/BAN/disambiguation.txt",true);
		//run on guava
		test.DisambiguationPageFromPagetxt("/home/bean/Wiki_chinese/Data/output/Page.txt","/home/bean/wiki/BAN/disambiguation_zh.txt",false);
	}
}
