package ssp.dbpf4j.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This simple inputStream provides to read data from a given array.<br>
 * 
 * @author Stefan Wertich
 * @version 1.0.0, 30.01.2009
 * 
 */
public class ArrayInputStream extends InputStream {

	private short[] data;
	private int pointer = 0;

	/**
	 * Constructor<br>
	 * The vector will set to NULL and read will always return -1;
	 */
	public ArrayInputStream() {
		this(null);
	}

	/**
	 * Constructor.<br>
	 * 
	 * @param data
	 *            The size of the stream to write
	 */
	public ArrayInputStream(short[] data) {
		setData(data);
	}

	/**
	 * Sets the data.<br>
	 * The pointer will be {@link #resetPointer()}.
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(short[] data) {
		this.data = data;
		resetPointer();
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
	 * Returns data of the array for the actual pointer.<br>
	 * 
	 * <ul>
	 * <li>If the data is NULL, this will always return -1.</li>
	 * <li>If the pointer has reached the end, this will always return -1.<br>
	 * To read it again, use reset.</li>
	 * <li>Else it returns the element of the data at the pointers position and
	 * increase the pointer position.</li>
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
