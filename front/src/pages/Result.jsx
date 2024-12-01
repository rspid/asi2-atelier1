import { useLocation } from "react-router-dom";
import Header from "../components/Header";

const Result = () => {
  const { isWinner } = useLocation().state;
  return (
    <div className="min-h-screen bg-black text-white flex flex-col font-body">
      <Header />
      <div className="flex-1 flex flex-col items-center justify-center">
        {isWinner ? "You won" : "You lost"}
      </div>
    </div>
  );
};

export default Result;
