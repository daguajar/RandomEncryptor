package cl.daguajar.re.encoder;

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

public class Encoder {

	private static float noisePercentage = 0.0f;
	private static float randomValue = 0.0f;
	private static int[] sizePerChar = null;
	private static String keyFilePath = null;

	public static boolean encode(String toEncodeFilePath, String keyFilePathValue) {
		File outputFile = new File(toEncodeFilePath + "." + Constants.ENCODED_FILE_EXTENSION);
		File inputFile = new File(toEncodeFilePath);
		keyFilePath = keyFilePathValue;

		try {
			if (ZipReader.existFileInZip(Constants.ENCODER_FOLDER_NAME + Constants.NOISE_PERCENTAGE_FILE_NAME, keyFilePath)) {
				BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.ENCODER_FOLDER_NAME + Constants.NOISE_PERCENTAGE_FILE_NAME, keyFilePath)));

				String line = br.readLine();
				if (line != null) {
					noisePercentage = Float.parseFloat(line);
				}
				br.close();
				ZipReader.closeZipFile();
				sizePerChar = new int[Constants.LENGT_WHIT_NOISE];
			} else {
				sizePerChar = new int[Constants.LENGTH_WITHOUT_NOISE];
			}
		} catch (FileNotFoundException e1) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_FILE_NOT_FOUND, Constants.ENCODER_FOLDER_NAME + Constants.NOISE_PERCENTAGE_FILE_NAME, keyFilePath);
			return false;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_READING_FILE, Constants.ENCODER_FOLDER_NAME + Constants.NOISE_PERCENTAGE_FILE_NAME, keyFilePath);
			return false;
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.ENCODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath)));

			String line = br.readLine();
			if (line != null) {
				String[] countPerByte = line.split(";");
				for (int i = 0; i < countPerByte.length; i++) {
					sizePerChar[i] = Integer.parseInt(countPerByte[i]);
				}
			}
			br.close();
			ZipReader.closeZipFile();
		} catch (FileNotFoundException e1) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_FILE_NOT_FOUND, Constants.ENCODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath);
			return false;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_ZIP_READING_FILE, Constants.ENCODER_FOLDER_NAME + Constants.SIZE_FILE_NAME, keyFilePath);
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
						
						if (noisePercentage > 0.0f) {
							if (!addNoise(fos)) {
								return false;
							}
						}
						int value = (int) (Math.random() * sizePerChar[readBytes]);
						BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.ENCODER_FOLDER_NAME + readBytes, keyFilePath)));
						for (int k = 0; k < value; ++k) {
							br.readLine();
						}
						String line = br.readLine();
						if (line != null) {
							String[] codesArray = line.split(Constants.CHAR_SEPARATOR);
							for (int j = 0; j < codesArray.length; j++) {
								fos.write(Integer.parseInt(codesArray[j]));
							}
						}
						br.close();
						ZipReader.closeZipFile();
					}
					if (noisePercentage > 0.0f) {
						if (!addNoise(fos)) {
							return false;
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

		} else {
			Message.printMessage(StatusMessageEnum.ERROR_IS_NOT_A_FILE, inputFile.getAbsolutePath());
			return false;
		}

		return true;
	}

	private static boolean addNoise(FileOutputStream fos) {
		randomValue = (float) Math.random();
		if (randomValue <= noisePercentage) {
			int value = (int) (Math.random() * sizePerChar[Constants.NOISE_INDEX]);
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(ZipReader.getStreamFromFileInZip(Constants.ENCODER_FOLDER_NAME + Constants.NOISE_INDEX, keyFilePath)));

				for (int i = 0; i < value; ++i) {
					br.readLine();
				}
				String line = br.readLine();
				if (line != null) {
					String[] codesArray = line.split(Constants.CHAR_SEPARATOR);
					for (int j = 0; j < codesArray.length; j++) {
						fos.write(Integer.parseInt(codesArray[j]));
					}
				}
				br.close();
				ZipReader.closeZipFile();
			} catch (FileNotFoundException e1) {
				Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND);
				return false;
			} catch (IOException e) {
				Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE);
				return false;
			}
			return addNoise(fos);
		}
		return true;
	}

}
