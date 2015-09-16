package Bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import BeanUtil.BeanWriter;
import BeanUtil.IntValueMapSorter;

public class AnchorMapOp {
	HashMap<String, HashMap<String, Integer>> anchorlabel_title_count = new HashMap<String, HashMap<String, Integer>>();
	HashMap<String, Integer> anchorlabeltitle_count = new HashMap<String, Integer>();
	public void AddTo2LevelMap(String title, String label) {
		if (anchorlabel_title_count.containsKey(label)) {
			HashMap<String, Integer> title_count = anchorlabel_title_count.get(label);
			if (title_count.containsKey(title)) {
				title_count.put(title, title_count.get(title) + 1);
			} else {
				title_count.put(title, 1);
			}
		} else {
			HashMap<String, Integer> title_count = new HashMap<String, Integer>();
			title_count.put(title, 1);
			anchorlabel_title_count.put(label, title_count);
		}
	}

	public void AddTo1levelMap(String title, String label) {
		if (!title.equals(label)) {
			String key = label + "\t" + title;
			if (anchorlabeltitle_count.containsKey(key)) {
				anchorlabeltitle_count.put(key,
						anchorlabeltitle_count.get(key) + 1);
			} else {
				anchorlabeltitle_count.put(key, 1);
			}
		}
	}

	public void WriteSortMap(String writefiles) throws IOException {
		IntValueMapSorter sorter = new IntValueMapSorter();
		List<Map.Entry<String, Integer>> sortlist = sorter
				.SortCountMap(anchorlabeltitle_count);
		BeanWriter writer = new BeanWriter(writefiles,
				"wiki anchor pairs and count(label!=title).");
		for (Map.Entry<String, Integer> entry : sortlist) {
			writer.writeln(entry.getKey() + "\t" + entry.getValue());
		}
		writer.close();
	}

	public void Write2levelSortMap(String writefiles) throws IOException {
		IntValueMapSorter sorter = new IntValueMapSorter();
		BeanWriter writer = new BeanWriter(
				writefiles,
				"wiki labelanchor_title_count,(label!=title), group by label, sorted by title count.");
		Iterator ite = anchorlabel_title_count.entrySet().iterator();
		while (ite.hasNext()) {
			Map.Entry<String, HashMap<String, Integer>> entry = (Entry<String, HashMap<String, Integer>>) ite
					.next();
			String label = entry.getKey();
			HashMap<String, Integer> title_count = entry.getValue();
			List<Map.Entry<String, Integer>> sortlist = sorter
					.SortCountMap(title_count);
			for (Map.Entry<String, Integer> titleentry : sortlist) {
				String labelslash = label.replace(" ", "_");
				String titleString = titleentry.getKey();
				if (!label.equals(titleString)
						&& !labelslash.equals(titleString)) {
					writer.writeln(label + "\t" + titleentry.getKey() + "\t"
							+ titleentry.getValue());
				}
			}
		}
		writer.close();
	}
}
