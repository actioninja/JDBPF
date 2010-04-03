package ssp.dbpf4j.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This simple outputStream provides to write data to an array.<br>
 * 
 * @author Stefan Wertich
 * @version 1.0.0, 30.01.2009
 * 
 */
public class ArrayOutputStream extends OutputStream {

	private short[] data;
	private int pointer;
	private int length;

	/**
	 * Constructor.<br>
	 * The size should be the maximum size of the data to write. If the pointer
	 * has reached the length of the array, the array will be exceed by 100
	 * elements and copied. This may take some time, so prevent this will happen
	 * by choosing the best size for the constructor.
	 * 
	 * @param size
	 *            The size of the stream to write
	 */
	public ArrayOutputStream(int size) {
		data = new short[size];
		pointer = 0;
		length = 0;
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
	 * Returns teh data.<br>
	 * Cause the internal array could be larger than the written data, this will
	 * copy the internal data to an new array with the needed length.
	 * 
	 * @return The data
	 */
	public short[] getData() {
		short[] dataN = new short[length];
		System.arraycopy(data, 0, dataN, 0, length);
		return dataN;
	}

	@Override
	public void write(int b) throws IOException {
		if (pointer == data.length) {
			short[] dataN = new short[data.length + 100];
			System.arraycopy(data, 0, dataN, 0, data.length);
			data = dataN;
		}
		if (pointer < data.length) {
			// Cause of overflow from char add 256 to the int
			if (b < 0) {
				b += 256;
			}
			data[pointer] = (short) b;
			pointer++;
			length = pointer;
		}
	}

}
