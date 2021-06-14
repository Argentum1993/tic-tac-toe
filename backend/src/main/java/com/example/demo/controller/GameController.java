package com.example.demo.controller;

import com.example.demo.Maps;
import com.example.demo.service.Events;
import com.example.demo.service.GamesService;
import com.example.demo.transfer.EventGameDTO;
import com.example.demo.transfer.EventRolesDTO;
import com.example.demo.transfer.EventUpdatesDTO;
import com.example.demo.transfer.EventGameOverDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
  private final GamesService service;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public GameController(GamesService service,
      SimpMessagingTemplate simpMessagingTemplate) {
    this.service = service;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @SubscribeMapping(Maps.UPDATES_SUBSCRIBE)
  public void initialUpdates() throws Exception {
    Thread.sleep(100);
    simpMessagingTemplate.convertAndSend(Maps.UPDATES_OUT, createEventUpdatesDTO(service));
  }

  @SubscribeMapping(Maps.GAME_CHANNEL_SUBSCRIBE + "/{gameName}")
  public void initialGameChannel(@DestinationVariable String gameName) throws Exception {
    Thread.sleep(200);
    sendToGameChannel(gameName, new EventRolesDTO(Events.GAME_ROLES, service.getRoles(gameName)));
  }

  @MessageMapping(Maps.GAME_CREATE)
  public void createGame(String name, @Header("simpSessionId") String sessionId) throws Exception {
    if (service.createGame(name, sessionId)) {
      service.cleanOldGame();
      simpMessagingTemplate.convertAndSend(Maps.UPDATES_OUT, createEventUpdatesDTO(service));
      sendToPlayer(sessionId, new EventGameDTO(Events.GAME_CREATED));
    }
  }
  
  @MessageMapping(Maps.GAME_CONNECT)
  public void connectGame(String name, @Header("simpSessionId") String sessionId) throws Exception {
    if (service.playHimself(name, sessionId)){
      sendToPlayer(sessionId, new EventGameDTO(Events.ERROR_PLAY_YOURSELF));
      return;
    }
    if (service.connectToGame(name, sessionId)) {
      simpMessagingTemplate.convertAndSend(Maps.UPDATES_OUT, createEventUpdatesDTO(service));
      sendToPlayer(sessionId, new EventGameDTO(Events.GAME_CONNECTED));
      Thread.sleep(200);
      sendToGameChannel(name, new EventGameDTO(Events.GAME_STARTED));
    }
  }

  @MessageMapping(Maps.GAME_ROOT + "/{gameName}")
  public void gameRoom(
      @DestinationVariable String gameName,
      @Payload EventGameDTO event) throws Exception {
    String winner;

    if (service.handleEvent(gameName, event)) {
      sendToGameChannel(
          gameName, new EventGameDTO(Events.UPDATE_BOARD, service.getGameBoard(gameName)));
      if ((winner = service.getWinner(gameName)) != null){
        sendToGameChannel(gameName, new EventGameOverDTO(Events.GAME_OVER, winner));
      }
    }
  }

  private EventUpdatesDTO createEventUpdatesDTO(GamesService service){
    return new EventUpdatesDTO(service.getAvailable(), service.getClose());
  }

  private void sendToPlayer(String id, Object out){
    simpMessagingTemplate.convertAndSend(String.format(Maps.PLAYER_CHANNEL_FORMAT, id), out);
  }

  private void sendToGameChannel(String gameName, Object out){
    simpMessagingTemplate.convertAndSend(String.format(Maps.GAME_CHANNEL_FORMAT, gameName), out);
  }
}
