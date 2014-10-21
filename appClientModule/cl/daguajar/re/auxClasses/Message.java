package cl.daguajar.re.auxClasses;

public class Message {

	// status messages
	public static enum StatusMessageEnum implements MessagesEnum {
		OK_FILE_GENERATED, ERROR_WRITING_FILE, ERROR_FILE_NULL, ERROR_READING_FILE, ERROR_LOADING_PROPERTIES, ERROR_WRONG_COMMAND, ERROR_MISSING_PARAMETERS, ERROR_MISSING_PARAMETERS_GENERATE, ERROR_MISSING_PARAMETERS_DECODE, ERROR_MISSING_PARAMETERS_ENCODE, ERROR_FILE_PATH_NULL, ERROR_COMPRESS_FOLDER, ERROR_CREATING_FOLDER, ERROR_DELETING_FILE, ERROR_DELETING_FOLDER, ERROR_WRITING_OR_READING_FILE, ERROR_FILE_NOT_FOUND, ERROR_NO_FILES_IN_FOLDER, ERROR_ZIP_FILE_NOT_FOUND, ERROR_ZIP_READING_FILE, ERROR_IS_NOT_A_FILE, ERROR_ZIP_FILE_NULL
	}

	public static enum InfoMessagesEnum implements MessagesEnum {
		READING_FILES, CREATING_STATISTICAL_FILE, CREATING_CODES_FILE, CREATING_CODES_FOLDER, GENERATING_NOISE_DATA, GENERATING_RANDOM_CODES_PER_CHAR, GENERATING_SIZE_FILES, CREATING_KEY_FILE, DELETING_TEMP_FILES, DELETING_TEMP_FOLDER, PROGRESS, STEPS, CURRENT_CODE
	}

	public static void printMessage(MessagesEnum messagesEnum) {
		printMessage(messagesEnum, null, null);
	}

	public static void printMessage(MessagesEnum messagesEnum, String param1) {
		printMessage(messagesEnum, param1, null);
	}

	public static void printMessage(MessagesEnum messagesEnum, String param1, String param2) {

		if (messagesEnum == StatusMessageEnum.OK_FILE_GENERATED) {
			System.out.println("File generated" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_COMPRESS_FOLDER) {
			System.out.println("Error : Cant compress folder" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_CREATING_FOLDER) {
			System.out.println("Error : Cant create folder" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_DELETING_FILE) {
			System.out.println("Error : Cant delete file" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_DELETING_FOLDER) {
			System.out.println("Error : Cant delete folder" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_FILE_NULL) {
			System.out.println("Error : File is null" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_FILE_NOT_FOUND) {
			System.out.println("Error : File not found" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_FILE_PATH_NULL) {
			System.out.println("Error : File path is null" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_LOADING_PROPERTIES) {
			System.out.println("Error : Cant load properties" + (param1 != null ? " from file : \"" + param1 : "\"") + ". Default values will be used");
		} else if (messagesEnum == StatusMessageEnum.ERROR_MISSING_PARAMETERS || messagesEnum == StatusMessageEnum.ERROR_WRONG_COMMAND) {
			System.out.println("Format: java -jar <path_of_encriptor_jar_file> <command> <parameters>\n" + "commands: " + Constants.GENERATE + " - create a new key file\n" + "          " + Constants.ENCODE
					+ " - encode a file using a key file previously generated\n" + "          " + Constants.DECODE + " - decode a file which was previously encoded with a key file\n" + "parameters: "
					+ Constants.GENERATE + " <path_folder_with_files_to_read> <name_of_key_file>\n" + "            " + Constants.ENCODE + " <path_file_to_encode> <path_key_file> [<steps>]\n"
					+ "            " + Constants.DECODE + " <path_file_to_decode> <path_key_file> [<steps>]");
		} else if (messagesEnum == StatusMessageEnum.ERROR_MISSING_PARAMETERS_ENCODE) {
			System.out.println("Format: java -jar <path_of_encriptor_jar_file>  " + Constants.ENCODE + " <path_file_to_encode> <path_key_file> [<steps>]\n" 
					+ "parameters: <path_file_to_encode> - path where file to be encoded exists\n"
					+ "            <path_key_file> - path where key file exists\n"
					+ "            (optional) <steps> : Divide process in steps, and show current step and estimated time to finish it. This delay the process");
		} else if (messagesEnum == StatusMessageEnum.ERROR_MISSING_PARAMETERS_DECODE) {
			System.out.println("Format: java -jar <path_of_encriptor_jar_file>  " + Constants.DECODE + " <path_file_to_decode> <path_key_file> [<steps>]\n"
					+ "parameters: <path_file_to_decode> - path where encoded file exists\n"
					+ "            <path_key_file> - path where key file exists\n"
					+ "            (optional) <steps> : Divide process in steps, and show current step and estimated time to finish it. This delay the process");
		} else if (messagesEnum == StatusMessageEnum.ERROR_MISSING_PARAMETERS_GENERATE) {
			System.out.println("Format: java -jar <path_of_encriptor_jar_file>  " + Constants.GENERATE + " <path_folder_with_files_to_read> <name_of_key_file>\n"
					+ "parameters: <path_folder_with_files_to_read> - path of folder where files to be analyzed exists \n"
					+ "            <name_of_key_file> - name for the future key. The key will be created in the path where the jar is being executed");
		} else if (messagesEnum == StatusMessageEnum.ERROR_READING_FILE) {
			System.out.println("Error : Cant read file" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_WRITING_OR_READING_FILE) {
			System.out.println("Error : Cant read file" + (param1 != null ? " \"" + param1 : "\" " + "and/or cant write file" + param2 != null ? " \"" + param2 : "\""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_WRITING_FILE) {
			System.out.println("Error : Cant write file" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_NO_FILES_IN_FOLDER) {
			System.out.println("Error : No files in folder" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_ZIP_FILE_NOT_FOUND) {
		} else if (messagesEnum == StatusMessageEnum.ERROR_ZIP_READING_FILE) {
			System.out.println("Error : Cant read file" + (param1 != null ? " : " + param1 : "") + " in key file" + (param2 != null ? " : " + param2 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_ZIP_FILE_NULL) {
			System.out.println("Error : File null" + (param1 != null ? " : " + param1 : "") + " in key file" + (param2 != null ? " : " + param2 : ""));
		} else if (messagesEnum == StatusMessageEnum.ERROR_IS_NOT_A_FILE) {
			System.out.println("Error : This is not a file" + (param1 != null ? " : " + param1 : ""));
		} else if (messagesEnum == InfoMessagesEnum.CREATING_CODES_FILE) {
			System.out.println("Generating codes file");
		} else if (messagesEnum == InfoMessagesEnum.CREATING_CODES_FOLDER) {
			System.out.println("Generating codes folder");
		} else if (messagesEnum == InfoMessagesEnum.CREATING_KEY_FILE) {
			System.out.println("Creating key file");
		} else if (messagesEnum == InfoMessagesEnum.CREATING_STATISTICAL_FILE) {
			System.out.println("Generating statistical file from samples");
		} else if (messagesEnum == InfoMessagesEnum.DELETING_TEMP_FILES) {
			System.out.println("Erasing temp files");
		} else if (messagesEnum == InfoMessagesEnum.DELETING_TEMP_FOLDER) {
			System.out.println("Erasing temp Folder");
		} else if (messagesEnum == InfoMessagesEnum.GENERATING_NOISE_DATA) {
			System.out.println("Generating noise data");
		} else if (messagesEnum == InfoMessagesEnum.GENERATING_RANDOM_CODES_PER_CHAR) {
			System.out.println("Generating random data per char");
		} else if (messagesEnum == InfoMessagesEnum.GENERATING_SIZE_FILES) {
			System.out.println("Generating size files");
		} else if (messagesEnum == InfoMessagesEnum.READING_FILES) {
			System.out.println("Reading sample files");
		} else if (messagesEnum == InfoMessagesEnum.STEPS) { // if seeProgress is true
			System.out.println("Steps : " + Constants.STEPS + (param1 != null ? " - Date : " + param1 : ""));
		} else if (messagesEnum == InfoMessagesEnum.PROGRESS) { // if seeProgress is true
			System.out.println("Step  : " + (param1 != null ? param1 : "?") + " of " + Constants.STEPS + (param2 != null ? " - estimated time : " + param2 : ""));
		} else if (messagesEnum == InfoMessagesEnum.CURRENT_CODE) {
			System.out.println("Current code : " + (param1 != null ? param1 : "?") + " of " + (Constants.CHAR_SET_LENGTH - 1) + (param2 != null ? " - estimated time : " + param2 : ""));
		}
	}

	public interface MessagesEnum {
	};
}
