import { useEffect, useState } from "react";
import "../style/Perfil.css";

const API_URL = "/api";

type Usuario = {
    nome: string;
    email: string;
    perfil: string;
    status: string;
};

function Perfil() {
  
    const [usuario, setUsuario] = useState<Usuario | null>(null);
    const [erro, setErro] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("accessToken");

        fetch(`${API_URL}/usuarios/me`, {
            headers: {Authorization: `Bearer ${token}`}
        })
            .then((res) => {
                if (!res.ok) throw new Error("Não foi possivel carregar o perfil.");
                return res.json();
            })
            .then(setUsuario)
            .catch(() => setErro("Não foi possivel carregar seus dados."));
    }, []);

    if (erro) {
        return (
            <p className="form-error" role="alert">
                {erro}
            </p>
        );
    }

    if (!usuario) {
        return <p>Carregando...</p>
    }

    return (
        <div className="perfil-page">
            <h1>Meu perfil</h1>

            <ul className="perfil-dados">
                <dt>Nome</dt>
                <dd>{usuario.nome}</dd>

                <dt>E-mail</dt>
                <dd>{usuario.email}</dd>

                <dt>Perfil</dt>
                <dd>{usuario.perfil}</dd>

                <dt>Status</dt>
                <dd>{usuario.status}</dd>
            </ul>
        </div>
    )
}

export default Perfil;