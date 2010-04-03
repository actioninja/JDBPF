package ssp.dbpf4j.properties;

/**
 * Defines a property of a DBPFType.<br>
 * 
 * For the available values, see DBPFProperties.
 * 
 * To create a new property:<br>
 * <ol>
 * <li>Get the nameValue from DBPFProperties</li>
 * <li>Get the dataType from DBPFProperties (later this will do automatically)</li>
 * <li>Create the right DBPFXXXProperty</li>
 * <li>Set the nameValue and dataType</li>
 * <li>Update the repSize for the wished size of values</li> 
 * <li>Set the value/s to the property</li>
 * </ol>
 * 
 * @author Stefan Wertich
 * @version 1.0.3, 06.02.2009
 * 
 */
public interface DBPFProperty {
	/**
	 * @param nameValue
	 *            The nameValue to set
	 */
	public void setNameValue(long nameValue);

	/**
	 * @return the nameValue
	 */
	public long getNameValue();
	
	/**
	 * @return the dataType
	 */
	public short getDataType();

	/**
	 * @param dataType
	 *            The dataType to set
	 */
	public void setDataType(short dataType);

	/**
	 * The repSize will be automatically updated!
	 * 
	 * @return The repSize
	 */
	public int getRepSize();

	
	/**
	 * Updates the repSize.<br>
	 * This will create a new array and copy elements from previous array if
	 * wished till the new size is reached.
	 * 
	 * @param repSize
	 *            The new repSize
	 * @param copy
	 *            TRUE, if copy from previous array; FALSE, otherwise
	 */
	public void updateRepSize(int repSize, boolean copy);
	
	/**
	 * Return the rep.<br>
	 * The rep will NOT be automatically updated!
	 * For writing determine this new!
	 * @return The rep
	 */
	public boolean isRep();
	
	/**
	 * @param rep The rep to set
	 */
	public void setRep(boolean rep);
}
