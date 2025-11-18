import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { crearPaciente } from "../api/pacientes";

const LABELS = {
  cuil: "CUIL",
  nombre: "Nombre",
  apellido: "Apellido",
  calle: "Calle",
  numero: "Número",
  localidad: "Localidad",
  codigoObraSocial: "Código Obra Social",
  numeroAfiliado: "Número Afiliado",
};

export default function RegistrarPacienteForm({ cuilPrefill = "" }) {
  const [form, setForm] = useState({
    cuil: cuilPrefill,
    nombre: "",
    apellido: "",
    calle: "",
    numero: "",
    localidad: "San Miguel de Tucumán",
    codigoObraSocial: "",
    numeroAfiliado: "",
  });
  const [mensaje, setMensaje] = useState(null);
  const [enviando, setEnviando] = useState(false);
  const navigate = useNavigate();

  const actualizar = (campo, valor) =>
    setForm((f) => ({ ...f, [campo]: valor }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje(null);
    setEnviando(true);

    try {
      const payload = {
        ...form,
        numero: form.numero ? Number(form.numero) : null,
      };

      await crearPaciente(payload);
      setMensaje("✅ Paciente registrado con éxito. Redirigiendo…");
      setTimeout(() => navigate("/pendientes"), 2000);
    } catch (err) {
      setMensaje("❌ Error: " + (err.message || "No se pudo registrar"));
    } finally {
      setEnviando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="paciente-form">
      <h3>Nuevo Paciente</h3>

      {Object.entries(form).map(([k, v]) => (
        <label key={k}>
          {LABELS[k] || k}:
          <input
            value={v}
            onChange={(e) => actualizar(k, e.target.value)}
          />
        </label>
      ))}

      <div style={{ display: "flex", alignItems: "center", gap: "1rem" }}>
            <button type="submit" disabled={enviando}>
              {enviando ? "Guardando…" : "Registrar"}
            </button>
         {mensaje && <p style={{ margin: 0 }}>{mensaje}</p>}
      </div>
    </form>
  );
}
