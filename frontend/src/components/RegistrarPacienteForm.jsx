import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { crearPaciente } from "../api/pacientes";

const OBRAS = {
  OSDE: "Osde Binario",
  PAMI: "PAMI",
  SUBSIDIO: "Subsidio de Salud",
  SWISS: "Swiss Medical",
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
    nombreObraSocial: "",
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
        ...(form.codigoObraSocial?.trim()
          ? {}
          : {
              codigoObraSocial: null,
              nombreObraSocial: null,
              numeroAfiliado: null,
            }),
      };

      await crearPaciente(payload);
      setMensaje("✅ Paciente registrado con éxito. Redirigiendo…");
      setTimeout(() => navigate("/pendientes"), 2000);
    } catch (err) {
      setMensaje("❌ " + (err.message || "No se pudo registrar el paciente"));
    } finally {
      setEnviando(false);
    }
  };

  // estilos inline para asegurar que se apliquen
  const formStyle = {
    maxWidth: "900px",
    margin: "2rem auto",
    padding: "1.5rem 2rem",
    background: "#ffffff",
    borderRadius: "12px",
    boxShadow: "0 4px 10px rgba(0, 0, 0, 0.06)",
    textAlign: "left",
  };

  const gridStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(2, minmax(0, 1fr))",
    gap: "1rem 1.5rem",
  };

  const campoFull = { gridColumn: "1 / -1" };

  const labelStyle = {
    display: "block",
    fontWeight: 600,
    fontSize: "0.95rem",
  };

  const inputStyle = {
    width: "100%",
    padding: "0.45rem 0.6rem",
    marginTop: "0.25rem",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "0.95rem",
  };

  return (
    <form onSubmit={handleSubmit} style={formStyle}>
      <h3 style={{ marginBottom: "1rem" }}>Nuevo Paciente</h3>

      <div style={gridStyle}>
        {/* CUIL (fila completa) */}
        <div style={campoFull}>
          <label style={labelStyle}>
            CUIL:
            <input
              style={inputStyle}
              value={form.cuil}
              onChange={(e) => actualizar("cuil", e.target.value)}
            />
          </label>
        </div>

        {/* Nombre / Apellido */}
        <div>
          <label style={labelStyle}>
            Nombre:
            <input
              style={inputStyle}
              value={form.nombre}
              onChange={(e) => actualizar("nombre", e.target.value)}
            />
          </label>
        </div>
        <div>
          <label style={labelStyle}>
            Apellido:
            <input
              style={inputStyle}
              value={form.apellido}
              onChange={(e) => actualizar("apellido", e.target.value)}
            />
          </label>
        </div>

        {/* Calle / Número */}
        <div>
          <label style={labelStyle}>
            Calle:
            <input
              style={inputStyle}
              value={form.calle}
              onChange={(e) => actualizar("calle", e.target.value)}
            />
          </label>
        </div>
        <div>
          <label style={labelStyle}>
            Número:
            <input
              style={inputStyle}
              value={form.numero}
              onChange={(e) => actualizar("numero", e.target.value)}
            />
          </label>
        </div>

        {/* Localidad (fila completa) */}
        <div style={campoFull}>
          <label style={labelStyle}>
            Localidad:
            <input
              style={inputStyle}
              value={form.localidad}
              onChange={(e) => actualizar("localidad", e.target.value)}
            />
          </label>
        </div>

        {/* Código OS / Nombre OS */}
        <div>
          <label style={labelStyle}>
            Código Obra Social:
            <input
              style={inputStyle}
              value={form.codigoObraSocial}
              onChange={(e) => {
                const valor = e.target.value;
                // guardar el código
                actualizar("codigoObraSocial", valor);

                // buscar nombre según el código
                const nombre = OBRAS[valor.trim().toUpperCase()] || "";
                actualizar("nombreObraSocial", nombre);
              }}
            />
          </label>
        </div>
        <div>
          <label style={labelStyle}>
            Nombre Obra Social:
            <input
              style={inputStyle}
              value={form.nombreObraSocial}
              onChange={(e) => actualizar("nombreObraSocial", e.target.value)}
            />
          </label>
        </div>

        {/* Número Afiliado (fila completa) */}
        <div style={campoFull}>
          <label style={labelStyle}>
            Número Afiliado:
            <input
              style={inputStyle}
              value={form.numeroAfiliado}
              onChange={(e) => actualizar("numeroAfiliado", e.target.value)}
            />
          </label>
        </div>
      </div>

      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "1rem",
          marginTop: "1rem",
        }}
      >
        <button type="submit" disabled={enviando}>
          {enviando ? "Guardando…" : "Registrar"}
        </button>
        {mensaje && <p style={{ margin: 0 }}>{mensaje}</p>}
      </div>
    </form>
  );
}
