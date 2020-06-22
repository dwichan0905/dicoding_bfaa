package id.dwichan.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    private val TAG = SmsReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val bundle = intent.extras
        try {
            if (bundle != null) {
                val PdusObj = bundle.get("pdus") as Array<Any>
                for (aPdusObj in PdusObj) {
                    val currMessage = getIncomingMessage(aPdusObj, bundle)
                    val senderNum = currMessage.displayOriginatingAddress
                    val message = currMessage.displayMessageBody
                    Log.d(TAG, "sender: $senderNum; msg: $message")

                    val showSmsIntent = Intent(context, SmsReceiverActivity::class.java)
                    showSmsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNum)
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message)
                    context.startActivity(showSmsIntent)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception @ SmsReceiver: $e")
        }
    }

    private fun getIncomingMessage(anObject: Any, bundle: Bundle): SmsMessage {
        val currentSms: SmsMessage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            currentSms = SmsMessage.createFromPdu(anObject as ByteArray, format)
        } else currentSms = SmsMessage.createFromPdu(anObject as ByteArray)
        return currentSms
    }
}
