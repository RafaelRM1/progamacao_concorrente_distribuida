package util;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {

        Lock lock = new ReentrantLock();

        Banco banco = new Banco(lock);

        Conta contaSalarioFuncionario1 = new Conta("Funcionario 1", 0);
        Conta contaInvestimentoFuncionario1 = new Conta("Funcionario 1", 0);
        Conta contaSalarioFuncionario2 = new Conta("Funcionario 2", 0);
        Conta contaInvestimentoFuncionario2 = new Conta("Funcionario 2", 0);

        Conta contaSalarioFuncionario3 = new Conta("Funcionario 3", 0);
        Conta contaInvestimentoFuncionario3 = new Conta("Funcionario 3", 0);
        Conta contaSalarioFuncionario4 = new Conta("Funcionario 4", 0);
        Conta contaInvestimentoFuncionario4 = new Conta("Funcionario 4", 0);

        Conta contaLoja1 = new Conta("Loja 1", 0);
        Conta contaLoja2 = new Conta("Loja 2", 0);

        ImplementarFuncionario funcionario1 = new ImplementarFuncionario(contaSalarioFuncionario1, contaInvestimentoFuncionario1);
        ImplementarFuncionario funcionario2 = new ImplementarFuncionario(contaSalarioFuncionario2, contaInvestimentoFuncionario2);

        ImplementarFuncionario funcionario3 = new ImplementarFuncionario(contaSalarioFuncionario3, contaInvestimentoFuncionario3);
        ImplementarFuncionario funcionario4 = new ImplementarFuncionario(contaSalarioFuncionario4, contaInvestimentoFuncionario4);

        List<Funcionario> funcionariosLoja1 = new ArrayList<>();
        List<Funcionario> funcionariosLoja2 = new ArrayList<>();

        funcionariosLoja1.add(funcionario1);
        funcionariosLoja1.add(funcionario2);
        funcionariosLoja2.add(funcionario3);
        funcionariosLoja2.add(funcionario4);

        Loja loja1 = new Loja(contaLoja1, funcionariosLoja1, banco);
        Loja loja2 = new Loja(contaLoja2, funcionariosLoja2, banco);

        funcionario1.start();
        funcionario2.start();
        funcionario3.start();
        funcionario4.start();

        for (int i = 1; i <= 5; i++) {
            Conta contaCliente = new Conta("Cliente" + i, 1000);
            Cliente cliente = new Cliente(contaCliente, banco, loja1, loja2);
            cliente.start();
        }

        try {
            banco.esperarTransferenciasConcluidas();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loja1.pagarFuncionario();
        loja2.pagarFuncionario();

        try {
            Thread.sleep(20000); // Tempo para as compras
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Conta loja 1: " + loja1.getConta().getSaldo());
        System.out.println("Cont loja 2: " + loja2.getConta().getSaldo());

        for (int i = 1; i <= 5; i++) {
            Conta contaCliente = new Conta("Cliente" + i, 0);
            System.out.println("Saldo do cliente Cliente" + i + ": " + contaCliente.getSaldo());
        }

        System.out.println("Saldo e investimento funcionários:");
        for (Funcionario funcionario : funcionariosLoja1) {
            System.out.println("Saldo : " + funcionario.getContaSalario().getId() + " na loja 1 : "
                    + funcionario.getContaSalario().getSaldo()
                    + " -- Investimento : " + funcionario.getContaInvestimento().getSaldo());
        }
        for (Funcionario funcionario : funcionariosLoja2) {
            System.out.println("Saldo : " + funcionario.getContaSalario().getId() + " na loja 1 : "
                    + funcionario.getContaSalario().getSaldo()
                    + " -- Investiment: " + funcionario.getContaInvestimento().getSaldo());
        }
        loja1.deveContinuar = false;
        loja2.deveContinuar = false;
    }
}