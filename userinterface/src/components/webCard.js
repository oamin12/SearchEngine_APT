import React from "react";
import { HiOutlineGlobeAmericas } from "react-icons/hi";
import classes from "./style/webCard.module.css";

const WebCard = (props) => {
  return (
    <div className={classes.main}>
      <div className={classes.websitetop}>
        <div>
          <HiOutlineGlobeAmericas size={5} />
        </div>
        <div className={classes.websiteinfo}>
          <label>Website Name</label>
          <label>Website Link</label>
        </div>
      </div>
      <div className={classes.hyperlink}>Website hyperlink</div>
      <div className={classes.websitedesc}>
        <p>Website Description</p>
      </div>
    </div>
  );
};
export default WebCard;
