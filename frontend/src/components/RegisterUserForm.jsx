import React, { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function RegisterUserForm() {
  const [email, setEmail] = useState("");
  const [contrasenia, setContrasenia] = useState("");
  const [rol, setRol] = useState("ENFERMERA");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    // Validaciones simples
    if (!nombre.trim() || !apellido.trim()) {
      setError("Nombre y apellido son obligatorios");
      return;
    }
    // Si querés exigir específicamente .com:
    if (!email.includes("@") || !email.endsWith(".com")) {
      setError("Ingresá un email válido que termine en .com");
      return;
    }
    if (contrasenia.length < 8) {
      setError("La contraseña debe tener al menos 8 caracteres");
      return;
    }

    setCargando(true);
    try {
      await register(email, contrasenia, rol, nombre.trim(), apellido.trim());
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
        Nombre:
        <input
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
        />
      </label>

      <label>
        Apellido:
        <input
          value={apellido}
          onChange={(e) => setApellido(e.target.value)}
          required
        />
      </label>

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
