import { useEffect, useState } from 'react';
import '../style/Home.css';
import { useNavigate } from 'react-router-dom';

const featuredProducts = [
  { id: 1, name: 'Tênis Urban Runner', price: 'R$ 259,90' },
  { id: 2, name: 'Mochila Traveler', price: 'R$ 189,90' },
  { id: 3, name: 'Fone Bluetooth Pulse', price: 'R$ 149,90' },
  { id: 4, name: 'Relógio Sport X', price: 'R$ 329,90' },
]

const API_URL = "/api";

function Home() {

    const [usuarioLogado, setUsuarioLogado] = useState<{nome: string} | null>(null);
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

    function handleLogut() {
        localStorage.removeItem("accessToken");
        setUsuarioLogado(null);
        navigate("/");
    }

    return (
        <div className="home">
            <header className="home-header">
                <span className="logo">Shoply</span>

                <nav className="home-nav">

                    {usuarioLogado ? (
                        <>
                            <span>Olá, {usuarioLogado.nome}</span>
                            <button type="button" className="logout-button" onClick={handleLogut}>
                                Sair
                            </button>
                        </>
                    ): (
                        <a href="/login">Entrar</a>
                    )}

                    <a href="#" className='cart-link'>Carrinho</a>
                </nav>
            </header>

            <section className="hero-banner">
                <h1>Encontre tudo em um só lugar</h1>
                <p>Produtos de diversos vendedores com entrega rápida.</p>
                <button className="cta-button" type='button'>
                    Explorar produtos
                </button>
            </section>

            <section className="products-section">
                <h2>Destaques</h2>

                <div className="products-grid">
                    {featuredProducts.map((product) => (
                        <article key={product.id} className='product-card'>
                            <div className="product-image-placeholder" aria-hidden="true"/>
                                <h3>{product.name}</h3>
                                <p className="product-price">{product.price}</p>
                                <button className="add-to-cart-button" type='button'>
                                    Adicionar ao carrinho
                                </button>
                        </article>
                    ))}
                </div>
            </section>
        </div>
    )
}

export default Home;