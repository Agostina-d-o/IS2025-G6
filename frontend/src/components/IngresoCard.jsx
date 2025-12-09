import React, { useState } from "react";

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
    : `${fecha.toLocaleDateString("es-AR")} · ${fecha.toLocaleTimeString("es-AR", {
        hour: "2-digit",
        minute: "2-digit",
      })}`;

  const estado = ingreso.estado?.nombre || ingreso.estado || "—";
  const nivel = ingreso.nivelEmergencia || ingreso.nivel || "—";
  const nivelCls = (nivel || "").toUpperCase();



  // Colapsar si está en proceso, expandido si es pendiente
  const [expandida, setExpandida] = useState(estado === "PENDIENTE");

  return (
    <div className={`card nivel-${nivelCls}`}>
      {/* CABECERA */}
      <div className="card-head">
        <h4>Ingreso #{ingreso.id}</h4>
        <span className={`badge nivel-${nivelCls}`}>{nivel}</span>
      </div>

      {/* DATOS BÁSICOS */}
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
          <span className="k"><span className="ico">🧑</span>Paciente</span>
          <span className="v">{ingreso.paciente}</span>
        </div>

        {/* Enfermera solo si expandida — Fecha si colapsada */}
        {expandida ? (
          <div className="row">
            <span className="k"><span className="ico">👩‍⚕️</span>Enfermera</span>
            <span className="v">
              {ingreso.enfermera || ingreso.enfermeraNombre || ingreso.nombreEnfermera || "—"}
            </span>
          </div>
        ) : null}

        <div className="row">
          <span className="k"><span className="ico">📝</span>Informe</span>
          <span className="v">{ingreso.informe}</span>
        </div>

        {/* Fecha solo cuando está colapsado */}
        {!expandida && (
          <p className="meta" style={{ marginTop: "4px" }}>
            <span className="ico">📅</span> {fechaStr}
          </p>
        )}

      </div>

      {/* BLOQUE EXPANDIBLE */}
      {expandida && (
        <>
          <hr className="separator" />

          <div className="kv">
            <p>
              <strong><span className="ico ico-temp">🌡️</span> Temp.:</strong>{" "}
              {ingreso.temperatura != null ? `${ingreso.temperatura} °C` : "—"}
            </p>
            <p>
              <strong><span className="ico ico-fc">❤️</span> FC:</strong>{" "}
              {ingreso.frecuenciaCardiaca || "—"}
            </p>
            <p>
              <strong><span className="ico ico-fr">🌬️</span> FR:</strong>{" "}
              {ingreso.frecuenciaRespiratoria || "—"}
            </p>
            <p>
              <strong><span className="ico ico-ta">🩺</span> TA:</strong>{" "}
              {ingreso.tensionArterial || "—"}
            </p>
          </div>

          <p className="meta">
            <strong>📅 Fecha y hora:</strong> {fechaStr}
          </p>

          <div className="actions">
            {mostrarBotonFinalizar && (
              <button
                className="btn btn-primary btn-medium"
                onClick={() => onFinalizar?.(ingreso.id)}
                style={{ minWidth: "220px", justifySelf: "center", whiteSpace: "nowrap" }}
              >
                Finalizar
              </button>
            )}


          </div>
        </>
      )}

      {/* BOTÓN VER MÁS/VER MENOS (solo si EN_PROCESO) */}
      {estado === "EN_PROCESO" && (
        <div style={{ marginTop: "0.75rem" }}>
          <button
            className="btn btn-outline btn-small-toggle"
            onClick={() => setExpandida(!expandida)}
          >
            {expandida ? "Ver menos" : "Ver más"}
            <span style={{ marginLeft: "0.4rem" }}>
              {expandida ? "▲" : "▼"}
            </span>
          </button>
        </div>
      )}
    </div>
  );
}
