import { io } from "socket.io-client";

let socket;

export const initSocket = () => {
  socket = io("http://localhost:3000", {
    autoConnect: false,
    reconnection: true,
    reconnectionAttempts: 5,
    reconnectionDelay: 1000,
  });

  // Gestionnaires d'événements de connexion
  socket.on("connect", () => {
    console.log("Connected to server");
  });

  socket.on("connect_error", (error) => {
    console.error("Connection error:", error);
  });

  socket.on("disconnect", (reason) => {
    console.log("Disconnected:", reason);
  });

  return socket;
};

// Fonction pour obtenir l'instance du socket
export const getSocket = () => {
  if (!socket) {
    return initSocket();
  }
  return socket;
};

// Fonctions utilitaires pour les événements de bataille
export const battleEvents = {
  // Rejoindre une partie
  joinBattle: (userData) => {
    socket.emit("join_battle", userData);
  },

  // Jouer une carte
  playCard: (cardData) => {
    socket.emit("play_card", cardData);
  },

  // Envoyer un message dans le chat
  sendMessage: (message) => {
    socket.emit("send_message", message);
  },
};

// Gestionnaires d'événements pour la bataille
export const battleListeners = {
  onGameStart: (callback) => {
    socket.on("game_start", callback);
  },

  onOpponentMove: (callback) => {
    socket.on("opponent_move", callback);
  },

  onGameEnd: (callback) => {
    socket.on("game_end", callback);
  },

  onMessage: (callback) => {
    socket.on("message", callback);
  },

  onError: (callback) => {
    socket.on("error", callback);
  },
};

// Fonction pour nettoyer les écouteurs d'événements
// export const cleanupListeners = () => {
//   if (socket) {
//     socket.removeAllListeners();
//   }
// };
export const cleanupListeners = () => {
  const socket = getSocket();

  // Retirer tous les listeners spécifiques
  socket.off("game_start");
  socket.off("opponent_move");
  socket.off("new_message");
  socket.off("game_end");

  console.log("🧹 Listeners nettoyés");
};
