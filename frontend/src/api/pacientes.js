//registrar pacientes
const API = "http://localhost:8080/api";

export async function crearPaciente(paciente) {
  const res = await fetch(`${API}/pacientes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(paciente),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Error al registrar paciente");
  }

  return await res.text();
}

export async function getPacientesRegistrados() {
  const r = await fetch(`${API}/pacientes`);
  if (!r.ok) throw new Error("No se pudieron obtener los pacientes");
  return await r.json();
}

export async function getPacienteByCuil(cuil) {
  const r = await fetch(`${API}/pacientes/${encodeURIComponent(cuil)}`);
  if (r.status === 404) return null;
  if (!r.ok) throw new Error("No se pudo buscar el paciente");
  return await r.json();
