public class Transaction {

    private String sender, receiver;
    private float amount;

    public Transaction(String sender, String receiver, float amount){
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public float getAmount() {
        return amount;
    }
}
