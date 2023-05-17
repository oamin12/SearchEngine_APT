import React from 'react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import classes from './style/layout.module.css'
import ArrowRightAltIcon from '@mui/icons-material/ArrowForward';
import duck from '../img/duck.png'
import ducky2 from '../img/giphy.gif'
import axios from 'axios'

const Layout = () => {
  const [search, setSearch] = useState('')
  const [results, setResults] = useState([])
  const navigate = useNavigate();
  let audio = new Audio("/DuckQuack.mp3")

  function handleEnter(e){
    if(e.key==='Enter'){
      audio.play()
      sendSearch()
    }
  }
  
  function sendSearch(){
    audio.play()
    if(search===''){
      return
    }
    navigate("/results/"+search);
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
            <input onKeyDown={handleEnter} type='text' placeholder='Search Quacky' onChange={(e)=>setSearch(e.target.value)} />
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