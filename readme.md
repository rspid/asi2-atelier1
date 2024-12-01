# Projet de Microservices

Ce projet est une application distribuée basée sur une architecture de microservices, permettant la gestion d'une application interactive de cartes.

---

## 🚀 Équipe de Développement
- **Ahmet Turk** : Développement du service de calcul des propriétés & génération d'images.
- **Matias Chatelet** : Génération de texte & génération d'images.
- **Antoine Adjamidis** : Gestion des demandes (Request Management).
- **David Pichard** : Développement de l'API Gateway (front-end en React) & Discovery Service.

---

## 🌐 Référentiel GitHub
[👉 Accéder au dépôt GitHub](https://github.com/rspid/asi2-atelier1/tree/main)

---

## ✅ Fonctionnalités Réalisées
### Front-End
- **Interface utilisateur** : Développée en React, permet d'interagir avec les différents microservices.

### Back-End
- **Génération d'images** : Service opérationnel pour créer des images à partir de données fournies.
- **Génération de texte** : Service fonctionnel pour générer des descriptions textuelles.
- **Request Management** : Gère les interactions entre microservices (à l'exception du service de calcul des propriétés).
- **Sauvegarde de données** : Permet la sauvegarde des messages échangés entre utilisateurs.
- **Fonctionnalités principales** :
  - Création de comptes utilisateurs.
  - Connexion sécurisée pour les utilisateurs.
  - Consultation des cartes disponibles pour chaque utilisateur.
  - Interaction avec d'autres utilisateurs via un chat.
  - Possibilité de jouer une partie avec un autre utilisateur.

---

## ❌ Fonctionnalités Non Réalisées
- **CI/CD** : Les pipelines pour la génération des images Docker et leur hébergement sur le Container Registry de GitLab n'ont pas été configurés.

---

## 📋 Fonctionnalités Disponibles
- Génération de cartes (fonctionnelle, mais pas encore accessible via l'interface).
- Gestion des utilisateurs : création et connexion.
- Consultation des cartes disponibles.
- Jeu en ligne avec d'autres utilisateurs.
- Chat en temps réel entre utilisateurs.

---

## ⚡ Lancer le Projet

### Pour Windows :
1. Exécuter le fichier `start.bat`.

### Pour Mac :
1. Lancer le script `start-service.sh`.

### Accéder à l'Application Web :
- Ouvrez votre navigateur et rendez-vous à l'adresse suivante : `http://localhost:8080`

