package com.bowyer.app.playermanage.database.dto;

public enum Rank {

  ALL(""), K3("三級"), K2("二級"), K1("一級"), D1("初段"), D2("弐段"), D3("参段"), D4("四段"), D5("五段"), D6(
      "六段"), D7("七段"), D8("八段"), D9("九段"), D10("十段");

  String rank;

  Rank(String rank) {
    this.rank = rank;
  }

  public String getRank() {
    return rank;
  }

  public static Rank of(String rank) {
    switch (rank) {
      case "三級":
        return K3;
      case "二級":
        return K2;
      case "一級":
        return K1;
      case "初段":
        return D1;
      case "弐段":
        return D2;
      case "参段":
        return D3;
      case "四段":
        return D4;
      case "五段":
        return D5;
      case "六段":
        return D6;
      case "七段":
        return D7;
      case "八段":
        return D8;
      case "九段":
        return D9;
      case "十段":
        return D10;
      default:
        return ALL;
    }
  }
}
