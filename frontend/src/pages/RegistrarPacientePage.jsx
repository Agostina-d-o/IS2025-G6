import React from "react";
import { useNavigate } from "react-router-dom";
import RegistrarPacienteForm from "../components/RegistrarPacienteForm";

export default function RegistrarPacientePage() {
  const navigate = useNavigate();

  return (
    <RegistrarPacienteForm
      onOk={() => {
        alert("✅ Paciente registrado correctamente.");
        navigate("/pendientes");
      }}
    />
  );
}
