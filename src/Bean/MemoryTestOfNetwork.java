package Bean;

import java.util.HashMap;

import path.Path;
import bottomLevel.fileIO.GraphFile;
import bottomLevel.fileIO.HashMapFile;

public class MemoryTestOfNetwork {
	public void ReadMap(String hashmapfile) throws Exception {
		double start = System.currentTimeMillis();
		Runtime run = Runtime.getRuntime();
		long total1 = run.totalMemory();
		long free1 = run.freeMemory();
		long used1 = (total1 - free1) / 1000000;
		HashMap<String, String> hashmap = new HashMapFile(hashmapfile).read(1,
				String.class, String.class);

		long total2 = run.totalMemory();
		long free2 = run.freeMemory();
		long used2 = (total2 - free2) / 1000000;
		double end = System.currentTimeMillis();
		System.out.println("Reading map:" + hashmapfile + ",mapsize:"
				+ hashmap.size());
		System.out.println("Time:" + (end - start) / 1000 + " s.");
		System.out.println("Memory:" + (used2 - used1) + " M.");
	}
	public void ReadGraph(String graphfile) throws Exception {
		double start = System.currentTimeMillis();
		Runtime run = Runtime.getRuntime();
		long total1 = run.totalMemory();
		long free1 = run.freeMemory();
		long used1 = (total1 - free1) / 1000000;
		HashMap<String, HashMap<String, Double>> en_network = new GraphFile(graphfile).read();

		long total2 = run.totalMemory();
		long free2 = run.freeMemory();
		long used2 = (total2 - free2) / 1000000;
		double end = System.currentTimeMillis();
		System.out.println("Reading map:" + graphfile + ",mapsize:"
				+ en_network.size());
		System.out.println("Time:" + (end - start) / 1000 + " s.");
		System.out.println("Memory:" + (used2 - used1) + " M.");
	}
}
