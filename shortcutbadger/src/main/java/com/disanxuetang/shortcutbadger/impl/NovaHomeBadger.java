package com.disanxuetang.shortcutbadger.impl;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.Arrays;
import java.util.List;

import com.disanxuetang.shortcutbadger.Badger;
import com.disanxuetang.shortcutbadger.ShortcutBadgeException;

/**
 * Shortcut Badger support for Nova Launcher.
 * TeslaUnread must be installed.
 * User: liukui 2018/04/18
 * Date: 2018/04/18
 */
public class NovaHomeBadger implements Badger {

    private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
    private static final String COUNT = "count";
    private static final String TAG = "tag";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG, componentName.getPackageName() + "/" + componentName.getClassName());
        contentValues.put(COUNT, badgeCount);
        context.getContentResolver().insert(Uri.parse(CONTENT_URI), contentValues);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList("com.teslacoilsw.launcher");
    }
}
