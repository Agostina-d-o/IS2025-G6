//login y register
const API = "http://localhost:8080/api";

export async function login(email, contrasenia) {
  const res = await fetch(`${API}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, contrasenia }),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Credenciales inválidas");
  }

  return await res.json();
}

export async function register(email, contrasenia, rol, nombre, apellido, matricula) {
  const res = await fetch(`${API}/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      email,
      contrasenia,
      rol,
      nombre,
      apellido,
      matricula,
    }),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || "Error al registrar usuario");
  }

  return await res.json();
}
