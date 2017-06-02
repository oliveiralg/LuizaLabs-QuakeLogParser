package main.java;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.luizalabs.spring.model.Partida;

@SpringBootApplication
public class Testes {

  private static final Logger log = LoggerFactory.getLogger(Testes.class);

  @SuppressWarnings({"rawtypes"})
  public static void main(String args[]) {
    // Remove todos os registros do banco
    log.debug("Limpando o banco!");
    RestTemplate rest = new RestTemplate();
    String url = "http://localhost:8080/cleandatabase";
    rest.getForEntity(url, null);

    // Carrega arquivo para iniciar o banco;
    log.debug("Processando o arquivo");
    List<Partida> listaPartidas = LogParser.ProcessaArquivo(
        "C:/Development/Repositories/LuizaLabs-QuakeLogParser/Testes/src/main/java/games1.log");
    if (listaPartidas != null) {
      log.debug("Gravando no banco a lista");
      LogParser.PersisteLista(listaPartidas);
      // Verifica se o arquivo foi carregado com sucesso buscando todas as partidas
      log.debug("Buscando todas as partidas");
      url = "http://localhost:8080/listarpartidas";
      ResponseEntity<Partida[]> responseLista = rest.getForEntity(url, Partida[].class);
      Partida partida = null;
      if (responseLista.getStatusCode().value() == 200) // Chamada ocorreu OK
      {
        // Check if the totalKills = 105, Size kills = 4 e size Players = 4
        partida = responseLista.getBody()[0];
        if (partida != null && partida.getTotalKills() == 105 && partida.getKills().length == 4
            && partida.getPlayers().length == 4)
          log.info("Teste OK. Partida retornada é a carregada!");
        else {
          log.error("Partida carregada não confere. Teste falhou!");
          return;
        }
      } else {
        log.error("Chamada para pegar partidas falhou!");
        return;
      }
      // Pega o id e faz a busca pelo id agora
      int id = partida.getId();
      log.debug("Buscando a partida pelo id = " + id);
      url = "http://localhost:8080/partida/" + id;
      ResponseEntity<Partida> responsePartida = rest.getForEntity(url, Partida.class);
      if (responsePartida.getStatusCode().value() == 200) // Chamada ocorreu OK
      {
        // Check if the totalKills = 105, Size kills = 4 e size Players = 4
        partida = responsePartida.getBody();
        if (partida != null && partida.getTotalKills() == 105 && partida.getKills().length == 4
            && partida.getPlayers().length == 4)
          log.info("Teste OK. Partida retornada é a correta!");
        else {
          log.error("Partida retornada não confere. Teste falhou!");
          return;
        }
      } else {
        log.error("Chamada para pegar partida id = " + id + " falhou!");
        return;
      }
      // Faz a busca por player
      log.debug("Buscando partida por player = Isgalamido");
      url = "http://localhost:8080/player/Isgalamido";
      responseLista = rest.getForEntity(url, Partida[].class);
      if (responseLista.getStatusCode().value() == 200) // Chamada ocorreu OK
      {
        // Check if the totalKills = 105, Size kills = 4 e size Players = 4
        partida = responseLista.getBody()[0];
        if (partida != null && partida.getTotalKills() == 105 && partida.getKills().length == 4
            && partida.getPlayers().length == 4)
          log.info("Teste OK. Partida retornada é a correta!");
        else {
          log.error("Partida retornada não confere. Teste falhou!");
          return;
        }
      } else {
        log.error("Chamada para pegar partida por player = Isgalamido falhou!");
        return;
      }

      // Remove o registro
      log.debug("Removendo a partida do banco");
      url = "http://localhost:8080/removerpartida/" + id;
      ResponseEntity response = rest.getForEntity(url, null);
      if (response.getStatusCode().value() != 200) // Ocorreu problema na chamada
      {
        log.error("Chamada para remover falhou!");
        return;
      } else
        log.info("Teste Ok. Chamada para remover partida executada");

      // Verifica se banco foi limpo buscando todas as partidas
      log.debug("Buscando todas as partidas");
      url = "http://localhost:8080/listarpartidas";
      responseLista = rest.getForEntity(url, Partida[].class);
      if (responseLista.getStatusCode().value() != 200) // Ocorreu erro
      {
        log.error("Ocorreu erro ao buscar as partidas!");
        return;

      } else if (responseLista.getBody().length != 0) // Ocorreu erro
      {
        log.error("A quantidade de partidas retornadas não corresponde. Teste falhou!");
        return;
      } else
        log.info("Teste Ok. Nenhuma partida retornada");

      log.info("Todos os testes passaram!");
    }
  }
}
