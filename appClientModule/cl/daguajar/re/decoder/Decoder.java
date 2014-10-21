package cl.daguajar.re.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import cl.daguajar.re.auxClasses.Constants;
import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.InfoMessagesEnum;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;
import cl.daguajar.re.zip.ZipReader;

public class Decoder {

	private static int startNumber = 0;
	private static String keyFilePath = null;

	public static boolean decode(String toDecodeFilePath, String keyFilePathValue) {

		File outputFile = new File(toDecodeFilePath + "." + Constants.DECODED_FILE_EXTENSION);
		File inputFile = new File(toDecodeFilePath);
		keyFilePath = keyFilePathValue;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.DECODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath)));

			String line = br.readLine();
			if (line != null) {
				startNumber = Integer.parseInt(line);
			}
			br.close();
			ZipReader.closeZipFile();
		} catch (FileNotFoundException e1) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_FILE_NOT_FOUND, Constants.DECODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath);
			return false;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_READING_FILE, Constants.DECODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath);
			return false;
		}

		if (inputFile.isFile()) {
			int readBytes = 0;
			FileInputStream myStream = null;

			if (inputFile != null) {
				try {
					myStream = new FileInputStream(inputFile);
				} catch (FileNotFoundException e) {
					Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, inputFile.getAbsolutePath());
					return false;
				}
			}

			if (inputFile.length() > 0) {
				try {

					int percent = (int) inputFile.length() / Constants.STEPS;

					FileOutputStream fos = new FileOutputStream(outputFile, false);
					byte[] myArray = new byte[(int) inputFile.length()];

					int count = 0;
					int percentCount = 0;
					Date beginDate = new Date();

					if (Constants.SEE_PROGRESS) {
						Message.printMessage(InfoMessagesEnum.STEPS, beginDate.toString());
					}

					String tempCode = "";
					String tempFolder = "";
					for (int i = 0; i < myArray.length; i++) {
						readBytes = myStream.read();

						if (Constants.SEE_PROGRESS) {
							if (count == percent) {
								percentCount++;
								Date now = new Date();
								long estimated = ((now.getTime() - beginDate.getTime()) * (Constants.STEPS - percentCount)) / percentCount;

								long estimatedSecs = estimated / 1000 % 60;
								long estimatedMinutes = estimated / (60 * 1000) % 60;
								long estimatedHours = estimated / (60 * 60 * 1000);
								
								String timeString = estimatedHours + ":"
										+ (estimatedMinutes < 10 ? "0" + estimatedMinutes : estimatedMinutes) + ":" + (estimatedSecs < 10 ? "0" + estimatedSecs : estimatedSecs);

								Message.printMessage(InfoMessagesEnum.PROGRESS, percentCount + "", timeString);
								count = 0;
							} else {
								count++;
							}
						}

						if (tempCode.length() == 0) {
							tempFolder = "" + readBytes;
						}
						tempCode = tempCode.length() == 0 ? "" + readBytes : tempCode + Constants.CHAR_SEPARATOR + readBytes;

						if (tempCode.split(Constants.CHAR_SEPARATOR).length >= startNumber) {
							if (ZipReader.existFileInZip(Constants.DECODER_FOLDER_NAME + tempFolder, keyFilePath)) {
								BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.DECODER_FOLDER_NAME + tempFolder, keyFilePath)));
								String line = "";
								while ((line = br.readLine()) != null) {
									String[] codeCharPair = line.split(Constants.CODE_SEPARATOR);
									if (tempCode.equals(codeCharPair[0])) {
										String resultChar = codeCharPair[1];
										if (!resultChar.equals(Constants.NOISE_FOLDER_NAME)) {
											fos.write(Integer.parseInt(resultChar));
										}
										tempCode = "";
										tempFolder = "";
									}
								}
								br.close();
							}

						}
					}
					fos.close();
				} catch (IOException e) {
					Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, outputFile.getAbsolutePath());
					return false;
				} finally {
					try {
						myStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {
				Message.printMessage(StatusMessageEnum.ERROR_FILE_NULL, outputFile.getAbsolutePath());
				try {
					myStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

		}
		else{
			Message.printMessage(StatusMessageEnum.ERROR_IS_NOT_A_FILE, inputFile.getAbsolutePath());
			return false;
		}

		return true;
	}

}
