import React, { useState } from "react";
import { crearPaciente } from "../api/pacientes";


export default function RegistrarPacienteForm() {
const [form, setForm] = useState({
cuil: "",
nombre: "",
apellido: "",
calle: "",
numero: "",
localidad: "San Miguel de Tucumán",
codigoObraSocial: "",
numeroAfiliado: "",
});
const [mensaje, setMensaje] = useState(null);


const actualizar = (campo, valor) => setForm((f) => ({ ...f, [campo]: valor }));


const handleSubmit = async (e) => {
e.preventDefault();
setMensaje(null);
try {
await crearPaciente(form);
setMensaje("Paciente registrado con éxito");
} catch (err) {
setMensaje("Error: " + err.message);
}
};


return (
<form onSubmit={handleSubmit} className="paciente-form">
<h3>Nuevo Paciente</h3>
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