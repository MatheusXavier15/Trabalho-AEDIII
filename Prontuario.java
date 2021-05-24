import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Hashing.RegistroHashExtensivel;

public class Prontuario implements RegistroHashExtensivel<Prontuario>{

  private String nome;
  private String data_nasc;
  private String sexo;
  private String diagnostico;
  private int cpf;
  private short tam = 30000;

  /*
  * Descrição: Construtor da classe. 
  * Entrada: void
  * Saída: Preenchimento dos atributos da classe instanciando-os.
  */
  public Prontuario() {
    this("", "", "", "", 0);
  }

  /*
  * Descrição: Construtor da classe. 
  * Entrada: Strings para nome, data de nascimento, sexo e diagnóstico médico, inteiro para cpf
  * Saída: Preenchimento dos atributos da classe.
  */
  public Prontuario(String n, String dt_nasc, String sexo, String diag, int cpf) {
    try {
      this.nome = n;
      this.cpf = cpf;
      this.data_nasc = dt_nasc;
      this.sexo = sexo;
      this.diagnostico = diag;
      if (nome.length() > tam)
        throw new Exception("Número de caracteres do nome maior que o permitido. Os dados serão cortados.");
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  /*
  * Descrição: Setter para o diagnóstico médico.
  * Entrada: String contendo o diagnóstico médico.
  * Saída: Preenchimento da variável private String diagnostico.
  */
  public void setDiagnostico(String diag) {
    this.diagnostico = diag;
  }

  @Override
  /*
  * Descrição: Realiza o hash do elemento a partir do CPF.
  * Entrada: void.
  * Saída: Inteiro com o valor do hashCode proveniente do hash do CPF.
  */
  public int hashCode() {
    return String.valueOf(this.cpf).hashCode();
  }

  /*
  * Descrição: Método que retorna o tam.
  * Entrada: void.
  * Saída: Inteiro com o valor do tam.
  */
  public short size() {
    return this.tam;
  }

  /*
  * Descrição: Retorna o prontuário em formato de string.
  * Entrada: void.
  * Saída: String s com a descritiva do prontuário.
  */
  public String toString() {
    return this.nome + "|" + this.sexo + "|" + this.cpf + "|" + this.data_nasc + "|" + this.diagnostico;
  }

  /*
  * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output stream no formato de um vetor de bytes
  * Entrada: void
  * Saída: vetor de bytes
  */
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(nome);
    dos.writeInt(cpf);
    dos.writeUTF(sexo);
    dos.writeUTF(data_nasc);
    dos.writeUTF(diagnostico);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[tam];
    for (int i = 0; i < tam; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < tam; i++)
      bs2[i] = bs[i];
    return bs2;
  }

  /*
  * Descrição: recebe um array de bytes e carrega os dados na memoria principal
  * Entrada: vetor de byte ba
  * Saída: preenche os elementos do cesto
  */
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.nome = dis.readUTF();
    this.cpf = dis.readInt();
    this.sexo = dis.readUTF();
    this.data_nasc = dis.readUTF();
    this.diagnostico = dis.readUTF();
  }

}