// IMyAidlInterface.aidl
package com.hhtc.deathrecipient;

import com.hhtc.deathrecipient.IMyRegisterInterface;
// Declare any non-default types here with import statements

interface IMyAidlInterface {

    void sendMsg(String msg);

    void registerLister(IMyRegisterInterface register);

    void unregisterLister(IMyRegisterInterface register);
}
