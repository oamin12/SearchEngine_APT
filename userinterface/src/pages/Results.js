import React from "react";
import resultNav from "../components/resultNav";
import WebCard from "../components/webCard";
import classes from "./style/results.module.css";
import Nav from '../components/Nav'
import Footer from '../components/Footer'
import Pagination from '@mui/material/Pagination';
import { margin } from "@mui/system";
import { useEffect } from "react";
import giphy2 from "../img/giphy.gif";
import lol from "../img/lol.gif";
const webarr = [
  { title: "Google", url: "https://www.google.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "AmonAmonGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "MosMosGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "amroyamroyGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "amramrGO", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "AmonAmonGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "MosMosGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "amroyamroyGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
  { title: "amramrGO", url: "https://www.duckduckgo.com", desc: "Search Engine" },

];
const Results = () => {

  const [page, setPage] = React.useState(1);
  const [count, setCount] = React.useState(Math.ceil(webarr.length/10));
  

  //take only a part of the array to display on the page according to the page number
  //each page has 10 results
  const start = (page-1)*10;
  const end = page*10;
  const [webarr2, setWebarr2] = React.useState(webarr.slice(start,end));
 // const webarr2 = webarr.slice(start,end);
  
 const handleChange = (event, value) => {
  setPage(value);
  setWebarr2([]); // Clear old results when page changes
};

useEffect(() => {
  const start = (page - 1) * 10;
  const end = page * 10;
  const slicedWebarr = webarr.slice(start, end);
  setWebarr2(slicedWebarr);
}, [page]);

      // useEffect(() => {
      //   if (page !== '') {
      //     window.location.reload();
      //   }
      // }, [page]);

  return (
    <div>
      <Nav/>
      <div className={classes.main}>
        <div className={classes.resultsArea}>
          {webarr2.map((web) => {
            return(
            <WebCard key={web.url} title={web.title} url={web.url} content={web.desc} />
            )
          })}
          <Pagination sx={{"marginLeft":"2rem","marginBottom":"3rem","marginTop":"2rem"}} count={count} page={page} onChange={handleChange} variant="outlined" shape="rounded" />
        </div>
        <div className={classes.gify}>
          <img src={lol} alt="gify" />
        </div>
      </div>
      <Footer/>
    </div>
  );
};
export default Results;
