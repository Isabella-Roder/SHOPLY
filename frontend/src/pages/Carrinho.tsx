import { Link } from "react-router-dom";
import { useCarrinho } from "../context/CarrinhoContext";
import "../style/Carrinho.css";

function formatarPreco(preco: number) {
    return preco.toLocaleString('pt-BR', {
        style: "currency",
        currency: "BRL"
    });
}


export default function Carrinho() {

    const { itens, removerItem, alterarQuantidade, totalPreco } = useCarrinho();

    if (itens.length === 0) {
        return (
            <div className="carrinho-vazio">
                <p>Seu carrinho está vazio.</p>
                <Link to="/">Ver produtos</Link>
            </div>
        );
    }

    return (
        <div className="carrinho-page">
            <h1>Meu carrinho</h1>

            <ul className="carrinho-lista">
                {itens.map((item) => (
                    <li className="carrinho-item" key={item.id}>
                        {item.imagemUrl ? (
                            <img src={item.imagemUrl} alt={item.nome} />
                        ) : (
                            <div className="carrinho-item-imagem-placeholder">
                                <span>{item.nome.charAt(0).toUpperCase()}</span>
                            </div>
                        )}

                        <div className="carrinho-item-info">
                            <p className="carrinhp-item-nome">{item.nome}</p>
                            <p className="carrinho-item-preco">{formatarPreco(item.preco)}</p>
                        </div>

                        <div className="carrinho-item-quantidade">
                            <button
                                type="button"
                                onClick={() => alterarQuantidade(item.id, item.quantidade - 1)}
                                disabled={item.quantidade <= 1}
                            >
                                -
                            </button>

                            <span>Qtd. {item.quantidade}</span>

                            <button
                                type="button"
                                onClick={() => alterarQuantidade(item.id, item.quantidade + 1)}
                            >
                                +
                            </button>
                        </div>

                        <p className="carrinho-item-subtotal">
                            {formatarPreco(item.preco * item.quantidade)}
                        </p>

                        <button
                            type="button"
                            className="carrinho-item-remover"
                            onClick={() => removerItem(item.id)}
                            aria-label={`Remover ${item.nome} do carrinho`}
                        >
                            Remover
                        </button>
                    </li>
                ))}
            </ul>

            <div className="carrinho-resumo">
                <p className="carrinho-total">
                    Total: <strong>{formatarPreco(totalPreco)}</strong>
                </p>
            </div>
        </div>
    )
}