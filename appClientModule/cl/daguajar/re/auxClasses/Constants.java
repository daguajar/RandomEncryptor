package cl.daguajar.re.auxClasses;

import java.io.File;

public class Constants {

	
	//Commands
	public static final String GENERATE 	= "generate";
	public static final String ENCODE 		= "encode";
	public static final String DECODE 		= "decode";
	
	
	//Folder
	public static final String MAIN_FOLDER_NAME 	= "codes";
	public static final String ENCODER_FOLDER_NAME 	= MAIN_FOLDER_NAME + File.separatorChar + "encode" + File.separatorChar;
	public static final String DECODER_FOLDER_NAME 	= MAIN_FOLDER_NAME + File.separatorChar + "decode" + File.separatorChar;
	public static final String NOISE_FOLDER_NAME 	= "256";

	//Files
	public static final String SIZE_FILE_NAME 						= "size";
	public static final String CODES_FILE_NAME 						= "codes";
	public static final String NOISE_PERCENTAGE_FILE_NAME			= "noisePercentage";
	public static final String ALL_COUNT_FILE_NAME_AND_EXTENSION 	= "file.acf";
	public static final String CODE_FILE_NAME_AND_EXTENSION 		= "codesFile.cdf";
		
	//Extensions
	public static final String SIMPLE_COUNT_FILE_EXTENSION 	= "scf";
	public static final String ALL_COUNT_FILE_EXTENSION 	= "acf";
	public static final String CODE_FILE_EXTENSION 			= "cdf";
	public static final String KEY_FILE_EXTENSION			= "rkf"; //random key file
	public static final String ENCODED_FILE_EXTENSION 		= "ref"; //random encoded file
	public static final String DECODED_FILE_EXTENSION 		= "drf"; //decoded random file
	
	//Values
	public static final int 	LENGTH_WITHOUT_NOISE 			= 256;
	public static final int 	LENGT_WHIT_NOISE 				= 257;
	public static final int 	NOISE_INDEX						= 256;
	public static final int		CHAR_SET_LENGTH					= 256;
	public static final String 	CHAR_SEPARATOR					= "-";
	public static final String 	CODE_SEPARATOR					= ":";
	public static final String 	COUNT_SEPARATOR					= ";";
	public static final int 	LOWEST_START_NUMBER_VALUE		= 1;
	public static final int 	LOWEST_END_NUMBER_VALUE			= 2;
	public static final float 	HIGHEST_NOISE_PERCENTAGE_VALUE	= 0.5f;
	
	//This data is overwritten from property file
	public static int	START_NUMBER 			= 1; 
	public static int 	END_NUMBER 				= 2; 
	public static float GENERATOR_PERCENTAGE 	= 0.5f; 
	public static float NOISE_PERCENTAGE 		= 0.2f; 
	
	//to see progress
	public static boolean 	SEE_PROGRESS 	= false;
	public static int 		STEPS			= 100;
	
}
