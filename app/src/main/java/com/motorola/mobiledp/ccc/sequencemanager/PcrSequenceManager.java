/**
 * 
 */
package com.motorola.mobiledp.ccc.sequencemanager;

import com.motorola.mobiledp.ccc.communicationpipe.ICommunicationPipe;
import com.motorola.mobiledp.common.DeviceInfo;
import com.motorola.mobiledp.common.ExtendedDeviceInfo;
import com.motorola.mobiledp.common.LanguagePackData;
import com.motorola.mobiledp.common.PbaObject;

/**
 * @author WDGK73
 *
 */
public class PcrSequenceManager implements ISequenceManager {

	
	
	public PcrSequenceManager(ICommunicationPipe pipe)
	{
		
	}
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#readDevice()
	 */
	@Override
	public PbaObject readDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#openSession(short)
	 */
	@Override
	public void openSession(short id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#closeSession(short, boolean)
	 */
	@Override
	public void closeSession(short id, boolean pendingDeploy) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#readDeviceInfo()
	 */
	@Override
	public DeviceInfo readDeviceInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#readExtendedDeviceInfo()
	 */
	@Override
	public ExtendedDeviceInfo readExtendedDeviceInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#readLanguagePacksInfo()
	 */
	@Override
	public LanguagePackData readLanguagePacksInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#writeLanguagePacks(com.motorola.mobiledp.common.LanguagePackData)
	 */
	@Override
	public void writeLanguagePacks(LanguagePackData data) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#deleteLanguagePacks(com.motorola.mobiledp.common.LanguagePackData)
	 */
	@Override
	public void deleteLanguagePacks(LanguagePackData data) {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see com.motorola.mobiledp.ccc.sequencemanager.ISequenceManager#writeDevice()
	 */
	@Override
	public PbaObject writeDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
