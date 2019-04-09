package ITSecurity.BigInteger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestFileReader {
	String filePath;

	public TestFileReader(String filePath) {
		this.filePath = filePath;
	}

	public List<ImportedTestCase> read() {
		List<ImportedTestCase> result = new ArrayList<ImportedTestCase>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.filePath));
			String line = reader.readLine();
			ImportedTestCase testCase = new ImportedTestCase();
			while (line != null) {
				if (line.startsWith("#---------")) {
					result.add(testCase);
					testCase = new ImportedTestCase();
				} else if (line.startsWith("#")) {
				} else {
					String[] splitLine = line.split("=");
					if (splitLine.length != 2 || splitLine[1].isEmpty()) {
						continue;
					}
					if (splitLine[0].equals("t")) {
						testCase.title = splitLine[1];
					} else if (splitLine[0].equals("s")) {
						testCase.size = Integer.parseInt(splitLine[1]);
					} else {
						testCase.map.put(splitLine[0], splitLine[1]);						
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
