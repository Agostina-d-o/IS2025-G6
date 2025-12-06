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

  return (
    <div className="pacientes-page">
      <h2>Pacientes registrados</h2>
      <table className="tabla">
        <thead>
          <tr>
            <th>CUIL</th>
            <th>Nombre</th>
          </tr>
        </thead>
        <tbody>
          {data.map((p) => (
            <tr key={p.cuil}>
              <td>{p.cuil}</td>
              <td>{p.nombreCompleto}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
