import React, { useEffect, useState } from "react";
import { getPendientes, atenderIngreso } from "../api/ingresos";
import PendientesList from "../components/PendientesList";

export default function PendientesPage({ usuario }) {
  const [pendientes, setPendientes] = useState([]);
  const [error, setError] = useState(null);

  const cargarPendientes = () => {
    getPendientes()
      .then(setPendientes)
      .catch(err => setError(err.message));
  };

  useEffect(() => {
    cargarPendientes();
  }, []);

  const handleAtender = async (idIngreso) => {
    try {
      await atenderIngreso(idIngreso);
      cargarPendientes(); // recarga lista
    } catch (err) {
      alert("Error al atender ingreso");
    }
  };

  return (
    <div>
      <h2>Ingresos Pendientes</h2>
      {error && <p className="error">{error}</p>}

      <PendientesList
        ingresos={pendientes}
        mostrarBotonAtender={usuario?.rol === "medico"}
        onAtender={handleAtender}
      />

      {usuario?.rol === "enfermera" && (
        <div style={{ marginTop: "2rem" }}>
          <a href="/registrar-paciente" style={{ marginRight: "1rem" }}>
            Registrar paciente
          </a>
          <a href="/registrar-urgencia">Registrar urgencia</a>
        </div>
      )}
    </div>
  );
}
