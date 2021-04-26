package com.bowyer.app.playermanage.database.dto;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;
import rx.functions.Func1;

@AutoValue public abstract class Player implements Parcelable {
  public static final String TABLE = "player";

  public static final String ID = "_id";
  public static final String FIRST_NAME = "first_name";
  public static final String LAST_NAME = "last_name";
  public static final String FIRST_NAME_PHONETIC = "first_name_phonetic";
  public static final String LAST_NAME_PHONETIC = "last_name_phonetic";
  public static final String SEX = "sex";
  public static final String MEMO = "memo";
  public static final String RANK = "rank";

  @ColumnName(ID) public abstract long id();

  @Nullable @ColumnName(FIRST_NAME) public abstract String firstName();

  @Nullable @ColumnName(LAST_NAME) public abstract String lastName();

  @Nullable @ColumnName(FIRST_NAME_PHONETIC) public abstract String firstNamePhonetic();

  @Nullable @ColumnName(LAST_NAME_PHONETIC) public abstract String lastNamePhonetic();

  public abstract int sex();

  @Nullable public abstract String memo();

  @Nullable public abstract String rank();

  public static Func1<Cursor, Player> mapper() {
    return AutoValue_Player.MAPPER;
  }

  public static final class ContentsBuilder {
    private final ContentValues values = new ContentValues();

    public ContentsBuilder id(long id) {
      values.put(ID, id);
      return this;
    }

    public ContentsBuilder firstName(String firstName) {
      values.put(FIRST_NAME, firstName);
      return this;
    }

    public ContentsBuilder lastName(String lastName) {
      values.put(LAST_NAME, lastName);
      return this;
    }

    public ContentsBuilder firstNamePhonetic(String firstNamePhonetic) {
      values.put(FIRST_NAME_PHONETIC, firstNamePhonetic);
      return this;
    }

    public ContentsBuilder lastNamePhonetic(String lastNamePhonetic) {
      values.put(LAST_NAME_PHONETIC, lastNamePhonetic);
      return this;
    }

    public ContentsBuilder sex(int sex) {
      values.put(SEX, sex);
      return this;
    }

    public ContentsBuilder memo(String memo) {
      values.put(MEMO, memo);
      return this;
    }

    public ContentsBuilder rank(String rank) {
      values.put(RANK, rank);
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
