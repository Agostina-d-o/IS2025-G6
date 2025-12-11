import React, { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";
import "../styles/RegisterUserForm.css";

export default function RegisterUserForm() {
  const [email, setEmail] = useState("");
  const [contrasenia, setContrasenia] = useState("");
  const [rol, setRol] = useState("ENFERMERA");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [matricula, setMatricula] = useState("");
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!nombre.trim() || !apellido.trim()) {
      setError("Nombre y apellido son obligatorios");
      return;
    }
    if (!email.includes("@") || !email.endsWith(".com")) {
      setError("Ingresá un email válido que termine en .com");
      return;
    }
    if (contrasenia.length < 8) {
      setError("La contraseña debe tener al menos 8 caracteres");
      return;
    }
    if ((rol === "ENFERMERA" || rol === "MEDICO") && !matricula.trim()) {
      setError("La matrícula es obligatoria para médicos y enfermeras");
      return;
    }

    setCargando(true);
    try {
      await register(
        email,
        contrasenia,
        rol,
        nombre.trim(),
        apellido.trim(),
        matricula.trim()
      );
      alert("Usuario registrado correctamente. Ahora podés iniciar sesión.");
      navigate("/");
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="formulario">
      <h2>Registrarse</h2>

      <div className="nombre-apellido">
        <div>
          <label>Nombre:</label>
          <input
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Apellido:</label>
          <input
            value={apellido}
            onChange={(e) => setApellido(e.target.value)}
            required
          />
        </div>
      </div>

      <label>Email:
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </label>

      <label>Contraseña:
        <input
          type="password"
          value={contrasenia}
          onChange={(e) => setContrasenia(e.target.value)}
          required
        />
      </label>

      <div className="rol-matricula">
        <div>
          <label>Rol:</label>
          <select value={rol} onChange={(e) => setRol(e.target.value)} required>
            <option value="ENFERMERA">Enfermera</option>
            <option value="MEDICO">Médico</option>
          </select>
        </div>
        <div>
          <label>Matrícula:</label>
          <input
            value={matricula}
            onChange={(e) => setMatricula(e.target.value)}
            required
          />
        </div>
      </div>

      {error && <p className="error">{error}</p>}

      <button type="submit" disabled={cargando}>
        {cargando ? "Registrando..." : "Registrarse"}
      </button>

      <p className="login-register-link">
        ¿Ya tenés cuenta? <a href="/">Iniciar sesión</a>
      </p>
    </form>
  );
}
