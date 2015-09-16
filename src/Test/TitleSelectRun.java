package Test;

import java.io.IOException;

import path.CommonPath;
import Bean.SortTitleCountMap;

public class TitleSelectRun {
	public static void main(String args[]) throws IOException{
		SortTitleCountMap test=new SortTitleCountMap();
		test.process(CommonPath.pageMapLinePath, CommonPath.PageWeightPath, 30000, CommonPath.SelectedTitlePath);
	}
}
