package com.example.demo.transfer;

import com.example.demo.service.Events;
import java.util.Map;

public class EventRolesDTO {
  Events event;
  Map<Character, String> usersSign;

  public EventRolesDTO(Events event, Map<Character, String> usersSign) {
    this.event = event;
    this.usersSign = usersSign;
  }

  public Events getEvent() {
    return event;
  }

  public void setEvent(Events event) {
    this.event = event;
  }

  public Map<Character, String> getUsersSign() {
    return usersSign;
  }

  public void setUsersSign(Map<Character, String> usersSign) {
    this.usersSign = usersSign;
  }
}
