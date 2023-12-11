package com.example.sumon.androidvolley;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * SendMailTask class that extends AsyncTask, this is the class that actually performs sending the email with
 * AsyncTask in the background while the app is running. Uses a non-main thread to perform this.
 * @author Zach
 */
public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;

    /**
     * Constructor for class that should always be used, keeps activity consistent between objects
     * @param activity Keeps the activity consistent between the where this object is created and the
     *                 activity stored in SendMailTask
     */
    public SendMailTask(Activity activity) {
        sendMailActivity = activity;

    }

    /**
     * Displays and sets parameters for ProgressDialog object
     */
    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    /**
     * Overrides the doInBackground method to call the methods to send the email, and publishes the
     * progress to the User
     * @param args The parameters of the task.
     *
     * @return
     */
    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GmailAuthenicationActivity androidEmail = new GmailAuthenicationActivity(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());
            publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            publishProgress("Sending email....");
            androidEmail.sendEmail();
            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Overriden method that sets the message of the ProgressDialog to the progressUpdate, keeping user informed
     * @param values The values indicating progress.
     *
     */
    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    /**
     * Overriden method that dismisses the ProgressDialog
     * @param result The result of the operation computed by {@link #doInBackground}.
     *
     */
    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }

}
