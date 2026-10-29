// =========================================================================
// SBA301 - Slot 32: Compass & MongoDB Shell Verification Queries
// Target Database: slot32_lab (or sba301_slot32)
// =========================================================================

// 1. Find all active products
db.products.find({ "active": true });

// 2. Query nested embedded document using Dot Notation
db.products.find({ "specs.hdmi": true });

// 3. Query array element membership
db.products.find({ "tags": "accessory" });

// 4. Combined Filter: category reference + price range
db.products.find({
  "categoryId": "CAT-ACC",
  "price": { "$lte": 50.0 }
});

// 5. Projection: Only return name, price, and specs.hdmi (exclude _id)
db.products.find(
  { "active": true },
  { "name": 1, "price": 1, "specs.hdmi": 1, "_id": 0 }
);

// 6. Sort and Limit: Top 3 most expensive products
db.products.find().sort({ "price": -1 }).limit(3);

// 7. Reference Lookup ($lookup - aggregation joining categories and products)
db.products.aggregate([
  {
    "$lookup": {
      "from": "categories",
      "localField": "categoryId",
      "foreignField": "_id",
      "as": "categoryDetails"
    }
  },
  { "$unwind": "$categoryDetails" },
  {
    "$project": {
      "name": 1,
      "price": 1,
      "categoryName": "$categoryDetails.name"
    }
  }
]);

// 8. Break-It Detection: Find documents where price is NOT a number (detect type drift)
db.products.find({ "price": { "$type": "string" } });
