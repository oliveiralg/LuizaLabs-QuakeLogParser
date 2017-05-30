package com.luizalabs.spring.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.luizalabs.spring.model.Partida;
import com.luizalabs.spring.service.PartidaService;

/*
 * Classe de controle REST para funções com partidas
 */
@RestController
public class PartidaRestController {

  private static final Logger log = LoggerFactory.getLogger(PartidaRestController.class);

  /*
   * Pesquisa partidas
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @GetMapping("/partida/{id}")
  public ResponseEntity<Partida> buscarPartida(@PathVariable("id") int id) {

    try {
      Partida partida = PartidaService.buscar(id);
      if (partida == null) {
        return new ResponseEntity("Partida não encontrada com id = " + id, HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<Partida>(partida, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Pesquisa partidas por player
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @GetMapping("/player/{nome}")
  public ResponseEntity<List<Partida>> buscarPartidaPlayer(@PathVariable("nome") String nome) {

    try {
      List<Partida> partidas = PartidaService.buscarPlayer(nome);
      if (partidas == null) {
        return new ResponseEntity("Partida não encontrada com player = " + nome,
            HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<List<Partida>>(partidas, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Salva partida em banco
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @RequestMapping(value = "/partida", method = RequestMethod.POST)
  public ResponseEntity<Partida> create(@RequestBody Partida partida) {

    try {
      if (partida != null) {
        PartidaService.criar(partida);
      }
      return new ResponseEntity<Partida>(partida, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Salva lista partidas em banco
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @RequestMapping(value = "/partidas", method = RequestMethod.POST)
  public ResponseEntity<List<Partida>> create(@RequestBody List<Partida> partidas) {

    try {
      if (partidas != null) {
        for (Partida p : partidas) {
          PartidaService.criar(p);
        }
      }
      return new ResponseEntity<List<Partida>>(partidas, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Remove partida
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @GetMapping("/removerpartida/{id}")
  public ResponseEntity remover(@PathVariable("id") int id) {
    try {
      PartidaService.remover(id);
      return new ResponseEntity(null, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Limpa o banco
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @GetMapping("/cleandatabase")
  public ResponseEntity clean() {
    try {
      PartidaService.clean();
      return new ResponseEntity(null, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * Lista todas partidas
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @GetMapping("/listarpartidas")
  public ResponseEntity<List<Partida>> listar() {
    try {
      List<Partida> partidas = PartidaService.listar();
      return new ResponseEntity<List<Partida>>(partidas, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
