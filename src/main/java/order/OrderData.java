package order;

import ingredient.Ingredient;

public class OrderData {
    private String[] ingredients;
    private String _id;
    private String status;
    private String number;

    public OrderData() {
    }

    public OrderData(Ingredient ingredient) {
        this.ingredients = new String[]{
                ingredient.get_id()
        };
    }
}