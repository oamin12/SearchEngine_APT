import React from "react";
import { useParams } from "react-router-dom";
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
import axios from "axios";
// const webarr = [
//   { title: "Google", url: "https://www.google.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "AmonAmonGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "MosMosGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "amroyamroyGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "amramrGO", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "DuckDuckGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "AmonAmonGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "MosMosGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "amroyamroyGo", url: "https://www.duckduckgo.com", desc: "Search Engine" },
//   { title: "amramrGO", url: "https://www.duckduckgo.com", desc: "Search Engine" },

// ];
const Results = (props) => {

  const {id}= useParams();
  const [page, setPage] = React.useState(1);
  const [results, setResults] = React.useState([]);
  const [count, setCount] = React.useState(Math.ceil(results.length/10));
  const [responseTime, setResponseTime] = React.useState(0.0);
  const [startTime, setStartTime] = React.useState(0.0);
  const [endTime, setEndTime] = React.useState(0.0);

  //when component mounts, send request to backend with the id from the url
  const start = (page-1)*10;
  const end = page*10;
  const [webarr2, setWebarr2] = React.useState(results.slice(start,end));
  useEffect(() => {
    setStartTime(performance.now() );
    var config = {
      method: 'get',
      url: "http://localhost:8080/words?q="+id,
    
      headers: {}
    };
    axios(config)
    .then(function (response) {
      console.log(JSON.stringify(response.data));
      setResults(response.data)
      setCount(Math.ceil(response.data.length/10))
      setWebarr2(response.data.slice(start,end))
      setEndTime(performance.now());
      setResponseTime(endTime-startTime);


    })
    .catch(function (error) {
      console.log(error);
    });
    
    //reload the page when the page number changes


  },[]);



  //take only a part of the array to display on the page according to the page number
  //each page has 10 results
  
 // const webarr2 = webarr.slice(start,end);
  
 const handleChange = (event, value) => {
  setPage(value);
  setWebarr2([]); // Clear old results when page changes
};

useEffect(() => {
  const start = (page - 1) * 10;
  const end = page * 10;
  const slicedWebarr = results.slice(start, end);
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
        <div className={classes.StatusArea}>
          <h1 className={classes.heading}>Results for {id}</h1>
          <div className={classes.stats}>
            <p className={classes.stat}>({responseTime} milli seconds)</p>
          </div>
        </div>
        <div className={classes.resultsWithDucky}>
          <div className={classes.resultsArea}>
            {webarr2.map((web) => {
              return(
              <WebCard key={web.url} title={web.title} url={web.url} content={web.content} />
              )
            })}
            <Pagination sx={{"marginLeft":"2rem","marginBottom":"3rem","marginTop":"2rem"}} count={count} page={page} onChange={handleChange} variant="outlined" shape="rounded" />
          </div>
          <div className={classes.gify}>
            <img src={lol} alt="gify" />
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  );
};
export default Results;
