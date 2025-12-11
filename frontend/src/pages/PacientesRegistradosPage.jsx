import React, { useEffect, useState } from "react";
import { getPacientesRegistrados } from "../api/pacientes";

export default function PacientesRegistradosPage() {
  const [data, setData] = useState([]);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const rows = await getPacientesRegistrados();
        setData(rows);
      } catch (e) {
        setErr(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <p>Cargando…</p>;
  if (err) return <p style={{ color: "crimson" }}>Error: {err}</p>;
  if (!data.length) return <p>No hay pacientes registrados.</p>;

  const containerStyle = {
    maxWidth: "1200px",
    width: "95%",
    margin: "2rem auto",
    padding: "1.5rem 2rem",
    backgroundColor: "#ffffff",
    borderRadius: "12px",
    boxShadow: "0 4px 10px rgba(0, 0, 0, 0.06)",
  };

  const tableStyle = {
    width: "100%",
    borderCollapse: "collapse",
    marginTop: "0.5rem",
    fontSize: "0.95rem",
  };

  const thtdStyle = {
    padding: "0.6rem 0.8rem",
    textAlign: "left",
    border: "1px solid #d3d3d3",
  };

  const headerRowStyle = {
    backgroundColor: "#f5f5f5",
  };

  return (
    <div style={containerStyle}>
      <h2 style={{ textAlign: "center", marginBottom: "1rem" }}>
        Pacientes registrados
      </h2>

      <table style={tableStyle}>
        <thead>
          <tr style={headerRowStyle}>
            <th style={thtdStyle}>CUIL</th>
            <th style={thtdStyle}>Nombre Completo</th>
            <th style={thtdStyle}>Domicilio Completo</th>
            <th style={thtdStyle}>Código OS</th>
            <th style={thtdStyle}>Nombre OS</th>
            <th style={thtdStyle}>Nro. Afiliado</th>
          </tr>
        </thead>
        <tbody>
          {data.map((p) => (
            <tr key={p.cuil}>
              <td style={thtdStyle}>{p.cuil}</td>
              <td style={thtdStyle}>{p.nombreCompleto}</td>
              <td style={thtdStyle}>{p.domicilioCompleto}</td>
              <td style={thtdStyle}>{p.codigoObraSocial || "—"}</td>
              <td style={thtdStyle}>{p.nombreObraSocial || "—"}</td>
              <td style={thtdStyle}>{p.numeroAfiliado || "—"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
