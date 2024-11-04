#!/bin/bash

# Arrêter et supprimer les conteneurs, réseaux, volumes et images créés par `up`
docker-compose down --remove-orphans

# Construire les images des services définis dans le fichier docker-compose.yml
docker-compose build

# Démarrer les services en mode détaché
docker-compose up -d
 