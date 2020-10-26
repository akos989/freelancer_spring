package hu.bme.aut.freelancer_spring.model.enums;

public enum Size {

    S(20,30,40,1000),
    M(40,30,40,1200),
    L(40,60,40,1500),
    XL(120,60,40,2000);

    private final int x, y, z, price;

    Size(int x, int y, int z, int price) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.price = price;
    }

    public int getCC() {
        return x * y * z;
    }

    public int getPrice() {
        return price;
    }
}
