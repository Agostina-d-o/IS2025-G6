import React, { useEffect, useState } from "react";
import {
  getPendientes, getEnProceso, getFinalizados,
  atenderIngreso, finalizarIngreso
} from "../api/ingresos";
import PendientesList from "../components/PendientesList";
import { Link } from "react-router-dom";

export default function PendientesPage({ usuario }) {
  console.log("Usuario actual:", usuario);


  const [pendientes, setPendientes]   = useState([]);
  const [enProceso, setEnProceso]     = useState([]);
  const [finalizados, setFinalizados] = useState([]);
  const [error, setError] = useState(null);

  const rol = usuario?.rol?.toLowerCase();

  async function cargarTodo() {
    try {
      setError(null);
      const [p, ep, fin] = await Promise.all([
        getPendientes(),
        getEnProceso(),
        getFinalizados()
      ]);
      setPendientes(p);
      setEnProceso(ep);
      setFinalizados(fin);
    } catch (e) { setError(e.message); }
  }

  useEffect(() => { cargarTodo(); }, []);

  const handleAtender = async () => {
    try {
      await atenderIngreso();
      alert(`Próximo ingreso reclamado y puesto EN PROCESO`);
      await cargarTodo();
    } catch (e) { alert(e.message); }
  };

  const handleFinalizar = async (id) => {
    const diagnostico = prompt("Diagnóstico final:");
    if (diagnostico == null) return;

    const medico = {
      nombreMedico: usuario.nombre,
      apellidoMedico: usuario.apellido,
      emailMedico: usuario.email,
      matriculaMedico: usuario.matricula
    };

    try {
      await finalizarIngreso(id, diagnostico, medico);
      alert(`Ingreso #${id} FINALIZADO`);
      await cargarTodo();
    } catch (e) {
      alert("Error al finalizar: " + e.message);
    }
  };


  // TARJETA FINALIZADO
  function FinalizadoCard({ f }) {
    const [expandido, setExpandido] = useState(false);

    const fecha = new Date(f.fechaIngreso);
    const fechaStr = isNaN(fecha.getTime())
      ? f.fechaIngreso
      : `${fecha.toLocaleDateString("es-AR")} · ${fecha.toLocaleTimeString("es-AR", {
          hour: "2-digit",
          minute: "2-digit"
        })}`;

    console.log(`Ingreso #${f.id} – Diagnóstico recibido:`, f.diagnostico);

    return (
      <div
        onClick={() => setExpandido(!expandido)}
        style={{
          background: "white",
          padding: "1rem 1.2rem",
          borderRadius: 10,
          boxShadow: "0 2px 8px rgba(0,0,0,.05)",
          minWidth: 260,
          maxWidth: 300,
          cursor: "pointer",
          borderLeft: "5px solid #2E7D32"
        }}
      >
        <h4 style={{ marginTop: 0, marginBottom: "0.4rem" }}>
          Informe del ingreso #{f.id}
        </h4>

        <p><strong>Paciente:</strong> {f.paciente}</p>
        <p><strong>Enfermera:</strong> {f.enfermera || "—"}</p>
        <p><strong>Médico:</strong> {f.medico || "—"}</p>
        <p><strong>Diagnóstico:</strong> {f.diagnostico?.trim() ? f.diagnostico : "—"}</p>
        <p style={{ whiteSpace: "nowrap" }}><strong>Fecha y hora:</strong> {fechaStr}</p>

        {expandido && (
          <>
            <hr style={{ margin: "0.6rem 0" }} />
            <p>🌡️ <strong>Temp.:</strong> {f.temperatura != null ? `${f.temperatura}` : "—"}</p>
            <p>❤️ <strong>FC:</strong> {f.frecuenciaCardiaca || "—"}</p>
            <p>💨 <strong>FR:</strong> {f.frecuenciaRespiratoria || "—"}</p>
            <p>🩺 <strong>TA:</strong> {f.tensionArterial || "—"}</p>
            <p>🔴 <strong>Nivel:</strong> {f.nivelEmergencia || "—"}</p>
            <p>📌 <strong>Estado:</strong> {f.estado?.nombre || f.estado}</p>
            <p>📝 <strong>Informe:</strong> {f.informe || "—"}</p>
          </>
        )}

        <p style={{ fontSize: "0.8rem", marginTop: "0.6rem", color: "#666" }}>
          {expandido ? "▲ Click para ocultar detalles" : "▼ Click para ver más detalles"}
        </p>
      </div>

    );
  }

  return (
    <div>
      <h2 className="section-title">Cola de Atención</h2>
      {error && <p className="error">Error: {error}</p>}

      {rol === "enfermera" && (
        <div className="cta-group" style={{ marginTop: "1rem", marginBottom: "1.2rem" }}>
          <Link to="/registrar-paciente" className="btn btn-secondary">
            Registrar paciente
          </Link>
          <Link to="/registrar-urgencia" className="btn btn-primary" style={{ background: "#EF476F", color: "#fff" }}>
            Registrar urgencia
          </Link>
          <Link to="/pacientes" className="btn btn-primary">
            Lista de pacientes
          </Link>
        </div>
      )}

      {/* PENDIENTES */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr auto 1fr",
          alignItems: "center",
          width: "100%",
          marginBottom: "0.5rem",
        }}
      >
        <h3
          style={{
            display: "flex",
            alignItems: "center",
            gap: "0.5rem",
            borderLeft: "6px solid #FFB703",
            paddingLeft: "0.75rem",
            margin: 0,
          }}
        >
          📋 Ingresos Pendientes
        </h3>

        {rol === "medico" && (
          <button
            className="btn btn-primary btn-medium"
            onClick={handleAtender}
            style={{ minWidth: "260px", justifySelf: "center", whiteSpace: "nowrap" }}
          >
            Atender próximo paciente
          </button>
        )}

        <div></div>
      </div>

      <PendientesList
        ingresos={pendientes}
        mostrarBotonAtender={rol === "medico"}
        onAtender={handleAtender}
      />

      {/* EN PROCESO */}
      <h3
        style={{
          display: "flex",
          alignItems: "center",
          gap: "0.5rem",
          borderLeft: "6px solid #9C27B0",
          paddingLeft: "0.75rem",
        }}
      >
        🩺 En Proceso
      </h3>

      <PendientesList
        ingresos={enProceso}
        mostrarBotonFinalizar={rol === "medico"}
        onFinalizar={handleFinalizar}
      />

      {/* FINALIZADOS */}
      <h3
        style={{
          display: "flex",
          alignItems: "center",
          gap: "0.5rem",
          borderLeft: "6px solid #2E7D32",
          paddingLeft: "0.75rem",
        }}
      >
        ✅ Finalizados
      </h3>

      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          gap: "1rem",
          justifyContent: "flex-start",
          marginTop: "0.25rem",
        }}
      >
        {finalizados.length === 0 && <p style={{ margin: 0 }}>No hay finalizados.</p>}
        {finalizados.map((f) => (
          <FinalizadoCard key={f.id} f={f} />
        ))}
      </div>
    </div>
  );
}
