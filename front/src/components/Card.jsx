const Card = ({ card, isSelected, onClick }) => {
  const hasRightImage = card.imgUrl !== "string";
  const truncedCardPrice = card.price.toFixed(1);
  return (
    <div
      onClick={onClick}
      className={`bg-gray-900 items-center justify-center flex flex-col rounded font-bold h-56 w-52 overflow-hidden object-cover relative ${
        isSelected
          ? "outline outline-3 outline-yellow-500"
          : "border-none cursor-pointer text-white"
      }`}
    >
      {hasRightImage ? (
        <img
          src={card.imgUrl}
          alt={card.description}
          className="w-full h-full"
        />
      ) : (
        <div className="flex w-full h-full justify-center items-center">
          <p className="text-center">{card.description}</p>
        </div>
      )}
      <div className="flex w-full absolute bottom-0 h-12 bg-slate-600/90 items-center justify-between px-2 text-yellow-300">
        <span>{card.id}</span>
        <span>{truncedCardPrice}€</span>
      </div>
    </div>
  );
};
export default Card;
