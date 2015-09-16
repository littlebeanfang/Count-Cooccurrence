package Bean;

import java.io.IOException;

public class DisambiguationProcess {
	public void ProcessPipe() throws Exception{
		WriteDisambiguationIdFile wridisam=new WriteDisambiguationIdFile();
		String dongrawdir = "/home/bean/wiki/BAN/raw/";
		String langlinkfile = dongrawdir + "langlinks_id.txt";
		String enid2titlefile = dongrawdir + "en_id2title.txt";
		String zhid2titlefile = dongrawdir + "zh_id2title.txt";
		String enlinkfile = dongrawdir + "en_links.txt";
		String zhlinkfile = dongrawdir + "zh_links.txt";
		
		String dongBANdir = "/home/bean/wiki/BAN/";
		String endisamFile=dongBANdir+"disambiguation_en.txt";
		String zhdisamFile=dongBANdir+"disambiguation_zh.txt";
		String en_disamidfile = dongBANdir + "en_disam_id.txt";
		String zh_disamidfile = dongBANdir + "zh_disam_id.txt";
		String en_disamid_filter_file = dongBANdir + "en_filter_disam_id.txt";
		String zh_disamid_filter_file = dongBANdir + "zh_filter_disam_id.txt";
		String grouplanglinkfile = dongBANdir + "disamgroup_lang_idlinks.txt";
		String newenlinkfile = dongBANdir + "disamgroup_en_links.txt";
		String newzhlinkfile = dongBANdir + "disamgroup_zh_links.txt";
		String jaccarfile=dongBANdir+"disamgroup_jaccard.txt";
		wridisam.WriteIdFile(endisamFile, zhdisamFile, enid2titlefile, zhid2titlefile, 
				en_disamidfile, zh_disamidfile);
		RewriteLanglinkGroupFile rewrilanglink=new RewriteLanglinkGroupFile();
		rewrilanglink.RewriteLanglinksFile(enid2titlefile, zhid2titlefile, langlinkfile,
				en_disamidfile, zh_disamidfile, grouplanglinkfile, 
				en_disamid_filter_file, zh_disamid_filter_file);
		RewriteenzhLinkFiles rewrilink=new RewriteenzhLinkFiles();
		rewrilink.RewriteLinkFile(en_disamid_filter_file, zh_disamid_filter_file, grouplanglinkfile, enlinkfile, zhlinkfile, newenlinkfile, newzhlinkfile);
		JaccarSimilarity compjaccar=new JaccarSimilarity(grouplanglinkfile, newenlinkfile, newzhlinkfile, jaccarfile);
		compjaccar.ComputeJaccard();
	}
	public static void main(String args[]) throws Exception{
		DisambiguationProcess dp=new DisambiguationProcess();
		dp.ProcessPipe();
	}
}
