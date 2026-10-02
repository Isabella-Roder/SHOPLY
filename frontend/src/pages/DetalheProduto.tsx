import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import "../style/DetalheProduto.css";
import { useCarrinho } from "../context/CarrinhoContext";

const API_URL = "/api";

type Produto = {
    id: string;
    nome: string;
    descricao: string | null;
    preco: number;
    estoque: number;
    categoria: string | null;
    status: string;
    imagemUrl: string | null;
};

function formatarPreco(preco: number) {
    return preco.toLocaleString('pt-BR', {
        style: "currency",
        currency: "BRL"
    });
}

export default function DetalheProduto() {

    const { id } = useParams();
    
    const [produto, setProduto] = useState<Produto | null>(null);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState<string | null>(null);

    const { adicionarItem } = useCarrinho();

    useEffect(() => {
        fetch(`${API_URL}/produtos/${id}`)
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Erro ao carregar produto (${res.status})`);
                }
                return res.json();
            })
            .then(setProduto)
            .catch(() => setErro("Produto não encontrado."))
            .finally(() => setCarregando(false));
    }, [id]);

    if (carregando) {
        return <p className="detalhe-status">Carregando...</p>;
    }

    if (erro || !produto) {
        return (
            <div className="detalhe-status">
                <p className="form-error" role="alert">{erro || "Produto não encontrado."}</p>
                <Link to="/">Voltar para a Home</Link>
            </div>
        );
    }

    return(
        <div className="detalhe-produto-page">
            <Link to="/" className="detalhe-voltar">&larr; Voltar</Link>

            <div className="detalhe-produto-card">
                {produto.imagemUrl ? (
                    <img src={produto.imagemUrl} alt={produto.nome} className="detalhe-imagem" />
                ) : (
                    <div className="detalhe-imagem-placeholder" aria-hidden="true">
                        <span>{produto.nome.charAt(0).toUpperCase()}</span>
                    </div>
                )}

                <div className="detalhe-info">
                    {produto.categoria && (
                        <span className="product-categoria">{produto.categoria}</span>
                    )}

                    <h1>{produto.nome}</h1>
                    <p className="detalhe-preco">{formatarPreco(produto.preco)}</p>

                    {produto.descricao && (
                        <p className="detalhe-descricao">{produto.descricao}</p>
                    )}

                    <p className="detalhe-estoque">
                        {produto.estoque > 0
                            ? `${produto.estoque} em estoque`
                            :  "Fora de estoque"
                        }
                    </p>

                    <button
                        type="button"
                        className="add-to-cart-button"
                        disabled={produto.estoque === 0 || produto.status !== "ATIVO"}
                        onClick={() => adicionarItem({
                            id: produto.id,
                            nome: produto.nome,
                            preco: produto.preco,
                            imagemUrl: produto.imagemUrl
                        })}
                    >
                        Adicionar no carrinho
                    </button>
                </div>
            </div>
        </div>
    )
}