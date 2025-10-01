db.createCollection("order_numbers");
db.order_number.createIndex({ "number": 1 }, { unique: true });