import './App.css';
import React from "react"
import {Route, Switch} from "react-router-dom";
import { SocketProvider } from './socketContext'

import Home from "./pages/Home";
import Game from "./pages/Game";

import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';



function App() {
    const socket = new SockJS('/game');
    const stompClient = Stomp.over(socket);
  return (
      <SocketProvider value={{stompClient:stompClient, socket:socket, sessionId: null, gameName: null}}>
          <div className="App">
              <Switch>
                  <Route exact path="/" component={Home} />
                  <Route exact path="/game" component={Game} />
              </Switch>
          </div>
      </SocketProvider>
  );
}

export default App;
