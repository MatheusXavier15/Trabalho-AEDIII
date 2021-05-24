package Hashing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

public class HashExtensivel<T extends RegistroHashExtensivel<T>> {

  int quantidadeDadosPorCesto;
  String nomeArquivoDiretorio;
  String nomeArquivoCestos;
  RandomAccessFile arqDiretorio;
  RandomAccessFile arqCestos;
  Diretorio diretorio;
  Constructor<T> construtor;

  public class Cesto {

    ArrayList<T> elementos; // sequência de elementos armazenados
    Constructor<T> construtor; // Construtor da classe
    byte profundidadeLocal; // profundidade local do cesto
    short quantidade; // quantidade de pares presentes no cesto
    short quantidadeMaxima; // quantidade máxima de pares que o cesto pode conter
    short bytesPorElemento; // tamanho fixo de cada elemento em bytes
    int bytesPorCesto; // tamanho fixo do cesto em bytes

    public Cesto(Constructor<T> ct, int qtdmax) throws Exception {
      this(ct, qtdmax, 0);
    }

    /*
    * Descrição: Construtor da classe.
    * Entrada: Constructor<T>, inteiro de quantidade maxima por cesto e inteiro profundidade local.
    * Saída: Preenchimento dos atributos da classe.
    */
    public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
      construtor = ct;
      if (qtdmax > 32767)
        throw new Exception("Quantidade máxima de 32.767 elementos");
      if (pl > 127)
        throw new Exception("Profundidade local máxima de 127 bits");
      profundidadeLocal = (byte) pl;
      quantidade = 0;
      quantidadeMaxima = (short) qtdmax;
      elementos = new ArrayList<>(quantidadeMaxima);
      bytesPorElemento = ct.newInstance().size();
      bytesPorCesto = (short) (bytesPorElemento * quantidadeMaxima + 3);
    }

    /*
    * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output stream no formato de um vetor de bytes
    * Entrada: void
    * Saída: vetor de bytes
    */
    public byte[] toByteArray() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeByte(profundidadeLocal);
      dos.writeShort(quantidade);
      int i = 0;
      while (i < quantidade) {
        dos.write(elementos.get(i).toByteArray());
        i++;
      }
      byte[] vazio = new byte[bytesPorElemento];
      while (i < quantidadeMaxima) {
        dos.write(vazio);
        i++;
      }
      return baos.toByteArray();
    }

    /*
    * Descrição: recebe um array de bytes e carrega os dados na memoria principal
    * Entrada: vetor de byte ba
    * Saída: preenche os elementos do cesto
    */
    public void fromByteArray(byte[] ba) throws Exception {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
      profundidadeLocal = dis.readByte();
      quantidade = dis.readShort();
      int i = 0;
      elementos = new ArrayList<>(quantidadeMaxima);
      byte[] dados = new byte[bytesPorElemento];
      T elem;
      while (i < quantidadeMaxima) {
        dis.read(dados);
        elem = construtor.newInstance();
        elem.fromByteArray(dados);
        elementos.add(elem);
        i++;
      }
    }

    /*
    * Descrição: Boolean Cria um bucket.
    * Entrada: um elemento T a ser inserido no cesto.
    * Saída: Boolean true confirmando a criação.
    */
    public boolean create(T elem) {
      if (full())
        return false;
      int i = quantidade - 1;
      while (i >= 0 && elem.hashCode() < elementos.get(i).hashCode())
        i--;
      elementos.add(i + 1, elem);
      quantidade++;
      return true;
    }

    /*
    * Descrição: Procura no cesto um elemento ao passar a sua chave de hash.
    * Entrada: Inteiro com o valor da chave resultante do hash.
    * Saída: Retorna um elemento ou null.
    */
    public T read(int chave) {
      if (empty())
        return null;
      int i = 0;
      while (i < quantidade && chave > elementos.get(i).hashCode())
        i++;
      if (i < quantidade && chave == elementos.get(i).hashCode())
        return elementos.get(i);
      else
        return null;
    }

    /*
    * Descrição: Substitui o elemento recebido pelo da seu correspondente, atualizando.
    * Entrada: Elemento do tipo T.
    * Saída: Retorna um boolean confirmando a exclusão ou não.
    */
    public boolean update(T elem) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && elem.hashCode() > elementos.get(i).hashCode())
        i++;
      if (i < quantidade && elem.hashCode() == elementos.get(i).hashCode()) {
        elementos.set(i, elem);
        return true;
      } else
        return false;
    }
    
    /*
    * Descrição: Procura e deleta um elemento pela sua chave.
    * Entrada: Inteiro com o valor da chave resultante do hash.
    * Saída: Boolean true confirmando a exclusão ou false indicando falha na exclusão.
    */
    public boolean delete(int chave) {
      if (empty())
        return false;
      int i = 0;
      while (i < quantidade && chave > elementos.get(i).hashCode())
        i++;
      if (chave == elementos.get(i).hashCode()) {
        elementos.remove(i);
        quantidade--;
        return true;
      } else
        return false;
    }

    /*
    * Descrição: Boolean verifica se está vazio.
    * Entrada: void.
    * Saída: Boolean true se o valor de quantidade for zero ou false caso diferente de zero.
    */
    public boolean empty() {
      return quantidade == 0;
    }

    /*
    * Descrição: Boolean verifica se está cheio.
    * Entrada: void.
    * Saída: Boolean true se o valor de quantidade for igual ao de quantidade máxima ou false caso diferente.
    */
    public boolean full() {
      return quantidade == quantidadeMaxima;
    }

    /*
    * Descrição: Retorna os elementos em formato de string.
    * Entrada: void.
    * Saída: String s com a descritiva do elemento.
    */
    public String toString() {
      String s = "Profundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
      int i = 0;
      while (i < quantidade) {
        s += elementos.get(i).toString() + " | ";
        i++;
      }
      while (i < quantidadeMaxima) {
        s += "- | ";
        i++;
      }
      return s;
    }

    /*
    * Descrição: Método que retorna o tamanho do cesto em bytes.
    * Entrada: void.
    * Saída: Inteiro bytesPorCesto com o valor do tamanho do cesto em bytes.
    */
    public int size() {
      return bytesPorCesto;
    }

  }

  protected class Diretorio {

    byte profundidadeGlobal;
    long[] enderecos;
    
    /*
    * Descrição: Construtor da classe Diretorio.
    * Entrada: void.
    * Saída: Preenchimento dos atributos da classe.
    */
    public Diretorio() {
      profundidadeGlobal = 0;
      enderecos = new long[1];
      enderecos[0] = 0;
    }

    /*
    * Descrição: Atualiza o endereco de um elemento.
    * Entrada: novo endereço p com elemento e.
    * Saída: Boolean com sucesso ou não na atualização.
    */
    public boolean atualizaEndereco(int p, long e) {
      if (p > Math.pow(2, profundidadeGlobal))
        return false;
      enderecos[p] = e;
      return true;
    }

    /*
    * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output stream no formato de um vetor de bytes
    * Entrada: void
    * Saída: vetor de bytes
    */
    public byte[] toByteArray() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeByte(profundidadeGlobal);
      int quantidade = (int) Math.pow(2, profundidadeGlobal);
      int i = 0;
      while (i < quantidade) {
        dos.writeLong(enderecos[i]);
        i++;
      }
      return baos.toByteArray();
    }

    /*
    * Descrição: recebe um array de bytes e carrega os dados na memoria principal
    * Entrada: vetor de byte ba
    * Saída: preenche os elementos do cesto
    */
    public void fromByteArray(byte[] ba) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      DataInputStream dis = new DataInputStream(bais);
      profundidadeGlobal = dis.readByte();
      int quantidade = (int) Math.pow(2, profundidadeGlobal);
      enderecos = new long[quantidade];
      int i = 0;
      while (i < quantidade) {
        enderecos[i] = dis.readLong();
        i++;
      }
    }

    /*
    * Descrição: Retorna os elementos em formato de string.
    * Entrada: void.
    * Saída: String s com a descritiva do elemento.
    */
    public String toString() {
      String s = "\nProfundidade global: " + profundidadeGlobal;
      int i = 0;
      int quantidade = (int) Math.pow(2, profundidadeGlobal);
      while (i < quantidade) {
        s += "\n" + i + ": " + enderecos[i];
        i++;
      }
      return s;
    }

    /*
    * Descrição: Retorna o endereço do cesto de acordo com a posição.
    * Entrada: Posição p.
    * Saída: Endereço do Cesto.
    */
    protected long endereço(int p) {
      if (p > Math.pow(2, profundidadeGlobal))
        return -1;
      return enderecos[p];
    }

    /*
    * Descrição: Duplica a quantidade de posições no diretorio se profundidade menor que 127.
    * Entrada: void.
    * Saída: Retorna boolean para mostrar se operação foi bem sucedida ou não.
    */
    protected boolean duplica() {
      if (profundidadeGlobal == 127)
        return false;
      profundidadeGlobal++;
      int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
      int q2 = (int) Math.pow(2, profundidadeGlobal);
      long[] novosEnderecos = new long[q2];
      int i = 0;
      while (i < q1) {
        novosEnderecos[i] = enderecos[i];
        i++;
      }
      while (i < q2) {
        novosEnderecos[i] = enderecos[i - q1];
        i++;
      }
      enderecos = novosEnderecos;
      return true;
    }

    /*
    * Descrição: Calcula o hash de acordo com uma chave recebida e com a profundidade global.
    * Entrada: Int chave.
    * Saída: Int hash gerado.
    * Obs: Para efeito de determinar o cesto em que o elemento deve ser inserido, só serão considerados valores absolutos da chave.
    */
    protected int hash(int chave) {
      return Math.abs(chave) % (int) Math.pow(2, profundidadeGlobal);
    }

    /*
    * Descrição: Calcula o hash de acordo com uma chave e com uma dada profundidade.
    * Entrada: Int chave.
    * Saída: Int hash gerado.
    * Obs: Para efeito de determinar o cesto em que o elemento deve ser inserido, só serão considerados valores absolutos da chave.
    */
    protected int hash(int chave, int pl) { // cálculo do hash para uma dada profundidade local
      return Math.abs(chave) % (int) Math.pow(2, pl);
    }

  }

  /*
  * Descrição: Construtor da classe HashExtensivel.
  * Entrada: Constructor<T>, Int quantidade maxima por cesto, String nome do arquivo para diretorio, Strin nome do arquivo para cestos
  * Saída: Preenchimento dos atributos da classe.
  */
  public HashExtensivel(Constructor<T> ct, int n, String nd, String nc) throws Exception {
    construtor = ct;
    quantidadeDadosPorCesto = n;
    nomeArquivoDiretorio = nd;
    nomeArquivoCestos = nc;

    arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
    arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

    // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista
    // de cestos
    if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {

      // Cria um novo diretório, com profundidade de 0 bits (1 único elemento)
      diretorio = new Diretorio();
      byte[] bd = diretorio.toByteArray();
      arqDiretorio.write(bd);

      // Cria um cesto vazio, já apontado pelo único elemento do diretório
      Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
      bd = c.toByteArray();
      arqCestos.seek(0);
      arqCestos.write(bd);
    }
  }

  /*
  * Descrição: Insere novo elemento no cesto, fazendo trocas de posição e novas inserções quando necessário.
  * Entrada: Elemento T
  * Saída: Boolean para mostrar se processo ocorreu com sucesso ou não.
  */
  public boolean create(T elem) throws Exception {

    // Carrega o diretório
    byte[] bd = new byte[(int) arqDiretorio.length()];
    arqDiretorio.seek(0);
    arqDiretorio.read(bd);
    diretorio = new Diretorio();
    diretorio.fromByteArray(bd);

    // Identifica a hash do diretório,
    int i = diretorio.hash(elem.hashCode());

    // Recupera o cesto
    long enderecoCesto = diretorio.endereço(i);
    Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
    byte[] ba = new byte[c.size()];
    arqCestos.seek(enderecoCesto);
    arqCestos.read(ba);
    c.fromByteArray(ba);

    // Testa se a chave já não existe no cesto
    if (c.read(elem.hashCode()) != null)
      throw new Exception("Elemento já existe");

    // Testa se o cesto já não está cheio
    // Se não estiver, create o par de chave e dado
    if (!c.full()) {
      // Insere a chave no cesto e o atualiza
      c.create(elem);
      arqCestos.seek(enderecoCesto);
      arqCestos.write(c.toByteArray());
      return true;
    }

    // Duplica o diretório
    byte pl = c.profundidadeLocal;
    if (pl >= diretorio.profundidadeGlobal)
      diretorio.duplica();
    byte pg = diretorio.profundidadeGlobal;

    // Cria os novos cestos, com os seus dados no arquivo de cestos
    Cesto c1 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
    arqCestos.seek(enderecoCesto);
    arqCestos.write(c1.toByteArray());

    Cesto c2 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
    long novoEndereco = arqCestos.length();
    arqCestos.seek(novoEndereco);
    arqCestos.write(c2.toByteArray());

    // Atualiza os dados no diretório
    int inicio = diretorio.hash(elem.hashCode(), c.profundidadeLocal);
    int deslocamento = (int) Math.pow(2, pl);
    int max = (int) Math.pow(2, pg);
    boolean troca = false;
    for (int j = inicio; j < max; j += deslocamento) {
      if (troca)
        diretorio.atualizaEndereco(j, novoEndereco);
      troca = !troca;
    }

    // Atualiza o arquivo do diretório
    bd = diretorio.toByteArray();
    arqDiretorio.seek(0);
    arqDiretorio.write(bd);

    // Reinsere as chaves
    for (int j = 0; j < c.quantidade; j++) {
      create(c.elementos.get(j));
    }
    create(elem);
    return false;
  }

  /*
  * Descrição: Le os dados do diretorio que possui a chave recebida.
  * Entrada: Int chave proveniente do hash
  * Saída: Boolean para mostrar se processo ocorreu com sucesso ou não.
  */
  public T read(int chave) throws Exception {

    // Carrega o diretório
    byte[] bd = new byte[(int) arqDiretorio.length()];
    arqDiretorio.seek(0);
    arqDiretorio.read(bd);
    diretorio = new Diretorio();
    diretorio.fromByteArray(bd);

    // Identifica a hash do diretório,
    int i = diretorio.hash(chave);

    // Recupera o cesto
    long enderecoCesto = diretorio.endereço(i);
    Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
    byte[] ba = new byte[c.size()];
    arqCestos.seek(enderecoCesto);
    arqCestos.read(ba);
    c.fromByteArray(ba);

    return c.read(chave);
  }

  /*
  * Descrição: Realiza a atualização do elemento recebendo o elemento com as novas informações a serem alteradas.
  * Entrada: T elem, elemento com os valores atualizados.
  * Saída: Boolean para mostrar se processo ocorreu com sucesso ou não.
  */
  public boolean update(T elem) throws Exception {

    // Carrega o diretório
    byte[] bd = new byte[(int) arqDiretorio.length()];
    arqDiretorio.seek(0);
    arqDiretorio.read(bd);
    diretorio = new Diretorio();
    diretorio.fromByteArray(bd);

    // Identifica a hash do diretório,
    int i = diretorio.hash(elem.hashCode());

    // Recupera o cesto
    long enderecoCesto = diretorio.endereço(i);
    Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
    byte[] ba = new byte[c.size()];
    arqCestos.seek(enderecoCesto);
    arqCestos.read(ba);
    c.fromByteArray(ba);

    // atualiza o dado
    if (!c.update(elem))
      return false;

    // Atualiza o cesto
    arqCestos.seek(enderecoCesto);
    arqCestos.write(c.toByteArray());
    return true;

  }

  /*
  * Descrição: Deleta um elemento no cesto.
  * Entrada: Int chave com o valor do hash
  * Saída: Boolean para mostrar se processo ocorreu com sucesso ou não.
  */
  public boolean delete(int chave) throws Exception {

    // Carrega o diretório
    byte[] bd = new byte[(int) arqDiretorio.length()];
    arqDiretorio.seek(0);
    arqDiretorio.read(bd);
    diretorio = new Diretorio();
    diretorio.fromByteArray(bd);

    // Identifica a hash do diretório,
    int i = diretorio.hash(chave);

    // Recupera o cesto
    long enderecoCesto = diretorio.endereço(i);
    Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
    byte[] ba = new byte[c.size()];
    arqCestos.seek(enderecoCesto);
    arqCestos.read(ba);
    c.fromByteArray(ba);

    // delete a chave
    if (!c.delete(chave))
      return false;

    // Atualiza o cesto
    arqCestos.seek(enderecoCesto);
    arqCestos.write(c.toByteArray());
    return true;
  }

  /*
  * Descrição: Imprime os diretórios e cestos.
  * Entrada: void
  * Saída: Impressão dos elementos do diretório e dos cestos.
  */
  public void print() {
    try {
      byte[] bd = new byte[(int) arqDiretorio.length()];
      arqDiretorio.seek(0);
      arqDiretorio.read(bd);
      diretorio = new Diretorio();
      diretorio.fromByteArray(bd);
      System.out.println("\nDIRETÓRIO ------------------");
      System.out.println(diretorio);

      System.out.println("\nCESTOS ---------------------");
      arqCestos.seek(0);
      while (arqCestos.getFilePointer() != arqCestos.length()) {
        System.out.println("Endereço: " + arqCestos.getFilePointer());
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        // ba = byte-array
        byte[] ba = new byte[c.size()];
        arqCestos.read(ba);
        c.fromByteArray(ba);
        System.out.println(c + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
