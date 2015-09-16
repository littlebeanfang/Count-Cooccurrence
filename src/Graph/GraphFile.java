package Graph;

import java.util.*;
import java.io.*;

public class GraphFile {
	String filePath = null;

	public GraphFile(String filePath) throws Exception {
		this.filePath = filePath;
	}
	
	public HashMap<String, HashMap<String, Double>> read() throws IOException {
		HashMap<String, HashMap<String, Double>> graph = new HashMap<String, HashMap<String,Double>>();
		readTo(graph,1.0);
		return graph;
	}
	
	public void readTo(HashMap<String, HashMap<String, Double>> graph, double score) throws IOException {
		File inputFile = new File(filePath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			String v1 = parts[0];
			String v2 = parts[1];
			//score = Double.parseDouble(parts[2]);
			if(!graph.containsKey(v1)){
				graph.put(v1, new HashMap<String, Double>());
			}
			graph.get(v1).put(v2, score);
		}
	}

	public void writeFrom(HashMap<String, HashMap<String, Double>> graph) throws IOException {
		File outputFile = new File(filePath);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		for (String v1 : graph.keySet()) {
			for (String v2 : graph.get(v1).keySet()) {
				bufferedWriter.write(v1 + "\t" + v2 + "\t" + graph.get(v1).get(v2) + "\n");
			}
		}
		bufferedWriter.flush();
	}
}