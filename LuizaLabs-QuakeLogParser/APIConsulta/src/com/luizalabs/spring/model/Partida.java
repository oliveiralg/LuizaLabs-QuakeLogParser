package com.luizalabs.spring.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/*
 * Classe entity/objeto de Thumbnail
 */
@Entity
@Table(name = "Partida")
public class Partida implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int totalKills;
  @OneToMany(cascade = CascadeType.ALL)
  @OrderColumn(name = "id")
  private Player[] players;
  @OneToMany(cascade = CascadeType.ALL)
  @OrderColumn(name = "id")
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
