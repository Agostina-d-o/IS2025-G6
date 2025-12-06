import React from "react";
import { useSearchParams } from "react-router-dom";
import RegistrarPacienteForm from "../components/RegistrarPacienteForm";

export default function RegistrarPacientePage() {
  const [qs] = useSearchParams();
  const cuilPrefill = qs.get("cuil") || "";

  return <RegistrarPacienteForm cuilPrefill={cuilPrefill} />;
}
