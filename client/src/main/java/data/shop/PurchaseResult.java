package data.shop;

public class PurchaseResult {
    public int money;
    public boolean success;
    public String message;

    public PurchaseResult(int money, boolean success, String message) {
        this.money = money;
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        return "PurchaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", newBalance=" + money +
                '}';
    }
}
