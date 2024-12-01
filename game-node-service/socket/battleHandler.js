const BattleManager = require("../game/BattleManager");
const battleManager = new BattleManager();

function handleBattle(io, socket, battleRooms,userCardsMap) {
  console.log("🎮 Début handleBattle pour socket:", socket.id);
  console.log(
    "📊 État actuel des battleRooms:",
    Array.from(battleRooms.entries())
  );

  // Recherche d'un adversaire disponible
  let opponentFound = false;

  for (const [roomId, room] of battleRooms.entries()) {
    if (room.players.length === 1) {
      console.log(`✅ Match trouvé! Room ${roomId}`);
      console.log(`👥 Joueurs: ${room.players[0]} vs ${socket.id}`);

      socket.join(roomId);
      room.players.push(socket.id);
      opponentFound = true;

      const player1SocketId = room.players[0];
      const player2SocketId = socket.id;
      console.log("userCardsMap", userCardsMap);

      const player1Cards = Array.from(userCardsMap.values())[0];
      const player2Cards =  Array.from(userCardsMap.values())[1];

      console.log("player1Cards", player1Cards);
      console.log("player2Cards", player2Cards);

      // Initialiser la bataille dans le BattleManager
      const battleState = battleManager.createBattle(
        roomId,
        player1SocketId,
        player2SocketId,
        player1Cards,
        player2Cards
      );

      console.log(`🎲 Battle créée dans room ${roomId}:`, battleState);

      // Informer les deux joueurs que la partie commence
      // io.to(roomId).emit("game_start", {
      //   battleId: roomId,
      //   playerCards: battleState.players[socket.id].cards,
      //   opponentCards: battleState.players[room.players[0]].cards,
      //   currentTurn: battleState.currentTurn,
      // });
      io.to(roomId).emit("game_start", battleState);

      break;
    }
  }

  if (!opponentFound) {
    const roomId = `battle_${Date.now()}`;
    console.log(`🆕 Création nouvelle room: ${roomId} pour ${socket.id}`);

    socket.join(roomId);
    battleRooms.set(roomId, {
      players: [socket.id],
      state: "waiting",
    });
  }

  // Gestion des cartes jouées
  socket.on("play_card", ({ battleId, cardId, targetId }) => {
    console.log(`Action reçu de ${socket.id}`);
    console.log(
      `🃏 Carte ${cardId} attaque ${targetId} dans bataille ${battleId}`
    );

    const result = battleManager.playCard(battleId, cardId, targetId);
    if (result) {
      console.log("result", result);
      // Envoyer l'état mis à jour aux deux joueurs
      const room = battleRooms.get(battleId);
      //const nextTurn = room.players.find((id) => id !== socket.id);
      room.players.forEach((playerId) => {
        const playerState = battleManager.getPublicBattleState(
          battleId,
          playerId
        );

        // const updatedState =
        //   result.status === "finished"
        //     ? { ...playerState, status: "finished", winner: result.winner }
        //     : {
        //         ...playerState,
        //         currentTurn: nextTurn,
        //       };
        const updatedState =
          result.status === "finished"
            ? { ...playerState, status: "finished", winner: result.winner }
            : {
                ...playerState,
              };
        io.to(playerId).emit("battle_update", updatedState);
      });

      // Si la partie est terminée
      if (result.status === "finished") {
        // io.to(battleId).emit("battle_update", {
        //   status: "finished",
        //   winner: result.winner,
        // });

        // Déconnecter les joueurs
        room.players.forEach((playerId) => {
          const playerSocket = io.sockets.sockets.get(playerId);
          if (playerSocket) {
            playerSocket.disconnect();
          }
        });
      }
    }
  });

  socket.on("end_turn", ({ battleId }) => {
    const result = battleManager.endTurn(battleId);
    if (result) {
      const room = battleRooms.get(battleId);
      room.players.forEach((playerId) => {
        const playerState = battleManager.getPublicBattleState(
          battleId,
          playerId
        );
        io.to(playerId).emit("battle_update", playerState);
      });
    }
  });
}

module.exports = { handleBattle };
