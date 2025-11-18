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
    navigate(`/registrar-paciente?cuil=${encodeURIComponent(form.cuilPaciente.trim())}`);
  };

  const validar = () => {
    const t = form.temperatura ? Number(form.temperatura) : null;
    const fc = form.frecuenciaCardiaca ? Number(form.frecuenciaCardiaca) : null;
    const fr = form.frecuenciaRespiratoria ? Number(form.frecuenciaRespiratoria) : null;
    const ps = form.presionSistolica ? Number(form.presionSistolica) : null;
    const pd = form.presionDiastolica ? Number(form.presionDiastolica) : null;

    if (!form.cuilPaciente.trim()) return "El CUIL del paciente es obligatorio";
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

    const valoresValidos = ["CRITICA", "EMERGENCIA", "URGENCIA", "URGENCIA_MENOR", "SIN_URGENCIA"];
    if (!valoresValidos.includes(form.nivelEmergencia))
      return "Nivel de emergencia inválido";

    return null;
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
        frecuenciaCardiaca: form.frecuenciaCardiaca ? Number(form.frecuenciaCardiaca) : null,
        frecuenciaRespiratoria: form.frecuenciaRespiratoria ? Number(form.frecuenciaRespiratoria) : null,
        presionSistolica: form.presionSistolica ? Number(form.presionSistolica) : null,
        presionDiastolica: form.presionDiastolica ? Number(form.presionDiastolica) : null,
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
      }
     finally {
      setEnviando(false);
    }
  };

  const deshabilitado = lookup.status !== "ok";

  return (
    <form onSubmit={handleSubmit} className="urgencia-form">
      <h3>Registrar Urgencia</h3>

      <label>
        CUIL Paciente:
        <div style={{ display: "flex", gap: 8 }}>
          <input
            value={form.cuilPaciente}
            onChange={(e) => actualizar("cuilPaciente", e.target.value)}
            placeholder="20XXXXXXXXX"
            required
            style={{ flex: 1 }}
          />
          <button type="button" onClick={buscarPaciente}>
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
        <p style={{ color: "#b45309", marginTop: 6 }}>
          ⚠️ Paciente no existe.{" "}
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
              font: "inherit"
            }}
          >
            Registrarlo ahora.
          </button>
        </p>
      )}
      {lookup.status === "error" && (
        <p style={{ color: "crimson", marginTop: 6 }}>
          ❌ {lookup.error}
        </p>
      )}

      <label>
        Informe:
        <input
          value={form.informe}
          onChange={(e) => actualizar("informe", e.target.value)}
          placeholder="Breve descripción del estado"
          required
          disabled={deshabilitado}
        />
      </label>

      <label>
        Nivel de Emergencia:
        <select
          value={form.nivelEmergencia}
          onChange={(e) => actualizar("nivelEmergencia", e.target.value)}
          disabled={deshabilitado}
        >
          <option value="CRITICA">Crítica</option>
          <option value="EMERGENCIA">Emergencia</option>
          <option value="URGENCIA">Urgencia</option>
          <option value="URGENCIA_MENOR">Urgencia Menor</option>
          <option value="SIN_URGENCIA">Sin Urgencia</option>
        </select>
      </label>

      <label>
        Temperatura (°C):
        <input
          type="number" step="0.1" min="30" max="45"
          value={form.temperatura}
          onChange={(e) => actualizar("temperatura", e.target.value)}
          placeholder="36.7"
          disabled={deshabilitado}
        />
      </label>

      <label>
        Frecuencia Cardíaca (lpm):
        <input
          type="number" step="1" min="30" max="220"
          value={form.frecuenciaCardiaca}
          onChange={(e) => actualizar("frecuenciaCardiaca", e.target.value)}
          placeholder="80"
          disabled={deshabilitado}
        />
      </label>

      <label>
        Frecuencia Respiratoria (rpm):
        <input
          type="number" step="1" min="6" max="60"
          value={form.frecuenciaRespiratoria}
          onChange={(e) => actualizar("frecuenciaRespiratoria", e.target.value)}
          placeholder="18"
          disabled={deshabilitado}
        />
      </label>

      <label>
        Presión Sistólica (mmHg):
        <input
          type="number" step="1" min="60" max="250"
          value={form.presionSistolica}
          onChange={(e) => actualizar("presionSistolica", e.target.value)}
          placeholder="120"
          disabled={deshabilitado}
        />
      </label>

      <label>
        Presión Diastólica (mmHg):
        <input
          type="number" step="1" min="30" max="140"
          value={form.presionDiastolica}
          onChange={(e) => actualizar("presionDiastolica", e.target.value)}
          placeholder="80"
          disabled={deshabilitado}
        />
      </label>

      <div style={{ display: "flex", alignItems: "center", gap: "1rem", marginTop: "1rem" }}>
        <button type="submit" disabled={deshabilitado || enviando}>
          {enviando ? "Guardando…" : "Registrar"}
        </button>

        {mensaje && (
          <span style={{ fontSize: "0.95rem", color: mensaje.startsWith("✅") ? "green" : mensaje.startsWith("⚠️") ? "#b45309" : "crimson" }}>
            {mensaje}
          </span>
        )}
      </div>

    </form>
  );
}
