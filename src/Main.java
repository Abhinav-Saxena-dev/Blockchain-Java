import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

    static BlockChain bc;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Set the difficulty of the chain");
        int dif = sc.nextInt();

        System.out.println("Genesis block mining initiated.");
        bc = new BlockChain(dif);

        boolean over = false;

        while(!over){
            System.out.println("1. Add New Block");
            System.out.println("2. Add New Transaction");
            System.out.println("3. Display Blockchain");
            System.out.println("4. Display Mempool");
            System.out.println("5. Check Validity");
            int input=sc.nextInt();

            switch (input){
                case 1 :
                    mineBlock();
                    break;
                case 2 :
                    System.out.println("Enter sender name");
                    String sender = sc.next();
                    System.out.println("Enter receiver name");
                    String receiver = sc.next();
                    System.out.println("Enter transaction amount");
                    float amount = (float)sc.nextDouble();
                    bc.addTransaction(sender, receiver, amount);
                    break;
                case 3 :
                    bc.displayChain();
                    break;
                case 4 :
                    bc.printTransactions();
                    break;
                case 5 :
                    checkValidity();
                    break;
                default:
                    over = true;
            }
        }
    }

    public static void mineBlock() throws NoSuchAlgorithmException {
        Block newBlock = bc.getProof();
        bc.addNewBlock(newBlock);
        System.out.println("Block mined with Hash : " + bc.getHashBlock(newBlock));
        System.out.println();
    }

    public static void checkValidity() throws NoSuchAlgorithmException {
        boolean check = bc.isChainValid();
        if(check){
            System.out.println("The chain is valid");
        }
        else{
            System.out.println("Chain is not valid");
        }
    }
}