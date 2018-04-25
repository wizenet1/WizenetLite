package com.receivers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.Activities.IncomingCallScreenActivity;
import com.DatabaseHelper;

import java.lang.reflect.Method;
import java.util.Date;

import static com.Activities.MainActivity.ctx;

/**
 * Created by bitro on 4/23/2018.
 */

public class BusyMessageReceiver extends PhoneCallReceiver {

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        final Context context = ctx;
        final String msg = "Busy, call back later";
        final DatabaseHelper db = DatabaseHelper.getInstance(ctx);
        final String phoneNumber = number;

        Thread pageTimer = new Thread() {

            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    boolean isBusy = db.getValueByKey("IS_BUSY").equals("1");

                    // If the is_busy is checked, send sms.
                    if(isBusy) {

                        Uri uri = Uri.parse("smsto:" + phoneNumber);
                        try {

                            // Send the short message.
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);

                            // Close the incoming call.
                            String serviceManagerName = "android.os.ServiceManager";
                            String serviceManagerNativeName = "android.os.ServiceManagerNative";
                            String telephonyName = "com.android.internal.telephony.ITelephony";
                            Class<?> telephonyClass;
                            Class<?> telephonyStubClass;
                            Class<?> serviceManagerClass;
                            Class<?> serviceManagerNativeClass;
                            Method telephonyEndCall;
                            Object telephonyObject;
                            Object serviceManagerObject;
                            telephonyClass = Class.forName(telephonyName);
                            telephonyStubClass = telephonyClass.getClasses()[0];
                            serviceManagerClass = Class.forName(serviceManagerName);
                            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
                            Method getService = // getDefaults[29];
                                    serviceManagerClass.getMethod("getService", String.class);
                            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
                            Binder tmpBinder = new Binder();
                            tmpBinder.attachInterface(null, "fake");
                            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
                            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
                            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
                            telephonyObject = serviceMethod.invoke(null, retbinder);
                            telephonyEndCall = telephonyClass.getMethod("endCall");
                            telephonyEndCall.invoke(telephonyObject);

                        }
                        catch (Exception e){

                            e.printStackTrace();

                        }
                    }
                }
            }
        };

        pageTimer.start();
    }
}