import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom"
import "../style/Login.css"

const API_URL = "/api";

function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setErro("");

        try {
            const resposta = await fetch(`${API_URL}/autenticacao/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({email: email.trim(), senha})
            });

            if (!resposta.ok) {
                throw new Error("E-mail ou senha invalidos.");
            }

            const dados = await resposta.json();
            localStorage.setItem("accessToken", dados.accessToken);
            navigate("/");
        } catch (erroRecebido) {
            setErro("Não foi possivel entrar. Verifique seus dados.");
        }
    }

    return (
        <div className="login-page">
            <form className="login-form" onSubmit={handleSubmit}>
                <h1>Entrar</h1>

                {erro && (
                    <p className="form-error" role="alert">
                        {erro}
                    </p>
                )}

                <label htmlFor="email">E-mail</label>
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

                <label htmlFor="senha">Senha</label>
                <input type="password" id="senha" value={senha} onChange={(e) => setSenha(e.target.value)} required />

                <button type="submit" className="submit-button">
                    Entrar
                </button>

                <p className="cadastro-link">
                    Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
                </p>
            </form>
        </div>
    )
}

export default Login