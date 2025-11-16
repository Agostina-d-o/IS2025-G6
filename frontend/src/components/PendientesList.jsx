import React from "react";
import IngresoCard from "./IngresoCard";


export default function PendientesList({ ingresos, mostrarBotonAtender, onAtender }) {
if (!ingresos.length) return <p>No hay ingresos pendientes.</p>;


return (
<div className="pendientes-list">
{ingresos.map((ingreso) => (
<IngresoCard
key={ingreso.id}
ingreso={ingreso}
mostrarBotonAtender={mostrarBotonAtender}
onAtender={onAtender}
/>
))}
</div>
);
}