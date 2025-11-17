import React from "react";
import RegistrarUrgenciaForm from "../components/RegistrarUrgenciaForm";

export default function RegistrarUrgenciaPage({ usuario }) {
  return (
    <RegistrarUrgenciaForm
      nombreEnfermera={usuario?.nombre || ""}
      apellidoEnfermera={usuario?.apellido || ""}
    />
  );
}
