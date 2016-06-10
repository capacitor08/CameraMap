package jp.astra.cameramap.helper;

import jp.astra.cameramap.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * @category Helper
 * @author Lau
 */
public final class Notify extends Handler {

	private boolean isToast = false;
	private Context context = null;
	private String title = null;
	private String message = null;

	private String alertLabel = null;
	private DialogInterface.OnClickListener alertListener = null;

	private String[] promptLabels = null;
	private DialogInterface.OnClickListener[] promptListeners = null;

	private Notify(Context context, String title, String message, String alertLabel) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.alertLabel = alertLabel;
	}

	private Notify(Context context, String title, String message, boolean isToast) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.isToast = isToast;
	}

	private Notify(Context context, String title, String message, DialogInterface.OnClickListener listener, String alertLabel) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.alertListener = listener;
		this.alertLabel = alertLabel;
	}

	private Notify(Context context, String title, String message, DialogInterface.OnClickListener[] listeners, String[] promptLabels) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.promptListeners = listeners;
		this.promptLabels = promptLabels;
	}

    @Override
    public void handleMessage(Message msg) {

		try {
	    	if (this.isToast) {
	    		Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();

	    	} else {

		            AlertDialog alert = new AlertDialog.Builder(this.context).create();
		            alert.setMessage(this.message);
		            alert.setCancelable(true);

		            if (StringUtil.isEmpty(this.title)) {
		            	alert.setTitle(this.title);
		            }

		            if	(StringUtil.isEmpty(this.alertLabel)) {
		            	this.alertLabel = this.context.getString(R.string.alert_button_ok);
		            }

		            if (this.alertListener != null) {

		            	alert.setButton(DialogInterface.BUTTON_POSITIVE, this.alertLabel, this.alertListener);

		            } else if (this.promptListeners != null) {

		            	for (int i = 0; i < this.promptLabels.length; i++) {
		            		alert.setButton(DialogInterface.BUTTON_POSITIVE - i, this.promptLabels[i], this.promptListeners[i]);
						}

		            	if (this.promptLabels.length == 1) {
		            		alert.setButton(DialogInterface.BUTTON_NEGATIVE, this.context.getString(R.string.alert_button_cancel), this.cancelListener);
		            	}

		            } else {

		            	alert.setButton(DialogInterface.BUTTON_POSITIVE, this.alertLabel, this.cancelListener);

		            }

		            alert.show();
	    	}
		} catch (Throwable t) {}

        super.handleMessage(msg);
    }

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    };

    /*  TOAST  */
    public static void toast(Context context, String message) {
    	new Notify(context, null, message, true).sendEmptyMessage(0);
    }

    public static void toast(Context context, int messageId) {
        Notify.toast(context, context.getString(messageId));
    }

    public static void toast(Context context, String title, String message) {
    	new Notify(context, title, message, true).sendEmptyMessage(0);
    }

    public static void toast(Context context, int titleId, String message) {
        Notify.toast(context, context.getString(titleId), message);
    }

    public static void toast(Context context, String title, int messageId) {
        Notify.toast(context, title, context.getString(messageId));
    }

    public static void toast(Context context, int titleId, int messageId) {
        Notify.toast(context, context.getString(titleId), context.getString(messageId));
    }

    /*  ALERT - OK - CANCEL LISTENER  */
    public static void alert(Context context, String message) {
    	new Notify(context, null, message, null).sendEmptyMessage(0);
    }

    public static void alert(Context context, int id) {
        Notify.alert(context, context.getString(id));
    }

    public static void alert(Context context, String title, String message) {
    	new Notify(context, title, message, null).sendEmptyMessage(0);
    }

    public static void alert(Context context, int titleId, String message) {
        Notify.alert(context, context.getString(titleId), message);
    }

    public static void alert(Context context, String title, int messageId) {
        Notify.alert(context, title, context.getString(messageId));
    }

    public static void alert(Context context, int titleId, int messageId) {
        Notify.alert(context, context.getString(titleId), context.getString(messageId));
    }

    /*  ALERT - CUSTOM OK - CANCEL LISTENER  */
    public static void alertWithCustomButton(Context context, String message, String alertLabel) {
    	new Notify(context, null, message, alertLabel).sendEmptyMessage(0);
    }

    public static void alertWithCustomButton(Context context, int messageId, String alertLabel) {
        Notify.alertWithCustomButton(context, context.getString(messageId), alertLabel);
    }

    public static void alertWithCustomButton(Context context, String message, int alertLabelId) {
        Notify.alertWithCustomButton(context, message, context.getString(alertLabelId));
    }

    public static void alertWithCustomButton(Context context, int messageId, int alertLabelId) {
        Notify.alertWithCustomButton(context, context.getString(messageId), context.getString(alertLabelId));
    }

    /*  ALERT - OK - CUSTOM LISTENER  */
    public static void alertWithCustomListener(Context context, String title, String message, DialogInterface.OnClickListener listener) {
    	new Notify(context, title, message, listener, null).sendEmptyMessage(0);
    }

    public static void alertWithCustomListener(Context context, String message, DialogInterface.OnClickListener listener) {
    	Notify.alertWithCustomListener(context, null, message, listener);
    }

    public static void alertWithCustomListener(Context context, int messageId, DialogInterface.OnClickListener listener) {
    	Notify.alertWithCustomListener(context, null, context.getString(messageId), listener);
    }

    public static void alertWithCustomListener(Context context, int titleId, String message, DialogInterface.OnClickListener listener) {
    	Notify.alertWithCustomListener(context, context.getString(titleId), message, listener);
    }

    public static void alertWithCustomListener(Context context, String title, int messageId, DialogInterface.OnClickListener listener) {
    	Notify.alertWithCustomListener(context, title, context.getString(messageId), listener);
    }

    public static void alertWithCustomListener(Context context, int titleId, int messageId, DialogInterface.OnClickListener listener) {
    	Notify.alertWithCustomListener(context, context.getString(titleId), context.getString(messageId), listener);
    }

    /*  ALERT - CUSTOM OK - CUSTOM LISTENER  */
    public static void alertWithCustomButtonAndListener(Context context, String title, String message, DialogInterface.OnClickListener listener, String alertLabel) {
    	new Notify(context, title, message, listener, alertLabel).sendEmptyMessage(0);
    }

    public static void alertWithCustomButtonAndListener(Context context, String message, DialogInterface.OnClickListener listener, String alertLabel) {
    	Notify.alertWithCustomButtonAndListener(context, null, message, listener, alertLabel);
    }

    public static void alertWithCustomButtonAndListener(Context context, int messageId, DialogInterface.OnClickListener listener, String alertLabel) {
    	Notify.alertWithCustomButtonAndListener(context, null, context.getString(messageId), listener, alertLabel);
    }

    public static void alertWithCustomButtonAndListener(Context context, String message, DialogInterface.OnClickListener listener, int alertLabelId) {
    	Notify.alertWithCustomButtonAndListener(context, null, message, listener, context.getString(alertLabelId));
    }

    public static void alertWithCustomButtonAndListener(Context context, int messageId, DialogInterface.OnClickListener listener, int alertLabelId) {
    	Notify.alertWithCustomButtonAndListener(context, null, context.getString(messageId), listener, context.getString(alertLabelId));
    }

    public static void alertWithCustomButtonAndListener(Context context, String title, int messageId, DialogInterface.OnClickListener listener, String alertLabel) {
    	Notify.alertWithCustomButtonAndListener(context, title, context.getString(messageId), listener, alertLabel);
    }

    public static void alertWithCustomButtonAndListener(Context context, int titleId, String message, DialogInterface.OnClickListener listener, String alertLabel) {
    	Notify.alertWithCustomButtonAndListener(context, context.getString(titleId), message, listener, alertLabel);
    }

    public static void alertWithCustomButtonAndListener(Context context, int titleId, String message, DialogInterface.OnClickListener listener, int alertLabelId) {
    	Notify.alertWithCustomButtonAndListener(context, context.getString(titleId), message, listener, context.getString(alertLabelId));
    }

    public static void alertWithCustomButtonAndListener(Context context, String title, int messageId, DialogInterface.OnClickListener listener, int alertLabelId) {
    	Notify.alertWithCustomButtonAndListener(context,title, context.getString(messageId), listener, context.getString(alertLabelId));
    }

    public static void alertWithCustomButtonAndListener(Context context, int titleId, int messageId, DialogInterface.OnClickListener listener, String alertLabel) {
    	Notify.alertWithCustomButtonAndListener(context, context.getString(titleId), context.getString(messageId), listener, alertLabel);
    }

    public static void alertWithCustomButtonAndListener(Context context, int titleId, int messageId, DialogInterface.OnClickListener listener, int alertLabelId) {
    	Notify.alertWithCustomButtonAndListener(context, context.getString(titleId), context.getString(messageId), listener, context.getString(alertLabelId));
    }

    /*  PROMPT - THREE BUTTONS  */
    public static void prompt(Context context, String title, String message, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {

    	DialogInterface.OnClickListener promptListeners[] = {positiveListener, negativeListener, neutralListener};
    	String promptLabels[] = {positiveButton, negativeButton, neutralButton};

    	if (StringUtil.isEmpty(neutralButton)) {
    		if (StringUtil.isEmpty(negativeButton)) {
    			if (!StringUtil.isEmpty(positiveButton)) {
    				promptLabels = ArrayUtil.reduceArraySize(promptLabels, 2);
    				promptListeners = new DialogInterface.OnClickListener[] {positiveListener};
    			} else {
    				promptListeners = null;
    				promptLabels = null;
    			}
    		} else {
    			promptLabels = ArrayUtil.reduceArraySize(promptLabels, 1);
    			promptListeners = new DialogInterface.OnClickListener[] {positiveListener, negativeListener};
    		}
    	}

    	new Notify(context, title, message, promptListeners, promptLabels).sendEmptyMessage(0);
    }

    public static void prompt(Context context, int titleId, String message, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, context.getString(titleId), message, positiveButton, negativeButton, neutralButton, positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, String title, int messageId, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, title, context.getString(messageId), positiveButton, negativeButton, neutralButton, positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, String title, String message, int positiveButtonId, int negativeButtonId, int neutralButtonId,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, title, message, context.getString(positiveButtonId), context.getString(negativeButtonId), context.getString(neutralButtonId), positiveListener, negativeListener, neutralListener);

    }

    public static void prompt(Context context, int titleId, int messageId, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), positiveButton, negativeButton, neutralButton, positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, int titleId, int messageId, int positiveButtonId, int negativeButtonId, int neutralButtonId,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), context.getString(positiveButtonId), context.getString(negativeButtonId), context.getString(neutralButtonId), positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, String message, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, null, message, positiveButton, negativeButton, neutralButton, positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, int messageId, String positiveButton, String negativeButton, String neutralButton,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, null, context.getString(messageId), positiveButton, negativeButton, neutralButton, positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, String message, int positiveButtonId, int negativeButtonId, int neutralButtonId,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, null, message, context.getString(positiveButtonId), context.getString(negativeButtonId), context.getString(neutralButtonId), positiveListener, negativeListener, neutralListener);
    }

    public static void prompt(Context context, int messageId, int positiveButtonId, int negativeButtonId, int neutralButtonId,
    		DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
    	Notify.prompt(context, null, context.getString(messageId), context.getString(positiveButtonId), context.getString(negativeButtonId), context.getString(neutralButtonId), positiveListener, negativeListener, neutralListener);
    }

    /* PROMPT - CUSTOM OK CUSTOM CANCEL */
    public static void prompt(Context context, String title, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, title, message, positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int titleId, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, context.getString(titleId), message, positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, String title, int messageId, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, title, context.getString(messageId), positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, String title, String message, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, title, message, context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, String title, int messageId, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, title, context.getString(messageId), context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int titleId, String message, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, context.getString(titleId), message, context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int titleId, int messageId, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int titleId, int messageId, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, null, message, positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int messageId, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, null, context.getString(messageId), positiveButton, negativeButton, null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, String message, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, null, message, context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    public static void prompt(Context context, int messageId, int positiveButtonId, int negativeButtonId, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
    	Notify.prompt(context, null, context.getString(messageId), context.getString(positiveButtonId), context.getString(negativeButtonId), null, positiveListener, negativeListener, null);
    }

    /*  PROMPT - OK - CANCEL */
    public static void promptCancel(Context context, String title, String message, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, message, context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    public static void promptCancel(Context context, int titleId, String message, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, context.getString(titleId), message, context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    public static void promptCancel(Context context, String title, int messageId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, context.getString(messageId), context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    public static void promptCancel(Context context, int titleId, int messageId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    public static void promptCancel(Context context, String message, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, message, context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    public static void promptCancel(Context context, int messageId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, context.getString(messageId), context.getString(R.string.alert_button_ok), null, null, positiveListener, null, null);
    }

    /*  PROMPT - CUSTOM OK - CANCEL */
    public static void promptCancelWithCustomButton(Context context, String title, String message, String positiveButton, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, message, positiveButton, null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, int titleId, String message, String positiveButton, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, context.getString(titleId), message, positiveButton, null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, String title, int messageId, String positiveButton, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, context.getString(messageId), positiveButton, null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, String title, String message, int positiveButtonId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, message, context.getString(positiveButtonId), null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, String title, int messageId, int positiveButtonId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, title, context.getString(messageId), context.getString(positiveButtonId), null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, int titleId, int messageId, int positiveButtonId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, context.getString(titleId), context.getString(messageId), context.getString(positiveButtonId), null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, String message, String positiveButton, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, message, positiveButton, null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, int messageId, String positiveButton, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, context.getString(messageId), positiveButton, null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, String message, int positiveButtonId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, message, context.getString(positiveButtonId), null, null, positiveListener, null, null);
    }

    public static void promptCancelWithCustomButton(Context context, int messageId, int positiveButtonId, DialogInterface.OnClickListener positiveListener) {
    	Notify.prompt(context, null, context.getString(messageId), context.getString(positiveButtonId), null, null, positiveListener, null, null);
    }

    public static void alertError(Context context) {
    	Notify.alert(context, R.string.alert_fatal);
    }
}
