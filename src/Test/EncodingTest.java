package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class EncodingTest {
	public void decoder() throws IOException {
		String uri = "%E8%8B%8F%E8%81%94";
		String decstr = URLDecoder.decode(uri,
				StandardCharsets.UTF_8.toString());
		System.out.println(decstr);
		String unistr = "\u82cf\u8054";
		System.out.println(unistr);
		File out = new File("encoding.test");
		if (!out.exists()) {
			out.createNewFile();
		}
		FileWriter writer = new FileWriter(out);
		writer.write(decstr + "\n");
		writer.write(unistr + "\n");
		writer.close();
	}
}
