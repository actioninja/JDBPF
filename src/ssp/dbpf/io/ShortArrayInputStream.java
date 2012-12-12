package ssp.dbpf.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * A ShortArrayInputStream contains an internal buffer that contains shorts that
 * may be read from the stream.<br>
 * <br>
 * An internal counter keeps track of the next short to be supplied by the read
 * method. Closing a ShortArrayInputStream has no effect. The methods in this
 * class can be called after the stream has been closed without generating an
 * IOException.
 * 
 * @author Stefan Wertich
 * @version 2.0.0, 14.08.2012
 * 
 */
public class ShortArrayInputStream extends InputStream {

	private short[] data;
	private int pointer = 0;

	/**
	 * Constructor<br>
	 * 
	 * Initialize the internal data with NULL.
	 */
	public ShortArrayInputStream() {
		this(null);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param data
	 *            The input buffer
	 */
	public ShortArrayInputStream(short[] data) {
		setShortArray(data);
	}

	/**
	 * Sets the data to read shorts from.<br>
	 * 
	 * The pointer will automatically be resetted ({@link #resetPointer()}).
	 * 
	 * @param data
	 *            The data
	 */
	public void setShortArray(short[] data) {
		this.data = data;
		resetPointer();
	}

	/**
	 * Returns the length of the short array.<br>
	 * If data was not set yet, this will return -1!
	 * 
	 * @return The length or -1, if no data set
	 */
	public int getLength() {
		if (data != null) {
			return data.length;
		}
		return -1;
	}

	/**
	 * This will reset the pointer to zero.<br>
	 */
	public void resetPointer() {
		this.pointer = 0;
	}

	/**
	 * Returns the actual pointer position.<br>
	 * 
	 * @return The pointer position
	 */
	public int getPointer() {
		return pointer;
	}

	/**
	 * Will do nothing.<br>
	 */
	@Override
	public void close() throws IOException {
		// do nothing
	}

	/**
	 * Returns shorts from the array for the actual pointer.<br>
	 * 
	 * <ul>
	 * <li>If the data is NULL, this will always return -1.</li>
	 * <li>If the pointer has reached the end, this will always return -1.<br>
	 * To read it again, use reset.</li>
	 * <li>Else it returns the element of the data at the pointer position and
	 * increase the pointer.</li>
	 * </ul>
	 * 
	 * @return The data
	 * @throws IOException
	 *             If any error occur
	 */
	public int read() throws IOException {
		int retValue = -1;
		if (data != null) {
			if (pointer != data.length) {
				retValue = data[pointer];
				pointer++;
			}
		}
		return retValue;
	}

}
