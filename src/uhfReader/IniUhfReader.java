package uhfReader;

import gnu.io.SerialPort;

import java.util.ArrayList;

import jd2xx.JD2XX.DeviceInfo;

import com.metratec.lib.connection.CommConnectionException;
import com.metratec.lib.connection.RS232Connection;
import com.metratec.lib.connection.USBConnection;
import com.metratec.lib.rfidreader.UHFReader;
import com.metratec.lib.rfidreader.UHFReader.READER_MODE;

/**
 * Initialisiert die UHF Reader und öffnet für jeden Reader einen eigenen Thread
 * 
 * @author Fabian Ranacher
 *
 */
public class IniUhfReader {
	
	/**
	 * constructor
	 */
	public IniUhfReader() {
		
	}
	
	/**
	 * Initialisierungsmethode 
	 * schaut erst an den USB Ports {@link #getConnectedUSBUHFReader()}, falls keine Geräte gefunden werden, 
	 * schaut es an die seriellen Ports {@link #getConnectedSerialUHFReader()}
	 * 
	 * Für jeden gefundene UHFReader wird ein Objekt @see UhfReaderThread.class innerhalb eines eigenen Threads 
	 * erstellt. Danach wird der Thread gestartet.
	 */
	public void initReader() {
		System.out.println("init start");
		System.out.println("Check USB");
		ArrayList<UHFReader> readerList = getConnectedUSBUHFReader();
		if (readerList.isEmpty()) {
			System.out.println("Check Serial");
			readerList = getConnectedSerialUHFReader();
			if (readerList.isEmpty()) {
				System.out.println("No reader found. Program stopped");
				return;
			}
		}
		
		for(UHFReader reader : readerList) {
			new Thread(new UhfReaderThread(reader)).start();
		}
		
		System.out.println("init finished");
	}

	/**
	 * Überprüft alle USB Geräte nach einem UHF Reader.
	 * Alle gefundenen Reader werden in einer ArrayList gespeichert.
	 * ACHTUNG! Bisher nur unter Windows getestet da es unter Linux nicht funktioniert.
	 * @return ArrayList<UHFReader> Liste mit gefundenen UHFReadern
	 */
	private ArrayList<UHFReader> getConnectedUSBUHFReader() {
		ArrayList<UHFReader> uhfReaderList = new ArrayList<UHFReader>();
		try {
			System.out.println("\tSearch for connected usb uhf reader devices");
			for (DeviceInfo info : USBConnection.getUSBDevices()) {
				System.out.println("\tCheck " + info.serial + " "
						+ info.description);
				UHFReader reader = new UHFReader(new USBConnection(info),
						READER_MODE.ETS);
				try {
					reader.connect();
					System.out.println("\tUsing " + info.serial + " "
							+ info.description);
					uhfReaderList.add(reader);
				} catch (Exception e) {
					System.out.println("\tSkip usb device " + info.description
							+ ": " + e.getClass().getSimpleName() + " "
							+ e.getMessage());
					try {
						reader.disconnect();
					} catch (Exception e1) {
					}
				}
			}
			return uhfReaderList;
		} catch (CommConnectionException e) {
			System.out.println("\tUSB Error " + e.getErrorDescription());
		}
		return null;
	}

	/**
	 * Überprüft alle seriellen Geräte nach einem UHF Reader.
	 * Alle gefundenen Reader werden in einer ArrayList gespeichert.
	 * ACHTUNG! Bisher nur unter Linux getestet (in früherer Version) da unter Windows {@link #getConnectedUSBUHFReader()} funktioniert.
	 * @return ArrayList<UHFReader> Liste mit gefundenen UHFReadern
	 */
	private ArrayList<UHFReader> getConnectedSerialUHFReader() {
		ArrayList<UHFReader> uhfReaderList = new ArrayList<UHFReader>();
		try {
			System.out.println("\tSearch for connected usb uhf reader devices");
			for (String port : RS232Connection.getSerialPorts()) {
				System.out.println("\tCheck " + port);
				UHFReader reader = new UHFReader(new RS232Connection(port,
						115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE, SerialPort.FLOWCONTROL_NONE),
						READER_MODE.ETS);
				try {
					reader.connect();
					System.out.println("\tUsing " + port);
					uhfReaderList.add(reader);
				} catch (Exception e) {
					System.out.println("\tSkip serial device " + port + ": "
							+ e.getClass().getSimpleName() + " "
							+ e.getMessage());
					try {
						reader.disconnect();
					} catch (Exception e1) {
					}
				}
			}
			return uhfReaderList;
		} catch (CommConnectionException e) {
			System.out.println("\tUSB Error " + e.getErrorDescription());
		}
		return null;
	}
}
