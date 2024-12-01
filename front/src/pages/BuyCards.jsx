import { useContext, useEffect, useState } from "react";

import Card from "../components/Card";
import Header from "../components/Header";
import { UserContext } from "../context/UserContext";

const BuyCards = () => {
  //const { cards, setCards, user } = useContext(UserContext);
  const { user, setUser } = useContext(UserContext);
  const [buyableCards, setBuyableCards] = useState([]);
  const [message, setMessage] = useState("");
  const [selectedCard, setSelectedCard] = useState(null);

  useEffect(() => {
    if (buyableCards.length === 0) {
      fetch("/api/spring/store/cards_to_sell")
        .then((response) => response.json())
        .then((data) => setBuyableCards(data));
    }
  }, []);
  //console.log(cards);

  //const userCards = cards.filter((card) => user.cardList.includes(card.id));

  const handleBuyCard = () => {
    if (selectedCard) {
      const sellData = {
        user_id: user.id,
        card_id: selectedCard.id,
      };

      fetch("/api/spring/store/buy", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(sellData),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Erreur lors de la vente de la carte");
          }
          return response.json();
        })
        .then(() => {
          setMessage("Carte achetée avec succès !");
          setBuyableCards((cards) =>
            cards.filter((card) => card.id !== selectedCard.id)
          );
          setUser((user) => ({
            ...user,
            account: user.account - selectedCard.price,
            cardList: [...user.cardList, selectedCard.id],
          }));
          setSelectedCard(null);
        })
        .catch((error) => {
          setMessage(`Erreur : ${error.message}`);
        });
    } else {
      setMessage("Veuillez sélectionner une carte et entrer un prix valide.");
    }
  };

  const resetAndSelectCard = (card) => {
    setSelectedCard(card);
    setMessage("");
  };
  return (
    <div className="min-h-screen bg-black text-white flex flex-col font-body">
      <Header />
      <div className="flex flex-col p-2 gap-3 items-center">
        <h1>Select a card to buy</h1>
        <div className="flex gap-9 flex-wrap items-start w-full">
          {buyableCards.length > 0 ? (
            buyableCards.map((card) => (
              <Card
                key={card.id}
                card={card}
                onClick={() => resetAndSelectCard(card)}
                isSelected={selectedCard && selectedCard.id === card.id}
              />
            ))
          ) : (
            <p>Aucune carte à afficher.</p>
          )}
        </div>
        {selectedCard && (
          <div className="flex flex-col">
            <button
              onClick={handleBuyCard}
              className="mt-2 bg-blue-500 text-white p-2 rounded"
            >
              Buy card
            </button>
          </div>
        )}
        {message && <p className="text-green-500">{message}</p>}
      </div>
    </div>
  );
};
export default BuyCards;
