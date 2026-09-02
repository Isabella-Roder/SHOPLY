# Requisitos do Shoply

Documento vivo para registrar o escopo e orientar as decisões do projeto. Alterações nos requisitos devem ser discutidas e registradas aqui.

## 1. Visão do produto

O Shoply será um marketplace no qual compradores poderão encontrar e adquirir produtos oferecidos por vendedores.

## 2. Diretrizes confirmadas

- O backend deve possuir uma estrutura clara, organizada e sustentável.
- Segurança deve ser considerada desde o início do desenvolvimento.
- Nenhum requisito funcional ainda não confirmado deve ser tratado como definitivo.
- A identidade visual do frontend terá preto e vermelho como cores principais.
- A interface deve preservar boa legibilidade, contraste e acessibilidade.

## 3. Identidade visual inicial

- Cor principal: preto.
- Cor de destaque e ações: vermelho.
- Cores auxiliares neutras poderão ser usadas em textos, fundos, bordas e estados da interface.
- Os tons exatos, tipografia, logotipo e componentes visuais ainda serão definidos.
- O vermelho não deve ser o único indicador de erro ou estado, para atender usuários com deficiência na percepção de cores.

## 4. Requisitos funcionais a definir

- Tipos de usuário e respectivos perfis: comprador, vendedor e administrador.
- Cadastro, login, recuperação de senha e verificação de conta.
- Cadastro e administração de lojas.
- Cadastro, estoque, categorias, imagens e variações de produtos.
- Busca, filtros e ordenação de produtos.
- Carrinho e lista de desejos.
- Endereços, frete e regiões atendidas.
- Pedidos, cancelamentos, devoluções e reembolsos.
- Pagamentos e divisão de valores entre vendedores.
- Cupons, descontos e promoções.
- Avaliações de produtos e vendedores.
- Notificações.
- Painéis do comprador, vendedor e administrador.
- Moderação de produtos, lojas e usuários.

> Os itens desta seção são possibilidades a validar, não funcionalidades aprovadas.

## 5. Requisitos não funcionais iniciais

### Organização

- Separar responsabilidades entre domínio, aplicação, infraestrutura e interface da API conforme a complexidade real do projeto.
- Evitar regras de negócio em controllers e dependência direta desnecessária de detalhes de infraestrutura.
- Manter contratos da API e decisões arquiteturais documentados.

### Segurança

- Validar toda entrada recebida pela API.
- Aplicar autenticação e autorização por recurso e por operação.
- Armazenar senhas apenas com algoritmo de hash apropriado; nunca em texto puro.
- Não versionar segredos, tokens, senhas ou chaves privadas.
- Adotar o princípio do menor privilégio.
- Evitar exposição de dados sensíveis em respostas, erros e logs.
- Proteger operações críticas contra abuso, fraude e repetição indevida.
- Manter trilha de auditoria para ações administrativas e financeiras relevantes.
- Considerar os requisitos da LGPD no tratamento de dados pessoais.
- Não armazenar dados completos de cartão; utilizar um provedor de pagamento adequado.

### Qualidade e operação

- Possuir testes automatizados para regras de negócio e fluxos críticos.
- Padronizar respostas de erro sem revelar detalhes internos.
- Utilizar migrações versionadas para alterações no banco de dados.
- Ter logs estruturados, métricas e rastreabilidade para diagnóstico.
- Definir estratégia de backup e recuperação antes da produção.
- Documentar configuração e execução dos ambientes de desenvolvimento e produção.

## 6. Decisões pendentes

- Público-alvo e região de operação.
- Produtos físicos, digitais, serviços ou combinação deles.
- Modelo de receita do marketplace.
- Responsabilidade pelo estoque e envio.
- Provedor de pagamento e regras de repasse.
- Política de cancelamento, devolução e disputa.
- Tecnologias do frontend e banco de dados.
- Estratégia de autenticação.
- Necessidade de aplicativo móvel.
- Requisitos esperados de escala, disponibilidade e desempenho.

## 7. Próximos passos sugeridos

1. Definir o objetivo do MVP e o público-alvo.
2. Confirmar os tipos de usuário e as permissões de cada um.
3. Descrever a jornada mínima de compra e venda.
4. Definir regras de pedidos, pagamentos, entrega e cancelamento.
5. Escolher a arquitetura inicial do backend com base nesses requisitos.
6. Transformar o escopo confirmado em entregas pequenas e testáveis.

## 8. Histórico de decisões

| Data | Decisão | Status |
| --- | --- | --- |
| 2026-09-02 | O produto será um marketplace. | Confirmada |
| 2026-09-02 | O backend priorizará boa estrutura e segurança. | Confirmada |
| 2026-09-02 | Preto e vermelho serão as cores principais do frontend. | Confirmada |

