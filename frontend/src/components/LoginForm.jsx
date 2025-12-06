import React, { useState } from "react";
import { login } from "../api/auth";

export default function LoginForm({ onLogin }) {
  const [email, setEmail] = useState("");
  const [contrasenia, setContrasenia] = useState("");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
      const usuario = await login(email, contrasenia);
      onLogin(usuario); // { email, rol }
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="formulario">
      <h2>Iniciar Sesión</h2>

      <label>
        Email:
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </label>

      <label>
        Contraseña:
        <input
          type="password"
          value={contrasenia}
          onChange={(e) => setContrasenia(e.target.value)}
          required
        />
      </label>

      {error && <p className="error">{error}</p>}

      <button type="submit" disabled={cargando}>
        {cargando ? "Ingresando..." : "Ingresar"}
      </button>
    </form>
  );
}
