package com.tinnhantet.nhantin.hengio.utils;

import android.support.annotation.IntDef;

import static com.tinnhantet.nhantin.hengio.utils.TabType.PENDING;
import static com.tinnhantet.nhantin.hengio.utils.TabType.SENT;

@IntDef({PENDING, SENT})
public @interface TabType {
    int PENDING = 0;
    int SENT = 1;
}
