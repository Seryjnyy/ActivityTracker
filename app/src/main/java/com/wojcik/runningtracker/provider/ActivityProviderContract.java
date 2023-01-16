package com.wojcik.runningtracker.provider;

import android.net.Uri;

public class ActivityProviderContract {

    public static final String AUTHORITY = "com.wojcik.runningtracker.provider.ActivityContentProvider";

    public static final Uri ACTIVITY_URI = Uri.parse("content://"+AUTHORITY+"/activity");

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/ActivityContentProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/ActivityContentProvider.data.text";
}
