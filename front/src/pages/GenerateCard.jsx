import {  useState,useContext } from "react";

import Header from "../components/Header";
import { UserContext } from "../context/UserContext";

const GenerateCard = () => {
  const [prompt, setPrompt] = useState("");
  const { user } = useContext(UserContext);

  const handleGenerateCard = async () => {
    console.log("generate card");
    const body ={
      userId : user.id,
      prompt : prompt
    }
    const response = await fetch("/api/requests/create",{
      method : "POST",
      headers : {
        "Content-Type" : "application/json"
      },
      body : JSON.stringify(body)
    });
    if(response.ok){
      console.log("Request created");
    }
  };

  return (
    <div className="min-h-screen bg-black text-white flex flex-col font-body">
      <Header />
      <div className="flex flex-col p-2 gap-3 items-center justify-center h-full">
        <form onSubmit={handleGenerateCard} className="flex flex-col">
          <div className="flex flex-col mb-4">
            <label>Prompt</label>
            <input
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              className="bg-gray-800"
            />
          </div>
          <button type="submit" className="bg-yellow-400 p-2 rounded">
            Generate
          </button>
        </form>
      </div>
    </div>
  );
};
export default GenerateCard;
