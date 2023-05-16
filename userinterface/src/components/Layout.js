import React from 'react'
import classes from './style/layout.module.css'
import ArrowRightAltIcon from '@mui/icons-material/ArrowForward';
import duck from '../img/duck.png'

const Layout = () => {
  return (
    <div className={classes.layout}>
        {/*search box container*/}
        <div className={classes.searchBoxContainer}>
          <div className={classes.searchBox}>
            <img src={duck} alt='duck'/>
            <input type='text' placeholder='Search Quacky'/>
            <button >
              Quack Quack
              <ArrowRightAltIcon style={{fontSize:25}} />
            </button>
          </div>
      </div>
    </div>
  )
}

export default Layout