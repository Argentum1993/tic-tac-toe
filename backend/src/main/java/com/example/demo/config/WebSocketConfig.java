package com.example.demo.config;

import com.example.demo.Maps;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(Maps.CONFIG_BROKER_PREFIXES);
    registry.setApplicationDestinationPrefixes(Maps.CONFIG_APP_DESTINATION_PREFIXES);
  }


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(Maps.CONFIG_ENDPOINTS).withSockJS();
  }
}
