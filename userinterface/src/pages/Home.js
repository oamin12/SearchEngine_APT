import React, {useEffect} from 'react'
import Layout from '../components/Layout'
import Nav from '../components/Nav'
import Footer from '../components/Footer'
const Home = () => {
  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
  }); 
  return (
    <div>
    <Nav/>
        <Layout/>
    <Footer/>
    </div>
  )
}

export default Home