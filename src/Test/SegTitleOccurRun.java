package Test;

import path.CommonPath;
import Bean.TitleOccurCountFromSegmentFile;

public class SegTitleOccurRun {
	public static void main(String args[]) throws Exception{
		TitleOccurCountFromSegmentFile test=new TitleOccurCountFromSegmentFile();
		test.CountOccurFromSeg(CommonPath.plainTextSegFile, CommonPath.synsetPath,CommonPath.PageTitleOccurNumPath);
	}
}
