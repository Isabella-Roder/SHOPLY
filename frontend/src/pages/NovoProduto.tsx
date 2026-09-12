import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../style/NovoProduto.css";

const API_URL = "/api";

export function NovoProduto() {

    const navigate = useNavigate();
    const [nome, setNome] = useState("");
    const [descricao, setDescricao] = useState("");
    const [preco, setPreco] = useState("");
    const [estoque, setEstoque] = useState("");
    const [categoria, setCategoria] = useState("");
    const [erro, setErro] = useState("");
    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setErro("");
        const token = localStorage.getItem("accessToken");
        try {
            const resposta = await fetch(`${API_URL}/produtos`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({
                    nome,
                    descricao: descricao || null,
                    preco: Number(preco),
                    estoque: Number(estoque),
                    categoria: categoria || null,
                    imagemUrl: null
                })
            });

            if (!resposta.ok) {
                if (resposta.status === 403) {
                    throw new Error("Apenas vendedores podem cadastrar produtos.");
                }
                throw new Error("Não foi possivel cadastrar o produto.");
            }
            navigate("/");

        } catch (erroRecebido) {
            setErro(erroRecebido instanceof Error ? erroRecebido.message : "Erro inesperado.");
        }
    }

    return (
        <div className="novo-produto-page">
            <form className="novo-produto-form" onSubmit={handleSubmit}>
                <h1>Cadastrar produto</h1>
                {erro && (
                    <p className="form-error" role="alert">
                        {erro}
                    </p>
                )}

                <label htmlFor="nome">Nome</label>
                <input type={"text"} id={"nome"} value={nome} onChange={(e) => setNome(e.target.value)} required/>

                <label htmlFor="descricao">Descrição</label>
                <textarea id="descricao" value={descricao} onChange={(e) => setDescricao(e.target.value)}/>

                <label htmlFor="preco">Preço</label>
                <input type={"number"} id={"preco"} step={"0.01"} min={"0.01"} value={preco} onChange={(e) => setPreco(e.target.value)} required/>

                <label htmlFor="estoque">Estoque</label>
                <input type={"number"} id={"estoque"} min="0" value={estoque} onChange={(e) => setEstoque(e.target.value)} required/>

                <label htmlFor="categoria">Categoria</label>
                <input type={"text"} id={"categoria"} value={categoria} onChange={(e) => setCategoria(e.target.value)}/>

                <button type={"submit"} className="submit-button">
                    Cadastrar
                </button>
            </form>
        </div>
    );
}