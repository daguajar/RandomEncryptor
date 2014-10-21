package cl.daguajar.re.main;

import cl.daguajar.re.auxClasses.Constants;
import cl.daguajar.re.auxClasses.Message;
import cl.daguajar.re.auxClasses.Message.StatusMessageEnum;
import cl.daguajar.re.decoder.Decoder;
import cl.daguajar.re.encoder.Encoder;

public class Main {

	public static void main(String[] args) {

		// Load data
		(new DataLoader()).loadData();

		if (args.length >= 1) {
			if (args[0].equals(Constants.GENERATE)) {
				if (args.length >= 3) {
					if (Generator.generate(args[1], args[2])) {
						Message.printMessage(StatusMessageEnum.OK_FILE_GENERATED, args[2] + "." + Constants.KEY_FILE_EXTENSION);
					}
				} else {
					Message.printMessage(StatusMessageEnum.ERROR_MISSING_PARAMETERS_GENERATE);
				}
			} else if (args[0].equals(Constants.ENCODE)) {
				if (args.length >= 3) {
					if (args.length >= 4) {
						Constants.SEE_PROGRESS = true;
						Constants.STEPS = Integer.parseInt(args[3]);
					}
					if (Encoder.encode(args[1], args[2])) {
						Message.printMessage(StatusMessageEnum.OK_FILE_GENERATED, args[1] + "." + Constants.ENCODED_FILE_EXTENSION);
					}
				} else {
					Message.printMessage(StatusMessageEnum.ERROR_MISSING_PARAMETERS_ENCODE);
				}
			} else if (args[0].equals(Constants.DECODE)) {
				if (args.length >= 3) {
					if (args.length >= 4) {
						Constants.SEE_PROGRESS = true;
						Constants.STEPS = Integer.parseInt(args[3]);
					}
					if (Decoder.decode(args[1], args[2])) {
						Message.printMessage(StatusMessageEnum.OK_FILE_GENERATED, args[1] + "." + Constants.DECODED_FILE_EXTENSION);
					}
				} else {
					Message.printMessage(StatusMessageEnum.ERROR_MISSING_PARAMETERS_DECODE);
				}
			} else {
				Message.printMessage(StatusMessageEnum.ERROR_WRONG_COMMAND);
			}
		} else {
			Message.printMessage(StatusMessageEnum.ERROR_MISSING_PARAMETERS);
		}
	}
}