import { getSocket, initSocket } from "../socket";

export function BattleSearchManager({
  onSearchStart,
  userId,
  setIsConnected,
  isConnected,
  userCards,
}) {
  function startSearch() {
    const socket = initSocket();
    socket.connect();
    socket.emit("join_battle", { userId, userCards });
    setIsConnected(true);
    onSearchStart();
  }

  function cancelSearch() {
    const socket = getSocket();
    if (socket) {
      socket.disconnect();
    }
    setIsConnected(false);
  }

  return (
    <div className="flex flex-col items-center gap-4">
      {!isConnected ? (
        <button
          onClick={startSearch}
          className="bg-yellow-400 text-black px-8 py-4 rounded-lg text-xl font-bold hover:bg-yellow-500 transition-colors"
        >
          Find a Game
        </button>
      ) : (
        <button
          onClick={cancelSearch}
          className="bg-red-500 text-white px-8 py-4 rounded-lg text-xl font-bold hover:bg-red-600 transition-colors"
        >
          Cancel Search
        </button>
      )}
    </div>
  );
}
