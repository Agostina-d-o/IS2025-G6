import React from "react";
import LoginForm from "../components/LoginForm";
import { useNavigate, Link } from "react-router-dom";
import "../styles/LoginPage.css";

export default function LoginPage({ setUsuario }) {
  const navigate = useNavigate();

  const handleLogin = (usuario) => {
    setUsuario(usuario);
    if (usuario.rol === "enfermera") navigate("/pendientes");
    else if (usuario.rol === "medico") navigate("/atencion");
    else navigate("/");
  };

  return (
    <div className="login-page">
      <h1 className="titulo-app">🏥 Guardia Clínica IS</h1>
      <LoginForm onLogin={handleLogin} />
      <p className="registro-link">
        ¿No tenés cuenta? <Link to="/register">Registrate acá</Link>
      </p>
    </div>
  );
}
