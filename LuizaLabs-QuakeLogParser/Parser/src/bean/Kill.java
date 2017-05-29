package bean;

import java.io.Serializable;

/*
 * Classe objeto de Kill
 */
public class Kill implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
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
