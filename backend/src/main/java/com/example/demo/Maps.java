package com.example.demo;

public class Maps {
  // Controller maps
  public static final String UPDATES_OUT = "/topic/updates";
  public static final String UPDATES_SUBSCRIBE = "/updates";
  public static final String GAME_CHANNEL_SUBSCRIBE = "/game";
  public static final String GAME_CONNECT = "/game/connect";
  public static final String GAME_ROOT = "/game";
  public static final String GAME_CREATE = "/game/create";
  public static final String PLAYER_CHANNEL_FORMAT = "/queue/replay-%s";
  public static final String GAME_CHANNEL_FORMAT = "/topic/game/%s";

  // Config maps
  public static final String[] CONFIG_BROKER_PREFIXES = new String[]{"/topic", "/queue"};
  public static final String[] CONFIG_APP_DESTINATION_PREFIXES = new String[]{"/app", "/topic"};
  public static final String[] CONFIG_ENDPOINTS = new String[]{GAME_ROOT};
}
