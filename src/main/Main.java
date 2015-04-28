package main;

import exceptions.NoReaderFoundException;
import model.Databean;
import uhfReader.IniUhfReader;

public class Main {

	public static void main(String[] args) {
		Databean databean = new Databean();
		try {
			new IniUhfReader(databean).initReader();
		} catch (NoReaderFoundException e) {
			databean.printlnError(e.getMessage());
		}	
	}

}
