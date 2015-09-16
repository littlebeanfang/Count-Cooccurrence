package Test;

import java.io.IOException;

import path.CommonPath;
import Bean.SelectSynset;

public class SelectSynsetRun {
	public static void main(String args[]) throws IOException{
		SelectSynset test=new SelectSynset();
		test.select(CommonPath.synsetPath, CommonPath.SelectedTitlePath, CommonPath.selectSynsetPath);
	}
}
