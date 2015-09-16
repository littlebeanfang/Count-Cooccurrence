package path;

import java.io.File;

public class CommonPath {
	public static final boolean printlog = true;
	
	public static final String workDir = "/home/bean/Wiki_chinese/Data/"; 
//	public static final String workDir = "D:\\Bean\\DATA\\wiki\\"; 

	public static final String plainTextDir = workDir + "extracted"+File.separator; 
	  
//	public static final String plainTextFile = workDir + "text1.xml"; 
//	public static final String plainTextSegFile = workDir + "text_seg1.xml";
	public static final String plainTextFile = workDir + "text.xml"; 
	public static final String plainTextSegFile = workDir + "chwikiseg.xml";  
  
	public static final String freeAssoDir = workDir + "freeAsso"+File.separator;
	public static final String synsetPath = workDir + "synset"+File.separator+"synset.txt";
	public static final String selectSynsetPath = workDir + "synset"+File.separator+"synset30000.txt";
	public static final String PageWeightPath = workDir + "synset"+File.separator+"pageweight.txt";
	public static final String PageTitleOccurNumPath = workDir + "synset"+File.separator+"titleoccur.txt";
	public static final String SelectedTitlePath = workDir + "synset"+File.separator+"title30000.txt";
	public static final String graphPath = workDir + "ANwiki.txt";

	public static final String tablesDir = workDir + "output"+File.separator;
	public static final String indexWordPath = tablesDir + "page_id.txt";
	public static final String pageLinkPath = tablesDir + "page_outlinks.txt";
	public static final String pageMapLinePath = tablesDir + "PageMapLine.txt";

	public static String coOccurDir = workDir + "coOccur30000"+File.separator;
	public static String slcPath = coOccurDir + "slCoOccur.txt";
	public static String tbcPath = coOccurDir + "tbCoOccur.txt";
	public static String tlcPath = coOccurDir + "tlCoOccur.txt";
	public static String tgcPath = coOccurDir + "tgCoOccur.txt";
	public static String llcPath = coOccurDir + "llCoOccur.txt";
	public static String[] layerPaths = { slcPath, tbcPath, tlcPath, tgcPath, llcPath };
	
	public static String popFileWritePath=workDir+"synset"+File.separator+"poptitlelist.txt";
}