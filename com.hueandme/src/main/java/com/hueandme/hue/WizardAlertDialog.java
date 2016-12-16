package com.hueandme.hue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hueandme.R;

public final class WizardAlertDialog {

    private static final WizardAlertDialog INSTANCE = new WizardAlertDialog();

    private ProgressDialog mProgressDialog;

    private WizardAlertDialog() {
    }

    public static WizardAlertDialog getInstance() {
        return INSTANCE;
    }

    /**
     * @param context      Activity context.
     * @param message      Message to show.
     * @param btnNameResId String resource id for button name.
     */
    public static void showErrorDialog(Context context, String message, int btnNameResId) {
        AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle(R.string.title_error)
                .setMessage(message)
                .setPositiveButton(btnNameResId, null)
                .create();

        if (!((Activity) context).isFinishing()) {
            alert.show();
        }
    }

    public static void closeProgressDialog() {
        if (getInstance().mProgressDialog != null) {
            getInstance().mProgressDialog.dismiss();
            getInstance().mProgressDialog = null;
        }
    }

    /**
     * @param resId   String resource for the message.
     * @param context Context.
     */
    public static void showProgressDialog(int resId, Context context) {
        String message = context.getString(resId);
        getInstance().mProgressDialog = ProgressDialog.show(context, null, message, true, false);
    }

    public static void showAuthenticationErrorDialog(final Activity activity, String message, int btnNameResId) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.title_error)
                .setPositiveButton(btnNameResId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .create();
        dialog.show();
    }
}
