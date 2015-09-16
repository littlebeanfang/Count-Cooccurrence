package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class RewriteenzhLinkFiles {
	public void RewriteLinkFile(String ensubmainfile, String zhsubmainfile,
			String langlinkfile, String enlinkfile, String zhlinkfile,
			String newenlinkfile, String newzhlinkfile) throws IOException {
		// Init map
		HashMap<String, String> en_sub_main_map = new HashMap<String, String>();
		HashMap<String, String> zh_sub_main_map = new HashMap<String, String>();
		HashSet<String> enid_inlanglink = new HashSet<String>();
		HashSet<String> zhid_inlanglink = new HashSet<String>();
		// Init IO
		BufferedReader ensubmainReader = new BufferedReader(new FileReader(
				ensubmainfile));
		BufferedReader zhsubmainReader = new BufferedReader(new FileReader(
				zhsubmainfile));
		BufferedReader langlinkReader = new BufferedReader(new FileReader(
				langlinkfile));
		BufferedReader enlinkReader = new BufferedReader(new FileReader(
				enlinkfile));
		BufferedReader zhlinkReader = new BufferedReader(new FileReader(
				zhlinkfile));
		File newenlink = new File(newenlinkfile);
		if (!newenlink.exists()) {
			newenlink.createNewFile();
		}
		FileWriter newenlinkWriter = new FileWriter(newenlink);
		File newzhlink = new File(newzhlinkfile);
		if (!newzhlink.exists()) {
			newzhlink.createNewFile();
		}
		FileWriter newzhlinkWriter = new FileWriter(newzhlink);
		// read group sub-main map
		String line;
		String subid;
		String mainid;
		while ((line = ensubmainReader.readLine()) != null) {
			String columns[] = line.split("\t");
			subid = columns[0];
			mainid = columns[1];
			en_sub_main_map.put(subid, mainid);
		}
		while ((line = zhsubmainReader.readLine()) != null) {
			String columns[] = line.split("\t");
			subid = columns[0];
			mainid = columns[1];
			zh_sub_main_map.put(subid, mainid);
		}
		// read langlinks en,zhid
		String enid;
		String zhid;
		while ((line = langlinkReader.readLine()) != null) {
			String columns[] = line.split("\t");
			enid = columns[0];
			zhid = columns[1];
			enid_inlanglink.add(enid);
			zhid_inlanglink.add(zhid);
		}

		// if both id not in langlinks en,zh id,filter out
		HashSet<String> tempwrite = new HashSet<String>();
		String enid1;
		String enid2;
		while ((line = enlinkReader.readLine()) != null) {
			String columns[] = line.split("\t");
			enid1 = columns[0];
			enid2 = columns[1];
			if (!enid_inlanglink.contains(enid1)
					&& !enid_inlanglink.contains(enid2)) {
				continue;
			} else {
				// else write new linkfile
				// if in group map, then rewrite id
				if (en_sub_main_map.containsKey(enid1)) {
					enid1 = en_sub_main_map.get(enid1);
				}
				if (en_sub_main_map.containsKey(enid2)) {
					enid2 = en_sub_main_map.get(enid2);
				}
				tempwrite.add(enid1 + "\t" + enid2 + "\n");
			}
		}
		for (String writeline : tempwrite) {
			newenlinkWriter.write(writeline);
		}
		tempwrite.clear();
		String zhid1;
		String zhid2;
		while ((line = zhlinkReader.readLine()) != null) {
			String columns[] = line.split("\t");
			zhid1 = columns[0];
			zhid2 = columns[1];
			if (!zhid_inlanglink.contains(zhid1)
					&& !zhid_inlanglink.contains(zhid2)) {
				continue;
			} else {
				// else write new linkfile
				// if in group map, then rewrite id
				if (zh_sub_main_map.containsKey(zhid1)) {
					zhid1 = en_sub_main_map.get(zhid1);
				}
				if (zh_sub_main_map.containsKey(zhid2)) {
					zhid2 = en_sub_main_map.get(zhid2);
				}
				tempwrite.add(zhid1 + "\t" + zhid2 + "\n");
			}
		}
		for (String writeline : tempwrite) {
			newzhlinkWriter.write(writeline);
		}

		// close IO
		ensubmainReader.close();
		zhsubmainReader.close();
		langlinkReader.close();
		enlinkReader.close();
		zhlinkReader.close();
		newenlinkWriter.close();
		newzhlinkWriter.close();
	}
}
