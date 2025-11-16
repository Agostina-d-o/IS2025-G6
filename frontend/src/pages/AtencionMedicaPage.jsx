import React, { useEffect, useState } from "react";
import { getPendientes, atenderIngreso } from "../api/ingresos";
import IngresoCard from "../components/IngresoCard";

export default function AtencionMedicaPage() {
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

  const atender = async (id) => {
    try {
      await atenderIngreso(id);
      cargarPendientes(); // refrescar lista
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <h2>Atención Médica</h2>
      {error && <p className="error">{error}</p>}
      {pendientes.map(i => (
        <IngresoCard key={i.id} ingreso={i} mostrarBotonAtender onAtender={() => atender(i.id)} />
      ))}
    </div>
  );
}
