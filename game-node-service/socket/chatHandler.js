function handleChat(io, socket, battleRooms) {
  socket.on("send_message", (message) => {
    console.log("📨 Message reçu de", socket.id);
    console.log("🔍 Rooms du socket:", socket.rooms);

    const roomId = [...socket.rooms].find((room) => room.startsWith("battle_"));
    console.log("🎯 Room trouvée:", roomId);

    if (roomId) {
      console.log(`💬 Envoi du message dans la room ${roomId}`);
      // io.to(roomId).emit("new_message", {
      //   userId: socket.id,
      //   text: message,
      //   timestamp: Date.now(),
      // });
      io.to(roomId).emit("message", message);
    } else {
      console.log("⚠️ Aucune room de bataille trouvée pour ce socket");
    }
  });
}

module.exports = { handleChat };
