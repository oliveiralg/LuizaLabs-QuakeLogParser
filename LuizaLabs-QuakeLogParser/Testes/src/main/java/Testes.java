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
import org.springframework.web.client.RestTemplate;

import bean.Kill;
import bean.Partida;
import bean.Player;

@SpringBootApplication
public class Testes {

  private static final Logger log = LoggerFactory.getLogger(Testes.class);

  private static void CarregarArquivo() {
    try {
      FileInputStream fstream = new FileInputStream("games1.log");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      List<Partida> listaPartidas = new ArrayList<Partida>();
      List<Player> listaPlayers = new ArrayList<Player>();
      List<Kill> listaKills = new ArrayList<Kill>();
      Map<String, Integer> kills = new HashMap<String, Integer>();
      int totalKills = 0;
      Partida partida = null;

      while ((strLine = br.readLine()) != null) {
        // Inicio de um jogo
        if (strLine.indexOf("InitGame") > -1) {
          // Não finalizou a partida com ShutdownGame
          if (partida != null) {
            listaPlayers = new ArrayList<Player>();
            listaKills = new ArrayList<Kill>();

            for (Map.Entry<String, Integer> entry : kills.entrySet()) {
              if (!entry.getKey().equals("<world>")) {
                // Adiciona o jogador
                Player player = new Player();
                player.setNome(entry.getKey());
                listaPlayers.add(player);

                // Adiciona suas kills
                Kill kill = new Kill();
                kill.setPlayer(player);
                kill.setQtde(entry.getValue());
                listaKills.add(kill);
              }
            }

            partida.setPlayers(listaPlayers.toArray(new Player[0]));
            partida.setKills(listaKills.toArray(new Kill[0]));
            partida.setTotalKills(totalKills);
            listaPartidas.add(partida);
          }
          partida = new Partida();
          totalKills = 0;
          kills = new HashMap<String, Integer>();
        }
        // Player
        else if (strLine.indexOf("ClientUserinfoChanged") > -1) {
          // Pega o nome do player
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
          totalKills++;

          // Usa regexp para pegar quem matou e foi morto
          Matcher m = Pattern.compile(":.*?:.*?:(.*?)\\S*killed\\S*(.*?)\\S*by?").matcher(strLine);
          if (m.find()) {
            String matou = m.group(1).trim();
            String morto = m.group(2).trim();
            if (matou.equals("<world>") || matou.equals(morto)) {
              if (kills.containsKey(morto)) {
                kills.replace(morto, kills.get(morto) - 1);
              } else {
                kills.put(morto, -1);
              }
            } else {
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
          listaPlayers = new ArrayList<Player>();
          listaKills = new ArrayList<Kill>();

          for (Map.Entry<String, Integer> entry : kills.entrySet()) {
            if (!entry.getKey().equals("<world>")) {
              // Adiciona o jogador
              Player player = new Player();
              player.setNome(entry.getKey());
              listaPlayers.add(player);

              // Adiciona suas kills
              Kill kill = new Kill();
              kill.setPlayer(player);
              kill.setQtde(entry.getValue());
              listaKills.add(kill);
            }
          }

          partida.setPlayers(listaPlayers.toArray(new Player[0]));
          partida.setKills(listaKills.toArray(new Kill[0]));
          partida.setTotalKills(totalKills);
          listaPartidas.add(partida);
          partida = null;
          totalKills = 0;
          kills = new HashMap<String, Integer>();
        }
      }
      br.close();

      RestTemplate rest = new RestTemplate();
      String url = "http://localhost:8080/partidas";
      rest.postForEntity(url, listaPartidas, List.class);
    } catch (IOException e) {
      log.info("Arquivo não encontrado!");
    }
  }

  public static void main(String args[]) {
    // Carrega arquivo para iniciar o banco;
    CarregarArquivo();
    // Verifica se o arquivo foi carregado com sucesso buscando todas as partidas

    // Pega o id e faz a busca pelo id agora

    // Faz a busca por player

    // Remove o registro

    // Verifica se banco foi limpo buscando todas as partidas


  }
}
