import React, { useEffect, useState } from "react";
import { getPendientes, atenderIngreso } from "../api/ingresos";
import { useSearchParams, useNavigate } from "react-router-dom";

export default function AtencionMedicaPage({ usuario }) {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [ingreso, setIngreso] = useState(null);
  const [diagnostico, setDiagnostico] = useState("");
  const [error, setError] = useState(null);
  const id = searchParams.get("id");

  useEffect(() => {
    getPendientes().then((todos) => {
      const encontrado = todos.find((i) => i.id === Number(id));
      if (encontrado) setIngreso(encontrado);
      else setError("Ingreso no encontrado.");
    });
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await atenderIngreso({
        idIngreso: Number(id),
        diagnostico,
        emailMedico: usuario.email,
      });
      navigate("/pendientes");
    } catch (err) {
      setError(err.message);
    }
  };

  if (!ingreso) return <p>{error || "Cargando ingreso..."}</p>;

  return (
    <div className="formulario">
      <h2>Atención Médica</h2>
      <p>
        <strong>Paciente:</strong> {ingreso.paciente.nombre}{" "}
        {ingreso.paciente.apellido}
      </p>
      <p>
        <strong>Nivel:</strong> {ingreso.nivelEmergencia.nombre}
      </p>

      <form onSubmit={handleSubmit}>
        <label>
          Diagnóstico:
          <textarea
            value={diagnostico}
            onChange={(e) => setDiagnostico(e.target.value)}
            required
            rows={4}
          />
        </label>

        {error && <p className="error">{error}</p>}

        <button type="submit">Finalizar Atención</button>
      </form>
    </div>
  );
}
