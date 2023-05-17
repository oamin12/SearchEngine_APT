import React from "react";
import resultNav from "../components/resultNav";
import WebCard from "../components/webCard";
import classes from "./results.module.css";
const webarr = [
  { title: "Google", link: "https://www.google.com", desc: "Search Engine" },
  { title: "DuckDuckGo", link: "https://www.duckduckgo.com", desc: "Search Engine" },
];
const Results = () => {
  return (
    <div className={classes.main}>
      {webarr.map((web) => {
        <WebCard title={web.title} link={web.link} desc={web.desc} />;
      })}
    </div>
  );
};
export default Results;
