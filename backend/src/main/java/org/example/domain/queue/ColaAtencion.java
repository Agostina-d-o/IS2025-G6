package org.example.domain.queue;

import org.example.domain.Ingreso;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ColaAtencion {
    // Usa el compareTo(Ingreso) ya definido en Ingreso
    private final PriorityQueue<Ingreso> pq = new PriorityQueue<>(Ingreso::compareTo);

    public void agregar(Ingreso ingreso) { pq.add(ingreso); }

    // Ver el próximo sin sacarlo
    public Ingreso proximo() { return pq.peek(); }

    //** Atender (saca de la cola)
    public Ingreso atender() { return pq.poll(); }

    // Vista ordenada actual (no modifica la cola)
    public List<Ingreso> verComoLista() {
        List<Ingreso> copia = new ArrayList<>(pq);
        copia.sort(Ingreso::compareTo);
        return copia;
    }

    public int tamano() { return pq.size(); }
    public boolean estaVacia() { return pq.isEmpty(); }
}
