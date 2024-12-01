const CardInBattle = ({
  card,
  onClick,
  isOpponent,
  isSelected,
  isPlayable = false,
  oneCardIsSelected = false,
}) => {
  const opponentCanBeSelected = isOpponent && oneCardIsSelected;
  return (
    <div
      className={`
        relative w-32 h-48 bg-gray-800 rounded-xl overflow-hidden  flex flex-col
        ${
          isPlayable && !isOpponent
            ? "cursor-pointer hover:scale-105 transition-transform"
            : ""
        }
        ${!isPlayable && !isOpponent ? "opacity-50 cursor-not-allowed" : ""}
        ${
          opponentCanBeSelected
            ? "cursor-crosshair hover:scale-105 transition-transform"
            : ""
        }
        ${
          isSelected && !isOpponent
            ? "shadow-[0_0_15px_3px] shadow-yellow-500"
            : ""
        }
        transition-all duration-200
      `}
      onClick={onClick}
    >
      <div className="absolute top-0 left-0 right-0 h-10 bg-gradient-to-b from-black/80 to-transparent z-10">
        <div className="flex justify-between items-center p-2">
          <span className="text-sm font-bold text-white truncate max-w-[50%] drop-shadow-lg">
            {card.name}
          </span>

          <div className="relative">
            <div className="absolute inset-0 bg-red-500 rounded-lg blur-sm opacity-50" />

            <div
              className="relative flex items-center bg-gradient-to-br from-red-600 to-red-800 rounded-lg px-2 py-1
                          border-2 border-red-400 shadow-lg"
            >
              <div className="flex items-center">
                <span className="font-bold text-white mr-1 text-[14px]">
                  {card.hp}
                </span>
                <span className="text-[12px] text-red-200 font-semibold">
                  HP
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <img
        src={"/images/magic.jpg"}
        alt={card.name}
        className="object-cover w-full h-full pt-8"
      />

      <div className="absolute bottom-1 w-full left-0 px-2 flex justify-between">
        <div className="bg-yellow-500 text-black rounded-full w-8 h-8 flex items-center justify-center">
          <span className="font-bold">{card.energy}</span>
        </div>

        {/* Attaque à droite */}
        <div className="bg-red-600 text-white rounded-full w-8 h-8 flex items-center justify-center ">
          <span className="font-bold">{card.attack}</span>
        </div>
      </div>
    </div>
  );
};

export default CardInBattle;
