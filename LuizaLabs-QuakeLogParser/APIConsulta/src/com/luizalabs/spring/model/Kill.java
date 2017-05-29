package com.luizalabs.spring.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/*
 * Classe entity/objeto de Thumbnail
 */
@Entity
@Table(name = "Kill")
public class Kill implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @OneToOne(cascade = CascadeType.ALL)
  @OrderColumn(name = "id")
  private Player player;
  private int qtde;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player players) {
    this.player = players;
  }

  public int getQtde() {
    return qtde;
  }

  public void setQtde(int qtde) {
    this.qtde = qtde;
  }
}
