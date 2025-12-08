import React, { useEffect, useState } from "react";
import {
  getPendientes, getEnProceso, getFinalizados,
  atenderIngreso, finalizarIngreso
} from "../api/ingresos";
import PendientesList from "../components/PendientesList";
import { Link } from "react-router-dom";

export default function PendientesPage({ usuario }) {
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

  const handleAtender = async (id) => {
    try {
      await atenderIngreso();
      alert(`Próximo ingreso reclamado y puesto EN PROCESO`);
      await cargarTodo();
    } catch (e) { alert(e.message); }
  };

  const handleFinalizar = async (id) => {
    const diagnostico = prompt("Diagnóstico final:");
    if (diagnostico == null) return;
    try {
      await finalizarIngreso(id, diagnostico);
      alert(`Ingreso #${id} FINALIZADO`);
      await cargarTodo();
    } catch (e) { alert(e.message); }
  };

  return (
    <div>
        <h2 className="section-title">Cola de Atención</h2>
      {error && <p className="error">Error: {error}</p>}

      {rol === "enfermera" && (
        <div className="cta-group" style={{ marginTop: "1rem" }}>
          <Link to="/registrar-paciente" className="btn btn-secondary">
            Registrar paciente
          </Link>
          <Link to="/registrar-urgencia" className="btn btn-primary" style={{ background: "#EF476F", color: "#fff" }}>
            Registrar urgencia
          </Link>
          <Link to="/pacientes" className="btn btn-primary">Lista de pacientes</Link>
        </div>
      )}

      {/* PENDIENTES */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr auto 1fr",
          alignItems: "center",
          marginTop: "1.5rem",
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
          }}
        >
          📋 Ingresos Pendientes
        </h3>
        {rol === "medico" && (
          <button
            className="btn btn-primary"
            onClick={handleAtender}
            style={{ justifySelf: "center" }}
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
      <div style={{ display:"flex", flexWrap:"wrap", gap:"1rem", justifyContent: "flex-start", marginTop: "0.25rem" }}>
        {finalizados.length === 0 && <p style={{ margin: 0 }}>No hay finalizados.</p>}
        {finalizados.map(f => (
          <div key={f.id} style={{background:"white", padding:"1rem 1.5rem", borderRadius:10, boxShadow:"0 2px 8px rgba(0,0,0,.05)", minWidth:280}}>
            <p><strong>Nivel:</strong> {f.nivelEmergencia}</p>
            <p><strong>Paciente:</strong> {f.paciente}</p>
              <strong>Fecha y hora:</strong> {f.fechaIngreso}
            <p><strong>Diagnóstico:</strong> {f.diagnostico}</p>
          </div>
        ))}
      </div>


    </div>
  );
}
