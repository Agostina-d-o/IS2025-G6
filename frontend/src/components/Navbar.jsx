import React from "react";
import { useNavigate, Link } from "react-router-dom";
import "../styles/Navbar.css";

export default function Navbar({ usuario, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    try {
      localStorage.removeItem("usuario");
    } catch {}
    onLogout?.();
    navigate("/");
  };

  const nombreVisible =
    usuario?.nombre && usuario?.apellido
      ? `${usuario.nombre} ${usuario.apellido}`
      : usuario?.email;

  return (
    <div className="navbar">
      <div>
        <strong>🏥 Guardia Clínica IS</strong>
        {usuario && (
          <div style={{ fontSize: ".9rem", opacity: 0.8 }}>
            {nombreVisible} ({(usuario.rol ?? "").toString().toUpperCase()})
          </div>
        )}
      </div>

      {usuario && (
        <div style={{ display: "flex", gap: "16px" }}>
          <Link to="/pendientes">
            <button className="navbar-button boton-verde">Ingresos</button>
          </Link>

          <button className="navbar-button boton-rojo"
                    onClick={() => {if (window.confirm("¿Seguro que desea cerrar sesión?"))
                        {handleLogout();  } }}>
            Cerrar sesión
          </button>
        </div>
      )}
    </div>
  );
}

