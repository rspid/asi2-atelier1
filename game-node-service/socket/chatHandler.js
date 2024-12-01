const Stomp = require('stomp-client');

// Configuration de l'ESB (ActiveMQ)
const activeMqHost = 'activemq';
const activeMqPort = 61613;       // Port STOMP par défaut
const queueName = '/queue/messageQueue'; // Nom de la queue où envoyer les messages
const username = 'myuser';        // Nom d'utilisateur ActiveMQ
const password = 'mypwd';         // Mot de passe ActiveMQ

const client = new Stomp(activeMqHost, activeMqPort, username, password);


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

      // Enregistrer le message dans la base de données
      const receiverId = battleRooms.get(roomId).players.find(
        (id) => id !== socket.id
      );

      const messagePayload = {
        senderId: socket.id,
        receiverId: receiverId,
        content: message,
        messageDate: new Date().toISOString(),
      };

      saveMessage(messagePayload);
      
    } else {
      console.log("⚠️ Aucune room de bataille trouvée pour ce socket");
    }
  });
}

function saveMessage(message){
  const messagePayload = JSON.stringify(message);

    // Connexion au serveur ActiveMQ
    client.connect((sessionId) => {
        console.log('Connected to ActiveMQ with session ID:', sessionId);

        // Envoi du message à la queue
        client.publish(queueName, messagePayload, {}, () => {
            console.log('Message sent to queue:', queueName);
            client.disconnect(() => {
                console.log('Disconnected from ActiveMQ.');
            });
        });
    }, (error) => {
        console.error('Failed to connect to ActiveMQ:', error);
    });
}

module.exports = { handleChat };
