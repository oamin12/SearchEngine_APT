import React from "react";
import { FaGlobeAfrica } from "react-icons/fa";
import classes from "./style/webcard.module.css";

const webCard = (props) => {
  return (
    <div className={classes.searchcard}>
      <h2 className={classes.searchcardtitle}>🦆  {props.title}</h2>
      <a href={props.url} className={classes.searchcardurl} >{props.url}</a>
      <p className={classes.searchcardcontent}>{props.content}</p>
  </div>
  );
};
export default webCard;
