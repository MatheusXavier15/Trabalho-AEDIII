import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Hashing.RegistroHashExtensivel;

public class pcvPessoa implements RegistroHashExtensivel<pcvPessoa> {

  private String nome;
  private String data_nasc;
  private String sexo;
  private String diagnostico;
  private int cpf;
  private short TAMANHO = 44;

  public pcvPessoa() {
    this("", "", "", "", 0);
  }

  public pcvPessoa(String n, String dt_nasc, String sexo, String diag, int cpf) {
    try {
      this.nome = n;
      this.cpf = cpf;
      this.data_nasc = dt_nasc;
      this.sexo = sexo;
      this.diagnostico = diag;
      if (nome.length() > TAMANHO)
        throw new Exception("Número de caracteres do nome maior que o permitido. Os dados serão cortados.");
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  public void setDiagnostico(String diag) {
    this.diagnostico = diag;
  }

  @Override
  public int hashCode() {
    return String.valueOf(this.cpf).hashCode();
  }

  public short size() {
    return this.TAMANHO;
  }

  public String toString() {
    return this.nome + ";" + this.sexo + ";" + this.cpf + ";" + this.data_nasc + ";" + this.diagnostico;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(nome);
    dos.writeInt(cpf);
    dos.writeUTF(sexo);
    dos.writeUTF(data_nasc);
    dos.writeUTF(diagnostico);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[TAMANHO];
    for (int i = 0; i < TAMANHO; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < TAMANHO; i++)
      bs2[i] = bs[i];
    return bs2;
  }

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