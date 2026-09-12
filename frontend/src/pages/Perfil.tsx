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
    const [carregandoAcao, setCarregandoAcao] = useState(false);

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

    async function handleTornarVendedor() {
        setCarregandoAcao(true);
        setErro("");

        const token = localStorage.getItem("accessToken");

        try {
            const resposta = await fetch(`${API_URL}/usuarios/me/tornar-vendedor`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` }
            });

            if (!resposta.ok) {
                throw new Error("Não foi possivel atualizar sua conta.");
            }

            const dados = await resposta.json();
            localStorage.setItem("accessToken", dados.accessToken);
            setUsuario(dados.usuario);
        } catch {
            setErro("Não foi possivel atualizar sua conta. Tente novamente.");
        } finally {
            setCarregandoAcao(false);
        }
    }

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

    const inicial = usuario.nome.trim().charAt(0).toUpperCase();

    return (
        <div className="perfil-page">
            <div className="perfil-card">
                <div className="perfil-cabecalho">
                    <div className="perfil-avatar" aria-hidden="true">{inicial}</div>
                    <div>
                        <h1>{usuario.nome}</h1>
                        <p className="perfil-email">{usuario.email}</p>
                    </div>
                </div>

                <div className="perfil-badges">
                    <span className={`badge badge-perfil-${usuario.perfil.toLowerCase()}`}>
                        {usuario.perfil}
                    </span>
                    <span className={`badge badge-status-${usuario.status.toLowerCase()}`}>
                        {usuario.status}
                    </span>
                </div>

                {usuario.perfil === "CLIENTE" && (
                    <div className="perfil-acao">
                        <p>Quer vender seus próprios produtos no Shoply?</p>
                        <button type={"button"} className="submit-button" onClick={handleTornarVendedor} disabled={carregandoAcao}>
                            {carregandoAcao ? "Atualizando..." : "Tornar-se vendedor"}
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Perfil;