/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

/**
 * @author WDGK73
 *
 */
public class ParameterWrapper<T> {
	private T value;
	
	public ParameterWrapper(T val)
	{
		this.value = val;
	}
	
	public T getValue()
	{
		return this.value;
	}
	
	public void setValue(T val)
	{
		this.value = val;
	}
}
