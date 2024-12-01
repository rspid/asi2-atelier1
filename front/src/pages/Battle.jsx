import { useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import BattleArea from "../components/BattleArea";
import { BattleSearchManager } from "../components/BattleSearchManager";
import Chat from "../components/Chat";
import Header from "../components/Header";
import { UserContext } from "../context/UserContext";
import { getSocket } from "../socket";

const Battle = () => {
  const { user } = useContext(UserContext);
  const socket = getSocket();
  const navigate = useNavigate();
  const [isConnected, setIsConnected] = useState(socket?.connected || false);
  const [gameState, setGameState] = useState("idle");
  const [searchTime, setSearchTime] = useState(0);
  const [timer, setTimer] = useState(null);
  const [messages, setMessages] = useState([]);
  const [gameData, setGameData] = useState(null);
  const [selectedCard, setSelectedCard] = useState(null);
  const location = useLocation();

  useEffect(() => {
    if (!socket) return;

    function onConnect() {
      setIsConnected(true);
    }

    function onDisconnect() {
      setIsConnected(false);
      setGameState("idle");
      if (timer) {
        clearInterval(timer);
        setTimer(null);
      }
    }

    function onGameStart(data) {
      //console.log("🎮 Game started:", data);
      setGameState("playing");
      setGameData(data);
      console.log("🎮 Game started:", data);
      // setOpponentCards(data.opponentCards);
      // setPlayerCards(data.playerCards);
      if (timer) {
        clearInterval(timer);
        setTimer(null);
      }
    }

    function onBattleUpdate(data) {
      setGameData(data);
      if (data.status === "finished") {
        const isWinner = data.winner === socket.id;
        navigate("/result", { state: { isWinner } });
        socket.disconnect();
      }
    }

    function onMessage(message) {
      setMessages((prev) => [...prev, message]);
    }

    socket.on("connect", onConnect);
    socket.on("disconnect", onDisconnect);
    socket.on("game_start", onGameStart);
    socket.on("battle_update", onBattleUpdate);
    socket.on("message", onMessage);

    return () => {
      socket.off("connect", onConnect);
      socket.off("disconnect", onDisconnect);
      socket.off("game_start", onGameStart);
      socket.off("battle_update", onBattleUpdate);
      socket.off("message", onMessage);

      if (timer) {
        clearInterval(timer);
      }
    };
  }, [timer, socket]);

  useEffect(() => {
    return () => {
      if (socket) {
        console.log("🔌 Déconnexion due au changement de route");
        socket.disconnect();
      }
    };
  }, [location, socket]);

  const handleSearchStart = () => {
    setGameState("searching");
    const newTimer = setInterval(() => {
      setSearchTime((prev) => prev + 1);
    }, 1000);
    setTimer(newTimer);
  };

  const sendMessage = (text) => {
    if (!socket) return;
    const message = { userId: user.id, text, timestamp: Date.now() };
    socket.emit("send_message", message);
  };

  const handleCardSelect = (card) => {
    if (gameState !== "playing") return;

    if (selectedCard?.id === card.id) {
      setSelectedCard(null);
    } else {
      setSelectedCard(card);
    }
  };

  const handleTargetSelect = (targetCard) => {
    if (!selectedCard || !gameData) return;
    socket.emit("play_card", {
      battleId: gameData.id,
      cardId: selectedCard.id,
      targetId: targetCard.id,
    });

    setSelectedCard(null);
  };

  const handleEndTurn = () => {
    if (!gameData || gameState !== "playing") return;
    socket.emit("end_turn", { battleId: gameData.id });
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, "0")}`;
  };

  return (
    <div className="h-screen bg-black text-white flex flex-col font-body">
      <Header />
      <div className="flex-1">
        {!isConnected ? (
          <div className="flex flex-col items-center justify-center h-full">
            <BattleSearchManager
              onSearchStart={handleSearchStart}
              userId={user.id}
              setIsConnected={setIsConnected}
              isConnected={isConnected}
            />
          </div>
        ) : gameState === "searching" ? (
          <div className="flex flex-col items-center justify-center h-full">
            <div className="mb-4 text-2xl">Searching for opponent...</div>
            <div className="text-xl text-yellow-400">
              Time elapsed: {formatTime(searchTime)}
            </div>
            <div className="mt-8 animate-spin text-4xl">🎮</div>
            <BattleSearchManager
              onSearchStart={handleSearchStart}
              userId={user.id}
              setIsConnected={setIsConnected}
              isConnected={isConnected}
            />
          </div>
        ) : (
          <div className="flex flex-row h-full">
            <BattleArea
              gameData={gameData}
              onCardSelect={handleCardSelect}
              onTargetSelect={handleTargetSelect}
              selectedCard={selectedCard}
              onEndTurn={handleEndTurn}
            />
            <div className="flex py-4 h-full w-92 px-4">
              <Chat
                messages={messages}
                sendMessage={sendMessage}
                currentUserId={user.id}
              />
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Battle;
