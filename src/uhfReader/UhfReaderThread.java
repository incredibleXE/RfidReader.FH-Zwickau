package uhfReader;

import java.util.List;

import com.metratec.lib.connection.CommConnectionException;
import com.metratec.lib.rfidreader.RFIDReaderException;
import com.metratec.lib.rfidreader.UHFReader;

/**
 * sucht mit gegebenem UHFReader nach EPCtags 
 * @author Fabian Ranacher
 * 
 */
public class UhfReaderThread implements Runnable
{
	private UHFReader reader = null;
	
	/**
	 * Constructor
	 * @param reader verbundener UHFReader 
	 */
	public UhfReaderThread(UHFReader reader) {
		this.reader = reader;
	}
	
	/**
	 * run Methode für Thread
	 * fängt RFIDReaderException und CommConnectionException ab.
	 * 
	 * Beendet am Schluss die Verbindung zum Reader. 
	 */
	@Override
	public void run() {
		try {

			printInventory(reader);

		} catch (RFIDReaderException | CommConnectionException e) {
			System.out.println(e.getClass().getSimpleName() + ": "
					+ e.getMessage());
		} finally {
			// Disconnect connected reader
			try {
				reader.disconnect();
			} catch (CommConnectionException e) {
				System.out.println("Error disconnect reader - "
						+ e.getClass().getSimpleName() + ": " + e.getMessage());
			}
		}		
	}
	
	/**
	 * Versucht vom Gerät gelesenen Tag zu bekommen und gibt ihn dann in der Konsole aus
	 * @param reader verbundener UHFReader
	 * @throws RFIDReaderException
	 * @throws CommConnectionException
	 */
	private void printInventory(UHFReader reader) throws RFIDReaderException, CommConnectionException {
		System.out.println("Search for uhf tags with reader "+reader.getSerialNumber()+":");
		List<String> inventory = reader.getInventory();
		while (inventory.isEmpty()) {
			inventory = reader.getInventory();
		}
		for (String tagEPC : inventory) {
			System.out.println("\tFound Tag with EPC " + tagEPC);
		}
	}
}
