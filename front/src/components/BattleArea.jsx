import { getSocket } from "../socket";
import CardInBattle from "./CardInBattle";
import PlayerCharacter from "./PlayerCharacter";

const BattleArea = ({
  gameData,
  onCardSelect,
  onTargetSelect,
  selectedCard,
  onEndTurn,
}) => {
  const socket = getSocket();

  if (!socket || !gameData) {
    return null;
  }

  const playerCards = gameData.players.find((p) => p.id === socket.id).cards;
  const opponentCards = gameData.players.find((p) => p.id !== socket.id).cards;
  const player = gameData.players.find((p) => p.id === socket.id);
  const opponent = gameData.players.find((p) => p.id !== socket.id);
  const isMyTurn = gameData.currentTurn === socket.id;

  const handleAttack = (card) => {
    if (!isMyTurn || !selectedCard) return;
    onTargetSelect(card);
  };

  const handleCardSelect = (card) => {
    if (!isMyTurn) return;
    if (player.energy < card.energy) return;
    onCardSelect(card);
  };

  return (
    <div className="flex-1 flex flex-col p-4 gap-8">
      {/* Zone de l'adversaire */}
      <div className="flex-1 flex flex-col items-center">
        {/* <h2 className="text-xl mb-4">Opponent&apos;s Cards</h2> */}
        <PlayerCharacter player={opponent} isOpponent={true} />
        <div className="flex gap-4 flex-wrap">
          {opponentCards.map((card) => (
            <CardInBattle
              key={card.id}
              card={card}
              onClick={() => handleAttack(card)}
              isOpponent={true}
              oneCardIsSelected={selectedCard}
            />
          ))}
        </div>
      </div>

      {isMyTurn && (
        <div className="flex justify-center gap-4 items-center">
          <button
            onClick={onEndTurn}
            className="px-4 py-2 rounded-xl  transition-colors text-black bg-yellow-400 hover:bg-yellow-500 text-sm border-2 border-orange-400"
          >
            END TURN
          </button>
        </div>
      )}

      {/* Zone du joueur */}
      <div className="flex-1 flex flex-col items-center">
        <PlayerCharacter player={player} />
        <div className="flex gap-4 flex-wrap">
          {playerCards.map((card) => (
            <CardInBattle
              key={card.id}
              card={card}
              isSelected={selectedCard?.id === card.id}
              onClick={() => handleCardSelect(card)}
              isPlayable={isMyTurn && card.energy <= player.energy}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default BattleArea;
