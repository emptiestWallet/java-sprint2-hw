public class Record {
        String name;
        Integer price;
        Integer quantity;
        Boolean isExpense;


        public Record(String name, Integer price, Boolean isExpense) {
            this.name = name;
            this.price = price;
            this.isExpense = isExpense;
        }

        public Record(String name, Boolean isExpense, Integer quantity, Integer price) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.isExpense = isExpense;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public Boolean getExpense() {
            return isExpense;
        }
}
