import React from 'react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import classes from './style/layout.module.css'
import ArrowRightAltIcon from '@mui/icons-material/ArrowForward';
import duck from '../img/duck.png'
import ducky2 from '../img/giphy.gif'
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

import axios from 'axios'
import { useEffect } from 'react';

const Layout = () => {
  const [search, setSearch] = useState('')
  const [results, setResults] = useState([])
  const [previosSearch, setPreviousSearch] = useState([])
  const [suggest, setSuggest] = useState([])
  const navigate = useNavigate();
  let audio = new Audio("/DuckQuack.mp3")




  function handleEnter(e){
    if(e.key==='Enter'){
      audio.play()
      sendSearch()
    }
  }
  useEffect(() => {
    if(sessionStorage.getItem('previous')!==null){
      setSuggest(sessionStorage.getItem('previous').split(','))
    }
    if(sessionStorage.getItem('previous')===null){
      sessionStorage.setItem('previous',"Quack")
    }
    
  }, [])

  function sendSearch(){
    audio.play()
    if(search===''){
      return
    }
    setPreviousSearch(...previosSearch,sessionStorage.getItem('previous'))
    
    sessionStorage.setItem('previous',sessionStorage.getItem('previous')+","+search)

    navigate("/results/"+search);
  }
 
 function handleChange(e){
    setSearch(e.target.value)
    console.log(e.target.value)
  }
  // (e)=>setSearch(e.target.value)

  return (
    <div className={classes.layout}>
        {/*search box container*/}
        <div className={classes.searchBoxContainer}>
          <div className={classes.searchBox}>
            <img src={ducky2} alt='duck'/>
            {/* <input onKeyDown={handleEnter} type='text' placeholder='Search Quacky' onChange={(e)=>setSearch(e.target.value)} /> */}
                <Autocomplete

                    inputValue={search}
                    onInputChange={(event, newInputValue) => {
                      setSearch(newInputValue);
                    }}
                    onKeyDown={handleEnter}
                    sx={{ "width": "70rem", "paddinLeft": "2rem",
                     "backgroundColor": "rgba(250, 235, 215, 0.853)"
                     , "borderRadius": "10px",
                      border: "none",
                      }}
                    id="free-solo-demo"
                    freeSolo
                    options={suggest.map((option) => option)}
                    renderInput={(params) => <TextField {...params} label="Search Quacky" />}
                  />
                 
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