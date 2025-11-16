import React from "react";
import { useNavigate } from "react-router-dom";

export default function Navbar({ usuario, setUsuario }) {
  const navigate = useNavigate();

  const cerrarSesion = () => {
    setUsuario(null);
    navigate("/");
  };

  return (
    <nav className="navbar">
      <span>Guardia Clínica</span>

      {usuario && (
        <div>
          <span>{usuario.email} ({usuario.rol})</span>
          <button onClick={cerrarSesion}>Cerrar sesión</button>
        </div>
      )}
    </nav>
  );
}
