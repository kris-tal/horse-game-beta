package data.shop;

import java.util.List;

public class ShopResponse {
    public List<HorseToBuy> horses;
    public List<Upgrade> upgrades;

    public static class HorseToBuy {
        public int id;
        public int price;

        public HorseToBuy(int id, int price) {
            this.id = id;
            this.price = price;
        }
    }

    public static class Upgrade {
        public int id;
        public int price;

        public Upgrade(int id, int price) {
            this.id = id;
            this.price = price;
        }
    }
}
