import React from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Navbar({ usuario, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    try { localStorage.removeItem("usuario"); } catch {}
    onLogout?.();
    navigate("/");
  };

  const nombreVisible = usuario?.nombre && usuario?.apellido
    ? `${usuario.nombre} ${usuario.apellido}`
    : usuario?.email;

  return (
    <div className="navbar">
      <div>
        {/* Home: clic en la marca te lleva a /pendientes */}
        <Link to="/pendientes" style={{ textDecoration: "none", color: "inherit" }}>
          <strong>🏥 Guardia Clínica</strong>
        </Link>
        {usuario && (
          <div style={{ fontSize: ".9rem", opacity: .8 }}>
            {nombreVisible} ({usuario.rol?.toUpperCase()})
          </div>
        )}
      </div>

      <div>
        {/* accesos por rol */}
        {usuario?.rol === "enfermera" && (
          <>
            <Link to="/pendientes" style={{ marginRight: 12 }}>Pendientes</Link>
            <Link to="/en-proceso" style={{ marginRight: 12 }}>En proceso</Link>
            <Link to="/finalizados" style={{ marginRight: 12 }}>Finalizados</Link>
            <Link to="/registrar-paciente" style={{ marginRight: 12 }}>Registrar paciente</Link>
            <Link to="/registrar-urgencia" style={{ marginRight: 12 }}>Registrar urgencia</Link>
          </>
        )}

        {usuario?.rol === "medico" && (
          <>
            <Link to="/pendientes" style={{ marginRight: 12 }}>Pendientes</Link>
            <Link to="/en-proceso" style={{ marginRight: 12 }}>En proceso</Link>
            <Link to="/finalizados" style={{ marginRight: 12 }}>Finalizados</Link>
          </>
        )}

        <button onClick={handleLogout}>Cerrar sesión</button>
      </div>
    </div>
  );
}
