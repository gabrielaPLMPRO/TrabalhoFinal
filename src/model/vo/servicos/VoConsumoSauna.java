/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.servicos;

import model.vo.I_VO;
import model.vo.cadastros.VoConsumo;

/**
 *
 * @author dlnotari
 */
public class VoConsumoSauna extends VoConsumo implements I_VO {
    // atributos
    private String nome;

    // construtor
    public VoConsumoSauna(int codigoCliente, String data, int sequencia,
            String nome) {
        super(codigoCliente, data, sequencia);
        this.nome = nome;
    }
    
    // construtor
    public VoConsumoSauna() {
        super();
        this.nome = "";
    }

    // construtor
    public VoConsumoSauna(VoConsumo vo, String nome) {
        super(vo.getCodigoCliente(), vo.getData(), vo.getSequencia(), vo.getQuantidade(),
                vo.isPago(), vo.getCodigo(), vo.getPreco());
        this.nome = nome;
    }
    
    // construtor
    public VoConsumoSauna(String nome, String data) {
        super(0, data);
        this.nome = nome;
    }
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return super.toString() + "VoConsumoSauna{" + "nome=" + nome + '}';
    }
}
