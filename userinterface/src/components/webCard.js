import React from "react";
import { FaGlobeAfrica } from "react-icons/fa";
import classes from "./style/webcard.module.css";

const webCard = (props) => {
  return (
    <div className={classes.main}>
      <div className={classes.websitetop}>
        <div>
          <FaGlobeAfrica size={5} />
        </div>
        <div className={classes.websiteinfo}>
          <label>{props.title}</label>
          <label>{props.link}</label>
        </div>
      </div>
      <div className={classes.hyperlink}>Website hyperlink</div>
      <div className={classes.websitedesc}>
        <p>{props.desc}</p>
      </div>
    </div>
  );
};
export default webCard;
