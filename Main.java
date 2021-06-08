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

    try {
      File d = new File("dados");
      if (!d.exists())
        d.mkdir();

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
        System.out.println("6 - Inserir dados de teste (5.000 registros)");
        System.out.println("7 - Realizar teste (apaga todos os dados para realizar os testes)");
        System.out.println("0 - Sair do sistema");

        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
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
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
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
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
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
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
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
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
            System.out.println("\nIMPRIMINDO PRONTUÁRIOS:");
            hash.print();
            break;
          }
          case 6: {
            inserirDadosAleatorios(9000);
            break;
          }
          case 7: {
            hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
                "dados/pessoas.hash_c.db");
            // apaga todos os registros existentes
            System.out.println("Deletando dados existentes");
            Files.deleteIfExists(Paths.get("./dados/pessoas.hash_c.db"));
            Files.deleteIfExists(Paths.get("./dados/pessoas.hash_d.db"));
            System.out.println("Dados existentes deletados");

            // criando dados aleatorios
            System.out.println("\nCriando dados aleatórios (5 mil dados, CPFs de 100000000 até 100005000)");
            inserirDadosAleatorios(5000);
            System.out.println("Inserção de dados finalizada");

            // realizando busca para medir tempo gasto pela busca
            System.out.println("\nRealizando a busca do registro 100004000");
            startTime = System.nanoTime();
            Prontuario temp = hash.read("100004000".hashCode());
            System.out.println("Dados: " + temp);
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));

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

  public static void inserirDadosAleatorios(int qtdRegistros) {
    try {
      HashExtensivel<Prontuario> hash;
      hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
          "dados/pessoas.hash_c.db");

      // opção para inserir dados aleatorios
      System.out.println("Inserindo dados de teste: ");
      Random gerador = new Random();
      int qtdInicial = 100000000;
      int qtdMax = qtdInicial + qtdRegistros;
      // int qtdMax = 100030000;
      // int qtdMax = 999999999;
      int cpf;
      String nome = "";
      String data_nasc = "";
      String sexo = "sexo teste";
      String diagnostico = "";

      for (cpf = qtdInicial; cpf < qtdMax; cpf++) {
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
    String diagnostico = "Diagnostico_teste ";
    String palavra;
    for (int i = 0; i < 4; i++) {
      palavra = "";
      for (int y = 0; y < 5; y++) {
        palavra += ((char) (gerador.nextInt(25) + 65));
      }
      diagnostico += (palavra + " ");
    }
    return diagnostico;
  }

  public static String gerarNomeAleatorio(Random gerador) {
    String diagnostico = "Nome_teste ";
    for (int y = 0; y < 10; y++) {
      diagnostico += ((char) (gerador.nextInt(25) + 65));
    }
    return diagnostico;
  }
}
