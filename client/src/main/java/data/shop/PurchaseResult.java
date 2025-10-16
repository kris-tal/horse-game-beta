package data.shop;

public class PurchaseResult {
    private final int money;
    private final boolean success;
    private final String message;
    private final boolean isUpgrade;

    public PurchaseResult(int money, boolean success, String message, boolean isUpgrade) {
        this.money = money;
        this.success = success;
        this.message = message;
        this.isUpgrade = isUpgrade;
    }

    public int getMoney() { return money; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isUpgrade() { return isUpgrade; }

    @Override
    public String toString() {
        return "PurchaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", money=" + money +
                ", isUpgrade=" + isUpgrade +
                '}';
    }
}
