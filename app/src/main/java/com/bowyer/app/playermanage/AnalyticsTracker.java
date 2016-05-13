package com.bowyer.app.playermanage;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import javax.inject.Inject;

public class AnalyticsTracker {
  final Tracker tracker;

  @Inject public AnalyticsTracker(Tracker tracker) {
    this.tracker = tracker;
  }

  public void sendScreenView(String screenName) {
    tracker.setScreenName(screenName);
    tracker.send(new HitBuilders.ScreenViewBuilder().build());
  }

  public void sendEvent(Category category, Action action) {
    if (category == null || action == null) {
      return;
    }
    tracker.send(new HitBuilders.EventBuilder(category.getCategoty(), action.getAction()).build());
  }

  public void sendEvent(Category category, Action action, String label) {
    if (category == null || action == null) {
      return;
    }
    tracker.send(
        new HitBuilders.EventBuilder(category.getCategoty(), action.getAction()).setLabel(label)
            .build());
  }

  public enum Category {

    DEFAULT("default");
    String categoty;

    Category(String categoty) {
      this.categoty = categoty;
    }

    public String getCategoty() {
      return categoty;
    }
  }

  public enum Action {

    ADD_PLAYER_FAB_TAP("add-player-fab-tap"),
    SEARCH_PLAYER_TAP("search-player-tap"),
    EXPAND_SEARCH_PLAYER("expand-search-player"),
    PLAYER_SEX_TAP("player-sex-tap"),
    PLAYER_SAVE("player-save");
    String action;

    Action(String action) {
      this.action = action;
    }

    public String getAction() {
      return action;
    }
  }
}
