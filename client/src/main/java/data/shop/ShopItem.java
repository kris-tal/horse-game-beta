package data.shop;


import data.horse.HorseType;

public class ShopItem {
    private HorseType horseType;
    private int price;
    private boolean owned;
    private boolean upgrade;

    public ShopItem(HorseType horseType, int price, boolean owned, boolean upgrade) {
        this.horseType = horseType;
        this.price = price;
        this.owned = owned;
        this.upgrade = upgrade;
    }

    public HorseType getHorseType() {
        return horseType;
    }

    public int getId() {
        return horseType.getId();
    }

    public String getName() {
        return horseType.getName();
    }

    public String getAtlasKey() {
        return horseType.getAtlasKey();
    }

    public int getPrice() {
        return price;
    }

    public boolean isOwned() {
        return owned;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    @Override
    public String toString() {
        return String.format("<%d, %s, %d>", getId(), getName(), getPrice());
    }
}


