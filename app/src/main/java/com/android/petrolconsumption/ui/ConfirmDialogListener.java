package com.android.petrolconsumption.ui;

import com.android.petrolconsumption.commands.BaseCommand;

public interface ConfirmDialogListener {

    public void onYes(BaseCommand command);

    public void onNo(String why);
}
