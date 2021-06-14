package com.example.demo.transfer;

import com.example.demo.service.Events;
import java.util.Map;

public class EventGameDTO {
  Events                    event;
  String                  id;
  int                     move;
  Character[]             board;

  public EventGameDTO(Events event) {
    this.event = event;
  }

  public EventGameDTO(Events event, Character[] board) {
    this.event = event;
    this.board = board;
  }

  public Events getEvent() {
    return event;
  }

  public void setEvent(Events event) {
    this.event = event;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMove(int move) {
    this.move = move;
  }

  public Character[] getBoard() {
    return board;
  }

  public String getId() {
    return id;
  }

  public int getMove() {
    return move;
  }
}
