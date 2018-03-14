package tkn.fidegar.models;

import java.util.Date;

/**
 * Created by Alfred on 13/03/2018.
 */

public class SmsModel {
    private String clave;
    private String folio;
    private String fechaVigencia;
    private Date fechaEnvio;


    public SmsModel(String clave, String folio, String fechaVigencia, Date fechaEnvio) {
        this.clave = clave;
        this.folio = folio;
        this.fechaVigencia = fechaVigencia;
        this.fechaEnvio = fechaEnvio;
    }

    public String getClave() {
        return clave;
    }

    public String getFolio() {
        return folio;
    }

    public String getFechaVigencia() {
        return fechaVigencia;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }
}
