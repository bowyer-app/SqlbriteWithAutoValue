package com.bowyer.app.playermanage.database.dto;

public enum Sex {

  ALL(0), MALE(1), FEMALE(2);
  int sex;

  Sex(int sex) {
    this.sex = sex;
  }

  public int getSex() {
    return sex;
  }

  public static Sex of(int sex) {
    switch (sex) {
      case 1:
        return MALE;
      case 2:
        return FEMALE;
      default:
        return ALL;
    }
  }
}
