/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.swing.text.MaskFormatter;

/**
 *
 * @author dlnotari
 */
public class Data {

    // retorna o número de segundas em um mês, por exemplo
    public static int numeroDiasMes(String dataHoje, int dia) {
        return 4;
    }

    /**
     * formata mascara de data
     *
     * @param mascara
     * @return
     */
    private static MaskFormatter setMascara(String mascara) {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter(mascara);
        } catch (java.text.ParseException ex) {
        }
        return mask;
    }
    
    /**
     * devolve a mascara para data
     * @return 
     */
    public static MaskFormatter getMascaraData(){
        return setMascara("##/##/####");
    }
    
    /**
     * devolve a mascara para telefone
     * @return 
     */
    public static MaskFormatter getMascaraTelefone(){
        return setMascara("(##) ####-####");
    }    
    
    /**
     * retirar mascara data
     * @param data
     * @return 
     */
    public static String retiraMascaraData(String data) {
        return (data.substring(0,2)+data.substring(3,5)+data.substring(6,10)).trim();
    }
    
    /**
     * retirar mascara telefone
     * @param data
     * @return 
     */
    public static String retiraMascaraTelefone(String telefone) {
       telefone = telefone.replace("(", "");
       telefone = telefone.replace(")", "");
       telefone = telefone.replace("-", "");
       telefone = telefone.replace(" ", "");
       return telefone.trim();
    }
    
    /**
     * converte valor do dia para interiro
     * @param valor
     * @return 
     */
    public static int converteStringToInteiro(String valor) {
        // variavel local
        int retorno = 0;
        
        // testa se esta vazio
        if (!valor.isEmpty()) {
            retorno = Integer.parseInt(valor);
        }          
        
        // retorna valor
        return retorno;
    }    
}
