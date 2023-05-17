import React, { useEffect } from "react";
import classes from "./style/nav.module.css";
import duck from "../img/duck.png";
const Nav = () => {
  return (
    <div className={classes.nav}>
      <img className={classes.image} src={duck} alt="duck" />
      <h1>Quacky</h1>
    </div>
  );
};

export default Nav;
