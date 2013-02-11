package com.OsMoDroid;

interface IRemoteOsMoDroidService {

    int getVersion();

    int getBackwardCompatibleVersion();
	
	void Deactivate();
	
	void Activate();

	boolean isActive();

}