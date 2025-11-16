import React from "react";
import RegistrarUrgenciaForm from "../components/RegistrarUrgenciaForm";

export default function RegistrarUrgenciaPage({ usuario }) {
  return (
    <div>
      <h2>Registrar Urgencia</h2>
      <RegistrarUrgenciaForm emailEnfermera={usuario.email} />
    </div>
  );
}
