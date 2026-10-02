import { createContext, useContext, useEffect, useState, type ReactNode } from "react";

type ItemCarrinho = {
    id: string;
    nome: string;
    preco: number;
    imagemUrl: string | null;
    quantidade: number;
};

type CarrinhoContextType = {
    itens: ItemCarrinho[];
    adicionarItem: (produto: Omit<ItemCarrinho, "quantidade">) => void;
    removerItem: (id: string) => void;
    alterarQuantidade: (id: string, quantidade: number) => void;
    limparCarrinho: () => void;
    totalItens: number;
    totalPreco: number;
};

const CHAVE_STORAGE = "shoply:carrinho";

const CarrinhoContext = createContext<CarrinhoContextType | null>(null);

function carregarDoStorage(): ItemCarrinho[] {
    try {
        const salvo = localStorage.getItem(CHAVE_STORAGE);
        return salvo ? JSON.parse(salvo) : [];
    } catch {
        return [];
    }
}

export function CarrinhoProvider({ children }: { children: ReactNode }) {
    const [itens, setItens] = useState<ItemCarrinho[]>(carregarDoStorage);

    useEffect(() => {
        localStorage.setItem(CHAVE_STORAGE, JSON.stringify(itens));
    }, [itens]);

    function adicionarItem(produto: Omit<ItemCarrinho, "quantidade">) {
        setItens((atual) => {
            const existente = atual.find((item) => item.id === produto.id);

            if (existente) {
                return atual.map((item) =>
                    item.id === produto.id
                        ? { ...item, quantidade: item.quantidade + 1 }
                        : item
                )
            }

            return [...atual, { ...produto, quantidade: 1 }];
        })
    }

    function removerItem(id: string) {
        setItens((atual) => atual.filter((item) => item.id !== id));
    }

    function alterarQuantidade(id: string, quantidade: number) {
        if (quantidade < 1) {
            return;
        }

        setItens((atual) => 
            atual.map((item) => (item.id === id ? { ...item, quantidade } : item))
        );
    }

    function limparCarrinho() {
        setItens([]);
    }

    const totalItens = itens.reduce((soma, item) => soma + item.quantidade, 0);
    const totalPreco = itens.reduce((soma, item) => soma + item.preco * item.quantidade, 0);

    return (
        <CarrinhoContext.Provider
            value={{ itens, adicionarItem, removerItem, alterarQuantidade, limparCarrinho, totalItens, totalPreco }}
        >
            {children}
        </CarrinhoContext.Provider>
    );
}

export function useCarrinho() {
    const contexto = useContext(CarrinhoContext);

    if (!contexto) {
        throw new Error("useCarrinho precisa ser usado dentro de um CarrinhoProvider");
    }

    return contexto;
}