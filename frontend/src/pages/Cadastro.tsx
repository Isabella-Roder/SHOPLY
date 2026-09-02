import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../style/Cadastro.css"

const API_URL = "/api";

function Cadastro() {

    const [nome, setNome] = useState("");
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [confirmarSenha, setConfirmarSenha] = useState("");
    const [erro, setErro] = useState("");

    async function handlesubmit(e: React.FormEvent) {
        e.preventDefault();
        setErro("");

        if (senha !== confirmarSenha) {
            setErro('As senhas não coincidem.');
            return;
        }

        const dados = {
            nome: nome.trim(),
            email: email.trim(),
            senha: senha
        };

        try {
            const resposta = await fetch(`${API_URL}/usuarios`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dados)
            });

            if (!resposta.ok) {
                throw new Error("Não foi possivel cadastrar usuario.");
            }


        } catch (erroRecebido) {
            setErro("Não foi possivel cadastrar. Tente novamente.");
        }
    }

    return (
        <div className="cadastro-page">
            <form className="cadastro-form" onSubmit={handlesubmit}>
                <h1>Criar conta</h1>

                {erro && (
                    <p className="form-error" role="alert">
                        {erro}
                    </p>
                )}

                <label htmlFor="nome">Nome</label>
                <input type="text" id="nome" value={nome} onChange={(e) => setNome(e.target.value)} required />

                <label htmlFor="email">E-mail</label>
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

                <label htmlFor="senha">Senha</label>
                <input type="password" id="senha" value={senha} onChange={(e) => setSenha(e.target.value)} required minLength={8}/>

                <label htmlFor="confirmarSenha">Confirmar senha</label>
                <input type="password" id="confirmarSenha" value={confirmarSenha} onChange={(e) => setConfirmarSenha(e.target.value)} required minLength={8} />

                <button type="submit" className="submit-button">
                    Cadastrar
                </button>

                <p className="login-link">
                    Já tem conta? <Link to="/login">Entrar</Link>
                </p>
            </form>
        </div>
    )
}

export default Cadastro;