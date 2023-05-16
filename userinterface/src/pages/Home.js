import React, {useEffect} from 'react'
import Layout from '../components/Layout'

const Home = () => {
  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
  }); 
  return (
    <div>
        <Layout/>
    </div>
  )
}

export default Home