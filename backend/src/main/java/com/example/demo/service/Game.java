package com.example.demo.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Game {
  private final int     LIVE_TIME =  5 * 1000 * 60;
  private final String  DRAW = "DRAW";

  private String        idFirstPlayer;
  private String        idSecondPlayer;
  private Character[]   board = new Character[9];
  private boolean       isClose;
  private char[]        signs = new char[2];
  private String        nextPlayer;
  private Date          liveTime;

  Game(String idFirstPlayer){
    SecureRandom random;

    this.idFirstPlayer = idFirstPlayer;
    random = new SecureRandom();
    signs[0] = (random.nextBoolean() ? 'X' : 'O');
    signs[1] = (signs[0] == 'X' ? 'O' : 'X');
    this.liveTime = new Date(new Date().getTime() + LIVE_TIME);
    this.nextPlayer = "";
  }

  public Character[] getBoard() {
    return board;
  }

  public boolean waitConnections(){
    return !isClose && idFirstPlayer != null;
  }

  public boolean connect(String idSecondPlayer){
    if (waitConnections() && !idFirstPlayer.equals(idSecondPlayer)) {
      this.idSecondPlayer = idSecondPlayer;
      this.isClose = true;
      nextPlayer = (signs[0] == 'X' ? this.idFirstPlayer : this.idSecondPlayer);
      return true;
    }
    return false;
  }

  public Date getLiveTime() {
    return liveTime;
  }

  public boolean isClose(){
    return isClose;
  }

  public boolean containPlayer(String id){
    return (idFirstPlayer != null && idFirstPlayer.equals(id))
        || (idSecondPlayer != null && idSecondPlayer.equals(id));
  }

  public boolean move(String playerID, int move){
    int indexPlayer;

    if (canMove(playerID, move)){
      if (playerID.equals(idFirstPlayer)){
        indexPlayer = 0;
        nextPlayer = idSecondPlayer;
      } else {
        indexPlayer = 1;
        nextPlayer = idFirstPlayer;
      }
      board[move] = signs[indexPlayer];
      return true;
    }
    return false;
  }

  public Map<Character, String> getRoles(){
    Map<Character, String> roles;

    roles = new HashMap<>();
    roles.put(signs[0], idFirstPlayer);
    roles.put(signs[1], idSecondPlayer);
    return roles;
  }

  public String getWinner(){
    int[][] solves = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},
        {2, 4, 6}
    };
    
    if (isClose){
      for (int[] solve : solves){
        if (board[solve[0]] != null &&
            board[solve[0]] == board[solve[1]] && board[solve[0]] == board[solve[2]]){
          return "" + board[solve[0]];
        }
      }
    }
    for (int i = 0; i < board.length; i++) {
      if (board[i] == null)
        return null;
    }
    return DRAW;
  }
  
  private boolean canMove(String playerID, int move){
    return isClose && containsPlayer(playerID) && validMove(move) && nextPlayer.equals(playerID);
  }
  
  private boolean containsPlayer(String id){
    return idFirstPlayer.equals(id) || idSecondPlayer.equals(id);
  }
  
  private boolean validMove(int move){
    return move >= 0 && move <= 9 && board[move] == null;
  }
}
