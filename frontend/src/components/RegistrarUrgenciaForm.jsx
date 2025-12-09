import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registrarIngreso } from "../api/ingresos";
import { getPacienteByCuil } from "../api/pacientes";

export default function RegistrarUrgenciaForm({
  usuario,
  nombreEnfermera,
  apellidoEnfermera,
}) {
  const [form, setForm] = useState({
    cuilPaciente: "",
    informe: "",
    nivelEmergencia: "URGENCIA",
    temperatura: "",
    frecuenciaCardiaca: "",
    frecuenciaRespiratoria: "",
    presionSistolica: "",
    presionDiastolica: "",
  });

  const [mensaje, setMensaje] = useState(null);
  const [enviando, setEnviando] = useState(false);
  const [pacienteEncontrado, setPacienteEncontrado] = useState(null);
  const [lookup, setLookup] = useState({ status: "idle", name: "", error: "" });

  const navigate = useNavigate();

  const enfNombre =
    usuario?.nombre ||
    (usuario?.email ? usuario.email.split("@")[0] : "") ||
    "Enfermera";
  const enfApellido = usuario?.apellido || "";

  const actualizar = (campo, valor) =>
    setForm((f) => ({ ...f, [campo]: valor }));

  const buscarPaciente = async () => {
    const cuil = form.cuilPaciente.trim();
    if (!cuil) return setLookup({ status: "idle", name: "", error: "" });

    setLookup({ status: "loading", name: "", error: "" });
    try {
      const paciente = await getPacienteByCuil(cuil);

      if (!paciente) {
        setPacienteEncontrado(null);
        setLookup({ status: "notfound", name: "", error: "" });
        setMensaje("⚠️ Paciente no existe.");
        return;
      }

      setPacienteEncontrado(paciente);
      setLookup({ status: "ok", name: paciente.nombreCompleto, error: "" });
      setMensaje(null);
    } catch (e) {
      setLookup({
        status: "error",
        name: "",
        error: e.message || "Error de búsqueda",
      });
    }
  };

  const irARegistrarPaciente = () => {
    navigate(
      `/registrar-paciente?cuil=${encodeURIComponent(
        form.cuilPaciente.trim()
      )}`
    );
  };

  const validar = () => {
    const t = form.temperatura ? Number(form.temperatura) : null;
    const fc = form.frecuenciaCardiaca ? Number(form.frecuenciaCardiaca) : null;
    const fr = form.frecuenciaRespiratoria
      ? Number(form.frecuenciaRespiratoria)
      : null;
    const ps = form.presionSistolica ? Number(form.presionSistolica) : null;
    const pd = form.presionDiastolica ? Number(form.presionDiastolica) : null;

    if (!form.cuilPaciente.trim())
      return "El CUIL del paciente es obligatorio";
    if (!form.informe.trim()) return "El informe es obligatorio";

    if (t != null && (isNaN(t) || t < 30 || t > 45))
      return "Temperatura fuera de rango (30–45 °C)";
    if (fc != null && (isNaN(fc) || fc < 30 || fc > 220))
      return "Frecuencia cardíaca fuera de rango (30–220 lpm)";
    if (fr != null && (isNaN(fr) || fr < 6 || fr > 60))
      return "Frecuencia respiratoria fuera de rango (6–60 rpm)";
    if (ps != null && (isNaN(ps) || ps < 60 || ps > 250))
      return "Presión sistólica fuera de rango (60–250 mmHg)";
    if (pd != null && (isNaN(pd) || pd < 30 || pd > 140))
      return "Presión diastólica fuera de rango (30–140 mmHg)";
    if (ps != null && pd != null && ps <= pd)
      return "La presión sistólica debe ser mayor que la diastólica";

    const valoresValidos = [
      "CRITICA",
      "EMERGENCIA",
      "URGENCIA",
      "URGENCIA_MENOR",
      "SIN_URGENCIA",
    ];
    if (!valoresValidos.includes(form.nivelEmergencia))
      return "Nivel de emergencia inválido";

    return null;
  };

const buscarButtonStyle = {
  padding: "0.45rem 0.75rem",
  fontWeight: "bold",
  backgroundColor: "#2b7cff",
  color: "white",
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
  height: "40px",
  flexShrink: 0,
  whiteSpace: "nowrap",
  maxWidth: "120px",
};


const buscarButtonHoverStyle = {
  backgroundColor: "#205dcf",
};


  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje(null);

    const err = validar();
    if (err) {
      setMensaje("❌ " + err);
      return;
    }

    setEnviando(true);
    try {
      const payload = {
        cuilPaciente: form.cuilPaciente.trim(),
        informe: form.informe.trim(),
        nivelEmergencia: form.nivelEmergencia,
        temperatura: form.temperatura ? Number(form.temperatura) : null,
        frecuenciaCardiaca: form.frecuenciaCardiaca
          ? Number(form.frecuenciaCardiaca)
          : null,
        frecuenciaRespiratoria: form.frecuenciaRespiratoria
          ? Number(form.frecuenciaRespiratoria)
          : null,
        presionSistolica: form.presionSistolica
          ? Number(form.presionSistolica)
          : null,
        presionDiastolica: form.presionDiastolica
          ? Number(form.presionDiastolica)
          : null,
        nombreEnfermera: enfNombre,
        apellidoEnfermera: enfApellido,
      };

      await registrarIngreso(payload);
      setMensaje("✅ Ingreso registrado correctamente. Redirigiendo…");
      setTimeout(() => navigate("/pendientes"), 2000);
    } catch (err) {
      const msg = err.message || "";

      if (msg.includes("ingreso") && msg.includes("paciente")) {
        setMensaje("⚠️ Este paciente ya tiene una urgencia en curso.");
      } else {
        setMensaje("❌ " + msg);
      }
    } finally {
      setEnviando(false);
    }
  };

  const deshabilitado = lookup.status !== "ok";


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
    marginTop: "1rem",
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

  const selectStyle = {
    ...inputStyle,
  };

  return (
    <form onSubmit={handleSubmit} style={formStyle}>
      <h3 style={{ marginBottom: "0.5rem" }}>Registrar Urgencia</h3>

      <div style={gridStyle}>
        {/* CUIL + Buscar  */}
        <div style={campoFull}>
          <label style={labelStyle}>
            CUIL Paciente:
            <div style={{ display: "flex", gap: 8, marginTop: "0.25rem" }}>
              <input
                style={{ ...inputStyle, flex: 1 }}
                value={form.cuilPaciente}
                onChange={(e) => actualizar("cuilPaciente", e.target.value)}
                placeholder="20XXXXXXXXX"
                required
              />
              <button type="button" onClick={buscarPaciente} style={buscarButtonStyle}>
                {lookup.status === "loading" ? "Buscando..." : "Buscar"}
              </button>
            </div>
          </label>

          {lookup.status === "ok" && (
            <p style={{ color: "#16a34a", marginTop: 6 }}>
              ✅ Paciente encontrado: <strong>{lookup.name}</strong>
            </p>
          )}
          {lookup.status === "notfound" && (
            <div
              style={{
                display: "inline-flex",
                alignItems: "center",
                gap: "0.4rem",
                color: "#b45309",
                marginTop: 6,
                flexWrap: "nowrap",
                flexDirection: "row",
              }}
            >
              <span style={{ whiteSpace: "nowrap" }}>⚠️ Paciente no existe.</span>
              <button
                type="button"
                onClick={irARegistrarPaciente}
                style={{
                  background: "none",
                  border: "none",
                  color: "#2563eb",
                  textDecoration: "underline",
                  cursor: "pointer",
                  padding: 0,
                  font: "inherit",
                  whiteSpace: "nowrap",
                }}
              >
                Registrarlo ahora.
              </button>
            </div>
          )}


          {lookup.status === "error" && (
            <p style={{ color: "crimson", marginTop: 6 }}>
              ❌ {lookup.error}
            </p>
          )}
        </div>

        {/* Informe */}
        <div style={campoFull}>
          <label style={labelStyle}>
            Informe:
            <input
              style={inputStyle}
              value={form.informe}
              onChange={(e) => actualizar("informe", e.target.value)}
              placeholder="Breve descripción del estado"
              required
              disabled={deshabilitado}
            />
          </label>
        </div>

        {/* Nivel de Emergencia / Temperatura */}
        <div>
          <label style={labelStyle}>
            Nivel de Emergencia:
            <select
              style={selectStyle}
              value={form.nivelEmergencia}
              onChange={(e) =>
                actualizar("nivelEmergencia", e.target.value)
              }
              disabled={deshabilitado}
            >
              <option value="CRITICA">Crítica</option>
              <option value="EMERGENCIA">Emergencia</option>
              <option value="URGENCIA">Urgencia</option>
              <option value="URGENCIA_MENOR">Urgencia Menor</option>
              <option value="SIN_URGENCIA">Sin Urgencia</option>
            </select>
          </label>
        </div>

        <div>
          <label style={labelStyle}>
            Temperatura (°C):
            <input
              type="number"
              step="0.1"
              min="30"
              max="45"
              style={inputStyle}
              value={form.temperatura}
              onChange={(e) => actualizar("temperatura", e.target.value)}
              placeholder="36.7"
              disabled={deshabilitado}
            />
          </label>
        </div>

        {/* Frecuencia cardíaca / respiratoria */}
        <div>
          <label style={labelStyle}>
            Frecuencia Cardíaca (lpm):
            <input
              type="number"
              step="1"
              min="30"
              max="220"
              style={inputStyle}
              value={form.frecuenciaCardiaca}
              onChange={(e) =>
                actualizar("frecuenciaCardiaca", e.target.value)
              }
              placeholder="80"
              disabled={deshabilitado}
            />
          </label>
        </div>

        <div>
          <label style={labelStyle}>
            Frecuencia Respiratoria (rpm):
            <input
              type="number"
              step="1"
              min="6"
              max="60"
              style={inputStyle}
              value={form.frecuenciaRespiratoria}
              onChange={(e) =>
                actualizar("frecuenciaRespiratoria", e.target.value)
              }
              placeholder="18"
              disabled={deshabilitado}
            />
          </label>
        </div>

        {/* Presión sistólica / diastólica */}
        <div>
          <label style={labelStyle}>
            Presión Sistólica (mmHg):
            <input
              type="number"
              step="1"
              min="60"
              max="250"
              style={inputStyle}
              value={form.presionSistolica}
              onChange={(e) =>
                actualizar("presionSistolica", e.target.value)
              }
              placeholder="120"
              disabled={deshabilitado}
            />
          </label>
        </div>

        <div>
          <label style={labelStyle}>
            Presión Diastólica (mmHg):
            <input
              type="number"
              step="1"
              min="30"
              max="140"
              style={inputStyle}
              value={form.presionDiastolica}
              onChange={(e) =>
                actualizar("presionDiastolica", e.target.value)
              }
              placeholder="80"
              disabled={deshabilitado}
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
        <button type="submit" disabled={deshabilitado || enviando}>
          {enviando ? "Guardando…" : "Registrar"}
        </button>

        {mensaje && (
          <span
            style={{
              fontSize: "0.95rem",
              color: mensaje.startsWith("✅")
                ? "green"
                : mensaje.startsWith("⚠️")
                ? "#b45309"
                : "crimson",
            }}
          >
            {mensaje}
          </span>
        )}
      </div>
    </form>
  );
}
