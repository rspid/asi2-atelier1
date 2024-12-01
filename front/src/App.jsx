import { useContext } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { UserContext, UserProvider } from "./context/UserContext";
import Battle from "./pages/Battle";
import BuyCards from "./pages/BuyCards";
import GenerateCard from "./pages/GenerateCard";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Result from "./pages/Result";
import SellCards from "./pages/SellCards";

// const PrivateRoute = ({ element }) => {
//   const user = localStorage.getItem("user");
//   return user ? element : <Navigate to="/login" />;
// };

const PrivateRoute = ({ element }) => {
  const { user } = useContext(UserContext);

  return user ? element : <Navigate to="/login" />;
};

const App = () => {
  return (
    <UserProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route path="/" element={<PrivateRoute element={<Home />} />} />
        <Route
          path="/sell"
          element={<PrivateRoute element={<SellCards />} />}
        />
        <Route path="/buy" element={<PrivateRoute element={<BuyCards />} />} />
        <Route
          path="/generate"
          element={<PrivateRoute element={<GenerateCard />} />}
        />
        <Route path="/battle" element={<PrivateRoute element={<Battle />} />} />
        <Route path="/result" element={<PrivateRoute element={<Result />} />} />

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </UserProvider>
  );
};

export default App;
