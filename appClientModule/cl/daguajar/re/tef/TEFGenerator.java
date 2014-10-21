package cl.daguajar.re.tef;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.daguajar.re.auxClasses.AmountPerChar;
import cl.daguajar.re.auxClasses.Constants;
import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.InfoMessagesEnum;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;
import cl.daguajar.re.zip.ZipCompressor;

public class TEFGenerator {

	private static BufferedWriter codeListFileOut = null;

	private static long codesCount = 0;
	private static String firstCharTemp = "";
	private static Date beginDate = new Date();

	public static boolean generate(String filePath, String outputFileName) {

		File acfFile = new File(filePath + File.separatorChar + Constants.ALL_COUNT_FILE_NAME_AND_EXTENSION);
		File codesFile = new File(filePath + File.separatorChar + Constants.CODE_FILE_NAME_AND_EXTENSION);
		long totalCount = 0;
		int zeroCount = 0;

		int[] count = new int[Constants.CHAR_SET_LENGTH];
		float[] percentage = new float[Constants.NOISE_PERCENTAGE > 0.0f ? Constants.LENGT_WHIT_NOISE : Constants.LENGTH_WITHOUT_NOISE];

		if (Constants.NOISE_PERCENTAGE > 0.0f) {
			percentage[Constants.NOISE_INDEX] = Constants.NOISE_PERCENTAGE;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(acfFile));
			String line = br.readLine();
			if (line != null) {
				String[] countPerByte = line.split(";");
				for (int i = 0; i < countPerByte.length; i++) {
					int value = Integer.parseInt(countPerByte[i]);
					count[i] += value;
					totalCount += value;
				}
			}
			br.close();
		} catch (FileNotFoundException e1) {
			Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, acfFile.getAbsolutePath());
			return false;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_READING_FILE, acfFile.getAbsolutePath());
			return false;
		}

		// create percentage
		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) {
				percentage[i] = (float) count[i] / totalCount;
			} else {
				percentage[i] = 0.0f;
				zeroCount++;
			}
		}

		// if Constants.NOISE_PERCENTAGE > 0, update new percentage
		if (Constants.NOISE_PERCENTAGE > 0.0f) {
			for (int i = 0; i < percentage.length; i++) {
				percentage[i] = percentage[i] / (1.0f + Constants.NOISE_PERCENTAGE);
			}
		}

		Message.printMessage(InfoMessagesEnum.CREATING_CODES_FILE);
		try {
			FileWriter fstream = new FileWriter(codesFile, false);
			codeListFileOut = new BufferedWriter(fstream);
			createCodes(""); // Start with a seed with lenght 0
			codeListFileOut.close();
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, codesFile.getAbsolutePath());
			return false;
		}

		long realAmount = codesCount - zeroCount;

		List<AmountPerChar> amountPerCharList = new ArrayList<AmountPerChar>();

		for (int i = 0; i < percentage.length; i++) {
			if (percentage[i] == 0.0f) {
				amountPerCharList.add(new AmountPerChar((i), 1));
			} else {
				long newAmount = (long) Math.floor(percentage[i] * realAmount);
				amountPerCharList.add(new AmountPerChar((i), newAmount));
			}
		}

		Message.printMessage(InfoMessagesEnum.CREATING_CODES_FOLDER);
		
		File theDir = new File(Constants.MAIN_FOLDER_NAME);
		if (!theDir.exists()) {
			if (!theDir.mkdir()) {
				Message.printMessage(StatusMessageEnum.ERROR_CREATING_FOLDER, theDir.getAbsolutePath());
				return false;
			}
		}

		theDir = new File(Constants.ENCODER_FOLDER_NAME);
		if (!theDir.exists()) {
			if (!theDir.mkdir()) {
				Message.printMessage(StatusMessageEnum.ERROR_CREATING_FOLDER, theDir.getAbsolutePath());
				return false;
			}
		}

		theDir = new File(Constants.DECODER_FOLDER_NAME);
		if (!theDir.exists()) {
			if (!theDir.mkdir()) {
				Message.printMessage(StatusMessageEnum.ERROR_CREATING_FOLDER, theDir.getAbsolutePath());
				return false;
			}
		}

		int lastFolder = Constants.NOISE_PERCENTAGE > 0.0f ? Constants.LENGT_WHIT_NOISE : Constants.LENGTH_WITHOUT_NOISE;
		long[] countArray = new long[lastFolder];

		if (Constants.NOISE_PERCENTAGE > 0.0f) {
			Message.printMessage(InfoMessagesEnum.GENERATING_NOISE_DATA);

			File tempFile = new File(Constants.ENCODER_FOLDER_NAME + Constants.NOISE_PERCENTAGE_FILE_NAME);
			try {
				FileWriter fstream = new FileWriter(tempFile, false);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("" + Constants.NOISE_PERCENTAGE);
				out.close();
			} catch (FileNotFoundException e1) {
				Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, tempFile.getAbsolutePath());
				return false;
			} catch (IOException e) {
				Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, tempFile.getAbsolutePath());
				return false;
			}
		}

		Message.printMessage(InfoMessagesEnum.GENERATING_RANDOM_CODES_PER_CHAR);

		try {
			BufferedReader br = new BufferedReader(new FileReader(codesFile));
			String line;
			while ((line = br.readLine()) != null) {
				if (amountPerCharList.size() > 0) {
					int index = amountPerCharList.size() == 1 ? 0 : (int) (Math.random() * (amountPerCharList.size()));
					AmountPerChar apc = amountPerCharList.get(index);
					int id = apc.getId();

					writeFiles(id, line);

					apc.setAmount(apc.getAmount() - 1);
					if (apc.getAmount() <= 0) {
						amountPerCharList.remove(apc);
					}

					countArray[id]++;

				} else {
					int index = (int) (Math.random() * (Constants.NOISE_PERCENTAGE > 0.0f ? Constants.LENGT_WHIT_NOISE : Constants.LENGTH_WITHOUT_NOISE));

					if (!writeFiles(index, line)) {
						br.close();
						return false;
					}

					countArray[index]++;
				}
			}
			br.close();
		} catch (FileNotFoundException e1) {
			Message.printMessage(StatusMessageEnum.ERROR_FILE_NOT_FOUND, codesFile.getAbsolutePath());
			return false;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_READING_FILE, codesFile.getAbsolutePath());
			return false;
		}

		Message.printMessage(InfoMessagesEnum.GENERATING_SIZE_FILES);

		String tempFilePath = Constants.ENCODER_FOLDER_NAME + Constants.SIZE_FILE_NAME;
		File tempFile = new File(tempFilePath);
		try {

			FileWriter fstream = new FileWriter(tempFile, true);
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < countArray.length; i++) {
				out.write(countArray[i] + (i == countArray.length - 1 ? "" : ";"));
			}
			out.close();

			tempFilePath = Constants.DECODER_FOLDER_NAME + Constants.SIZE_FILE_NAME;
			tempFile = new File(tempFilePath);
			fstream = new FileWriter(tempFile, false);
			out = new BufferedWriter(fstream);
			out.write("" + Constants.START_NUMBER);
			out.close();

		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, tempFile.getAbsolutePath());
			return false;
		}

		Message.printMessage(InfoMessagesEnum.CREATING_KEY_FILE);

		try {
			ZipCompressor.zipFolder((new File(Constants.MAIN_FOLDER_NAME)).getAbsolutePath(), (new File(outputFileName + "." + Constants.KEY_FILE_EXTENSION)).getAbsolutePath());
		} catch (Exception e) {
			Message.printMessage(StatusMessageEnum.ERROR_COMPRESS_FOLDER, (new File(Constants.MAIN_FOLDER_NAME)).getAbsolutePath());
			return false;
		}

		Message.printMessage(InfoMessagesEnum.DELETING_TEMP_FILES);

		File folder = new File(filePath);
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()
					&& (fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.SIMPLE_COUNT_FILE_EXTENSION)
							|| fileEntry.getName().substring(fileEntry.getName().length() - 3).equals(Constants.ALL_COUNT_FILE_EXTENSION) || fileEntry.getName()
							.substring(fileEntry.getName().length() - 3).equals(Constants.CODE_FILE_EXTENSION))) {

				if (!fileEntry.delete()) {
					Message.printMessage(StatusMessageEnum.ERROR_DELETING_FILE, fileEntry.getAbsolutePath());
				}
			}
		}

		Message.printMessage(InfoMessagesEnum.DELETING_TEMP_FOLDER);

		deleteFolder(new File(Constants.MAIN_FOLDER_NAME));

		return true;

	}

	private static void createCodes(String father) throws IOException {

		if (getLength(father) < Constants.START_NUMBER) {
			for (int i = 0; i < Constants.CHAR_SET_LENGTH; i++) {
				String newFather = getLength(father) == 0 ? i + "" : father + Constants.CHAR_SEPARATOR + i;
				createCodes(newFather);
			}
		} else if (getLength(father) >= Constants.END_NUMBER) {
			addCode(father);
		} else {
			float randomValue = (float) Math.random();
			if (randomValue <= Constants.GENERATOR_PERCENTAGE) {
				for (int i = 0; i < Constants.CHAR_SET_LENGTH; i++) {
					String newFather = getLength(father) == 0 ? i + "" : father + Constants.CHAR_SEPARATOR + i;
					createCodes(newFather);
				}
			} else {
				addCode(father);
			}
		}
	}

	private static int getLength(String father) {
		if (father.length() == 0)
			return 0;
		return father.split(Constants.CHAR_SEPARATOR).length;
	}

	private static void addCode(String code) throws IOException {
		codeListFileOut.write(code + "\n");
		codesCount++;
	}

	private static boolean writeFiles(int id, String line) {
		String tempFilePath = Constants.ENCODER_FOLDER_NAME + id;
		try {

			// create folder structure

			File tempFile = new File(tempFilePath);
			FileWriter fstream = new FileWriter(tempFile, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(line + "\n");
			out.close();

			String firstChar = line.split(Constants.CHAR_SEPARATOR)[0];
			tempFilePath = Constants.DECODER_FOLDER_NAME + firstChar;
			tempFile = new File(tempFilePath);
			fstream = new FileWriter(tempFile, true);
			out = new BufferedWriter(fstream);
			out.write(line + Constants.CODE_SEPARATOR + id + "\n");
			out.close();
			
			if(!firstCharTemp.equals(firstChar)){
				firstCharTemp = firstChar;
				int intChar = Integer.parseInt(firstCharTemp) + 1;
				
				Date now = new Date();
				long estimated = ((now.getTime() - beginDate.getTime()) * (Constants.CHAR_SET_LENGTH - intChar)) / intChar;

				long estimatedSecs = estimated / 1000 % 60;
				long estimatedMinutes = estimated / (60 * 1000) % 60;
				long estimatedHours = estimated / (60 * 60 * 1000);
				
				String timeString = estimatedHours + ":"
						+ (estimatedMinutes < 10 ? "0" + estimatedMinutes : estimatedMinutes) + ":" + (estimatedSecs < 10 ? "0" + estimatedSecs : estimatedSecs);

				
				Message.printMessage(InfoMessagesEnum.CURRENT_CODE, firstCharTemp, timeString);
			}
			
			
			return true;
		} catch (IOException e) {
			Message.printMessage(StatusMessageEnum.ERROR_WRITING_FILE, tempFilePath);
			return false;
		}
	}

	private static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					if (!f.delete()) {
						Message.printMessage(StatusMessageEnum.ERROR_DELETING_FILE, f.getAbsolutePath());
					}
				}
			}
		}
		if (!folder.delete()) {
			Message.printMessage(StatusMessageEnum.ERROR_DELETING_FOLDER, folder.getAbsolutePath());
		}
	}
}
