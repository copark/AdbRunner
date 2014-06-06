package com.cosooki.adb.item;

import java.util.Comparator;

public class TopItem extends BaseItem {
    private static final String TAG = TopItem.class.getSimpleName();

    public static final String FIELD_CPU = "_cpu";
    public static final String FIELD_VSS = "_vss";
    public static final String FIELD_RSS = "_rss";

    public class CPUCompare implements Comparator<BaseItem> {
        @Override
        public int compare(BaseItem arg0, BaseItem arg1) {
            return _compare(arg0, arg1, FIELD_CPU, true);
        }
    }
}
