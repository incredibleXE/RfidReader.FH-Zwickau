package uhfReader;

import java.util.List;

import model.Databean;

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
	private Databean databean = null;
	private java.text.SimpleDateFormat sdf = null;
	/**
	 * Constructor
	 * @param reader verbundener UHFReader 
	 */
	public UhfReaderThread(UHFReader reader, Databean databean) {
		this.reader = reader;
		this.databean = databean;
		sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
			databean.println(e.getClass().getSimpleName() + ": "
					+ e.getMessage());
			databean.printlnError("trying to reconnect ...");
			try {
				reader.connect();
				printInventory(reader);
			} catch (RFIDReaderException | CommConnectionException e1) {
				databean.println(e1.getClass().getSimpleName() + ": "
						+ e1.getMessage());
				databean.printlnError("Reader not reachable.");
			}
		} finally {
			// Disconnect connected reader
			try {
				reader.disconnect();
			} catch (CommConnectionException e) {
				databean.println(sdf.format(new java.util.Date())+" Error disconnect reader - "
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
		databean.println(sdf.format(new java.util.Date()));
		databean.println("Search for uhf tags with reader "+reader.getSerialNumber()+":");
		List<String> inventory = reader.getInventory();
		while (inventory.isEmpty()) {
			inventory = reader.getInventory();
		}
		for (String tagEPC : inventory) {
			databean.println(sdf.format(new java.util.Date())+"\tFound Tag with EPC " + tagEPC);
		}
	}
}
