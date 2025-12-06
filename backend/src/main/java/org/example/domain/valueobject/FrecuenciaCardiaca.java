package org.example.domain.valueobject;

public class FrecuenciaCardiaca extends Frecuencia{

    public FrecuenciaCardiaca(Float value){
        super(value);
    }

    @Override
    protected RuntimeException notificarError() {
        return new RuntimeException("La frecuencia cardíaca no puede ser negativa");
    }

    protected  String mensajeFaltanteErronea(){
        return "Falta la frecuencia cardíaca";
    }

    //20.5 lmp
    @Override
    public String getValorFormateado() {
        return String.format("%.1f lpm",this.value);
    }


}
