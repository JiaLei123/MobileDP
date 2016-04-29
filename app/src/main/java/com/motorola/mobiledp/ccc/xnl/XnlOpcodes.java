/**
 * 
 */
package com.motorola.mobiledp.ccc.xnl;

/**
 * @author WDGK73
 *
 */
public enum XnlOpcodes {
	Reserved((short)0x0000),
	MasterPresentBroadcast((short)0x0001),
	MasterStatusBroadcast((short)0x0002),
	DeviceMasterQuery((short)0x0003),
	DeviceAuthKeyRequest((short)0x0004),
	DeviceAuthKeyReply((short)0x0005),
	DeviceConnectRequest((short)0x0006),
	DeviceConnectReply((short)0x0007),
	DeviceSysMapRequest((short)0x0008),
	DeviceSysMapBroadcast((short)0x0009),
	DeviceResetMessage((short)0x000A),
	DataMessage((short)0x000B),
	DataMessageAck((short)0x000C);
	
	private short value;
	private XnlOpcodes(short val)
	{
		this.value = val;
	}
	
	public short getValue()
	{
		return this.value;
	}
	
	public static XnlOpcodes valueOf(short val)
	{
		switch (val) {
		case 0:
			return Reserved;
		case 1:
			return MasterPresentBroadcast;
		case 2:
			return MasterStatusBroadcast;
		case 3:
			return DeviceMasterQuery;
		case 4:
			return DeviceAuthKeyRequest;
		case 5:
			return DeviceAuthKeyReply;
		case 6:
			return DeviceConnectRequest;
		case 7:
			return DeviceConnectReply;
		case 8:
			return DeviceSysMapRequest;
		case 9:
			return DeviceSysMapBroadcast;
		case 10:
			return DeviceResetMessage;
		case 11:
			return DataMessage;
		case 12:
			return DataMessageAck;
		default:
			throw new IllegalArgumentException(String.format("{0} is not a valid value of Enum XnlOpcodes", val));
		}
	}
}
