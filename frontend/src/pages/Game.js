import React, {useContext} from 'react';
import '../Game.css'
import SocketContext from '../socketContext'
import ModalWindow from "../components/ModalWindow";


function Square(props) {
    return (
        <button className="square" onClick={props.onClick}>
            {props.value}
        </button>
    );
}

class Board extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            squares: Array(9).fill(null),
            xIsNext: true,
            sign: 'undefined',
            gameStarted: (localStorage.getItem("gameRunning") === "true" ? true : false),
            winner: null,
            window: {
                header: null,
                message: null,
            }
        }
        const linkThis = this;
        const gameName = linkThis.props.context.gameName
        this.subscribe = props.context.stompClient.subscribe('/topic/game/' + gameName, function (messageOutput) {
            const data = JSON.parse(messageOutput.body);

            if (data.event === 'UPDATE_BOARD'){
                linkThis.setState(Object.assign({
                    squares: data.board,
                    xIsNext: !linkThis.state.xIsNext,
                }, linkThis))
            } else if (data.event === 'GAME_STARTED'){
                linkThis.setState(Object.assign({
                    gameStarted: true,
                }, linkThis))
            } else if (data.event === 'GAME_ROLES') {
                const sign = (data.usersSign.X === linkThis.props.context.sessionId ? 'X' : 'O')
                linkThis.setState(Object.assign({
                    sign: sign,
                }, linkThis))
            } else if (data.event === 'GAME_OVER'){
                let window = {
                    message: "You win.",
                    header: "Congratulations!",
                }
                if (data.winner === 'DRAW'){
                    window = {
                        message: "Draw.",
                        header: "Opps!",
                    }
                } else if (data.winner !== linkThis.state.sign){
                    window = {
                        message: "You lost.",
                        header: "Sorry",
                    }
                }
                linkThis.setState(Object.assign({
                    winner: data.winner,
                    window: window
                }, linkThis))
            }
        })
    }

    handleClick(i) {
        if (this.state.squares[i] !== null) {
            return;
        }
        const outData = {
            event: "MOVE",
            id: this.props.context.sessionId,
            move: i,
        }
        const out = JSON.stringify(outData);
        this.props.context.stompClient.send("/app/game/" + this.props.context.gameName, {}, out);
    }

    renderSquare(i) {
        return <Square
            value={this.state.squares[i]}
            onClick={() => this.handleClick(i)}
        />;
    }

    render() {
        const sign =  'Your sing ' + this.state.sign;

        let status;
        if (!this.state.gameStarted){
            status = "Waiting for a partner."
        } else if (this.state.winner != null) {
            status = 'Win ' + this.state.winner;
        } else {
            status = 'Next move: ' + (this.state.xIsNext ? 'X' : 'O');
        }

        return (
            <div>
                <div className="status">{sign}</div>
                <div className="status">{status}</div>
                <div className="board-row">
                    {this.renderSquare(0)}
                    {this.renderSquare(1)}
                    {this.renderSquare(2)}
                </div>
                <div className="board-row">
                    {this.renderSquare(3)}
                    {this.renderSquare(4)}
                    {this.renderSquare(5)}
                </div>
                <div className="board-row">
                    {this.renderSquare(6)}
                    {this.renderSquare(7)}
                    {this.renderSquare(8)}
                </div>
                <ModalWindow
                    value={this.state.window}
                    show={this.state.winner !== null}
                    subscribe={this.subscribe}
                />
            </div>
        );
    }
}


const Game = (props) => {
    const context = useContext(SocketContext)

    return(
        <div className="game">
            <div className="game-board">
                <Board context={context} />
            </div>
        </div>
    )
}

export default Game;