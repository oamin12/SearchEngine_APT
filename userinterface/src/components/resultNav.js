import React, { useEffect } from "react";
import classes from "./style/resultnav.module.css";
import duck from "../img/duck.png";
const resultNav = () => {
  return (
    <div className={classes.resultnav}>
      <img className={classes.image} src={duck} alt="duck" />
      <h1>Quack bruhy</h1>
      <input className={classes.searchbar}></input>
    </div>
  );
};
export default resultNav;
