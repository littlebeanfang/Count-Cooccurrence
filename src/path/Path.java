package path;


public class Path {
//	public static final String workDir = "C:\\Users\\Bean\\Desktop\\Count-Cooccurrence\\Count-Cooccurrence\\Count-Cooccurrence\\data\\";
//	public static final String BANDir = workDir + "BAN\\";
//	public static final String BANRawDir=BANDir+"raw\\";
	public static final String workDir = "/home/bean/wiki/";
	public static final String BANDir = workDir + "BAN/";
	public static final String BANRawDir=BANDir+"raw/";

	public static final String enTitle2zhTitleFile = BANDir + "en2zh.txt";
	public static final String enNetworkTitleFile = BANDir + "en_network.txt";
	public static final String zhNetworkTitleFile = BANDir + "zh_network.txt";
	public static final String JarcarSimilarityOutFile = BANDir + "matches.txt";
	


	public static final String en_id2titleFile=BANRawDir+"en_id2title.txt";
	public static final String zh_id2titleFile=BANRawDir+"zh_id2title.txt";
	public static final String en_linksFile=BANRawDir+"en_links.txt";
	public static final String en_linksFileSplit00=BANRawDir+"en_links_split00";
	public static final String zh_linksFile=BANRawDir+"zh_links.txt";
	public static final String enId2zhTitleFile=BANRawDir+"langlinks.txt";//3 columns:enid,lang,langtitle
	public static final String enId2zhIdFile=BANRawDir+"lang_idlinks.txt";
	public static final String JarcaSimilarityIdOutFile= BANRawDir+"id_matches.txt";
}