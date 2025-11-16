import React, { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function RegisterUserForm() {
  const [email, setEmail] = useState("");
  const [contrasenia, setContrasenia] = useState("");
  const [rol, setRol] = useState("ENFERMERA");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
          if (contrasenia.length < 8) {
            setError("La contraseña debe tener al menos 8 caracteres");
            setCargando(false);
            return;
          }
      await register(email, contrasenia, rol);
      alert("Usuario registrado correctamente. Ahora podés iniciar sesión.");
      navigate("/"); // ir al login
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="formulario">
      <h2>Registrarse</h2>

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

      <label>
        Rol:
        <select value={rol} onChange={(e) => setRol(e.target.value)}>
          <option value="ENFERMERA">Enfermera</option>
          <option value="MEDICO">Médico</option>
        </select>
      </label>

      {error && <p className="error">{error}</p>}

      <button type="submit" disabled={cargando}>
        {cargando ? "Registrando..." : "Registrarse"}
      </button>

      <p style={{ textAlign: "center", marginTop: "1rem" }}>
        ¿Ya tenés cuenta? <a href="/">Iniciar sesión</a>
      </p>
    </form>
  );
}
