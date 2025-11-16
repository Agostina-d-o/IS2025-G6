import React, { useState } from "react";
import { registrarIngreso } from "../api/ingresos";


export default function RegistrarUrgenciaForm({ emailEnfermera }) {
const [form, setForm] = useState({
cuilPaciente: "",
informe: "",
nivelEmergencia: "BAJA", // opciones: BAJA, MEDIA, ALTA, CRITICA
temperatura: "",
frecuenciaCardiaca: "",
frecuenciaRespiratoria: "",
presionSistolica: "",
presionDiastolica: "",
});
const [mensaje, setMensaje] = useState(null);


const actualizar = (campo, valor) => setForm((f) => ({ ...f, [campo]: valor }));


const handleSubmit = async (e) => {
e.preventDefault();
setMensaje(null);
try {
await registrarIngreso({ ...form, emailEnfermera });
setMensaje("Ingreso registrado correctamente");
} catch (err) {
setMensaje("Error: " + err.message);
}
};


return (
<form onSubmit={handleSubmit} className="urgencia-form">
<h3>Registrar Urgencia</h3>
{Object.entries(form).map(([k, v]) => (
<label key={k}>
{k}:
<input
value={v}
onChange={(e) => actualizar(k, e.target.value)}
/>
</label>
))}
<button type="submit">Registrar</button>
{mensaje && <p>{mensaje}</p>}
</form>
);
}