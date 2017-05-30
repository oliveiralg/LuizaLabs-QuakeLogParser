package bean;

import java.io.Serializable;

/*
 * Classe objeto de Partida
 */
public class Partida implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private int totalKills;
  private Player[] players;
  private Kill[] kills;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTotalKills() {
    return totalKills;
  }

  public void setTotalKills(int totalKills) {
    this.totalKills = totalKills;
  }

  public Player[] getPlayers() {
    return players;
  }

  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public Kill[] getKills() {
    return kills;
  }

  public void setKills(Kill[] kills) {
    this.kills = kills;
  }



}
