import React, { useEffect } from "react";
import classes from "./style/nav.module.css";
import duck from "../img/duck.png";
const resultNav = () => {
  return (
    <div className={classes.resultnav}>
      <img className={classes.image} src={duck} alt="duck" />
      <h1>Quacky</h1>
    </div>
  );
};

export default resultNav;
