/**
 * No lugar do JTextField vc vai precisar usar o JFormattedTextField. 

Eu particularmente uso assim ... 

Crio um método assim ... 

private MaskFormatter setMascara(String mascara){  
    MaskFormatter mask = null;  
    try{  
        mask = new MaskFormatter(mascara);                        
        }catch(java.text.ParseException ex){}  
    return mask;  
}  



e depois na hora q vc criar o JFormattedTextField vc faz assim: 

JFormattedTextField jftfTelefone = new JFormattedTextField((setMascara("(##) ####-####")); 

Pronto ... ta ai uma mascara para o Telefone. 

isso é um exemplo, agora basta vc implementar de acordo com suas necessidades. 

Agora para título de informação: 

# = qualquer numero válido 
U = qualquer caractere, as letras minusculas são formatadas em maiuscula. 
L = qualquer caractere, as letras maiusculas são formatadas em minusculas. 
A = qualquer caractere ou numero. 
 * 
 * 
 * 
 */






/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import java.util.Calendar;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class TesteData {
    
    
    
    public void testaData(){
        String data;
        data = "10022014";
        Calendar aux = ValidarStrings.converteStringParaData(data);
        System.out.println("dia é "+ValidarStrings.getDiaData(aux));
        System.out.println("mes é "+ValidarStrings.getMesData(aux));
        System.out.println("ano é "+ValidarStrings.getAnoData(aux));
        
//        // escreve data de hoje
//        aux = Calendar.getInstance();
//        System.out.println("dia é "+ValidarStrings.getDiaData(aux));
//        System.out.println("mes é "+ValidarStrings.getMesData(aux));
//        System.out.println("ano é "+ValidarStrings.getAnoData(aux));        
//        
//        // escreve data de hoje
//        System.out.println("data de hoje é "+ValidarStrings.getDataHoje());        
//        
//        // escreve data de hoje
//        System.out.println("data de hoje é "+Integer.parseInt(ValidarStrings.getDataHoje()));            
//        
        // simula conversao de data do banco de dados
 //       data = "22051975";
 //       System.out.println("data de hoje é "+ValidarStrings.converteDataBDParaDataComboBox(data));      
//        System.out.println("O dia da semana do dia de hoje é "+ValidarStrings.getDiaSemana());
//        data = "29072013";
//        System.out.println("O dia da semana do dia de hoje é "+ValidarStrings.getDiaSemana(ValidarStrings.converteStringParaData(data)));
//        data = "30072013";
//        System.out.println("O dia da semana do dia de hoje é "+ValidarStrings.getDiaSemana(ValidarStrings.converteStringParaData(data)));
//        data = "31072013";
//        System.out.println("O dia da semana do dia de hoje é "+ValidarStrings.getDiaSemana(ValidarStrings.converteStringParaData(data)));
//        data = "31/07/2013";
//        System.out.println(Data.retiraMascaraData(data));
//        data = "(54) 3201-1811";
//        System.out.println(Data.retiraMascaraTelefone(data));

        
    }
    
    public static void main (String args[]){
        new TesteData().testaData();
      //  System.out.println(java.util.Calendar.getInstance().getTimeInMillis());
    }
    
    
}
