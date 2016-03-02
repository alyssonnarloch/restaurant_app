package com.app.narlocks.helper;

import java.text.NumberFormat;

public class Extras {

    public static String brFormat(double value) {
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
        return " " + moneyFormat.format(value);
    }

}
