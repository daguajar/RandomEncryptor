package cl.daguajar.re.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;

public class ZipReader {

	private static ZipFile zipFile = null;

	
	public static InputStream getStreamFromFileInZip(String pathInside, String zipFilePath) throws IOException {
		InputStream is = null;
			zipFile = new ZipFile(zipFilePath);
			ZipEntry entry = zipFile.getEntry(pathInside);
			if (entry != null) {
				 is = zipFile.getInputStream(entry);
			} else {
				Message.printMessage(StatusMessageEnum.ERROR_ZIP_FILE_NULL, pathInside, zipFilePath);
			}
		return is;
	}
	
	public static void closeZipFile() throws IOException{
		if(zipFile!=null){
			zipFile.close();
			zipFile = null;
		}
	}

	public static boolean existFileInZip(String pathInside, String zipFilePath) throws IOException {
		boolean toReturn;
		ZipFile zipFile = new ZipFile(zipFilePath);
		ZipEntry entry = zipFile.getEntry(pathInside);
		if (entry != null) {
			toReturn =  true;
		} else {
			toReturn =  false;
		}
		zipFile.close();
		return toReturn;
		
	}
	
}
