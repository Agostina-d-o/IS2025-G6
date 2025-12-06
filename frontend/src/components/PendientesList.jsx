import React from "react";
import IngresoCard from "./IngresoCard";

export default function PendientesList({ ingresos, mostrarBotonAtender, onAtender, mostrarBotonFinalizar, onFinalizar }) {
  if (!ingresos || ingresos.length === 0) return <p>No hay ingresos.</p>;
  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "1rem", justifyContent: "center" }}>
      {ingresos.map(ingreso => (
        <IngresoCard
          key={ingreso.id}
          ingreso={ingreso}
          mostrarBotonAtender={mostrarBotonAtender}
          onAtender={onAtender}
          mostrarBotonFinalizar={mostrarBotonFinalizar}
          onFinalizar={onFinalizar}
        />
      ))}
    </div>
  );
}
