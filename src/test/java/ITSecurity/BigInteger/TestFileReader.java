package ITSecurity.BigInteger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
						testCase.map.put(splitLine[0], "");						
					} else if (splitLine[0].equals("t")) {
						testCase.title = splitLine[1];
					} else {
						while (testCase.map.get(splitLine[0]) != null) {
							// There is already a value specified for that key. Append a number.
							// e.g. in primeNumberTests.txt: split "a" into keys "a", "a0", "a1", "a2", ...
							if (splitLine[0].length() == 1) {
								splitLine[0] = splitLine[0].concat("0");
							} else {
								int number = Integer.parseInt(splitLine[0].substring(1, splitLine[0].length()));
								splitLine[0] = splitLine[0].substring(0, 1) + ++number;
							}
						}
						testCase.map.put(splitLine[0], splitLine[1]);
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Filter out any test-cases that have no title.
		return result.stream().filter(r -> r.title != null).collect(Collectors.toList());
	}
}
