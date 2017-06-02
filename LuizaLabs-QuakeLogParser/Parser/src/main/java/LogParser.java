package main.java;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.luizalabs.spring.model.Kill;
import com.luizalabs.spring.model.Partida;
import com.luizalabs.spring.model.Player;

@SpringBootApplication
public class LogParser {

  private static final Logger log = LoggerFactory.getLogger(LogParser.class);

  public static List<Partida> ProcessaArquivo(String arquivo) {
    List<Partida> listaPartidas = new ArrayList<Partida>();
    try {
      log.debug("Abrindo o arquivo!");
      FileInputStream fstream = new FileInputStream(arquivo);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      List<Player> listaPlayers = new ArrayList<Player>();
      List<Kill> listaKills = new ArrayList<Kill>();
      Map<String, Integer> kills = new HashMap<String, Integer>();
      int totalKills = 0;
      Partida partida = null;

      log.debug("Arquivo aberto. Pegando suas linhas para fazer o parse!");
      while ((strLine = br.readLine()) != null) {
        log.debug("Encontrado inicio de jogo!"); // Inicio de um jogo
        if (strLine.indexOf("InitGame") > -1) {
          // Não finalizou a partida com ShutdownGame
          if (partida != null) {
            log.debug("Partida anterior não foi finalizado graciosamente!");
            listaPlayers = new ArrayList<Player>();
            listaKills = new ArrayList<Kill>();
            log.debug("Loop na lista de kills para adicionar players e kills ao objeto partida");
            for (Map.Entry<String, Integer> entry : kills.entrySet()) {
              if (!entry.getKey().equals("<world>")) {
                log.debug("Adicionando player");// Adiciona o jogador
                Player player = new Player();
                player.setNome(entry.getKey());
                listaPlayers.add(player);

                log.debug("Adicionando suas kills");// Adiciona suas kills
                Kill kill = new Kill();
                kill.setPlayer(player);
                kill.setQtde(entry.getValue());
                listaKills.add(kill);
              }
            }

            partida.setPlayers(listaPlayers.toArray(new Player[0]));
            partida.setKills(listaKills.toArray(new Kill[0]));
            log.debug("Setando o total de kills");
            partida.setTotalKills(totalKills);
            log.debug("Adicionando a partida na lista");
            listaPartidas.add(partida);
          }
          log.debug("Resetando variáveis para nova partida!");
          partida = new Partida();
          totalKills = 0;
          kills = new HashMap<String, Integer>();
        }
        // Player
        else if (strLine.indexOf("ClientUserinfoChanged") > -1) {
          log.debug("Novo player entrou na partida!");// Pega o nome do player
          Matcher m = Pattern.compile("n\\\\(.*?)\\\\t").matcher(strLine);
          if (m.find()) {
            String player = m.group(1).trim();
            // Adiciona o player com 0 kill
            if (!kills.containsKey(player)) {
              kills.put(player, 0);
            }
          }
        }
        // Kill
        else if (strLine.indexOf("Kill") > -1) {
          log.debug("Nova morte aconteceu!");
          totalKills++;

          // Usa regexp para pegar quem matou e foi morto
          Matcher m = Pattern.compile(":.*?:.*?:(.*?)\\S*killed\\S*(.*?)\\S*by?").matcher(strLine);
          if (m.find()) {
            String matou = m.group(1).trim();
            String morto = m.group(2).trim();
            if (matou.equals("<world>") || matou.equals(morto)) {
              log.debug("Suicídio ou morto pelo mapa! Perde frag.");
              if (kills.containsKey(morto)) {
                kills.replace(morto, kills.get(morto) - 1);
              } else {
                kills.put(morto, -1);
              }
            } else {
              log.debug("Computando morte para player!");
              if (kills.containsKey(matou)) {
                kills.replace(matou, kills.get(matou) + 1);
              } else {
                kills.put(matou, 1);
              }
            }
          }
        }
        // Fim da partida
        else if (strLine.indexOf("ShutdownGame") > -1) {
          log.debug("Partida anterior encerrada!");
          listaPlayers = new ArrayList<Player>();
          listaKills = new ArrayList<Kill>();

          log.debug("Loop na lista de kills para adicionar players e kills ao objeto partida");
          for (Map.Entry<String, Integer> entry : kills.entrySet()) {
            if (!entry.getKey().equals("<world>")) {
              log.debug("Adicionando player");// Adiciona o jogador
              Player player = new Player();
              player.setNome(entry.getKey());
              listaPlayers.add(player);

              log.debug("Adicionando suas kills");// Adiciona suas kills
              Kill kill = new Kill();
              kill.setPlayer(player);
              kill.setQtde(entry.getValue());
              listaKills.add(kill);
            }
          }

          partida.setPlayers(listaPlayers.toArray(new Player[0]));
          partida.setKills(listaKills.toArray(new Kill[0]));
          log.debug("Setando o total de kills");
          partida.setTotalKills(totalKills);
          log.debug("Adicionando a partida na lista");
          listaPartidas.add(partida);
          log.debug("Resetando variáveis para nova partida!");
          partida = null;
          totalKills = 0;
          kills = new HashMap<String, Integer>();
        }
      }
      br.close();
    } catch (IOException e) {
      log.error("Arquivo " + arquivo + " não encontrado!");
      return null;
    }
    return listaPartidas;
  }

  @SuppressWarnings("rawtypes")
  public static List PersisteLista(List<Partida> lista) {
    log.debug("Passando lista para WebService REST persistir em banco!");
    RestTemplate rest = new RestTemplate();
    String url = "http://localhost:8080/partidas";
    ResponseEntity<List> response = rest.postForEntity(url, lista, List.class);
    return response.getBody();
  }

  @SuppressWarnings("rawtypes")
  public static void main(String args[]) {
    if (args.length != 1) {
      log.info(
          "Para usar o parser, passe o caminho do arquivo por parametro. Ex: java LogParser \"C:\\quake.log\"");
    } else {
      List<Partida> listaPartidas = ProcessaArquivo(args[0]);
      if (listaPartidas != null) {
        log.debug("Mostrando retorno do WebService.");
        List retorno = PersisteLista(listaPartidas);
        System.out.println(retorno);
      }
    }
  }
}
