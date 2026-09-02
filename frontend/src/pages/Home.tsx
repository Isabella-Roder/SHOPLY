import '../style/Home.css';

const featuredProducts = [
  { id: 1, name: 'Tênis Urban Runner', price: 'R$ 259,90' },
  { id: 2, name: 'Mochila Traveler', price: 'R$ 189,90' },
  { id: 3, name: 'Fone Bluetooth Pulse', price: 'R$ 149,90' },
  { id: 4, name: 'Relógio Sport X', price: 'R$ 329,90' },
]

function Home() {
    return (
        <div className="home">
            <header className="home-header">
                <span className="logo">Shoply</span>

                <nav className="home-nav">
                    <a href="#">Entrar</a>
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