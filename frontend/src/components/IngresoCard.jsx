import React from "react";


export default function IngresoCard({ ingreso, mostrarBotonAtender, onAtender }) {
return (
<div className="ingreso-card">
<p><strong>Paciente:</strong> {ingreso.paciente?.nombre} {ingreso.paciente?.apellido}</p>
<p><strong>Nivel:</strong> {ingreso.nivel.nombre}</p>
<p><strong>Informe:</strong> {ingreso.informe}</p>
<p><strong>Ingreso:</strong> {new Date(ingreso.fechaHoraIngreso).toLocaleString()}</p>
{mostrarBotonAtender && (
<button onClick={onAtender}>Atender</button>
)}
</div>
);
}