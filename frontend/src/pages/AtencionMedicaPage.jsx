import React, { useEffect, useState } from "react";
import { getPendientes, atenderIngreso, finalizarIngreso } from "../api/ingresos";
import { useSearchParams, useNavigate } from "react-router-dom";

export default function AtencionMedicaPage({ usuario }) {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [ingreso, setIngreso] = useState(null);
  const [diagnostico, setDiagnostico] = useState("");
  const [error, setError] = useState(null);
  const id = searchParams.get("id");

  useEffect(() => {
    async function fetchAndAtender() {
      try {
        const todos = await getPendientes();
        const encontrado = todos.find((i) => i.id === Number(id));

        if (!encontrado) {
          setError("Ingreso no encontrado.");
          return;
        }

        await atenderIngreso(); // Reclamar el ingreso al médico actual
        setIngreso(encontrado);
      } catch (err) {
        setError("Error al reclamar el ingreso.");
      }
    }

    fetchAndAtender();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const medico = {
        nombreMedico: usuario.nombre,
        apellidoMedico: usuario.apellido,
        emailMedico: usuario.email,
        matriculaMedico: usuario.matricula,
      };

      await finalizarIngreso(id, diagnostico, medico);
      navigate("/pendientes"); // volver a la lista
    } catch (err) {
      setError(err.message);
    }
  };

  if (!ingreso) return <p>{error || "Cargando ingreso..."}</p>;

  return (
    <div className="formulario">
      <h2>Atención Médica</h2>

      <p>
        <strong>Paciente:</strong> {ingreso.paciente.nombre} {ingreso.paciente.apellido}
      </p>
      <p>
        <strong>Nivel de Emergencia:</strong> {ingreso.nivelEmergencia.nombre}
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
