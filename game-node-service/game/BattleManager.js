class BattleManager {
  constructor() {
    this.battles = new Map();
  }

  createBattle(roomId, player1Id, player2Id, player1Cards, player2Cards) {
    const battle = {
      id: roomId,
      players: [
        {
          id: player1Id,
          cards: player1Cards,
          energy: 1,
        },
        {
          id: player2Id,
          cards: player2Cards,
          energy: 1,
        },
      ],
      status: "active",
      currentTurn: player1Id,
      lastMove: null,
    };

    this.battles.set(roomId, battle);
    return battle;
  }

  generateInitialCards(playerId) {
    const possibleAttacks = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100];
    const possibleHPs = [20, 30, 40, 50, 60, 70, 80, 90, 100];
    const cardNames = ["Dragon", "Phoenix", "Demon", "Angel", "Reaper"];

    const calculateCardEnergy = (attack) => {
      return attack < 40 ? 1 : attack < 70 ? 2 : 3;
    };

    return Array(5)
      .fill()
      .map((_, index) => {
        const attack =
          possibleAttacks[Math.floor(Math.random() * possibleAttacks.length)];
        return {
          id: `card_${playerId}_${Date.now()}_${index}`,
          name: cardNames[index],
          attack: attack,
          hp: possibleHPs[Math.floor(Math.random() * possibleHPs.length)],
          energy: calculateCardEnergy(attack),
        };
      });
  }

  playCard(battleId, cardId, targetId) {
    const battle = this.battles.get(battleId);
    if (!battle) return null;

    const attackingCard = this.findCard(battle, cardId);
    const targetCard = this.findCard(battle, targetId);

    if (!attackingCard || !targetCard) return null;

    const currentPlayer = battle.players.find(
      (player) => player.id === battle.currentTurn
    );
    if (currentPlayer.energy < attackingCard.energy) {
      return {
        status: "error",
        message: "Not enough energy",
        battleState: battle,
      };
    }

    currentPlayer.energy -= attackingCard.energy;
    // Appliquer les dégâts
    targetCard.hp -= attackingCard.attack;

    console.log(
      `🗡️ Carte ${cardId} attaque ${targetId}: ${attackingCard.attack} dégâts`
    );
    console.log(`💝 Carte ${targetId} HP restants: ${targetCard.hp}`);

    // Vérifier si la carte est détruite
    if (targetCard.hp <= 0) {
      console.log(`💀 Carte ${targetId} détruite`);
      this.removeCard(battle, targetId);
    }

    // Vérifier si un joueur a perdu toutes ses cartes
    const status = this.checkGameStatus(battle);

    return {
      status,
      winner: status === "finished" ? this.determineWinner(battle) : null,
      battleState: battle,
    };
  }

  findCard(battle, cardId) {
    for (const player of battle.players) {
      const card = player.cards.find((c) => c.id === cardId);
      if (card) return card;
    }
    return null;
  }

  removeCard(battle, cardId) {
    battle.players.forEach((player) => {
      player.cards = player.cards.filter((c) => c.id !== cardId);
    });
  }

  checkGameStatus(battle) {
    // Vérifier si un joueur a toutes ses cartes avec 0 HP ou moins
    for (const player of Object.values(battle.players)) {
      const hasLivingCards = player.cards.some((card) => card.hp > 0);
      if (!hasLivingCards) {
        return "finished";
      }
    }
    return "active";
  }

  determineWinner(battle) {
    const winner = battle.players.find((player) =>
      player.cards.some((card) => card.hp > 0)
    );
    return winner ? winner.id : null;
  }

  getPublicBattleState(battleId, playerId) {
    const battle = this.battles.get(battleId);
    if (!battle) return null;

    return {
      ...battle,
      winner:
        battle.status === "finished" ? this.determineWinner(battle) : null,
    };

    // return {
    //   battleId,
    //   status: battle.status,
    //   playerCards: battle.players[playerId].cards,
    //   opponentCards: battle.players[opponentId].cards,
    //   currentTurn: battle.currentTurn,
    //   winner:
    //     battle.status === "finished" ? this.determineWinner(battle) : null,
    // };
  }

  endTurn(battleId) {
    const battle = this.battles.get(battleId);
    if (!battle) return null;

    const nextPlayer = battle.players.find(
      (player) => player.id !== battle.currentTurn
    );
    nextPlayer.energy += 1;
    battle.currentTurn = nextPlayer.id;
    return {
      status: "active",
      battleState: battle,
    };
  }

  endBattle(battleId) {
    return this.battles.delete(battleId);
  }
}

module.exports = BattleManager;
