// import java.util.Scanner;
// import java.io.File;
// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.DataInputStream;
// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.io.RandomAccessFile;
// import java.util.ArrayList;
// import java.lang.reflect.Constructor;
// import java.util.Random;
// import java.nio.file.Files;
// import java.nio.file.Paths;

// public interface RegistroProntuario<T> {

//   public int hashCode(); // chave numérica para ser usada no diretório

//   public short size(); // tamanho FIXO do registro

//   public byte[] toByteArray() throws IOException; // representação do elemento em um vetor de bytes

//   public void fromByteArray(byte[] ba) throws IOException; // vetor de bytes a ser usado na construção do elemento

// }

// public class Main {
//   public static void main(String[] args) {

//     HashExtensivel<Prontuario> hash;
//     Scanner console = new Scanner(System.in);
//     long startTime, endTime;

//     try {
//       File d = new File("dados");
//       if (!d.exists())
//         d.mkdir();

//       int opcao;

//       do {
//         System.out.println("\n\n-------------------------------");
//         System.out.println("              MENU");
//         System.out.println("-------------------------------");
//         System.out.println("1 - Inserir novo prontuário");
//         System.out.println("2 - Buscar prontuário");
//         System.out.println("3 - Excluir prontuário");
//         System.out.println("4 - Editar Diagnóstico");
//         System.out.println("5 - Imprimir prontuários disponíveis");
//         System.out.println("6 - Inserir dados de teste (5.000 registros)");
//         System.out.println("7 - Realizar teste (apaga todos os dados para realizar os testes)");
//         System.out.println("0 - Sair do sistema");

//         try {
//           opcao = Integer.valueOf(console.nextLine());
//         } catch (NumberFormatException e) {
//           opcao = -1;
//         }

//         switch (opcao) {
//           case 1: {
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             System.out.println("\nINCLUSÃO DE NOVO PRONTUÁRIO");
//             System.out.print("Nome: ");
//             String nome = console.nextLine();
//             System.out.print("cpf: ");
//             int cpf = console.nextInt();
//             System.out.print("Data de Nascimento: ");
//             console.nextLine();
//             String data_nasc = console.nextLine();
//             System.out.print("Sexo: ");
//             String sexo = console.nextLine();
//             System.out.print("Diagnostico médico: ");
//             String diagnostico = console.nextLine();
//             hash.create(new Prontuario(nome, data_nasc, sexo, diagnostico, cpf));
//             break;
//           }
//           case 2: {
//             System.out.println("\nBUSCANDO PRONTUÁRIO");
//             System.out.println("\nDigite o CPF para prosseguir.");
//             System.out.print("CPF: ");
//             int cpf = console.nextInt();
//             console.nextLine();
//             startTime = System.nanoTime();
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             Prontuario temp = hash.read(String.valueOf(cpf).hashCode());
//             if (temp == null) {
//               System.out.println("\nCPF não encontrado na base de dados");
//               break;
//             }
//             System.out.println("Dados: " + temp);
//             endTime = System.nanoTime();
//             System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
//             break;
//           }
//           case 3: {
//             System.out.println("\nEXCLUSÃO DE PRONTUÁRIO");
//             System.out.println("\nDigite o CPF para prosseguir.");
//             System.out.print("CPF: ");
//             int cpf = console.nextInt();
//             console.nextLine();
//             startTime = System.nanoTime();
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             hash.delete(String.valueOf(cpf).hashCode());
//             endTime = System.nanoTime();
//             System.out.println("Tempo de busca e exclusão em nanosegundos: " + (endTime - startTime));
//             break;
//           }
//           case 4: {
//             System.out.println("\nBUSQUE O CPF PARA EDITAR O DIAGNÓSTICO");
//             System.out.println("\nDigite o CPF para prosseguir.");
//             System.out.print("CPF: ");
//             int cpf = console.nextInt();
//             console.nextLine();
//             startTime = System.nanoTime();
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             Prontuario temp = hash.read(String.valueOf(cpf).hashCode());
//             endTime = System.nanoTime();
//             System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
//             if (temp == null) {
//               System.out.println("\nCPF não encontrado na base de dados");
//               break;
//             }
//             String diag;
//             System.out.print("\nDigite a alteração do diagnóstico: ");
//             diag = console.nextLine();
//             temp.setDiagnostico(diag);
//             hash.update(temp);
//             System.out.println("\nDiagnóstico alterado com sucesso");
//             break;
//           }
//           case 5: {
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             System.out.println("\nIMPRIMINDO PRONTUÁRIOS:");
//             hash.print();
//             break;
//           }
//           case 6: {
//             inserirDadosAleatorios(9000);
//             break;
//           }
//           case 7: {
//             hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//                 "dados/pessoas.hash_c.db");
//             // apaga todos os registros existentes
//             System.out.println("Deletando dados existentes");
//             Files.deleteIfExists(Paths.get("./dados/pessoas.hash_c.db"));
//             Files.deleteIfExists(Paths.get("./dados/pessoas.hash_d.db"));
//             System.out.println("Dados existentes deletados");

//             // criando dados aleatorios
//             System.out.println("\nCriando dados aleatórios (5 mil dados, CPFs de 100000000 até 100005000)");
//             inserirDadosAleatorios(5000);
//             System.out.println("Inserção de dados finalizada");

//             // realizando busca para medir tempo gasto pela busca
//             System.out.println("\nRealizando a busca do registro 100004000");
//             startTime = System.nanoTime();
//             Prontuario temp = hash.read("100004000".hashCode());
//             System.out.println("Dados: " + temp);
//             endTime = System.nanoTime();
//             System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));

//           }
//           case 0:
//             break;
//           default:
//             System.out.println("Opção inválida");
//         }
//       } while (opcao != 0);

//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     console.close();
//   }

//   public static void inserirDadosAleatorios(int qtdRegistros) {
//     try {
//       HashExtensivel<Prontuario> hash;
//       hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
//           "dados/pessoas.hash_c.db");

//       // opção para inserir dados aleatorios
//       System.out.println("Inserindo dados de teste: ");
//       Random gerador = new Random();
//       int qtdInicial = 100000000;
//       int qtdMax = qtdInicial + qtdRegistros;
//       // int qtdMax = 100030000;
//       // int qtdMax = 999999999;
//       int cpf;
//       String nome = "";
//       String data_nasc = "";
//       String sexo = "sexo teste";
//       String diagnostico = "";

//       for (cpf = qtdInicial; cpf < qtdMax; cpf++) {
//         nome = gerarNomeAleatorio(gerador);
//         data_nasc = gerarDataAleatoria(gerador);
//         sexo = gerarSexoAleatorio(gerador);
//         diagnostico = gerarDiagnosticoAleatorio(gerador);
//         // System.out.println("Nome: " + nome);
//         System.out.println("CPF: " + cpf);
//         // System.out.println("Data Nascimento: " + data_nasc);
//         // System.out.println("Sexo: " + sexo);
//         // System.out.println("Diagnostico: " + diagnostico + "\n");
//         hash.create(new Prontuario(nome, data_nasc, sexo, diagnostico, cpf));
//         // hash.print();
//       }
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//   }

//   public static String gerarSexoAleatorio(Random gerador) {
//     if (gerador.nextInt(2) == 0) {
//       return "Feminino";
//     }
//     return "Masculino";
//   }

//   public static String gerarDataAleatoria(Random gerador) {
//     return (gerador.nextInt(31)) + 1 + "-" + (gerador.nextInt(12) + 1) + "-" + "1984";
//   }

//   public static String gerarDiagnosticoAleatorio(Random gerador) {
//     String diagnostico = "Diagnostico_teste ";
//     String palavra;
//     for (int i = 0; i < 4; i++) {
//       palavra = "";
//       for (int y = 0; y < 5; y++) {
//         palavra += ((char) (gerador.nextInt(25) + 65));
//       }
//       diagnostico += (palavra + " ");
//     }
//     return diagnostico;
//   }

//   public static String gerarNomeAleatorio(Random gerador) {
//     String diagnostico = "Nome_teste ";
//     for (int y = 0; y < 10; y++) {
//       diagnostico += ((char) (gerador.nextInt(25) + 65));
//     }
//     return diagnostico;
//   }
// }

// class Prontuario implements RegistroProntuario<Prontuario> {

//   private String nome;
//   private String data_nasc;
//   private String sexo;
//   private String diagnostico;
//   private int cpf;
//   private short tam = 30000;

//   /*
//    * Descrição: Construtor da classe. Entrada: void Saída: Preenchimento dos
//    * atributos da classe instanciando-os.
//    */
//   public Prontuario() {
//     this("", "", "", "", 0);
//   }

//   /*
//    * Descrição: Construtor da classe. Entrada: Strings para nome, data de
//    * nascimento, sexo e diagnóstico médico, inteiro para cpf Saída: Preenchimento
//    * dos atributos da classe.
//    */
//   public Prontuario(String n, String dt_nasc, String sexo, String diag, int cpf) {
//     try {
//       this.nome = n;
//       this.cpf = cpf;
//       this.data_nasc = dt_nasc;
//       this.sexo = sexo;
//       this.diagnostico = diag;
//       if (nome.length() > tam)
//         throw new Exception("Número de caracteres do nome maior que o permitido. Os dados serão cortados.");
//     } catch (Exception ec) {
//       ec.printStackTrace();
//     }
//   }

//   /*
//    * Descrição: Setter para o diagnóstico médico. Entrada: String contendo o
//    * diagnóstico médico. Saída: Preenchimento da variável private String
//    * diagnostico.
//    */
//   public void setDiagnostico(String diag) {
//     this.diagnostico = diag;
//   }

//   @Override
//   /*
//    * Descrição: Realiza o hash do elemento a partir do CPF. Entrada: void. Saída:
//    * Inteiro com o valor do hashCode proveniente do hash do CPF.
//    */
//   public int hashCode() {
//     return String.valueOf(this.cpf).hashCode();
//   }

//   /*
//    * Descrição: Método que retorna o tam. Entrada: void. Saída: Inteiro com o
//    * valor do tam.
//    */
//   public short size() {
//     return this.tam;
//   }

//   /*
//    * Descrição: Retorna o prontuário em formato de string. Entrada: void. Saída:
//    * String s com a descritiva do prontuário.
//    */
//   public String toString() {
//     return this.nome + "|" + this.sexo + "|" + this.cpf + "|" + this.data_nasc + "|" + this.diagnostico;
//   }

//   /*
//    * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output
//    * stream no formato de um vetor de bytes Entrada: void Saída: vetor de bytes
//    */
//   public byte[] toByteArray() throws IOException {
//     ByteArrayOutputStream baos = new ByteArrayOutputStream();
//     DataOutputStream dos = new DataOutputStream(baos);
//     dos.writeUTF(nome);
//     dos.writeInt(cpf);
//     dos.writeUTF(sexo);
//     dos.writeUTF(data_nasc);
//     dos.writeUTF(diagnostico);
//     byte[] bs = baos.toByteArray();
//     byte[] bs2 = new byte[tam];
//     for (int i = 0; i < tam; i++)
//       bs2[i] = ' ';
//     for (int i = 0; i < bs.length && i < tam; i++)
//       bs2[i] = bs[i];
//     return bs2;
//   }

//   /*
//    * Descrição: recebe um array de bytes e carrega os dados na memoria principal
//    * Entrada: vetor de byte ba Saída: preenche os elementos do cesto
//    */
//   public void fromByteArray(byte[] ba) throws IOException {
//     ByteArrayInputStream bais = new ByteArrayInputStream(ba);
//     DataInputStream dis = new DataInputStream(bais);
//     this.nome = dis.readUTF();
//     this.cpf = dis.readInt();
//     this.sexo = dis.readUTF();
//     this.data_nasc = dis.readUTF();
//     this.diagnostico = dis.readUTF();
//   }

// }

// class HashExtensivel<T extends RegistroProntuario<T>> {

//   int quantidadeDadosPorCesto;
//   String nomeArquivoDiretorio;
//   String nomeArquivoCestos;
//   RandomAccessFile arqDiretorio;
//   RandomAccessFile arqCestos;
//   Diretorio diretorio;
//   Constructor<T> construtor;

//   public class Cesto {

//     ArrayList<T> elementos; // sequência de elementos armazenados
//     Constructor<T> construtor; // Construtor da classe
//     byte profundidadeLocal; // profundidade local do cesto
//     short quantidade; // quantidade de pares presentes no cesto
//     short quantidadeMaxima; // quantidade máxima de pares que o cesto pode conter
//     short bytesPorElemento; // tamanho fixo de cada elemento em bytes
//     int bytesPorCesto; // tamanho fixo do cesto em bytes

//     public Cesto(Constructor<T> ct, int qtdmax) throws Exception {
//       this(ct, qtdmax, 0);
//     }

//     /*
//      * Descrição: Construtor da classe. Entrada: Constructor<T>, inteiro de
//      * quantidade maxima por cesto e inteiro profundidade local. Saída:
//      * Preenchimento dos atributos da classe.
//      */
//     public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
//       construtor = ct;
//       if (qtdmax > 32767)
//         throw new Exception("Quantidade máxima de 32.767 elementos");
//       if (pl > 127)
//         throw new Exception("Profundidade local máxima de 127 bits");
//       profundidadeLocal = (byte) pl;
//       quantidade = 0;
//       quantidadeMaxima = (short) qtdmax;
//       elementos = new ArrayList<>(quantidadeMaxima);
//       bytesPorElemento = ct.newInstance().size();
//       bytesPorCesto = (int) (bytesPorElemento * quantidadeMaxima + 3);
//     }

//     /*
//      * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output
//      * stream no formato de um vetor de bytes Entrada: void Saída: vetor de bytes
//      */
//     public byte[] toByteArray() throws Exception {
//       ByteArrayOutputStream baos = new ByteArrayOutputStream();
//       DataOutputStream dos = new DataOutputStream(baos);
//       dos.writeByte(profundidadeLocal);
//       dos.writeShort(quantidade);
//       int i = 0;
//       while (i < quantidade) {
//         dos.write(elementos.get(i).toByteArray());
//         i++;
//       }
//       byte[] vazio = new byte[bytesPorElemento];
//       while (i < quantidadeMaxima) {
//         dos.write(vazio);
//         i++;
//       }
//       return baos.toByteArray();
//     }

//     /*
//      * Descrição: recebe um array de bytes e carrega os dados na memoria principal
//      * Entrada: vetor de byte ba Saída: preenche os elementos do cesto
//      */
//     public void fromByteArray(byte[] ba) throws Exception {
//       ByteArrayInputStream bais = new ByteArrayInputStream(ba);
//       DataInputStream dis = new DataInputStream(bais);
//       profundidadeLocal = dis.readByte();
//       quantidade = dis.readShort();
//       int i = 0;
//       elementos = new ArrayList<>(quantidadeMaxima);
//       byte[] dados = new byte[bytesPorElemento];
//       T elem;
//       while (i < quantidadeMaxima) {
//         dis.read(dados);
//         elem = construtor.newInstance();
//         elem.fromByteArray(dados);
//         elementos.add(elem);
//         i++;
//       }
//     }

//     /*
//      * Descrição: Boolean Cria um bucket. Entrada: um elemento T a ser inserido no
//      * cesto. Saída: Boolean true confirmando a criação.
//      */
//     public boolean create(T elem) {
//       if (full())
//         return false;
//       int i = quantidade - 1;
//       while (i >= 0 && elem.hashCode() < elementos.get(i).hashCode())
//         i--;
//       elementos.add(i + 1, elem);
//       quantidade++;
//       return true;
//     }

//     /*
//      * Descrição: Procura no cesto um elemento ao passar a sua chave de hash.
//      * Entrada: Inteiro com o valor da chave resultante do hash. Saída: Retorna um
//      * elemento ou null.
//      */
//     public T read(int chave) {
//       if (empty())
//         return null;
//       int i = 0;
//       while (i < quantidade && chave > elementos.get(i).hashCode())
//         i++;
//       if (i < quantidade && chave == elementos.get(i).hashCode())
//         return elementos.get(i);
//       else
//         return null;
//     }

//     /*
//      * Descrição: Substitui o elemento recebido pelo da seu correspondente,
//      * atualizando. Entrada: Elemento do tipo T. Saída: Retorna um boolean
//      * confirmando a exclusão ou não.
//      */
//     public boolean update(T elem) {
//       if (empty())
//         return false;
//       int i = 0;
//       while (i < quantidade && elem.hashCode() > elementos.get(i).hashCode())
//         i++;
//       if (i < quantidade && elem.hashCode() == elementos.get(i).hashCode()) {
//         elementos.set(i, elem);
//         return true;
//       } else
//         return false;
//     }

//     /*
//      * Descrição: Procura e deleta um elemento pela sua chave. Entrada: Inteiro com
//      * o valor da chave resultante do hash. Saída: Boolean true confirmando a
//      * exclusão ou false indicando falha na exclusão.
//      */
//     public boolean delete(int chave) {
//       if (empty())
//         return false;
//       int i = 0;
//       while (i < quantidade && chave > elementos.get(i).hashCode())
//         i++;
//       if (chave == elementos.get(i).hashCode()) {
//         elementos.remove(i);
//         quantidade--;
//         return true;
//       } else
//         return false;
//     }

//     /*
//      * Descrição: Boolean verifica se está vazio. Entrada: void. Saída: Boolean true
//      * se o valor de quantidade for zero ou false caso diferente de zero.
//      */
//     public boolean empty() {
//       return quantidade == 0;
//     }

//     /*
//      * Descrição: Boolean verifica se está cheio. Entrada: void. Saída: Boolean true
//      * se o valor de quantidade for igual ao de quantidade máxima ou false caso
//      * diferente.
//      */
//     public boolean full() {
//       return quantidade == quantidadeMaxima;
//     }

//     /*
//      * Descrição: Retorna os elementos em formato de string. Entrada: void. Saída:
//      * String s com a descritiva do elemento.
//      */
//     public String toString() {
//       String s = "Profundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
//       int i = 0;
//       while (i < quantidade) {
//         s += elementos.get(i).toString() + " | ";
//         i++;
//       }
//       while (i < quantidadeMaxima) {
//         s += "- | ";
//         i++;
//       }
//       return s;
//     }

//     /*
//      * Descrição: Método que retorna o tamanho do cesto em bytes. Entrada: void.
//      * Saída: Inteiro bytesPorCesto com o valor do tamanho do cesto em bytes.
//      */
//     public int size() {
//       return bytesPorCesto;
//     }

//   }

//   protected class Diretorio {

//     byte profundidadeGlobal;
//     long[] enderecos;

//     /*
//      * Descrição: Construtor da classe Diretorio. Entrada: void. Saída:
//      * Preenchimento dos atributos da classe.
//      */
//     public Diretorio() {
//       profundidadeGlobal = 0;
//       enderecos = new long[1];
//       enderecos[0] = 0;
//     }

//     /*
//      * Descrição: Atualiza o endereco de um elemento. Entrada: novo endereço p com
//      * elemento e. Saída: Boolean com sucesso ou não na atualização.
//      */
//     public boolean atualizaEndereco(int p, long e) {
//       if (p > Math.pow(2, profundidadeGlobal))
//         return false;
//       enderecos[p] = e;
//       return true;
//     }

//     /*
//      * Descrição: cria uma nova alocação de buffer com os tamanhos do atual output
//      * stream no formato de um vetor de bytes Entrada: void Saída: vetor de bytes
//      */
//     public byte[] toByteArray() throws IOException {
//       ByteArrayOutputStream baos = new ByteArrayOutputStream();
//       DataOutputStream dos = new DataOutputStream(baos);
//       dos.writeByte(profundidadeGlobal);
//       int quantidade = (int) Math.pow(2, profundidadeGlobal);
//       int i = 0;
//       while (i < quantidade) {
//         dos.writeLong(enderecos[i]);
//         i++;
//       }
//       return baos.toByteArray();
//     }

//     /*
//      * Descrição: recebe um array de bytes e carrega os dados na memoria principal
//      * Entrada: vetor de byte ba Saída: preenche os elementos do cesto
//      */
//     public void fromByteArray(byte[] ba) throws IOException {
//       ByteArrayInputStream bais = new ByteArrayInputStream(ba);
//       DataInputStream dis = new DataInputStream(bais);
//       profundidadeGlobal = dis.readByte();
//       int quantidade = (int) Math.pow(2, profundidadeGlobal);
//       enderecos = new long[quantidade];
//       int i = 0;
//       while (i < quantidade) {
//         enderecos[i] = dis.readLong();
//         i++;
//       }
//     }

//     /*
//      * Descrição: Retorna os elementos em formato de string. Entrada: void. Saída:
//      * String s com a descritiva do elemento.
//      */
//     public String toString() {
//       String s = "\nProfundidade global: " + profundidadeGlobal;
//       int i = 0;
//       int quantidade = (int) Math.pow(2, profundidadeGlobal);
//       while (i < quantidade) {
//         s += "\n" + i + ": " + enderecos[i];
//         i++;
//       }
//       return s;
//     }

//     /*
//      * Descrição: Retorna o endereço do cesto de acordo com a posição. Entrada:
//      * Posição p. Saída: Endereço do Cesto.
//      */
//     protected long endereço(int p) {
//       if (p > Math.pow(2, profundidadeGlobal))
//         return -1;
//       return enderecos[p];
//     }

//     /*
//      * Descrição: Duplica a quantidade de posições no diretorio se profundidade
//      * menor que 127. Entrada: void. Saída: Retorna boolean para mostrar se operação
//      * foi bem sucedida ou não.
//      */
//     protected boolean duplica() {
//       if (profundidadeGlobal == 127)
//         return false;
//       profundidadeGlobal++;
//       int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
//       int q2 = (int) Math.pow(2, profundidadeGlobal);
//       long[] novosEnderecos = new long[q2];
//       int i = 0;
//       while (i < q1) {
//         novosEnderecos[i] = enderecos[i];
//         i++;
//       }
//       while (i < q2) {
//         novosEnderecos[i] = enderecos[i - q1];
//         i++;
//       }
//       enderecos = novosEnderecos;
//       return true;
//     }

//     /*
//      * Descrição: Calcula o hash de acordo com uma chave recebida e com a
//      * profundidade global. Entrada: Int chave. Saída: Int hash gerado. Obs: Para
//      * efeito de determinar o cesto em que o elemento deve ser inserido, só serão
//      * considerados valores absolutos da chave.
//      */
//     protected int hash(int chave) {
//       return Math.abs(chave) % (int) Math.pow(2, profundidadeGlobal);
//     }

//     /*
//      * Descrição: Calcula o hash de acordo com uma chave e com uma dada
//      * profundidade. Entrada: Int chave. Saída: Int hash gerado. Obs: Para efeito de
//      * determinar o cesto em que o elemento deve ser inserido, só serão considerados
//      * valores absolutos da chave.
//      */
//     protected int hash(int chave, int pl) { // cálculo do hash para uma dada profundidade local
//       return Math.abs(chave) % (int) Math.pow(2, pl);
//     }

//   }

//   /*
//    * Descrição: Construtor da classe HashExtensivel. Entrada: Constructor<T>, Int
//    * quantidade maxima por cesto, String nome do arquivo para diretorio, Strin
//    * nome do arquivo para cestos Saída: Preenchimento dos atributos da classe.
//    */
//   public HashExtensivel(Constructor<T> ct, int n, String nd, String nc) throws Exception {
//     construtor = ct;
//     quantidadeDadosPorCesto = n;
//     nomeArquivoDiretorio = nd;
//     nomeArquivoCestos = nc;

//     arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
//     arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

//     // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista
//     // de cestos
//     if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {

//       // Cria um novo diretório, com profundidade de 0 bits (1 único elemento)
//       diretorio = new Diretorio();
//       byte[] bd = diretorio.toByteArray();
//       arqDiretorio.write(bd);

//       // Cria um cesto vazio, já apontado pelo único elemento do diretório
//       Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//       bd = c.toByteArray();
//       arqCestos.seek(0);
//       arqCestos.write(bd);
//     }
//   }

//   /*
//    * Descrição: Insere novo elemento no cesto, fazendo trocas de posição e novas
//    * inserções quando necessário. Entrada: Elemento T Saída: Boolean para mostrar
//    * se processo ocorreu com sucesso ou não.
//    */
//   public boolean create(T elem) throws Exception {

//     // Carrega o diretório
//     byte[] bd = new byte[(int) arqDiretorio.length()];
//     arqDiretorio.seek(0);
//     arqDiretorio.read(bd);
//     diretorio = new Diretorio();
//     diretorio.fromByteArray(bd);

//     // Identifica a hash do diretório,
//     int i = diretorio.hash(elem.hashCode());

//     // Recupera o cesto
//     long enderecoCesto = diretorio.endereço(i);
//     Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//     byte[] ba = new byte[c.size()];
//     arqCestos.seek(enderecoCesto);
//     arqCestos.read(ba);
//     c.fromByteArray(ba);

//     // Testa se a chave já não existe no cesto
//     if (c.read(elem.hashCode()) != null)
//       throw new Exception("Elemento já existe");

//     // Testa se o cesto já não está cheio
//     // Se não estiver, create o par de chave e dado
//     if (!c.full()) {
//       // Insere a chave no cesto e o atualiza
//       c.create(elem);
//       arqCestos.seek(enderecoCesto);
//       arqCestos.write(c.toByteArray());
//       return true;
//     }

//     // Duplica o diretório
//     byte pl = c.profundidadeLocal;
//     if (pl >= diretorio.profundidadeGlobal)
//       diretorio.duplica();
//     byte pg = diretorio.profundidadeGlobal;

//     // Cria os novos cestos, com os seus dados no arquivo de cestos
//     Cesto c1 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
//     arqCestos.seek(enderecoCesto);
//     arqCestos.write(c1.toByteArray());

//     Cesto c2 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
//     long novoEndereco = arqCestos.length();
//     arqCestos.seek(novoEndereco);
//     arqCestos.write(c2.toByteArray());

//     // Atualiza os dados no diretório
//     int inicio = diretorio.hash(elem.hashCode(), c.profundidadeLocal);
//     int deslocamento = (int) Math.pow(2, pl);
//     int max = (int) Math.pow(2, pg);
//     boolean troca = false;
//     for (int j = inicio; j < max; j += deslocamento) {
//       if (troca)
//         diretorio.atualizaEndereco(j, novoEndereco);
//       troca = !troca;
//     }

//     // Atualiza o arquivo do diretório
//     bd = diretorio.toByteArray();
//     arqDiretorio.seek(0);
//     arqDiretorio.write(bd);

//     // Reinsere as chaves
//     for (int j = 0; j < c.quantidade; j++) {
//       create(c.elementos.get(j));
//     }
//     create(elem);
//     return false;
//   }

//   /*
//    * Descrição: Le os dados do diretorio que possui a chave recebida. Entrada: Int
//    * chave proveniente do hash Saída: Boolean para mostrar se processo ocorreu com
//    * sucesso ou não.
//    */
//   public T read(int chave) throws Exception {

//     // Carrega o diretório
//     byte[] bd = new byte[(int) arqDiretorio.length()];
//     arqDiretorio.seek(0);
//     arqDiretorio.read(bd);
//     diretorio = new Diretorio();
//     diretorio.fromByteArray(bd);

//     // Identifica a hash do diretório,
//     int i = diretorio.hash(chave);

//     // Recupera o cesto
//     long enderecoCesto = diretorio.endereço(i);
//     Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//     byte[] ba = new byte[c.size()];
//     arqCestos.seek(enderecoCesto);
//     arqCestos.read(ba);
//     c.fromByteArray(ba);

//     return c.read(chave);
//   }

//   /*
//    * Descrição: Realiza a atualização do elemento recebendo o elemento com as
//    * novas informações a serem alteradas. Entrada: T elem, elemento com os valores
//    * atualizados. Saída: Boolean para mostrar se processo ocorreu com sucesso ou
//    * não.
//    */
//   public boolean update(T elem) throws Exception {

//     // Carrega o diretório
//     byte[] bd = new byte[(int) arqDiretorio.length()];
//     arqDiretorio.seek(0);
//     arqDiretorio.read(bd);
//     diretorio = new Diretorio();
//     diretorio.fromByteArray(bd);

//     // Identifica a hash do diretório,
//     int i = diretorio.hash(elem.hashCode());

//     // Recupera o cesto
//     long enderecoCesto = diretorio.endereço(i);
//     Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//     byte[] ba = new byte[c.size()];
//     arqCestos.seek(enderecoCesto);
//     arqCestos.read(ba);
//     c.fromByteArray(ba);

//     // atualiza o dado
//     if (!c.update(elem))
//       return false;

//     // Atualiza o cesto
//     arqCestos.seek(enderecoCesto);
//     arqCestos.write(c.toByteArray());
//     return true;

//   }

//   /*
//    * Descrição: Deleta um elemento no cesto. Entrada: Int chave com o valor do
//    * hash Saída: Boolean para mostrar se processo ocorreu com sucesso ou não.
//    */
//   public boolean delete(int chave) throws Exception {

//     // Carrega o diretório
//     byte[] bd = new byte[(int) arqDiretorio.length()];
//     arqDiretorio.seek(0);
//     arqDiretorio.read(bd);
//     diretorio = new Diretorio();
//     diretorio.fromByteArray(bd);

//     // Identifica a hash do diretório,
//     int i = diretorio.hash(chave);

//     // Recupera o cesto
//     long enderecoCesto = diretorio.endereço(i);
//     Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//     byte[] ba = new byte[c.size()];
//     arqCestos.seek(enderecoCesto);
//     arqCestos.read(ba);
//     c.fromByteArray(ba);

//     // delete a chave
//     if (!c.delete(chave))
//       return false;

//     // Atualiza o cesto
//     arqCestos.seek(enderecoCesto);
//     arqCestos.write(c.toByteArray());
//     return true;
//   }

//   /*
//    * Descrição: Imprime os diretórios e cestos. Entrada: void Saída: Impressão dos
//    * elementos do diretório e dos cestos.
//    */
//   public void print() {
//     try {
//       byte[] bd = new byte[(int) arqDiretorio.length()];
//       arqDiretorio.seek(0);
//       arqDiretorio.read(bd);
//       diretorio = new Diretorio();
//       diretorio.fromByteArray(bd);
//       System.out.println("\nDIRETÓRIO ------------------");
//       System.out.println(diretorio);

//       System.out.println("\nCESTOS ---------------------");
//       arqCestos.seek(0);
//       while (arqCestos.getFilePointer() != arqCestos.length()) {
//         System.out.println("Endereço: " + arqCestos.getFilePointer());
//         Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
//         // ba = byte-array
//         byte[] ba = new byte[c.size()];
//         arqCestos.read(ba);
//         c.fromByteArray(ba);
//         System.out.println(c + "\n");
//       }
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//   }
// }