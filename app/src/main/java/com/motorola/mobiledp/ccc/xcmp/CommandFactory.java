/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

import android.util.SparseArray;

/**
 * singleton implementation of XCMP command factory
 * @author WDGK73
 *
 */
public class CommandFactory {
	private static CommandFactory instance = new CommandFactory();
	private static SparseArray<Class<?>> cmdRepository = new SparseArray<Class<?>>();
	
	/**
	 * constructor
	 */
	private CommandFactory()
	{
		// add all <opcode, command.class> pair into cmdRepository
		cmdRepository.append(RadioReset.OPCODE, RadioReset.class);
		cmdRepository.append(ReadIshItem.OPCODE, ReadIshItem.class);
		cmdRepository.append(XcmpSuperBundle.OPCODE, XcmpSuperBundle.class);
	}
	
	/**
	 * get the instance of XCMP command factory
	 * @return XCMP command factory
	 */
	public static CommandFactory getInstance() { return instance; }
	
	/**
	 * create the XCMP command according to opcode
	 * @param opcode
	 * @return XCMP command
	 */
	public ICommand Create(int opcode)
	{
		Class<?> cmdClass = cmdRepository.get(opcode);
		if (cmdClass == null)
		{
			return null;
		}
		try {
			return (ICommand)cmdClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
