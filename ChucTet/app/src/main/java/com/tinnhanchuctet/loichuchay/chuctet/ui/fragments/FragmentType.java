package com.tinnhanchuctet.loichuchay.chuctet.ui.fragments;

import android.support.annotation.IntDef;

import static com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.FragmentType.LIST_MSG;
import static com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.FragmentType.MSG_DETAIL;
import static com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.FragmentType.MSG_LIB;

@IntDef({MSG_LIB, LIST_MSG, MSG_DETAIL})
public @interface FragmentType {
    int MSG_LIB = 0;
    int LIST_MSG = 1;
    int MSG_DETAIL = 2;
}
