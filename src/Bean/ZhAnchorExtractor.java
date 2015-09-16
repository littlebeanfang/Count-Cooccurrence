package Bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class ZhAnchorExtractor {
	/**
	 * get Chinese anchor label-title pairs from json format file.
	 * the json file is extract from Annotated-WikiExtractor
	 */
	public void Extractor(String wikijsonfile,String anchor1levelfile
			,String anchor2levelfile) throws IOException {
		// String wikijsonfile="wiki00.txt";
		BufferedReader wikijsonReader = new BufferedReader(new FileReader(
				wikijsonfile));
		String lineString;
		JSONObject obj;
		AnchorMapOp anchormaps=new AnchorMapOp();
		while ((lineString = wikijsonReader.readLine()) != null) {
			obj = new JSONObject(lineString);
			// System.out.println(obj.toString());
			JSONArray annArray = obj.getJSONArray("annotations");
			for (int i = 0; i < annArray.length(); i++) {
				JSONObject anchor = annArray.getJSONObject(i);
				String anchorlabel = (String) anchor.get("surface_form");
				// System.out.println("label:"+anchorlabel);
				String uri = anchor.getString("uri");
				String wikititle = URLDecoder.decode(uri,
						StandardCharsets.UTF_8.toString());
				// System.out.println("title:"+wikititle);
				anchormaps.AddTo2LevelMap(wikititle, anchorlabel);
				anchormaps.AddTo1levelMap(wikititle, anchorlabel);
			}
		}
		anchormaps.WriteSortMap(anchor1levelfile);
		anchormaps.Write2levelSortMap(anchor2levelfile);
		wikijsonReader.close();
	}
	public static void main(String args[]) throws IOException{
		ZhAnchorExtractor test =new ZhAnchorExtractor();
		String dir="/home/bean/Wiki_Chinese/Data/extracted_link/";
		String wikijsonfile=dir+"extracted_link.json";
		String anchor1levelfile=dir+"zh_anchorlabeltitle_count.map";
		String anchor2levelfile=dir+"zh_anchorlabel_title_count.map";
		test.Extractor(wikijsonfile, anchor1levelfile, anchor2levelfile);
	}
}
