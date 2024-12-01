import { useContext } from "react";
import { NavLink } from "react-router-dom";
import { UserContext } from "../context/UserContext";

const Header = () => {
  const { user } = useContext(UserContext);

  const truncedMoney = user.account.toFixed(1);

  return (
    <div className="w-full flex justify-between p-2 items-center h-16">
      <div className="flex gap-4">
        <NavLink
          to="/"
          className={({ isActive }) => (isActive ? "underline" : "")}
        >
          Home
        </NavLink>
        <NavLink
          to="/buy"
          className={({ isActive }) => (isActive ? "underline" : "")}
        >
          Buy cards
        </NavLink>
        <NavLink
          to="/sell"
          className={({ isActive }) => (isActive ? "underline" : "")}
        >
          Sell cards
        </NavLink>
        <NavLink
          to="/generate"
          className={({ isActive }) => (isActive ? "underline" : "")}
        >
          Create
        </NavLink>
        <NavLink
          to="/battle"
          className={({ isActive }) => (isActive ? "underline" : "")}
        >
          Battle
        </NavLink>
      </div>
      <div className="flex flex-col items-end">
        <span>ID : {user.id}</span>
        <span>{truncedMoney}$</span>
      </div>
    </div>
  );
};

export default Header;
