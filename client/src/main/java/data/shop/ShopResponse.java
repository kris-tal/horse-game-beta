package data.shop;

import java.util.List;

public class ShopResponse {
    private final List<HorseToBuy> horses;
    private final List<Upgrade> upgrades;

    public ShopResponse(List<HorseToBuy> horses, List<Upgrade> upgrades) {
        this.horses = horses;
        this.upgrades = upgrades;
    }

    public List<HorseToBuy> getHorses() { return horses; }
    public List<Upgrade> getUpgrades() { return upgrades; }

    public static class HorseToBuy {
        private final int id;
        private final int price;

        public HorseToBuy(int id, int price) {
            this.id = id;
            this.price = price;
        }

        public int getId() { return id; }
        public int getPrice() { return price; }
    }

    public static class Upgrade {
        private final int id;
        private final int price;

        public Upgrade(int id, int price) {
            this.id = id;
            this.price = price;
        }

        public int getId() { return id; }
        public int getPrice() { return price; }
    }
}
