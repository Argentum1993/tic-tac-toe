package com.example.demo.transfer;

import com.example.demo.service.Events;
import java.util.List;

public class EventUpdatesDTO {
  private Events event = Events.UPDATE_GAMES_LIST;
  private List<String> availableGames;
  private List<String> closedGames;

  public EventUpdatesDTO(List<String> availableGames, List<String> closedGames) {
    this.availableGames = availableGames;
    this.closedGames = closedGames;
  }


  public Events getEvent() {
    return event;
  }

  public List<String> getAvailableGames() {
    return availableGames;
  }

  public List<String> getClosedGames() {
    return closedGames;
  }
}
