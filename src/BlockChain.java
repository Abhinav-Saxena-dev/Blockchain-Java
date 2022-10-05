import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockChain {
    public List<Block> chain;
    int target;

    public List<Transaction> txn;

    public BlockChain(int target) throws NoSuchAlgorithmException {
        this.chain = new ArrayList<>();
        this.txn = new ArrayList<>();
        this.target = target;
        Block b = getGenesis();
        addNewBlock(b);
    }

    public Block getGenesis() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        int index = chain.size();
        int currProof = 1;
        String prev_hash = "";
        for(int i = 0; i < 64; i++){
            prev_hash += '0';
        }
        Block b = null;
        while(true){
            Date date = new Date();
            String input = "0 " + prev_hash + " " + currProof + " " + date + " ";
            String transaction = "This is the genesis block";
            input += transaction;

            // Converting the String into a byte array and using digest to compute the hash function on the md object.
            // we can also use md.update(byteArray) and simply use md.digest();
            byte[] arr = md.digest(input.getBytes(StandardCharsets.UTF_8));
            String hash = toHexString(arr);

            if(isOkay(hash, this.target)){
                b = new Block(chain.size(), currProof, date, prev_hash, transaction);
                System.out.println("The block mined wish hash : " +hash);
                break;
            }
            currProof++;
        }
        return b;
    }

    public String getHashBlock(Block b)throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        String input = b.getIndex() + " " + b.getPrev_hash() + " " + b.getProof() + " " + b.getTimestamp() + " ";
        String transaction = b.getData();
        input += transaction;

        byte[] arr = md.digest(input.getBytes(StandardCharsets.UTF_8));
        String hash = toHexString(arr);

        return hash;
    }

    // Add the block to the existing chain.
    public Block addNewBlock(Block b){
        chain.add(b);
        return b;
    }

    // return the previous block
    public Block getPrevBlock(){
        Block lb = chain.get(chain.size() - 1);
        return lb;
    }

    public Block getProof() throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        int currProof = 1;
        Block prev = getPrevBlock();
        String prevHash = getHashBlock(prev);
        Block b = null;
        while(true){
            Date date = new Date();
            String input = chain.size() + " " + prevHash + " " + currProof + " " + date + " ";
            String transaction = "";

            for(Transaction t : txn){
                transaction += (t.getSender() + " -> " + t.getReceiver() + " | amount : " + t.getAmount() + ";");
            }
            input += transaction;

            byte[] arr = md.digest(input.getBytes(StandardCharsets.UTF_8));
            String hash = toHexString(arr);

            if(isOkay(hash, target)){
                b = new Block(chain.size(), currProof, date, prevHash, transaction);
                break;
            }
            currProof++;
        }
        return b;
    }

    public String toHexString(byte[] arr){
        // converting byte array to signum representation.(signum is signed number)
        BigInteger bi = new BigInteger(1, arr);

        // bi.toString(16) means that function will first convert the BigInteger
        // to hexadecimal form then it will return String representation of that hexadecimal number.
        StringBuilder str = new StringBuilder(bi.toString(16));

        // add leading zeroes to make the string length 64
        while(str.length() < 64){
            str.insert(0, '0');
        }
        return str.toString();
    }

    //This function checks that the hash is valid according to the target set.
    public boolean isOkay(String hash, int target){
        for(int i = 0; i < target; i++){
            if(hash.charAt(i) != '0'){
                return false;
            }
        }
        return true;
    }

    // check if the chain is valid by checking each block in the chain for hash check and target check.
    public boolean isChainValid() throws NoSuchAlgorithmException {
        Block first = chain.get(0);
        String hash = getHashBlock(first);
        if(!isOkay(hash, target)){
            return false;
        }
        for(int i = 1; i < chain.size(); i++){
            Block curr = chain.get(i);
            Block prev = chain.get(i-1);

            String prevHashInCurr = curr.getPrev_hash();
            String prevHash = getHashBlock(prev);

            if(!prevHashInCurr.equals(prevHash)){
                return false;
            }

            String currHash = getHashBlock(curr);
            if(!isOkay(currHash, target)){
                System.out.println("Proof of work is wrong");
                return false;
            }
        }
        return true;
    }

    public void displayChain(){
        for(Block b : chain){
            System.out.println("{");
            System.out.println("index : " + b.getIndex());
            System.out.println("Previous hash : " + b.getPrev_hash());
            System.out.println("nonce : " + b.getProof());
            System.out.println("Timestamp : " + b.getTimestamp());
            System.out.println("Data : " + b.getData());
            System.out.println("}");
        }
        System.out.println("Length of chain : " + chain.size());
    }

    public void addTransaction(String sender, String receiver, float amount){
        Transaction transaction = new Transaction(sender, receiver, amount);
        txn.add(transaction);
    }

    public void printTransactions(){
        System.out.println("{");
        for(Transaction t : txn){
            System.out.println(t.getSender() + " -> " + t.getReceiver() + " | amount : " + t.getAmount());
        }
        System.out.println("}");
    }
}
