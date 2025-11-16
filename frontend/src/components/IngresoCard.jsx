import React from "react";

export default function IngresoCard({ ingreso, mostrarBotonAtender, onAtender }) {
  const {
    paciente,
    nivelEmergencia,
    fechaHoraIngreso,
    atendido
  } = ingreso;

  return (
    <div style={{ border: "1px solid #ccc", padding: "1rem", borderRadius: "8px", width: "300px", backgroundColor: "#fefefe" }}>
      <p><strong>Paciente:</strong> {paciente.nombre} {paciente.apellido}</p>
      <p><strong>Emergencia:</strong> {nivelEmergencia.nombre} (Prioridad {nivelEmergencia.prioridad})</p>
      <p><strong>Fecha:</strong> {new Date(fechaHoraIngreso).toLocaleString()}</p>
      <p><strong>Estado:</strong> {atendido ? "Atendido" : "Pendiente"}</p>
      {mostrarBotonAtender && !atendido && (
        <button onClick={() => onAtender(ingreso.id)}>Atender</button>
      )}
    </div>
  );
}
