import {useEffect, useState} from 'react';
import '../style/Home.css';
import { useNavigate } from 'react-router-dom';

type Produto = {
    id: string;
    nome: string;
    descricao: string | null;
    preco: number;
    estoque: number;
    categoria: string | null;
    status: string;
    vendedorId: string;
    imagemUrl: string | null;
}

const API_URL = "/api";

function Home() {

    const [usuarioLogado, setUsuarioLogado] = useState<{nome: string} | null>(null);
    const [produtos, setProdutos] = useState<Produto[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (!token) return;

        fetch(`${API_URL}/usuarios/me`, {
            headers: { Authorization: `Bearer ${token}` }
        })
        .then((res) => (res.ok ? res.json() : null))
        .then((dados) => setUsuarioLogado(dados))
    }, []);

    useEffect(() => {
        fetch(`${API_URL}/produtos`)
            .then((res) => (res.ok ? res.json() : []))
            .then((dados) => setProdutos(dados))
    }, []);

    function handleLogout() {
        localStorage.removeItem("accessToken");
        setUsuarioLogado(null);
        navigate("/");
    }

    function formatarPreco(preco: number) {
        return preco.toLocaleString('pt-BR', {
            style: "currency",
            currency: "BRL"
        });
    }

    return (
        <div className="home">
            <header className="home-header">
                <span className="logo">Shoply</span>
                <nav className="home-nav">
                    {usuarioLogado ? (
                        <>
                            <span>Olá {usuarioLogado.nome}</span>
                            <button type="button" className="logout-button" onClick={handleLogout}>
                                Sair
                            </button>
                        </>
                    ) : (
                        <a href="/login">Entrar</a>
                    )}
                    <a href="#" className="cart-link">Carrinho</a>
                </nav>
            </header>
            <section className="hero-banner">
                <h1>Encontre tudo em um só lugar</h1>
                <p>Produtos de diversos vendedores com entrega rápida.</p>
                <button type="button" className="cta-button">
                    Explorar produtos
                </button>
            </section>

            <section className="products-section">
                <h2>Destaques</h2>

                {produtos.length === 0 ? (
                    <p className="products-empty">Nenhum produto disponível no momento.</p>
                ) : (
                    <div className="product-grid">
                        {produtos.map((produto) => (
                            <article key={produto.id} className="product-card">
                                <div className="product-image-placeholder" aria-hidden="true" />

                                <h3>{produto.nome}</h3>
                                <p className="product-price">{formatarPreco(produto.preco)}</p>
                                <button className="add-to-cart-button" type="button">
                                    Adicionar ao carrinho
                                </button>
                            </article>
                        ))}
                    </div>
                )}
            </section>
        </div>
    )
}

export default Home;