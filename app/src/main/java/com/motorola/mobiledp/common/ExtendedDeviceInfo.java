package com.motorola.mobiledp.common;

import android.R.string;

public class ExtendedDeviceInfo extends DeviceInfo {
	public string uuid;
	public string bootloaderVersion;
	public string l3BootloaderVersion;
	public string configurationVersion;
	public string kernelVersion;
	public string macAddress;
	public string macAddressPort1;
	public string macAddressPort2;
	public string macAddressWIFIPort1;
	public string macAddressWIFIPort2;
	public string psdtVersion;
	public byte region;
	public string paFirmwareVersion;
	public boolean dspEnabled;
	public boolean dtpEnabled;
	public string physicalSerialNumber;
}
