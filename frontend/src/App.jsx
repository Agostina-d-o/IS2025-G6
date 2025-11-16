// src/App.jsx
import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterUserPage from "./pages/RegisterUserPage";
import RegistrarPacientePage from "./pages/RegistrarPacientePage";
import RegistrarUrgenciaPage from "./pages/RegistrarUrgenciaPage";
import PendientesPage from "./pages/PendientesPage";
import AtencionMedicaPage from "./pages/AtencionMedicaPage";
import Navbar from "./components/Navbar";

export default function App() {
  const [usuario, setUsuario] = useState(null); // { email, rol }

  const handleLogout = () => setUsuario(null);

  return (
    <Router>
      {usuario && <Navbar rol={usuario.rol} onLogout={handleLogout} />}
      <Routes>
        <Route
          path="/"
          element={
            usuario ? (
              <Navigate to={usuario.rol === "medico" ? "/atencion" : "/pendientes"} />
            ) : (
              <LoginPage setUsuario={setUsuario} />
            )
          }
        />
        <Route
          path="/registrar-paciente"
          element={
            usuario?.rol === "enfermera" ? (
              <RegistrarPacientePage emailActor={usuario.email} />
            ) : (
              <Navigate to="/" />
            )
          }
        />
        <Route
          path="/registrar-urgencia"
          element={
            usuario?.rol === "enfermera" ? (
              <RegistrarUrgenciaPage emailActor={usuario.email} />
            ) : (
              <Navigate to="/" />
            )
          }
        />
        <Route
          path="/pendientes"
          element={
            usuario ? (
              <PendientesPage rol={usuario.rol} />
            ) : (
              <Navigate to="/" />
            )
          }
        />
        <Route
          path="/atencion"
          element={
            usuario?.rol === "medico" ? (
              <AtencionMedicaPage />
            ) : (
              <Navigate to="/" />
            )
          }
        />
        <Route path="/register" element={<RegisterUserPage setUsuario={setUsuario} />} />
      </Routes>
    </Router>
  );
}
