import React from "react";
import LoginForm from "../components/LoginForm";
import { useNavigate, Link } from "react-router-dom";

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
      <LoginForm onLogin={handleLogin} />
      <p style={{ marginTop: "1rem", textAlign: "center" }}>
        ¿No tenés cuenta? <Link to="/register">Registrate acá</Link>
      </p>
    </div>
  );
}
