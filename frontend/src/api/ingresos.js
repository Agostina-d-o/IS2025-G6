//registrar ingreso y obtener pacientes
const API = "http://localhost:8080/api";

export async function registrarIngreso(ingreso) {
  const res = await fetch(`${API}/urgencias/ingresos`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(ingreso),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Error al registrar ingreso");
  }

  return await res.text();
}

export async function getPendientes() {
  const res = await fetch(`${API}/urgencias/pendientes`);

  if (!res.ok) {
    throw new Error("Error al obtener ingresos pendientes");
  }

  return await res.json(); // array de ingresos
}

export async function atenderIngreso(idIngreso) {
  const res = await fetch(`${API}/urgencias/atender`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ idIngreso }),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Error al atender ingreso");
  }

  return await res.text();
}
