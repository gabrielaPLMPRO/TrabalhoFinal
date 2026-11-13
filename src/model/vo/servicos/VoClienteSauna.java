/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.servicos;

import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoCliente;
import java.util.ArrayList;
import model.vo.I_VO;

/**
 *
 * @author dlnotari
 */
public class VoClienteSauna implements I_VO{
    // atributos
    private VoCliente cliente;
    private ArrayList<VoConsumo> consumo;
    private boolean consumoPago;            
    
    // construtor
    public VoClienteSauna() {
        this.cliente = new VoCliente();
        this.consumo = new ArrayList<>();
        this.consumoPago = false;
    }    

    public VoClienteSauna(VoCliente cliente) {
        this.cliente = cliente;
        this.consumo = new ArrayList<>();  
        this.consumoPago = false;        
    }

    public VoClienteSauna(VoCliente cliente, ArrayList<VoConsumo> consumo, boolean consumoPago) {
        this.cliente = cliente;
        this.consumo = consumo;
        this.consumoPago = consumoPago;
    }
    
    public VoCliente getCliente() {
        return cliente;
    }

    public ArrayList<VoConsumo> getConsumo() {
        return consumo;
    }

    public void setConsumo(ArrayList<VoConsumo> consumo) {
        this.consumo = consumo;
    }

    public boolean isConsumoPago() {
        return consumoPago;
    }

    public void setConsumoPago(boolean consumoPago) {
        this.consumoPago = consumoPago;
    } 
}
