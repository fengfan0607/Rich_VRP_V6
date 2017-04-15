package DataIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataOutPut {

	public static void writeFile(String fileName, String s, boolean title) {
		if (title) {
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
		}
		try {
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
