package com.example.demo.transfer;

import com.example.demo.service.Events;

public class EventGameOverDTO {
  private Events event;
  private String winner;

  public EventGameOverDTO(Events event, String winner) {
    this.event = event;
    this.winner = winner;
  }

  public Events getEvent() {
    return event;
  }

  public void setEvent(Events event) {
    this.event = event;
  }

  public String getWinner() {
    return winner;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }
}
