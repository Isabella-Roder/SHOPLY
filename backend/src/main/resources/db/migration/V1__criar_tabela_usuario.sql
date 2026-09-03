CREATE TABLE usuarios (
    id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
    atualizado_em TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),

    CONSTRAINT ck_usuarios_perfil CHECK (
        perfil IN ('CLIENTE',  'VENDEDOR', 'ADMINISTRADOR')
    ),

    CONSTRAINT ck_usuarios_status CHECK (
        status IN ('PENDENTE', 'ATIVO', 'BLOQUEADO', 'DESATIVADO')
    )
);