
const API = (import.meta?.env?.VITE_API_BASE) ?? "http://localhost:8080/api";

export async function crearPaciente(dto) {
  const resp = await fetch(`${API}/pacientes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  });

  if (!resp.ok) {
    const text = await resp.text();
    throw new Error(`POST /pacientes ${resp.status}: ${text}`);
  }

  const text = await resp.text();
  return text;
}


export async function getPacientesRegistrados() {
  const resp = await fetch(`${API}/pacientes`);
  if (!resp.ok) {
    const text = await resp.text();
    throw new Error(`GET /pacientes ${resp.status}: ${text}`);
  }
  return resp.json();
}

export async function getPacienteByCuil(cuil) {
  const resp = await fetch(`${API}/pacientes/${cuil}`);

  if (resp.status === 404) return null;

  if (!resp.ok) {
    const text = await resp.text();
    throw new Error(`Error ${resp.status}: ${text}`);
  }

  return resp.json();
}
