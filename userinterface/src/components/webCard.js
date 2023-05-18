import React from "react";
import { FaGlobeAfrica } from "react-icons/fa";
import { useState, useEffect } from "react";
import classes from "./style/webcard.module.css";

const WebCard = (props) => {

  const [contentArray, setContentArray] = useState([]);
  useEffect(() => {
    setContentArray(props.content.split(" "));
  }, []);

  //the word at index 5 is the searched word
  //we want to make it bold
  //so we map through the array and make the word at index 5 bold
  function makeBold(word, index) {
    
    return 
  }

  return (
    <div className={classes.searchcard}>
      <h2 className={classes.searchcardtitle}>🦆  {props.title}</h2>
      <a href={props.url} className={classes.searchcardurl} >{props.url}</a>
      <p className={classes.searchcardcontent}>{contentArray.map((word, index) => {
        if (index === 4) {
          return (<b> {word} </b>);
        } else {
          return word+" ";
        }
      })
      }</p>
  </div>
  );
};
export default WebCard;
