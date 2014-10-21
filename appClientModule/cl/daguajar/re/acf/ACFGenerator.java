package cl.daguajar.re.acf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cl.daguajar.re.auxClasses.Constants;
import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.InfoMessagesEnum;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;

public class ACFGenerator {

	public static boolean generate(String filePath) {

		Message.printMessage(InfoMessagesEnum.CREATING_STATISTICAL_FILE);
		
		File folder = new File(filePath);
		File acfFile = new File(filePath + File.separatorChar + Constants.ALL_COUNT_FILE_NAME_AND_EXTENSION);

		int[] count = new int[Constants.CHAR_SET_LENGTH];

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile() && fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.SIMPLE_COUNT_FILE_EXTENSION)) {

				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(fileEntry));
					String line = br.readLine();
					if (line != null) {
						String[] countPerByte = line.split(Constants.COUNT_SEPARATOR);
						for (int i = 0; i < countPerByte.length; i++) {
							count[i] += Integer.parseInt(countPerByte[i]);
						}
					}
					br.close();
				} catch (FileNotFoundException e1) {
					Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, fileEntry.getAbsolutePath());
					return false;
				} catch (IOException e) {
					Message.printMessage(StatusMessageEnum.ERROR_READING_FILE, fileEntry.getAbsolutePath());
					return false;
				}

			}

		}

		try {

			FileWriter fstream = new FileWriter(acfFile, false);
			BufferedWriter out = new BufferedWriter(fstream);

			for (int i = 0; i < count.length; i++) {
				out.write(count[i] + (i == count.length - 1 ? "" : Constants.COUNT_SEPARATOR));
			}
			out.close();
		} catch (Exception e) {
			Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, acfFile.getAbsolutePath());
			return false;		}

		return true;
	}

}
