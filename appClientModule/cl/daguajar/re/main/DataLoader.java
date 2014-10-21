package cl.daguajar.re.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cl.daguajar.re.auxClasses.Constants;

public class DataLoader {

	public void loadData() {

		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/data.properties");
		Properties prop = new Properties();
		try {
			prop.load(inputStream);

			Constants.START_NUMBER 			= Integer.parseInt(prop.getProperty("startNumber"));
			Constants.END_NUMBER 			= Integer.parseInt(prop.getProperty("endNumber"));
			Constants.GENERATOR_PERCENTAGE 	= Float.parseFloat(prop.getProperty("generatorPercentage"));
			Constants.NOISE_PERCENTAGE 		= Float.parseFloat(prop.getProperty("noisePercentage"));

			//fixed values in order to avoid errors...
			Constants.NOISE_PERCENTAGE 	= Constants.NOISE_PERCENTAGE 	> Constants.HIGHEST_NOISE_PERCENTAGE_VALUE 	? Constants.HIGHEST_NOISE_PERCENTAGE_VALUE 	: Constants.NOISE_PERCENTAGE;
			Constants.START_NUMBER 		= Constants.START_NUMBER 		< Constants.LOWEST_START_NUMBER_VALUE 			? Constants.LOWEST_START_NUMBER_VALUE 			: Constants.START_NUMBER;
			Constants.END_NUMBER 		= Constants.END_NUMBER 			< Constants.LOWEST_END_NUMBER_VALUE 			? Constants.LOWEST_END_NUMBER_VALUE 			: Constants.END_NUMBER;
			Constants.END_NUMBER 		= Constants.END_NUMBER 			< Constants.START_NUMBER 					? Constants.START_NUMBER 					: Constants.END_NUMBER;
		} catch (IOException e) {
			System.out.println("Error loading properties. Values by default");
		}


		
		
	}
}
