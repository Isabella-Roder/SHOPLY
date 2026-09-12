import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import "../style/PainelVendedor.css";

const API_URL = "/api";

type Produto = {
    id: string;
    nome: string;
    preco: number;
    estoque: number;
    status: string;
}

function formatarPreco(preco: number) {
    return preco.toLocaleString('pt-BR', {
        style: "currency",
        currency: "BRL"
    });
}

export function PainelVendedor() {

    const [produtos, setProdutos] = useState<Produto[]>([]);

    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState("");
    const [acaoEmAndamento, setAcaoEmAndamento] = useState<string | null>(null);

    function carregarProduto() {
        const token = localStorage.getItem("accessToken");
        fetch(`${API_URL}/produtos/meus`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then((res) => {
                if (!res.ok) throw new Error("Não foi possivel carregar seus produtos.");
                return res.json();
            })
            .then(setProdutos)
            .catch(() => setErro("Não foi possivel carregar seus produtos."))
            .finally(() => setCarregando(false));
    }

    useEffect(() => {
        carregarProduto();
    }, []);

    async function alterarStatus(id: string, acao: "ativar" | "desativar") {
        setAcaoEmAndamento(id);

        const token = localStorage.getItem("accessToken");

        try {
            const resposta = await fetch(`${API_URL}/produtos/${id}/${acao}`, {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (!resposta.ok) throw new Error();
            carregarProduto();
        } catch {
            setErro("Não foi possivel atualizar o status do produto.");
        } finally {
            setAcaoEmAndamento(null);
        }
    }

    async function excluirProduto(id: string) {
        if (!window.confirm("Tem certeza que deseja excluir este produto?")) return;

        setAcaoEmAndamento(id);

        const token = localStorage.getItem("accessToken");

        try {
            const resposta = await fetch(`${API_URL}/produtos/${id}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (!resposta.ok) throw new Error();
            carregarProduto();
        } catch {
            setErro("Não foi possivel excluir o produto.");
        } finally {
            setAcaoEmAndamento(null);
        }
    }

    if (carregando) {
        return <p className="painel-carregando">Carregando...</p>
    }

    return (
        <div className="painel-vendedor-page">
            <div className="painel-cabecalho">
                <h1>Meus produtos</h1>
                <Link to="/produtos/novo" className="submit-button">
                    Cadastrar produto
                </Link>
            </div>

            {erro && (
                <p className="form-error" role="alert">
                    {erro}
                </p>
            )}

            {produtos.length === 0 ? (
                <p className="painel-vazio">Você ainda não cadastrou nenhum produto.</p>
            ): (
                <table className="painel-tabela">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Preço</th>
                            <th>Estoque</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {produtos.map((produto) => (
                            <tr key={produto.id}>
                                <td>{produto.nome}</td>
                                <td>{formatarPreco(produto.preco)}</td>
                                <td>{produto.estoque}</td>
                                <td>
                                    <span className={`badge badge-status-${produto.status.toLowerCase()}`}>
                                        {produto.status}
                                    </span>
                                </td>
                                <td className="painel-acoes">
                                    {produto.status === "ATIVO" ? (
                                        <button type={"button"} disabled={acaoEmAndamento === produto.id} onClick={() => alterarStatus(produto.id, "desativar")}>
                                            Desativar
                                        </button>
                                    ) : (
                                        <button type={"button"} disabled={acaoEmAndamento === produto.id} onClick={() => alterarStatus(produto.id, "ativar")}>
                                            Ativar
                                        </button>
                                    )}

                                    <button type={"button"} className="botao-excluir" disabled={acaoEmAndamento === produto.id} onClick={() => excluirProduto(produto.id)}>
                                        Excluir
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}