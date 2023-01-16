package com.wojcik.runningtracker.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;

public class ActivityContentProvider extends ContentProvider {
    private Repository repo;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ActivityProviderContract.AUTHORITY, "activity", 1);
        uriMatcher.addURI(ActivityProviderContract.AUTHORITY, "activity/#", 2);
        uriMatcher.addURI(ActivityProviderContract.AUTHORITY, "*", 3);
    }

    @Override
    public boolean onCreate() {
        repo = new Repository(this.getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"id", "year", "month", "day", "distance", "time", "avgPace", "opinion", "weather", "type"});;
        switch (uriMatcher.match(uri)){
            case 1:
                int id;
                try{
                    id = Integer.parseInt(uri.getLastPathSegment());
                }catch(NumberFormatException e){
                    throw new IllegalArgumentException("ID needs to be a integer.");
                }
                matrixCursor.addRow(new ExerciseEntity[] {repo.getExerciseDirect(id)});
                return matrixCursor;
            case 2:
            case 3:
                matrixCursor.addRow(repo.getAllExercisesDirect().toArray());
                return matrixCursor;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = ActivityProviderContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = ActivityProviderContract.CONTENT_TYPE_SINGLE;
        }

        return contentType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not allowed.");
    }
}
