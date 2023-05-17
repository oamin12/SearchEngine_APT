import React from 'react'
import { useState } from 'react'
import classes from './style/layout.module.css'
import ArrowRightAltIcon from '@mui/icons-material/ArrowForward';
import duck from '../img/duck.png'
import ducky2 from '../img/giphy.gif'
import axios from 'axios'

const Layout = () => {
  const [search, setSearch] = useState('')
  const [results, setResults] = useState([])

  var config = {
    method: 'get',
    url: "http://localhost:8080/words?q="+search,
  
    headers: {}
  };
  function sendSearch(){
    axios(config)
    .then(function (response) {
      console.log(JSON.stringify(response.data));
      setResults(response.data)
    })
    .catch(function (error) {
      console.log(error);
    });
  }
 
  // function searchDuck(){
  //   console.log(search)

  //   axios.get("http://localhost:8080/words?q=plane"+search)
  //   .then(res=>{
  //     console.log(res)
  //     setResults(res.data)
  //   }
  //   )
  //   .catch(err=>console.log(err))

  // }




  return (
    <div className={classes.layout}>
        {/*search box container*/}
        <div className={classes.searchBoxContainer}>
          <div className={classes.searchBox}>
            <img src={ducky2} alt='duck'/>
            <input type='text' placeholder='Search Quacky' onChange={(e)=>setSearch(e.target.value)} />
            <button onClick={sendSearch} >
              Quack Quack
              <ArrowRightAltIcon style={{fontSize:25}} />
            </button>
          </div>
      </div>
    </div>
  )
}

export default Layout