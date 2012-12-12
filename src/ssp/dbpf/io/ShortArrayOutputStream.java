package ssp.dbpf.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class implements an output stream in which the data is written into a
 * short array.<br>
 * <br>
 * The buffer automatically grows as data is written to it. The data can be
 * retrieved using toShortArray(). Closing a ShortArrayOutputStream has no
 * effect. The methods in this class can be called after the stream has been
 * closed without generating an IOException.
 * 
 * @author Stefan Wertich
 * @version 2.0.0, 14.08.2012
 * 
 */
public class ShortArrayOutputStream extends OutputStream {

	private int increaseSize = 100;

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
	public ShortArrayOutputStream(int size) {
		this(size, 100);
	}

	/**
	 * Constructor.<br>
	 * The size should be the maximum size of the data to write. If the pointer
	 * has reached the length of the array, the array will be exceed by
	 * <code>increaseSize</code> elements and copied. This may take some time,
	 * so prevent this will happen by choosing the best size for the
	 * constructor.
	 * 
	 * @param size
	 *            The size of the stream to write
	 * @param increaseSize
	 *            The size to increase the array
	 */
	public ShortArrayOutputStream(int size, int increaseSize) {
		data = new short[size];
		this.increaseSize = increaseSize;
		pointer = 0;
		length = 0;
	}

	/**
	 * Sets the increase size.<br>
	 * 
	 * @param increaseSize
	 *            The increase size
	 */
	public void setIncreaseSize(int increaseSize) {
		if (increaseSize > 0) {
			this.increaseSize = increaseSize;
		}
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
	 * Returns the length of the short array.<br>
	 * 
	 * This will not return the size of the written data!
	 * 
	 * @return The length
	 */
	public int getLength() {
		return data.length;
	}

	/**
	 * Returns the data.<br>
	 * Cause the internal array could be larger than the written data, this will
	 * copy the internal data to an new array with the needed length.
	 * 
	 * @return The data
	 */
	public short[] toShortArray() {
		short[] dataN = new short[length];
		System.arraycopy(data, 0, dataN, 0, length);
		return dataN;
	}

	/**
	 * Will do nothing.<br>
	 */
	@Override
	public void close() throws IOException {
		// do nothing
	}

	@Override
	public void write(int b) throws IOException {
		if (pointer == data.length) {
			short[] dataN = new short[data.length + increaseSize];
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
			length = Math.max(pointer, length);
		}
	}

}
