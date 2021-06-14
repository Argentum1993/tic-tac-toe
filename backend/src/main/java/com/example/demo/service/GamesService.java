package com.example.demo.service;

import com.example.demo.transfer.EventGameDTO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GamesService {


  private final Map<String, Game> games = new HashMap<>();

  public boolean createGame(String gameName, String idFirstPlayer){
    if (!games.containsKey(gameName)) {
      games.put(gameName, new Game(idFirstPlayer));
      return true;
    }
    return false;
  }

  private boolean playerHaveGame(String id){
    for (Game game : games.values()){
      if (game.containPlayer(id))
        return true;
    }
    return false;
  }

  public boolean connectToGame(String gameName, String idSecondPlayer){
    Game game;

    if (games.containsKey(gameName) && (game = games.get(gameName)).waitConnections())
      return game.connect(idSecondPlayer);
    return false;
  }

  public List<String> getAvailable(){
    return games.entrySet().stream()
        .filter(game -> game.getValue().waitConnections())
        .map(game -> game.getKey())
        .collect(Collectors.toList());
  }

  public List<String> getClose(){
    return games.entrySet().stream()
        .filter(game -> game.getValue().isClose())
        .map(game -> game.getKey())
        .collect(Collectors.toList());
  }

  public boolean handleEvent(String gameName, EventGameDTO event){
    Game game;

    if ((game = games.get(gameName)) != null) {
      if (event.getEvent().equals(Events.MOVE)) {
        return game.move(event.getId(), event.getMove());
      }
    }
    return false;
  }

  public Character[] getGameBoard(String gameName){
    Game game;

    if ((game = games.get(gameName)) != null)
      return game.getBoard();
    return null;
  }

  public Map<Character, String> getRoles(String gameName){
    Game game;

    if ((game = games.get(gameName)) != null)
      return game.getRoles();
    return null;
  }

  public String getWinner(String gameName){
    Game    game;
    String  winner;

    if ((game = games.get(gameName)) != null && (winner = game.getWinner()) != null) {
      games.remove(gameName);
      return winner;
    }
    return null;
  }
  
  public void cleanOldGame(){
    games.entrySet().stream()
        .forEach(e -> {
          if (e.getValue().getLiveTime().before(new Date()))
            games.remove(e);
        });
  }

  public boolean playHimself(String gameName, String playerId){
    Game game;

    if ((game = games.get(gameName)) != null)
      return game.containPlayer(playerId);
    return false;
  }
}
