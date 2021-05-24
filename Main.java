import java.util.Scanner;
import Hashing.HashExtensivel;
import java.io.File;

public class Main {
  public static void main(String[] args) {

    HashExtensivel<Prontuario> hash;  
    Scanner console = new Scanner(System.in);
    long startTime, endTime;

    try {
      File d = new File("dados");
      if (!d.exists())
        d.mkdir();

      hash = new HashExtensivel<>(Prontuario.class.getConstructor(), 4, "dados/pessoas.hash_d.db",
          "dados/pessoas.hash_c.db");
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
        System.out.println("0 - Sair do sistema");

        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
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
            hash.print();
          }
            break;
          case 2: {
            System.out.println("\nBUSCANDO PRONTUÁRIO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            System.out.println("Dados: " + hash.read(String.valueOf(cpf).hashCode()));
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
          }
          break;
          case 3: {
            System.out.println("\nEXCLUSÃO DE PRONTUÁRIO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            hash.delete(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca e exclusão em nanosegundos: " + (endTime - startTime));
            hash.print();
          }
          break;
          case 4: {
            System.out.println("\nBUSQUE O CPF PARA EDITAR O DIAGNÓSTICO");
            System.out.println("\nDigite o CPF para prosseguir.");
            System.out.print("CPF: ");
            int cpf = console.nextInt();
            startTime = System.nanoTime();
            Prontuario temp = hash.read(String.valueOf(cpf).hashCode());
            endTime = System.nanoTime();
            System.out.println("Tempo de busca em nanosegundos: " + (endTime - startTime));
            System.out.println("\nDigite a alteração do diagnóstico:");
            String diag = console.nextLine();
            temp.setDiagnostico(diag);
            hash.update(temp);
            System.out.println("\nDiagnóstico alterado com sucesso");
          }
          break;
          case 5: {
            System.out.println("\nIMPRIMINDO PRONTUÁRIOS:");
            hash.print();
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
