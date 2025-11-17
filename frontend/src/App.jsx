// src/App.jsx
import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterUserPage from "./pages/RegisterUserPage";
import RegistrarPacientePage from "./pages/RegistrarPacientePage";
import RegistrarUrgenciaPage from "./pages/RegistrarUrgenciaPage";
import PacientesRegistradosPage from "./pages/PacientesRegistradosPage.jsx";
import PendientesPage from "./pages/PendientesPage";
import AtencionMedicaPage from "./pages/AtencionMedicaPage";
import Navbar from "./components/Navbar";

export default function App() {
  const [usuario, setUsuario] = useState(() => {
      try { return JSON.parse(localStorage.getItem("usuario")) || null; }
      catch { return null; }
    });

  useEffect(() => {
    try {
      if (usuario) localStorage.setItem("usuario", JSON.stringify(usuario));
      else localStorage.removeItem("usuario");
    } catch {}
  }, [usuario]);

  const handleLogout = () => setUsuario(null);


  const rol = usuario?.rol?.toLowerCase();
  const onLogin = async (email, pass) => {
    const u = await login(email, pass);
    setUsuario(u);
    localStorage.setItem("usuario", JSON.stringify(u));
  };

  const onRegister = async (dto) => {
    const u = await register(dto);
    setUsuario(u);
    localStorage.setItem("usuario", JSON.stringify(u));
  };

  return (
    <Router>
      {usuario && <Navbar usuario={usuario} onLogout={handleLogout} />}

      <Routes>
        {/* Home */}
        <Route
          path="/"
          element={
            usuario ? (
              <Navigate to="/pendientes" />
            ) : (
              <LoginPage setUsuario={setUsuario} />
            )
          }
        />

        {/* Registrar paciente – SOLO enfermera */}
        <Route
          path="/registrar-paciente"
          element={
            rol === "enfermera" ? (
              <RegistrarPacientePage />
            ) : (
              <Navigate to="/" />
            )
          }
        />

        <Route
          path="/registrar-urgencia"
          element={<RegistrarUrgenciaPage usuario={usuario} />}
        />

        {/* Ver pendientes – enfermera y médico */}
        <Route
          path="/pendientes"
          element={
            usuario ? (
              <PendientesPage usuario={usuario} />
            ) : (
              <Navigate to="/" />
            )
          }
        />

        {/* Atención médica – SOLO médico */}
        <Route
          path="/atencion"
          element={
            rol === "medico" ? (
              <AtencionMedicaPage usuario={usuario} />
            ) : (
              <Navigate to="/" />
            )
          }
        />

        {/* registro de usuarios */}
        <Route
          path="/register"
          element={<RegisterUserPage setUsuario={setUsuario} />}
        />

        <Route path="/pacientes" element={<PacientesRegistradosPage />} />
      </Routes>
    </Router>
  );
}
