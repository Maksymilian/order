db.createCollection("orders");
db.orders.createIndex({ "orderNumber": 1 }, { unique: true });
paymentBankAccountNumber