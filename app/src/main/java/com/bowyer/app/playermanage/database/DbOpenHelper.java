package com.bowyer.app.playermanage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bowyer.app.playermanage.database.dto.Player;

public class DbOpenHelper extends SQLiteOpenHelper {

  private static final int VERSION = 4;

  private static final String CREATE_PLAYER = ""
      + "CREATE TABLE "
      + Player.TABLE
      + "("
      + Player.ID
      + " INTEGER NOT NULL PRIMARY KEY,"
      + Player.SEX
      + " INTEGER NOT NULL DEFAULT 1,"
      + Player.FIRST_NAME
      + " TEXT,"
      + Player.LAST_NAME
      + " TEXT,"
      + Player.FIRST_NAME_PHONETIC
      + " TEXT,"
      + Player.LAST_NAME_PHONETIC
      + " TEXT,"
      + Player.MEMO
      + " TEXT,"
      + Player.RANK
      + " TEXT"
      + ")";

  public DbOpenHelper(Context context) {
    super(context, "player_manage.db", null /* factory */, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {

    db.execSQL(CREATE_PLAYER);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // do nothing
  }
}
