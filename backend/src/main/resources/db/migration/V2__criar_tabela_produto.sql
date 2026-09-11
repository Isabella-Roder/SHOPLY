CREATE TABLE produtos (
    id UUID NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),
    preco NUMERIC(10,2) NOT NULL,
    estoque INTEGER NOT NULL DEFAULT 0,
    categoria VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    vendedor_id UUID NOT NULL,
    imagem_url VARCHAR(500),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
    atualizado_em TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_produtos PRIMARY KEY (id),
    CONSTRAINT fk_produtos_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuarios (id),

    CONSTRAINT ck_produtos_status CHECK (
        status IN ('ATIVO', 'INATIVO', 'ESGOTADO')
    ),

    CONSTRAINT ck_produtos_preco CHECK (preco > 0),
    CONSTRAINT ck_produtos_estoque CHECK (estoque >= 0)
);