package bean;

import java.io.Serializable;

/*
 * Classe objeto de Player
 */
public class Player implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private String nome;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
