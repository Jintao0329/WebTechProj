package models.user;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.InsertOneResult;
import dbconnection.DbConnection;
import models.ui_util.ItemBoxUi;
import models.foodItems.Fooditem;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Restaurant extends User implements login {
    @BsonProperty(value = "food_items")
    private ArrayList<ObjectId> foodItems;
    @BsonProperty(value = "ratings")
    private ArrayList<ObjectId> ratings;
    @BsonProperty(value = "address")
    private Object address;
    @BsonProperty(value = "delivery_services")
    private ArrayList<ObjectId> deliveryServices;
    @BsonProperty(value = "tags")
    private ArrayList<ObjectId> tags;
    @BsonProperty(value = "combos")
    private ArrayList<ObjectId> combos;

    public Restaurant() {
        super();
    }

    public Restaurant(Document document) {
        super(document, "restaurant");
        setAddress(document.get("address"));
        setTags((ArrayList<ObjectId>) document.get("tags"));
        setDeliveryServices((ArrayList<ObjectId>) document.get("delivery_services"));
        setFoodItems((ArrayList<ObjectId>) document.get("food_items"));
        setRatings((ArrayList<ObjectId>) document.get("ratings"));
        setCombos((ArrayList<ObjectId>) document.get("combos"));
    }

    public Restaurant(String username,
                      String password,
                      String name,
                      String email,
                      ArrayList<ObjectId> orders,
                      ArrayList<ObjectId> foodItems,
                      ArrayList<ObjectId> combos,
                      ArrayList<ObjectId> ratings,
                      ArrayList<ObjectId> deliveryServices,
                      ArrayList<ObjectId> tags,
                      JsonObject address) {
        super(username, password, name, email, orders, "restaurant");
        setFoodItems(foodItems);
        setRatings(ratings);
        setDeliveryServices(deliveryServices);
        setTags(tags);
        setAddress(address);
        setCombos(combos);
    }

    public boolean write() {
        Document doc = this.toDocument();
        doc.put("address", getAddress());
        doc.put("food_items", getFoodItems());
        doc.put("ratings", getRatings());
        doc.put("delivery_services", getDeliveryServices());
        doc.put("tags", getTags());
        doc.put("combos", getCombos());
        InsertOneResult written = DbConnection.insertOne("restaurants", doc);
        BsonObjectId id = (BsonObjectId) written.getInsertedId();
        this.setId(new ObjectId(String.valueOf(id.getValue())));
        return written.wasAcknowledged();
    }

    public ArrayList<ObjectId> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<ObjectId> foodItems) {
        this.foodItems = foodItems;
    }

    public ArrayList<ObjectId> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<ObjectId> ratings) {
        this.ratings = ratings;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public ArrayList<ObjectId> getDeliveryServices() {
        return deliveryServices;
    }

    public void setDeliveryServices(ArrayList<ObjectId> deliveryServices) {
        this.deliveryServices = deliveryServices;
    }

    public ArrayList<ObjectId> getTags() {
        return tags;
    }

    public void setTags(ArrayList<ObjectId> tags) {
        this.tags = tags;
    }

    public ArrayList<ObjectId> getCombos() {
        return combos;
    }

    public void setCombos(ArrayList<ObjectId> combos) {
        this.combos = combos;
    }

    public boolean addFoodItem(Document foodItem){
        ObjectId id = (ObjectId) foodItem.get("_id");
        Document find = new Document("_id", this.getId());
        BasicDBObject update = new BasicDBObject("food_items", id);
        BasicDBObject updatequery = new BasicDBObject("$push", update);
        this.foodItems.add(id);
        return DbConnection.updateOne("restaurants", find, updatequery).wasAcknowledged();
    }

    public JsonObject getJsonAddress() {
        org.bson.Document doc = (Document) this.address;
        return new JsonObject(doc.toJson());
    }

    public ItemBoxUi getUiItemBox() {
        //*Creates a ItemBox object to display in an item box element
        //*Creates the string from an address object
        Document addrs = (Document) this.address;
        ArrayList<Object> addrs_doc = (ArrayList<Object>) addrs.get("address_components");
        StringBuilder addrs_str = new StringBuilder();
        for (Object obj : addrs_doc) {
            Document doc = (Document) obj;
            addrs_str.append(doc.getString("short_name"));
            addrs_str.append(", ");
        }

        /*TODO:Change photo_url and action url*/
        return new ItemBoxUi(
                this.getName(),
                addrs_str.toString(),
                "",
                "");
    }
}
