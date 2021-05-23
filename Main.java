import java.util.Scanner;
import Hashing.HashExtensivel;
import java.io.File;

public class Main {
  public static void main(String[] args) {

    HashExtensivel<pcvPessoa> he;
    Scanner console = new Scanner(System.in);
    long startTime, endTime;

    try {

      File d = new File("dados");
      if (!d.exists())
        d.mkdir();

      he = new HashExtensivel<>(pcvPessoa.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
          "dados/pessoas.hash_c.db");
      int opcao;

      do {
        System.out.println("\n\n-------------------------------");
        System.out.println("              MENU");
        System.out.println("-------------------------------");
        System.out.println("1 - Inserir");
        System.out.println("2 - Buscar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Editar Diagnóstico");
        System.out.println("5 - Imprimir");
        System.out.println("0 - Sair");

        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
            System.out.println("\nINCLUSÃO");
            System.out.print("Nome: ");
            String nome = console.nextLine();
            System.out.print("cpf: ");
            int cpf = console.nextInt();
            // System.out.print("Data de Nascimento: ");
            // String data_nasc = console.nextLine();
            // System.out.print("Sexo: ");
            // String sexo = console.nextLine();
            // System.out.print("Diagnostico médico: ");
            // String diagnostico = console.next();
            String data_nasc = "20/03/2002";
            String sexo = "sla";
            String diagnostico = "gay";
            he.create(new pcvPessoa(nome, data_nasc, sexo, diagnostico, cpf));
            he.print();
          }
            break;
          case 2: {
            System.out.println("\nBUSCA");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            System.out.println("Dados: " + he.read(String.valueOf(cpf).hashCode()));
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
          }
          break;
          case 3: {
            System.out.println("\nEXCLUSÃO");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            he.delete(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca e exclusão em nanosegundos: " + (endTime - startTime));
            he.print();
          }
            break;
          case 4: {
            System.out.println("\nBUSQUE O CPF PARA EDITAR");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            pcvPessoa temp = he.read(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
            System.out.println("\nDigite a alteração do diagnóstico:");
            String diag = console.next();
            temp.setDiagnostico(diag);
            he.update(temp);
            System.out.println("\nDiagnóstico alterado com sucesso");
          }
            break;
          case 5: {
            he.print();
          }
            break;
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
}