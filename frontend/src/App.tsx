import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Cadastro from "./pages/Cadastro";
import Login from "./pages/Login";
import RotaProtegida from "./components/RotaProtegida";
import Perfil from "./pages/Perfil";
import {NovoProduto} from "./pages/NovoProduto.tsx";
import {PainelVendedor} from "./pages/PainelVendedor.tsx";
import DetalheProduto from "./pages/DetalheProduto.tsx";
import Carrinho from "./pages/Carrinho.tsx";

function App() {
    return(
        <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="/produtos/:id" element={<DetalheProduto />} />
            <Route path="/cadastro" element={<Cadastro />} />
            <Route path="/login" element={<Login />} />
            <Route path="/carrinho" element={<Carrinho />} />

            <Route element={<RotaProtegida />} >
                <Route path="/perfil" element={<Perfil/>}/>
                <Route path="/produtos/novo" element={<NovoProduto/>}/>
                <Route path="/painel" element={<PainelVendedor/>}/>
            </Route>
        </Routes>
    );
}

export default App