import { useContext, useEffect, useState } from "react";
import Card from "../components/Card";
import Header from "../components/Header";
import { UserContext } from "../context/UserContext";

const SellCards = () => {
  const { cards, setCards, user, setUser } = useContext(UserContext);
  const [message, setMessage] = useState("");
  const [selectedCard, setSelectedCard] = useState(null);

  useEffect(() => {
    if (cards.length === 0) {
      fetch("/api/spring/cards")
        .then((response) => response.json())
        .then((data) => setCards(data));
    }
  }, []);
  console.log(cards);
  if (cards.length === 0) {
    return <div>Loading...</div>;
  }
  const userCards = cards.filter((card) => user.cardList.includes(card.id));

  const handleSellCard = () => {
    if (selectedCard) {
      const sellData = {
        user_id: user.id,
        card_id: selectedCard.id,
        //price: parseFloat(price),
      };

      fetch("/api/spring/store/sell", {
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
          setMessage("Carte vendue avec succès !");
          setUser((user) => ({
            ...user,
            account: user.account + selectedCard.price,
            cardList: user.cardList.filter(
              (cardId) => cardId !== selectedCard.id
            ),
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
      <div className="flex flex-col p-2 gap-9 items-center">
        <h1>Select a card to sell it</h1>
        <div className="flex gap-2 items-start w-full flex-wrap">
          {userCards.length > 0 ? (
            userCards.map((card) => (
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
              onClick={handleSellCard}
              className="mt-2 bg-blue-500 text-white p-2 rounded"
            >
              Sell Card
            </button>
            {message && <p className="text-green-500">{message}</p>}
          </div>
        )}
      </div>
    </div>
  );
};
export default SellCards;
