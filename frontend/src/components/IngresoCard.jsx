import React from "react";

export default function IngresoCard({
  ingreso,
  mostrarBotonAtender,
  onAtender,
  mostrarBotonFinalizar,
  onFinalizar
}) {
  const fecha = new Date(ingreso.fechaIngreso);
  const fechaStr = isNaN(fecha.getTime())
    ? ingreso.fechaIngreso
    : `${fecha.toLocaleDateString("es-AR")} · ${fecha.toLocaleTimeString("es-AR",{hour:"2-digit",minute:"2-digit"})}`;

  const estado = ingreso.estado?.nombre || ingreso.estado || "—";
  const nivel  = ingreso.nivelEmergencia || ingreso.nivel || "—";
  const nivelCls = (nivel || "").toUpperCase(); // p.ej. URGENCIA

  return (
    <div className={`card nivel-${nivelCls}`}>
      <div className="card-head">
        <h4>Ingreso #{ingreso.id}</h4>
        <span className={`badge nivel-${nivelCls}`}>{nivel}</span>
      </div>

      <div className="rows">
        <div className="row">
          <span className="k"><span className="ico">🔖</span>Estado</span>
          <span className="v status">{estado}</span>
        </div>
        {ingreso.posicion != null && (
          <div className="row">
            <span className="k"><span className="ico">🧭</span>Posición en cola</span>
            <span className="v">{ingreso.posicion}</span>
          </div>
        )}
        <div className="row">
          <span className="k"><span className="ico">🧑‍</span>Paciente</span>
          <span className="v">{ingreso.paciente}</span>
        </div>
        <div className="row">
          <span className="k"><span className="ico">👩‍⚕️</span>Enfermera</span>
          <span className="v">{ingreso.enfermera}</span>
        </div>
        <div className="row">
          <span className="k"><span className="ico">📝</span>Informe</span>
          <span className="v">{ingreso.informe}</span>
        </div>
      </div>

      <div className="kv">
        <p><strong>🌡️ Temp.:</strong> {ingreso.temperatura != null ? `${ingreso.temperatura} °C` : "—"}</p>
        <p><strong>❤️ FC:</strong> {ingreso.frecuenciaCardiaca || "—"}</p>
        <p><strong>🌬️ FR:</strong> {ingreso.frecuenciaRespiratoria || "—"}</p>
        <p><strong>🩺 TA:</strong> {ingreso.tensionArterial || "—"}</p>
      </div>

      <p className="meta"><strong>📅 Fecha y hora:</strong> {fechaStr}</p>

      <div className="actions">
        {mostrarBotonAtender && (
          <button className="btn btn-primary" onClick={() => onAtender?.(ingreso.id)}>
            Atender
          </button>
        )}
        {mostrarBotonFinalizar && (
          <button className="btn btn-dark" onClick={() => onFinalizar?.(ingreso.id)}>
            Finalizar
          </button>
        )}
      </div>
    </div>
  );
}
