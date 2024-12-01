import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const { setUser } = useContext(UserContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("/api/spring/auth", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      const userId = await response.json();
      if (response.ok) {
        //setUser(data);
        console.log("Connexion réussie", userId);

        const userResponse = await fetch(`/api/spring/user/${userId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });
        const userData = await userResponse.json();
        //localStorage.setItem("userId", data);
        localStorage.setItem("user", JSON.stringify(userData));
        setUser(userData);

        navigate("/");
      } else {
        console.error("Erreur lors de la connexion", userId);
      }
    } catch (error) {
      console.error("Erreur réseau", error);
    }
  };

  return (
    <div className="min-h-screen bg-black text-white flex flex-col justify-center items-center font-body">
      <form onSubmit={handleSubmit} className="flex flex-col">
        <div className="flex flex-col mb-4">
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="bg-gray-800"
          />
        </div>
        <div className="flex flex-col mb-4">
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="bg-gray-800"
          />
        </div>
        <Link to="/register">
          <p>Pas de compte ? Créez un compte</p>
        </Link>
        <button type="submit" className="bg-yellow-400 p-2 rounded">
          Submit
        </button>
      </form>
    </div>
  );
};

export default Login;
