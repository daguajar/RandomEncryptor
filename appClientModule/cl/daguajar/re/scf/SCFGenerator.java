package cl.daguajar.re.scf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import cl.daguajar.re.auxClasses.Constants;
import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.InfoMessagesEnum;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;

public class SCFGenerator {

	public static boolean generate(String folderPath) {

		if (folderPath == null) {
			Message.printMessage(StatusMessageEnum.ERROR_FILE_PATH_NULL, folderPath);
			return false;
		} else {
			Message.printMessage(InfoMessagesEnum.READING_FILES);
			File folder = new File(folderPath);
			boolean noFiles = true;
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isFile() && !fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.SIMPLE_COUNT_FILE_EXTENSION)
						&& !fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.ALL_COUNT_FILE_EXTENSION)
						&& !fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.CODE_FILE_EXTENSION)) {
					noFiles = false;
					int readBytes = 0;
					FileInputStream myStream = null;

					if (fileEntry != null) {
						try {
							myStream = new FileInputStream(fileEntry);
						} catch (FileNotFoundException e) {
							Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, fileEntry.getAbsolutePath());
						} 
					} 

					if (fileEntry.length() > 0) {

						int[] count = new int[Constants.CHAR_SET_LENGTH];

						try {
							while((readBytes = myStream.read())>=0){
								count[readBytes]++;
							}
						} catch (IOException e) {
							Message.printMessage(StatusMessageEnum.ERROR_READING_FILE, fileEntry.getAbsolutePath());
						} finally {
							try {
								myStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						try {

							FileWriter fstream = new FileWriter(fileEntry + "." + Constants.SIMPLE_COUNT_FILE_EXTENSION, false);
							BufferedWriter out = new BufferedWriter(fstream);

							for (int i = 0; i < count.length; i++) {
								out.write(count[i] + (i == count.length - 1 ? "" : ";"));
							}
							out.close();
						} catch (Exception e) {
							Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, fileEntry.getAbsolutePath() + "." + Constants.SIMPLE_COUNT_FILE_EXTENSION);
						}

					} else {
						Message.printMessage(StatusMessageEnum.ERROR_FILE_NULL, fileEntry.getAbsolutePath());
					}
				}
			}

			if(noFiles){
				Message.printMessage(StatusMessageEnum.ERROR_NO_FILES_IN_FOLDER, folderPath);
				return false;
			}
			else{
				return true;
			}
		}
	}
}
