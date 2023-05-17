import React from 'react'
import classes from './style/footer.module.css'
import LinkIcon from '@mui/icons-material/Link';
import FacebookIcon from '@mui/icons-material/Facebook';
const Footer = () => {
  return (
    <div className={classes.footer}>
       
         <div className={classes.logoContainer}>
            <div className={classes.info}>
              <p>
                Developed by TEAM #3 Team No Sleep 𓂀
              </p>
              <a href="https://github.com/oamin12">
                <LinkIcon style={{fontSize:25}} />
              </a>
            </div>
         </div>

    </div>
  )
}

export default Footer