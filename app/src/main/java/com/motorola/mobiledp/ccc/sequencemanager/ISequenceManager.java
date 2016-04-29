/**
 * 
 */
package com.motorola.mobiledp.ccc.sequencemanager;

import com.motorola.mobiledp.common.DeviceInfo;
import com.motorola.mobiledp.common.ExtendedDeviceInfo;
import com.motorola.mobiledp.common.LanguagePackData;
import com.motorola.mobiledp.common.PbaObject;

/**
 * @author WDGK73
 *
 */
public interface ISequenceManager {
	PbaObject readDevice();
	PbaObject writeDevice();
	void openSession(short id);
	void closeSession(short id, boolean pendingDeploy);
	DeviceInfo readDeviceInfo();
	ExtendedDeviceInfo readExtendedDeviceInfo();
	LanguagePackData readLanguagePacksInfo();
	void writeLanguagePacks(LanguagePackData data);
	void deleteLanguagePacks(LanguagePackData data);
}
