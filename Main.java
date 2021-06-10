import java.util.Scanner;
import Hashing.HashExtensivel;
import java.io.File;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) {

    HashExtensivel<Prontuario> hash;
    Scanner console = new Scanner(System.in);
    long startTime, endTime;

    // wsl2 - SSD
    String CestosDir = "./dados/pessoas.hash_d.db";
    String DiretoriosDir = "./dados/pessoas.hash_c.db";

    // HD Externo no E:
    String CestosDirHD = "/mnt/e/dados/pessoas.hash_d.db";
    String DiretoriosDirHD = "/mnt/e/dados/pessoas.hash_c.db";

    try {
      // wsl2 - ssd
      File d_ssd = new File("dados");
      if (!d_ssd.exists())
        d_ssd.mkdir();

      // HD Externo no E:
      File d_hd = new File("/mnt/e/dados");
      if (!d_hd.exists())
        d_hd.mkdir();

      int opcao;

      do {
        System.out.println("\n\n-------------------------------");
        System.out.println("              MENU");
        System.out.println("-------------------------------");
        System.out.println("1 - Inserir novo prontuário");
        System.out.println("2 - Buscar prontuário");
        System.out.println("3 - Excluir prontuário");
        System.out.println("4 - Editar Diagnóstico");
        System.out.println("5 - Imprimir prontuários disponíveis");
        System.out.println("6 - Realizar teste (apaga todos os dados para realizar os testes)");
        System.out.println("0 - Sair do sistema");

        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);
            System.out.println("\nINCLUSÃO DE NOVO PRONTUÁRIO");
            System.out.print("Nome: ");
            String nome = console.nextLine();
            System.out.print("cpf: ");
            int cpf = console.nextInt();
            System.out.print("Data de Nascimento: ");
            console.nextLine();
            String data_nasc = console.nextLine();
            System.out.print("Sexo: ");
            String sexo = console.nextLine();
            System.out.print("Diagnostico médico: ");
            String diagnostico = console.nextLine();
            hash.create(new Prontuario(nome, data_nasc, sexo, diagnostico, cpf));
            break;
          }
          case 2: {
            System.out.println("\nBUSCANDO PRONTUÁRIO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            console.nextLine();
            startTime = System.nanoTime();
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);
            Prontuario temp = hash.read(String.valueOf(cpf).hashCode());
            if (temp == null) {
              System.out.println("\nCPF não encontrado na base de dados");
              break;
            }
            System.out.println("Dados: " + temp);
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
            break;
          }
          case 3: {
            System.out.println("\nEXCLUSÃO DE PRONTUÁRIO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            console.nextLine();
            startTime = System.nanoTime();
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);
            hash.delete(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca e exclusão em nanosegundos: " + (endTime - startTime));
            break;
          }
          case 4: {
            System.out.println("\nBUSQUE O CPF PARA EDITAR O DIAGNÓSTICO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            console.nextLine();
            startTime = System.nanoTime();
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);
            Prontuario temp = hash.read(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
            if (temp == null) {
              System.out.println("\nCPF não encontrado na base de dados");
              break;
            }
            String diag;
            System.out.print("\nDigite a alteração do diagnóstico: ");
            diag = console.nextLine();
            temp.setDiagnostico(diag);
            hash.update(temp);
            System.out.println("\nDiagnóstico alterado com sucesso");
            break;
          }
          case 5: {
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);
            System.out.println("\nIMPRIMINDO PRONTUÁRIOS:");
            hash.print();
            break;
          }
          case 6: {
            // insere os dados e testa com SSD
            realizaTestes(CestosDir, DiretoriosDir, true, false);
            System.out.println("Analize com SDD finalizada");
            System.out.println("Pressione Enter para continuar...");
            System.in.read();
            // insere os dados e testa com HD
            realizaTestes(CestosDirHD, DiretoriosDirHD, false, false);
            System.out.println("Analize com HD finalizada");
            System.out.println("Pressione Enter para continuar...");
            System.in.read();
          }
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
    console.close();
  }

  public static void realizaTestes(String CestosDir, String DiretoriosDir, Boolean inserirNovosRegistros,
      Boolean pausarCodigo) {
    try {
      HashExtensivel<Prontuario> hash;
      Scanner console = new Scanner(System.in);
      long startTime, endTime;

      long timeBusca1 = 0;
      long timeBusca2 = 0;
      long timeBusca3 = 0;
      long totalTime1000, totalTime3000, totalTime5000, totalTime7000, totalTime10000, totalTime40000;
      Prontuario temp;

      hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);

      if (inserirNovosRegistros) {
        // apaga todos os registros existentes
        System.out.println("Deletando dados existentes");
        Files.deleteIfExists(Paths.get(CestosDir));
        Files.deleteIfExists(Paths.get(DiretoriosDir));
        System.out.println("Dados existentes deletados");
      }

      // criando dados aleatorios
      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (1 mil dados, CPFs de 100000000 até 100001000)");
        inserirDadosAleatorios(0, 1000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100000000 até 100001000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 1000 registros
      System.out.println("\nRealizando a busca do registro 100000033");
      startTime = System.nanoTime();
      temp = hash.read("100000033".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);
      System.out.println("\nRealizando a busca do registro 100000500");
      startTime = System.nanoTime();
      temp = hash.read("100000500".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);
      System.out.println("\nRealizando a busca do registro 100000999");
      startTime = System.nanoTime();
      temp = hash.read("100000999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);
      totalTime1000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime1000);

      if (pausarCodigo) {
        System.out.println("Pressione Enter para continuar...");
        System.in.read();
      }

      // criando dados aleatorios
      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (Inserindo CPFs de 100001001 até 100003000)");
        inserirDadosAleatorios(1001, 3000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100001001 até 100003000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 3000 registros
      System.out.println("\nRealizando a busca do registro 100001033");
      startTime = System.nanoTime();
      temp = hash.read("100001033".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);
      System.out.println("\nRealizando a busca do registro 100002500");
      startTime = System.nanoTime();
      temp = hash.read("100002500".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);
      System.out.println("\nRealizando a busca do registro 100002999");
      startTime = System.nanoTime();
      temp = hash.read("100002999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);
      totalTime3000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime3000);

      if (pausarCodigo) {
        System.out.println("Pressione Enter para continuar...");
        System.in.read();
      }

      // criando dados aleatorios
      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (Inserindo CPFs de 100003001 até 100005000)");
        inserirDadosAleatorios(3001, 5000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100003001 até 100005000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 3000 registros
      System.out.println("\nRealizando a busca do registro 100004033");
      startTime = System.nanoTime();
      temp = hash.read("100004033".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);

      System.out.println("\nRealizando a busca do registro 100003500");
      startTime = System.nanoTime();
      temp = hash.read("100003500".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);

      System.out.println("\nRealizando a busca do registro 100004999");
      startTime = System.nanoTime();
      temp = hash.read("100004999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);

      totalTime5000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime5000);

      if (pausarCodigo) {
        System.out.println("\nPressione Enter para continuar...");
        System.in.read();
      }

      // criando dados aleatorios
      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (Inserindo CPFs de 100005001 até 100007000)");
        inserirDadosAleatorios(5001, 7000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100005001 até 100007000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 3000 registros
      System.out.println("\nRealizando a busca do registro 100005033");
      startTime = System.nanoTime();
      temp = hash.read("100005033".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);
      System.out.println("\nRealizando a busca do registro 100005700");
      startTime = System.nanoTime();
      temp = hash.read("100005700".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);
      System.out.println("\nRealizando a busca do registro 100006999");
      startTime = System.nanoTime();
      temp = hash.read("100006999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);
      totalTime7000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime7000);

      if (pausarCodigo) {
        System.out.println("Pressione Enter para continuar...");
        System.in.read();
      }

      // criando dados aleatorios
      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (Inserindo CPFs de 100007001 até 100010000)");
        inserirDadosAleatorios(7001, 10000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100005001 até 100007000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 10000 registros
      System.out.println("\nRealizando a busca do registro 100007500");
      startTime = System.nanoTime();
      temp = hash.read("100007500".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);
      System.out.println("\nRealizando a busca do registro 100008950");
      startTime = System.nanoTime();
      temp = hash.read("100008950".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);
      System.out.println("\nRealizando a busca do registro 100009999");
      startTime = System.nanoTime();
      temp = hash.read("100009999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);
      totalTime10000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime10000);

      if (pausarCodigo) {
        System.out.println("Pressione Enter para continuar...");
        System.in.read();
      }

      if (inserirNovosRegistros) {
        System.out.println("\nCriando dados aleatórios (Inserindo CPFs de 100010001 até 100025000)");
        inserirDadosAleatorios(10001, 25000, CestosDir, DiretoriosDir);
        System.out.println("\nDados com CPFs de 100010001 até 100025000 inseridos");
      }

      // realizando busca para medir tempo gasto pela busca de 10000 registros
      System.out.println("\nRealizando a busca do registro 100014500");
      startTime = System.nanoTime();
      temp = hash.read("100014500".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca1 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca1);
      System.out.println("\nRealizando a busca do registro 100017950");
      startTime = System.nanoTime();
      temp = hash.read("100017950".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca2 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca2);
      System.out.println("\nRealizando a busca do registro 100024999");
      startTime = System.nanoTime();
      temp = hash.read("100024999".hashCode());
      System.out.println("Dados: " + temp);
      endTime = System.nanoTime();
      timeBusca3 += (endTime - startTime);
      System.out.println("Tempo de busca em nanosegundos: " + timeBusca3);
      totalTime40000 = ((timeBusca1 + timeBusca2 + timeBusca3) / 3);
      System.out.println("\nMédia de tempo gasto nas ultimas 3 buscas em nanosegundos: " + totalTime40000);

      System.out.println("\nTempo médio gasto para buscar dados: ");
      System.out.println("1000 registros: " + totalTime1000);
      System.out.println("3000 registros: " + totalTime3000);
      System.out.println("5000 registros: " + totalTime5000);
      System.out.println("7000 registros: " + totalTime7000);
      System.out.println("10000 registros: " + totalTime10000);
      System.out.println("40000 registros: " + totalTime40000);
      console.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void inserirDadosAleatorios(int valorInicial, int valorFinal, String CestosDir, String DiretoriosDir) {
    try {
      HashExtensivel<Prontuario> hash;
      hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, CestosDir, DiretoriosDir);

      // opção para inserir dados aleatorios
      System.out.println("Inserindo dados de teste: ");
      Random gerador = new Random();
      int qtdInicial = 100000001 + valorInicial;
      int qtdMax = 100000001 + valorFinal;
      String nome = "";
      String data_nasc = "";
      String sexo = "sexo teste";
      String diagnostico = "";

      for (int cpf = qtdInicial; cpf < qtdMax; cpf += 1) {
        nome = gerarNomeAleatorio(gerador);
        data_nasc = gerarDataAleatoria(gerador);
        sexo = gerarSexoAleatorio(gerador);
        diagnostico = gerarDiagnosticoAleatorio(gerador);
        // System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        // System.out.println("Data Nascimento: " + data_nasc);
        // System.out.println("Sexo: " + sexo);
        // System.out.println("Diagnostico: " + diagnostico + "\n");
        hash.create(new Prontuario(nome, data_nasc, sexo, diagnostico, cpf));
        // hash.print();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String gerarSexoAleatorio(Random gerador) {
    if (gerador.nextInt(2) == 0) {
      return "Feminino";
    }
    return "Masculino";
  }

  public static String gerarDataAleatoria(Random gerador) {
    return (gerador.nextInt(31)) + 1 + "-" + (gerador.nextInt(12) + 1) + "-" + "1984";
  }

  public static String gerarDiagnosticoAleatorio(Random gerador) {
    String diagnostico = "Diagnostico_teste_";
    String palavra;
    for (int i = 0; i < 4; i++) {
      palavra = "";
      for (int y = 0; y < 5; y++) {
        palavra += ((char) (gerador.nextInt(25) + 65));
      }
      diagnostico += (palavra + "_");
    }
    return diagnostico;
  }

  public static String gerarNomeAleatorio(Random gerador) {
    String diagnostico = "Nome_teste_";
    for (int y = 0; y < 10; y++) {
      diagnostico += ((char) (gerador.nextInt(25) + 65));
    }
    return diagnostico;
  }
}
