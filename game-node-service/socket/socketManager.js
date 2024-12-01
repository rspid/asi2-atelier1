const { handleBattle } = require("./battleHandler");
const { handleChat } = require("./chatHandler");

function initializeSocket(io) {
  console.log("🚀 Initializing socket");

  const connectedUsers = new Map();
  const battleRooms = new Map();

  const usersInBattle = new Map();

  io.on("connection", (socket) => {
    console.log("🟢 User connected:", socket.id);

    socket.on("join_battle", ({ userId }) => {
      // Vérifier si l'utilisateur est déjà en bataille
      if (usersInBattle.has(userId)) {
        console.log(`⚠️ User ${userId} is already in battle`);
        return;
      }
      console.log(`👤 User ${userId} (Socket: ${socket.id}) joining battle`);
      usersInBattle.set(userId, socket.id);
      connectedUsers.set(socket.id, userId);

      // Gérer d'abord la bataille
      handleBattle(io, socket, battleRooms);

      // Puis initialiser le chat pour la même room
      handleChat(io, socket, battleRooms);
    });

    socket.on("disconnect", () => {
      const userId = connectedUsers.get(socket.id);
      console.log(`🔴 User disconnected: ${socket.id} (userId: ${userId})`);

      // Nettoyer les rooms
      for (const [roomId, room] of battleRooms.entries()) {
        if (room.players.includes(socket.id)) {
          console.log(`🧹 Cleaning up room ${roomId}`);

          const remainingPlayers = room.players.filter(
            (id) => id !== socket.id
          );
          if (remainingPlayers.length > 0) {
            io.to(roomId).emit("battle_update", {
              status: "finished",
              reason: "opponent_disconnected",
            });
          }

          room.players = remainingPlayers;
          if (room.players.length === 0) {
            battleRooms.delete(roomId);
          }
        }
      }

      connectedUsers.delete(socket.id);
      if (userId) {
        usersInBattle.delete(userId);
      }
    });
  });
}

module.exports = { initializeSocket };
