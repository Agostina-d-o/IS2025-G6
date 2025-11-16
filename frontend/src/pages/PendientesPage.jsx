import React, { useEffect, useState } from "react";
import { getPendientes } from "../api/ingresos";
import PendientesList from "../components/PendientesList";

export default function PendientesPage({ usuario }) {
  const [pendientes, setPendientes] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    getPendientes()
      .then(setPendientes)
      .catch(err => setError(err.message));
  }, []);

  return (
    <div>
      <h2>Ingresos Pendientes</h2>
      {error && <p className="error">{error}</p>}
      <PendientesList
        ingresos={pendientes}
        mostrarBotonAtender={usuario?.rol === "medico"}
      />
    </div>
  );
}
