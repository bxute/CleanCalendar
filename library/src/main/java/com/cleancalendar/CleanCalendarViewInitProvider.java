/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class CleanCalendarViewInitProvider extends ContentProvider {
  public static final String Authority = "com.cleancalendar.viewinitprovider";
  @Override
  public boolean onCreate() {
    AndroidThreeTen.init(getContext());
    return true;
  }

  @Override
  public void attachInfo(Context context, ProviderInfo info) {
    if(info == null){
      throw  new NullPointerException("CleanCalendarInitProvider cannot be null");
    }

    if(Authority.equals(info.authority)){
      throw new IllegalStateException(
       "Incorrect provider authority in manifest. Most likely due to a "
        + "missing applicationId variable in application\'s build.gradle.");

    }
    super.attachInfo(context, info);
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri,
                      @Nullable String[] projection,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs,
                      @Nullable String sortOrder) {
    return null;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri,
                    @Nullable ContentValues values) {
    return null;
  }

  @Override
  public int delete(@NonNull Uri uri,
                    @Nullable String selection,
                    @Nullable String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(@NonNull Uri uri,
                    @Nullable ContentValues values,
                    @Nullable String selection,
                    @Nullable String[] selectionArgs) {
    return 0;
  }
}
