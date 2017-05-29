package com.luizalabs.spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.luizalabs.spring.dao.PartidaDAO;
import com.luizalabs.spring.model.Partida;

/*
 * Classe de serviço para Partidas
 */
@Component
public class PartidaService {

  private static final Logger log = LoggerFactory.getLogger(PartidaService.class);

  /*
   * Busca partida no banco de dados Retorna Partida
   */
  public static Partida buscar(int id) throws Exception {
    log.debug("Buscando partida por id = " + id);
    List<Partida> partidas = PartidaDAO.getPartida(id);
    return partidas.get(0);
  }

  public static List<Partida> listar() throws Exception {
    log.debug("Listando todas as partidas");
    List<Partida> partidas = PartidaDAO.getPartidas();
    return partidas;
  }

  public static void criar(Partida partida) throws Exception {
    log.debug("Salvando partida");
    PartidaDAO.salvarPartida(partida);
  }

  public static void remover(int id) throws Exception {
    log.debug("Removendo partida id = " + id);
    PartidaDAO.removePartida(id);
  }

}