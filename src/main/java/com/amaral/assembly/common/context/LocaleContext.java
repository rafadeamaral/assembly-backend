package com.amaral.assembly.common.context;

import java.util.Locale;

public class LocaleContext {

    private static final Locale BRASIL = new Locale("pt", "BR");

    public static Locale getDefault() {
        return BRASIL;
    }

    public static Locale get() {
        return BRASIL;
    }

}
