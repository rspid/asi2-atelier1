const PlayerCharacter = ({ player }) => {
  console.log(player, "player");
  return (
    <div className="flex w-full justify-center items-center my-6">
      <div className="relative w-32 h-32 rounded-t-full overflow-hidden">
        <img
          src="/images/rexxar.jpg"
          alt="Hero"
          className="w-full h-full object-cover"
        />
        <div className="absolute bottom-0 left-0 w-6 h-6 flex justify-center items-center bg-yellow-400 text-black rounded-full">
          {player.energy}
        </div>
      </div>
    </div>
  );
};

export default PlayerCharacter;
