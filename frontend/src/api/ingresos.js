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
  if (!res.ok) throw new Error("Error al obtener ingresos pendientes");
  return await res.json();
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
  return await res.json(); // IngresoPendienteDTO
}

export async function finalizarIngreso(idIngreso, diagnostico) {
  const res = await fetch(`${API}/urgencias/finalizar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ idIngreso: String(idIngreso), diagnostico }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Error al finalizar ingreso");
  }
  return await res.json();
}

export async function getEnProceso() {
  const res = await fetch(`${API}/urgencias/en-proceso`);
  if (!res.ok) throw new Error("Error al obtener ingresos en proceso");
  return res.json();
}

export async function getFinalizados() {
  const r = await fetch(`${API}/urgencias/finalizados`);
  if (!r.ok) throw new Error("Error al obtener finalizados");
  return r.json();
}
