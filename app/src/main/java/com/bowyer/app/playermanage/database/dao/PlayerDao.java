package com.bowyer.app.playermanage.database.dao;

import android.text.TextUtils;
import com.bowyer.app.playermanage.database.dto.Player;
import com.bowyer.app.playermanage.database.dto.Rank;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

public class PlayerDao {

  private static final String SELECT_BY_ID =
      "SELECT * FROM " + Player.TABLE + " WHERE " + Player.ID + " = ? ";

  public static Observable<Player> getPlayerById(BriteDatabase database, String playerId) {
    return database.createQuery(Player.TABLE, SELECT_BY_ID, playerId).mapToOne(Player.MAPPER);
  }

  public static QueryObservable getPlayerByQuery(BriteDatabase database, PlayerQuery query) {
    return database.createQuery(Player.TABLE, query.sql, query.values);
  }

  public static final class SQLBuilder {
    private String sql = "SELECT * FROM " + Player.TABLE + " WHERE ";
    private final List<String> values = new ArrayList<>();

    private String getAndSql() {
      if (!values.isEmpty()) {
        sql = sql + " and ";
      }
      return sql;
    }

    private String getOrSql() {
      if (!values.isEmpty()) {
        sql = sql + " or ";
      }
      return sql;
    }

    public SQLBuilder name(String name) {
      if (TextUtils.isEmpty(name)) {
        return this;
      }
      //全部マッチさせるため
      sql = getAndSql() + Player.LAST_NAME + " like ? ";
      values.add("%" + name + "%");
      sql = getOrSql() + Player.FIRST_NAME + " like ? ";
      values.add("%" + name + "%");
      sql = getOrSql() + Player.LAST_NAME_PHONETIC + " like ? ";
      values.add("%" + name + "%");
      sql = getOrSql() + Player.FIRST_NAME_PHONETIC + " like ? ";
      values.add("%" + name + "%");
      return this;
    }

    public SQLBuilder sex(Sex sex) {
      if (sex == Sex.ALL) {
        return this;
      }
      sql = getAndSql() + Player.SEX + " = ? ";
      values.add(String.valueOf(sex.getSex()));
      return this;
    }

    public SQLBuilder rank(Rank rank) {
      if (rank == Rank.ALL) {
        return this;
      }
      sql = getAndSql() + Player.RANK + " = ? ";
      values.add(rank.getRank());
      return this;
    }

    public SQLBuilder id(long playerId) {
      sql = getAndSql() + Player.ID + " = ? ";
      values.add(String.valueOf(playerId));
      return this;
    }

    public PlayerQuery build() {
      if (values.isEmpty()) {
        sql = sql.replace("WHERE", "");
      }
      return new PlayerQuery(sql, values.toArray(new String[values.size()]));
    }
  }

  public static class PlayerQuery {
    public final String sql;
    public final String[] values;

    public PlayerQuery(String sql, String[] values) {
      this.sql = sql + " ORDER BY " + Player.ID + " DESC, " + Player.LAST_NAME + " ASC";
      this.values = values;
    }
  }
}
