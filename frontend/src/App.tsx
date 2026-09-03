import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Cadastro from "./pages/Cadastro";
import Login from "./pages/Login";
import RotaProtegida from "./components/RotaProtegida";
import Perfil from "./pages/Perfil";

function App() {
    return(
        <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="/cadastro" element={<Cadastro />} />
            <Route path="/login" element={<Login />} />

            <Route element={<RotaProtegida />} >
                <Route path="/perfil" element={<Perfil/>}/>
            </Route>
        </Routes>
    );
}

export default App