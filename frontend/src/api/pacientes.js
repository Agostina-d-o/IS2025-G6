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
