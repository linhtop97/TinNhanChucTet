package thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments;

import android.support.annotation.IntDef;

import static thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.FragmentType.LIST_MSG;
import static thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.FragmentType.MSG_DETAIL;
import static thiepchuctet.tinnhanchuctet.tetnguyendan.ui.fragments.FragmentType.MSG_LIB;


@IntDef({MSG_LIB, LIST_MSG, MSG_DETAIL})
public @interface FragmentType {
    int MSG_LIB = 0;
    int LIST_MSG = 1;
    int MSG_DETAIL = 2;
}
