package com.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.DatabaseHelper;
import com.Helper;

import java.util.Date;

/**
 * The class represents an abstract phone call broadcast receiver.
 * It provides the ability to handle different types of phone calls states.
 */
public abstract class PhoneCallReceiver extends BroadcastReceiver {

    //The receiver will be recreated when the OS will decide to call it.
    //The static variable will be used to remember data between instantiations.
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;

    //needed because the passed incoming is only valid in ringing.
    private static String savedNumber;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("mytag","-----------------reveiced here-----------------");
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call,
        //We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }
    }


    //Derived classes should override these to respond to specific events of interest.

    /**
     * Handles the event of a starting incoming call.
     * @param ctx context
     * @param number phone number
     * @param start starting date
     */
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    /**
     * Handles the event of a starting outgoing call.
     * @param ctx context
     * @param number phone number
     * @param start starting date
     */
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    /**
     * Handles the event of an ending incoming call.
     * @param ctx context
     * @param number phone number
     * @param start starting date
     */
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    /**
     * Handles the event of an ending outgoing call.
     * @param ctx context
     * @param number phone number
     * @param start starting date
     */
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    /**
     * Handles the event of a missed call.
     * @param ctx context
     * @param number phone number
     * @param start starting date
     */
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    //Deals with actual events

    /**
     * Changes call states in the following way:
     * Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up.
     * Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up.
     * @param context context
     * @param state state number
     * @param number phone number
     */
    public void onCallStateChanged(Context context, int state, String number) {

        /**
         * TODO if the state is not saved, add this code, and replace each lastState with lastStateTemp
         *  int lastStateTemp = lastState;
         *  lastState = state;
         */

        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                try{
                    String option = "";
                    option = DatabaseHelper.getInstance(context).getValueByKey("IS_BUSY_OPTION");
                    if (!option.contains("ללא")){
                        onIncomingCallStarted(context, number, callStartTime);
                    }
                }catch(Exception e){
                    Helper h = new Helper();
                    h.LogPrintExStackTrace(e);
                }


                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        //Ring but no pickup-  a miss
                        onMissedCall(context, savedNumber, callStartTime);
                    } else if (isIncoming) {
                        onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    } else {
                        onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    }

                //Went to idle-  this is the end of a call.  What type depends on previous state(s)

                break;
        }
        lastState = state;
    }
}

