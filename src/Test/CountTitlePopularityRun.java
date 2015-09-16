package Test;

import java.io.IOException;

import path.CommonPath;
import Bean.Util;

public class CountTitlePopularityRun {
	public static void main(String args[]) throws IOException{
		Util test=new Util();
		test.TitlePopularity(CommonPath.synsetPath, CommonPath.plainTextFile, CommonPath.popFileWritePath);
	}
}
