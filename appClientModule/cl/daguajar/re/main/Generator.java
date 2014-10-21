package cl.daguajar.re.main;

import cl.daguajar.re.acf.ACFGenerator;
import cl.daguajar.re.scf.SCFGenerator;
import cl.daguajar.re.tef.TEFGenerator;

public class Generator {

	public static boolean generate(String filePath, String outputFileName) {
		
		if(!SCFGenerator.generate(filePath)){
			return false;
		}
		if(!ACFGenerator.generate(filePath)){
			return false;
		}
		return TEFGenerator.generate(filePath, outputFileName);
	}
	
	
}
