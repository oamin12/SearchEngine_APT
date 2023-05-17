import Footer from "./components/Footer";
import Nav from "./components/Nav";
import Home from "./pages/Home";
import Results from "./pages/Results";
import { Switch, Routes, Route } from "react-router-dom"

function App() {
  return (
    <div className="App">
      <Nav />
      <Routes>
        <Route path="/" element={<Home />} exact></Route>
        <Route path="/results" element={<Results />} exact></Route>
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
