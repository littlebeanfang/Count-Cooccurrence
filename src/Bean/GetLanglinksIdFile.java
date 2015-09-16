package Bean;

import java.io.IOException;
import java.util.HashMap;

import BeanUtil.BeanReader;
import BeanUtil.BeanWriter;

public class GetLanglinksIdFile {
	public void Rewritelanglinks(String zh_id2titlefile,
			String en_id2titlefile, String langlinkfile,
			String newlanglinkfile, String newlanglinktitlefile)
			throws IOException {
		HashMap<String, String> zh_title2idMap = new HashMap<String, String>();
		HashMap<String, String> en_id2titleMap = new HashMap<String, String>();
		BeanReader zh_id2titleReader = new BeanReader(zh_id2titlefile);
		BeanReader en_id2titleReader = new BeanReader(en_id2titlefile);
		BeanReader langlinkReader = new BeanReader(langlinkfile);
		BeanWriter newlanglinkWriter = new BeanWriter(newlanglinkfile,
				"new langlink file," + "format: <enid>\t<zhid>");
		BeanWriter newlanglink_titleWriter = new BeanWriter(
				newlanglinktitlefile, "new langlink file,"
						+ "format: <entitle>\t<zhtitle>");
		String columns[];
		String id;
		String title;
		while ((columns = zh_id2titleReader.readSplitLine()) != null) {
			id = columns[0];
			title = columns[1];
			zh_title2idMap.put(title, id);
		}
		System.out.println("zh_title2id read done... size:"
				+ zh_title2idMap.size());
		while ((columns = en_id2titleReader.readSplitLine()) != null) {
			id = columns[0];
			title = columns[1];
			en_id2titleMap.put(id, title);
		}
		System.out.println("en_id2title read done... size:"
				+ en_id2titleMap.size());
		String enid;
		String zhtitle;
		String entitle;
		String zhid;
		int losscount=0;
		while((columns=langlinkReader.readSplitLine())!=null){
			if(columns.length!=3){
				//wrong format
				continue;
			}
			enid=columns[0];
			zhtitle=columns[2].replace(" ", "_");
			entitle=en_id2titleMap.get(enid);
			zhid=zh_title2idMap.get(zhtitle);
			if(entitle==null||zhid==null){
				losscount++;
				System.out.println("langlink loss: enid "+enid+",zhtitle:"+zhtitle);
			}else{
				newlanglinkWriter.writeln(enid+"\t"+zhid);
				newlanglink_titleWriter.writeln(entitle+"\t"+zhtitle);
			}
		}
		en_id2titleReader.close();
		zh_id2titleReader.close();
		langlinkReader.close();
		newlanglinkWriter.close();
		newlanglink_titleWriter.close();
	}
	public static void main(String args[]) throws IOException{
		GetLanglinksIdFile test=new GetLanglinksIdFile();
		//Run on Guava
		String BANdir="/home/bean/wiki/BAN/";
		String rawdir=BANdir+"/raw/";
		String zh_id2titlefile=rawdir+"zh_id2title.txt";
		String en_id2titlefile=rawdir+"en_id2title.txt";
		String langlinkfile=rawdir+"langlinks.txt";
		String newlanglinkfile=rawdir+"langlinks_id.txt";
		String newlanglinktitlefile=rawdir+"langlinks_title.txt";
		test.Rewritelanglinks(zh_id2titlefile, en_id2titlefile, langlinkfile, newlanglinkfile, newlanglinktitlefile);
	}
}
