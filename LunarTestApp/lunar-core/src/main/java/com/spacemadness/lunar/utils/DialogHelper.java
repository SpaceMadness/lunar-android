package com.spacemadness.lunar.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper
{
    public static void showDialog(Context context, String message, DialogButton positive)
    {
        showDialog(context, message, positive, (DialogButton)null);
    }

    public static void showDialog(Context context, String message, DialogButton positive, String negative)
    {
        showDialog(context, message, positive, new DumbDialogButton(negative));
    }

    public static void showDialog(Context context, String message, DialogButton positive, DialogButton negative)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        if (positive == null)
        {
            throw new NullPointerException("Positive button is null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // message
        builder.setMessage(message);

        // positive button
        builder.setPositiveButton(positive.getText(), positive);

        // negative button
        if (negative != null)
        {
            builder.setNegativeButton(negative.getText(), negative);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static abstract class DialogButton implements DialogInterface.OnClickListener
    {
        private final String text;

        public DialogButton(String text)
        {
            if (text == null)
            {
                throw new NullPointerException("Text is null");
            }

            this.text = text;
        }

        public String getText()
        {
            return text;
        }
    }

    private static class DumbDialogButton extends DialogButton
    {
        public DumbDialogButton(String text)
        {
            super(text);
        }

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
        }
    }
}
