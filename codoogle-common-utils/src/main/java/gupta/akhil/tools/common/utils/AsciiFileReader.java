package gupta.akhil.tools.common.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AsciiFileReader {
	private String file;
	private boolean isClasspathResource;
	private BufferedReader input;

	public AsciiFileReader(String file) {
		this.file = file;
		initializeReader();
	}

	public AsciiFileReader(InputStream is) {
		input = new BufferedReader(new InputStreamReader(is));
	}

	public AsciiFileReader(String file, boolean isClasspathResource) {
		this.file = file;
		this.isClasspathResource = isClasspathResource;
		initializeReader();
	}

	private void initializeReader() {
		try {
			Reader fr = null;
			if (!isClasspathResource) {
				fr = new FileReader(file);
			} else {
				InputStream is = getClass().getClassLoader()
						.getResourceAsStream(file);
				fr = new InputStreamReader(is);
			}
			input = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found! : '" + file + "'");
		}
	}

	public String getNextLine() {
		String line;
		try {
			line = input.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file " + file);
		}
		return line;
	}

}
