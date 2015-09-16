package Test;

import java.io.IOException;

import path.CommonPath;
import Bean.Util;

public class GetCNSynset {
	public static void main(String args[]) throws IOException{
		Util test=new Util();
		test.GetMainTitle(CommonPath.pageMapLinePath, CommonPath.synsetPath);
	}
}
