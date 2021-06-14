function eventsHandler(props){
    switch (props.response.event) {
        case 'UPDATE_GAMES_LIST':
            updateWhiteList(props.settings, props.response.availableGames);
            updateTags(props.setterValues, props.response.closedGames)
            break
    }
}

function updateWhiteList(settings, newElements){
    settings.whitelist = newElements;
}

function updateTags(setter, closedGames){
    if (closedGames !== null && closedGames !== undefined){
        setter(closedGames.map(game => {return{value: game, readonly: true}}))
    }
}
export default eventsHandler