import React, { useEffect } from "react";
import classes from "./style/nav.module.css";
import duck from "../img/duck.png";
import { NavLink } from "react-router-dom";

const Nav = () => {
  return (
    <div className={classes.nav}>
      <img className={classes.image} src={duck} alt="duck" />
      <NavLink to="/"><h1>Quacky</h1></NavLink>
    </div>
  );
};

export default Nav;
