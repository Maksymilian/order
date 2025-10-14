db.createUser({
  user: "client",
  pwd: "pa33w0rd",
  roles: [{ role: "readWrite", db: db.getName() }]
});
