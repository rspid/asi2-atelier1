const express = require("express");
const { createServer } = require("http");
const { Server } = require("socket.io");
const { initializeSocket } = require("./socket/socketManager");

const app = express();
const httpServer = createServer(app);
const io = new Server(httpServer, {
  cors: {
    // origin: "http://localhost:5173", // Ajustez selon votre frontend
    origin: "http://localhost:5173",
    methods: ["GET", "POST"],
  },
});

// Initialize socket handlers
initializeSocket(io);

const PORT = process.env.PORT || 3000;
httpServer.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
