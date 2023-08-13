import java.util.List;

public class OrderData {
    public class Order {

        public List<String> ingredients;

        public Order(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }
    }
}