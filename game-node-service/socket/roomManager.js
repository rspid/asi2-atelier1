function cleanupPlayerFromRoom(socket, battleRooms) {
  const roomId = [...socket.rooms].find((room) => room.startsWith("battle_"));
  if (!roomId) return null;

  console.log(`🧹 Cleaning up player ${socket.id} from room ${roomId}`);
  const room = battleRooms.get(roomId);

  if (room) {
    // Retirer le joueur de la room
    room.players = room.players.filter((id) => id !== socket.id);
    socket.leave(roomId);

    // Si la room est vide, la supprimer
    if (room.players.length === 0) {
      battleRooms.delete(roomId);
    }

    return { roomId, remainingPlayers: room.players };
  }

  return null;
}

module.exports = { cleanupPlayerFromRoom };
