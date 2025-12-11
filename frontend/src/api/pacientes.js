const API = (import.meta?.env?.VITE_API_BASE) ?? "http://localhost:8080/api";

export async function crearPaciente(dto) {
  const resp = await fetch(`${API}/pacientes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  });

  if (!resp.ok) {
    let msg = "No se pudo registrar el paciente";

    try {
      // El backend devuelve { "message": "CUIL inválido (11 dígitos)" }
      const data = await resp.json();
      if (data && data.message) {
        msg = data.message;
      }
    } catch {
      // Si por alguna razón no es JSON, usamos el texto crudo
      try {
        const text = await resp.text();
        if (text) msg = text;
      } catch {
        // ignoramos, nos quedamos con el msg por defecto
      }
    }

    throw new Error(msg);
  }

  // En éxito, tu backend devuelve un String ("Paciente registrado con éxito")
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
