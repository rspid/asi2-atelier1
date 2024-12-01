# Image de base Node.js
FROM node:18-alpine

# Création et définition du répertoire de travail
WORKDIR /usr/src/app

# Copie des fichiers package.json et package-lock.json
COPY package*.json ./

# Installation des dépendances
RUN npm install

# Copie du reste des fichiers du projet
COPY . .

# Exposition du port (à adapter selon votre configuration)
EXPOSE 3000

# Commande de démarrage
CMD ["npm", "start"]