db.createCollection("order_numbers");
db.order_numbers.createIndex({ "number": 1 }, { unique: true });