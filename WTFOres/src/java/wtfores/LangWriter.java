package wtfores;

import java.io.BufferedWriter;

public class LangWriter {
	public LangWriter() {
	}

	public static void genLangFile(java.util.HashSet<String> hashset) {
		try {
			BufferedWriter writer = new BufferedWriter(new java.io.FileWriter("WTFOres_en_US.lang"));

			java.util.Iterator<String> iterator = hashset.iterator();
			while (iterator.hasNext()) {
				String string = (String) iterator.next();
				writer.write(string);
				writer.newLine();
			}
			writer.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
