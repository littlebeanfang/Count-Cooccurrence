package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class SplitDumpLinkFile {
	public void SplitByBilingualPairFile(String PairFile, String enDumpFile,String zhDumpFile, String suffix) throws IOException{
		//offering the spliting pair file, give out split dump file
		BufferedReader reader=new BufferedReader(new FileReader(PairFile));
		String line;
		HashSet<String> enidSet=new HashSet<String>();
		HashSet<String> zhidSet=new HashSet<String>();
		//read in enidset and zhidset
		while((line=reader.readLine())!=null){
			String columns[]=line.split("\t");
			String enid=columns[0];
			String zhid=columns[1];
			enidSet.add(enid);
			zhidSet.add(zhid);
		}
		//split english dump file
		File ensplitfile=new File("en_links_split"+suffix);
		if(!ensplitfile.exists()){
			ensplitfile.createNewFile();
		}
		FileWriter enwriter=new FileWriter(ensplitfile);
		BufferedReader endumpreader=new BufferedReader(new FileReader(enDumpFile));
		while((line=endumpreader.readLine())!=null){
			String columns[]=line.split("\t");
			if(enidSet.contains(columns[0])||enidSet.contains(columns[1])){
				enwriter.write(line+"\n");
				enwriter.flush();
			}
		}
		
		//split zhongwen dump file
				File zhsplitfile=new File("zh_links_split"+suffix);
				if(!zhsplitfile.exists()){
					zhsplitfile.createNewFile();
				}
				FileWriter zhwriter=new FileWriter(zhsplitfile);
				BufferedReader zhdumpreader=new BufferedReader(new FileReader(zhDumpFile));
				while((line=zhdumpreader.readLine())!=null){
					String columns[]=line.split("\t");
					if(zhidSet.contains(columns[0])||zhidSet.contains(columns[1])){
						zhwriter.write(line+"\n");
						zhwriter.flush();
					}
				}
		
		reader.close();
		endumpreader.close();
		zhdumpreader.close();
		enwriter.close();
		zhwriter.close();
	}
	public static void main(String args[]) throws IOException{
		SplitDumpLinkFile test=new SplitDumpLinkFile();
		test.SplitByBilingualPairFile("lang_idlinks_split0", "en_links.txt", "zh_links.txt", "0");
		test.SplitByBilingualPairFile("lang_idlinks_split1", "en_links.txt", "zh_links.txt", "1");
		test.SplitByBilingualPairFile("lang_idlinks_split2", "en_links.txt", "zh_links.txt", "2");
		test.SplitByBilingualPairFile("lang_idlinks_split3", "en_links.txt", "zh_links.txt", "3");
		test.SplitByBilingualPairFile("lang_idlinks_split4", "en_links.txt", "zh_links.txt", "4");
	}
}
