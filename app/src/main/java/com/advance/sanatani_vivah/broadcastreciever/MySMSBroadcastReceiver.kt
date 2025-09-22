package com.advance.sanatani_vivah.broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.advance.sanatani_vivah.utility.AppDebugLog


/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
class MySMSBroadcastReceiver : BroadcastReceiver() {

    private var otpReceiver: OTPReceiveListener? = null

    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status?.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {
                    var message: String? = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?

                    // Get SMS message contents
                    var otp: String = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    AppDebugLog.print("OTP_Message : "+otp)
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server for SMS authenticity.
                    // But here we are just passing it to MainActivity
                    if (otpReceiver != null) {
                        otp = otp.replace("<#>Megamatrimony2.1: Your verification code is ", "").split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        otpReceiver!!.onOTPReceived(otp.split(" ")[0].trim())
                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }
        }
    }

    interface OTPReceiveListener {

        fun onOTPReceived(otp: String)

        fun onOTPTimeOut()
    }
}