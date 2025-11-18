import React from "react";
import RegisterUserForm from "../components/RegisterUserForm";
import { useNavigate, Link } from "react-router-dom";

export default function RegisterUserPage({ setUsuario }) {
  const navigate = useNavigate();

  const handleRegister = (usuario) => {
    setUsuario(usuario);
    if (usuario.rol === "enfermera") navigate("/pendientes");
    else if (usuario.rol === "medico") navigate("/atencion");
  };

  return (
    <div className="register-page">
      <RegisterUserForm onRegister={handleRegister} />

    </div>
  );
}
