package count;

import io.HashMapWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import BeanUtil.BeanReader;
import path.CommonPath;
import synset.Synset;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class CountCoOccur {
	Synset synset = null;
	HashSet<String> allVoca = null;
	StanfordCoreNLP pipeline = null;
	HashMap<String, Integer> slc = null;
	HashMap<String, Integer> tbc = null;
	HashMap<String, Integer> tgc = null;
	HashMap<String, Integer> tlc = null;

	public CountCoOccur() throws Exception {
		synset = new Synset(CommonPath.selectSynsetPath);
		allVoca = synset.getAllVoca();
		slc = new HashMap<String, Integer>();
		tbc = new HashMap<String, Integer>();
		tgc = new HashMap<String, Integer>();
		tlc = new CountTlCoOccur().getTlc();
	}

	private void InitStanfordPipe(String property) {
		Properties props = new Properties();
		props.setProperty("annotators", property);
		pipeline = new StanfordCoreNLP(props);
	}

	public void RunEnglishCount() throws Exception {
		InitStanfordPipe("tokenize, ssplit");
		countAllFiles(new File(CommonPath.plainTextDir), false);
		convertTbTl();
		output();
	}

	public void RunChineseCount() throws Exception {
		countAllFiles(new File(CommonPath.plainTextSegFile), true);
		convertTbTl();
		output();
	}

	private void convertTbTl() throws Exception {
		for (String pair : tbc.keySet()) {
			if (tlc.containsKey(pair)) {
				tlc.put(pair, tbc.get(pair) + tlc.get(pair));
			}
		}
	}

	private void output() throws Exception {
		new HashMapWriter(slc).writeTo(CommonPath.slcPath);
		new HashMapWriter(tbc).writeTo(CommonPath.tbcPath);
		new HashMapWriter(tgc).writeTo(CommonPath.tgcPath);
		new HashMapWriter(tlc).writeTo(CommonPath.tlcPath);
	}

	private void countAllFiles(File inputFile, boolean chineseflag)
			throws Exception {
		if (inputFile.isDirectory()) {
			for (File file : inputFile.listFiles()) {
				countAllFiles(file, chineseflag);
			}
		} else {
			countOneFile(inputFile, chineseflag);
		}
	}

	private void countOneFile(File inputFile, boolean chineseflag)
			throws Exception {
		if (!chineseflag) {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(inputFile), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = null;
			String title = null;
			boolean glossFlag = false;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("<doc id=")) {
					title = line.substring(
							line.indexOf("title=\"") + "title=\"".length(),
							line.indexOf("\">")).toLowerCase();
					glossFlag = true;
				}
				if (!line.startsWith("<doc id=") && !line.startsWith("</doc>")) {
					Annotation paragraph = new Annotation(line);
					pipeline.annotate(paragraph);
					List<CoreMap> sentences = paragraph
							.get(SentencesAnnotation.class);
					for (CoreMap sentence : sentences) {
						List<CoreLabel> tokens = sentence
								.get(TokensAnnotation.class);
						ArrayList<String> words = new ArrayList<String>();
						for (CoreLabel token : tokens) {
							String word = token.get(TextAnnotation.class)
									.toLowerCase();
							if (allVoca.contains(word)) {
								words.add(synset.getMainForm(word));
							}
						}
						for (String word1 : words) {
							for (String word2 : words) {
								if (word1.compareTo(word2) < 0) {
									String pair = word1 + "\t" + word2;
									if (!slc.containsKey(pair)) {
										slc.put(pair, 0);
									}
									slc.put(pair, slc.get(pair) + 1);
								}
							}
						}
						if (glossFlag) {
							if (tokens.size() > 4) {
								glossFlag = false;
							}
							if (allVoca.contains(title)) {
								title = synset.getMainForm(title);
								for (String word : words) {
									String pair = "";
									if (title.compareTo(word) < 0) {
										pair = title + "\t" + word;
									} else {
										pair = word + "\t" + title;
									}
									if (!tgc.containsKey(pair)) {
										tgc.put(pair, 0);
									}
									tgc.put(pair, tgc.get(pair) + 1);
								}
							}
						}
						if (allVoca.contains(title)) {
							for (String word : words) {
								String pair = "";
								if (title.compareTo(word) < 0) {
									pair = title + "\t" + word;
								} else {
									pair = word + "\t" + title;
								}
								if (!tbc.containsKey(pair)) {
									tbc.put(pair, 0);
								}
								tbc.put(pair, tbc.get(pair) + 1);
							}
						}
					}
				}
			}
		} else {
			countOneFileZH(inputFile.toString());
		}
	}

	private HashSet<String> getTitleTokens(String line) {
		HashSet<String> titles = new HashSet<String>();
		String tokenspliter = "\\s+";
		String labelspliter = "/";
		HashSet<String> targetlabel = new HashSet<String>();
		targetlabel.add("title");
		String[] tokens = line.split(tokenspliter);
		String[] columns;
		String word, label;
		for (String token : tokens) {
			// System.out.println("token:"+token);
			columns = token.split(labelspliter);
			word = columns[0];
			label = columns[1];
			if (targetlabel.contains(label) && this.allVoca.contains(word)) {
				titles.add(this.synset.getMainForm(word));
			}
		}
		return titles;
	}

	private void countOneFileZH(String inputFile) throws Exception {
		BeanReader reader = new BeanReader(inputFile);
		String line = null;
		String title = null;
		boolean glossFlag = false;
		int errorlinecount=0;
		int linecount=0;
		while ((line = reader.readLine()) != null) {
			linecount++;
			try{
	//			System.out.println("Line:" + line);
	//			System.out.println("gloss:" + glossFlag);
				if (line.startsWith("<doc id=")) {
	//				System.out.println("======title line");
					title = line.substring(
							line.indexOf("title=\"") + "title=\"".length(),
							line.indexOf("\">"));
					glossFlag = true;
				} else if (!line.startsWith("</doc>") && !line.equals("")) {
					HashSet<String> words = this.getTitleTokens(line);
					if (words.size() < 3) {
	//					System.out.println("======skip short line");
						continue;
					}
					for (String word1 : words) {
						for (String word2 : words) {
							if (word1.compareTo(word2) < 0) {
								String pair = word1 + "\t" + word2;
								if (!slc.containsKey(pair)) {
									slc.put(pair, 0);
								}
								slc.put(pair, slc.get(pair) + 1);
							}
						}
					}
					if (glossFlag) {
	//					if (!words.contains(title)) {
	//						System.out.println(words.size());
	//						System.out.println(words);
	//						continue;
	//					}
	//					System.out.println("======gloss line");
						if (allVoca.contains(title)) {
							title = synset.getMainForm(title);
							for (String word : words) {
								String pair = "";
								if (title.compareTo(word) < 0) {
									pair = title + "\t" + word;
								} else if (title.compareTo(word) > 0) {
									pair = word + "\t" + title;
								} else {
									// skip
								}
								if (!tgc.containsKey(pair)) {
									tgc.put(pair, 0);
								}
								tgc.put(pair, tgc.get(pair) + 1);
							}
						}
						glossFlag = false;
					} else {
	//					System.out.println("======body line");
						if (allVoca.contains(title)) {
//							System.out.println("contain title");
							for (String word : words) {
								String pair = "";
								if (title.compareTo(word) < 0) {
									pair = title + "\t" + word;
								} else if (title.compareTo(word) > 0) {
									pair = word + "\t" + title;
								} else {
									// skip
								}
								if (!tbc.containsKey(pair)) {
									tbc.put(pair, 0);
								}
								tbc.put(pair, tbc.get(pair) + 1);
	//							System.out.println("tbc:"+pair+tbc.get(pair));
							}
						}
					}
				} else {
	//				System.out.println("======empty line or doc end line");
				}
			}catch(Exception e){
				errorlinecount++;
				System.out.println("Error "+errorlinecount+"line "+linecount+":"+line);
			}
		}
		reader.close();
	}

}