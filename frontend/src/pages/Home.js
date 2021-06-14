import '../Tags.css'
import React, {useEffect, useState, useRef, useContext} from "react";

import Tags from "@yaireo/tagify/dist/react.tagify"
import "@yaireo/tagify/dist/tagify.css"
import {useHistory} from "react-router-dom"

import eventsHandler from "../handlers/EventsHandler"
import SocketContext from '../socketContext'
import {Alert} from "react-bootstrap";

localStorage.clear()

const Home = () => {
    const history = useHistory()
    const context = useContext(SocketContext)

    const stompClient = context.stompClient;
    const socket = context.socket;

    const [values, setValues] = useState('');

    const inputEl = useRef(null);
     let whitelist = [];

    useEffect(() => {
        function subscribe() {

            const subscriptions = []
            subscriptions.push(stompClient.subscribe('/topic/updates', function (messageOutput) {
                const data = JSON.parse(messageOutput.body);
                eventsHandler({
                    response: data,
                    settings: inputEl.current.settings,
                    setterValues: setValues
                })
            }))

            const sessionId = context.sessionId
            subscriptions.push(stompClient.subscribe('/queue/replay-' + sessionId, function (messageOutput) {
                const data = JSON.parse(messageOutput.body);
                if (data && (data.event === "GAME_CREATED" || data.event === "GAME_CONNECTED")) {
                    history.push('/game')
                    subscriptions.map((s) => s.unsubscribe())
                } else if (data && data.event === "ERROR_PLAY_YOURSELF"){
                    alert("You can't play with yourself!")
                }
            }))
        }
        if (context.sessionId === null) {

            stompClient.connect({}, function (frame) {
                const sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1]
                context.sessionId = sessionId
                subscribe();
            })
        } else {
            subscribe()
        }
    }, [])

    const testTransform = (tag) => {
        if (!tag.readonly &&inputEl.current.settings !== null) {
            if (inputEl.current.settings.whitelist.includes(tag.value)) {
                tag.__isValid = false;
                connectToGame(tag.value);
            } else {
                tag.__isValid = false;
                createNewGame(tag.value);
            }
            context.gameName = tag.value
        }
        tag.readonly = true;
        return tag;
    }

    const settings = {
        whitelist:whitelist,
        maxTags: 100,
        transformTag:testTransform,
        skipInvalid: true,
        dropdown: {
            maxItems: 20,
            classname: "tags-look",
            enabled: 0,
            closeOnSelect: false
        }
    }

    const createNewGame = (name) => {
        stompClient.send("/app/game/create", {}, name);
    }

    const connectToGame = (name) => {
        localStorage.setItem("gameRunning", "true");
        stompClient.send("/app/game/connect", {}, name);
    }

    const test = [
        {
            "value"    : "foo",
            "readonly" : true,
            "title"    : "read-only tag"
        },
        {
            "value"    : "bar"
        },
        {
            "value"    : "Locked tag",
            "editable" : false,
            "title"    : "Another readonly tag"
        }
    ]

    return(
        <>
            <div className={"row justify-content-md-center"}>
                <Alert variant="secondary">
                    <Alert.Heading>Welcome to the game of tic-tac-toe!</Alert.Heading>
                    <ul>
                        <li>To create a new game, just input a name.</li>
                        <li>To join, start typing a name and select it from the drop-down list.</li>
                        <li>The list of running games is displayed as tags.</li>
                    </ul>
                    <hr/>
                    <p>Enjoy the game!</p>
                </Alert>
            </div>
            <div className={"tagifyBlock"}>
                <Tags
                    settings={settings}
                    tagifyRef={inputEl}
                    value={values}
                />
            </div>
        </>
    )
}

export default Home;