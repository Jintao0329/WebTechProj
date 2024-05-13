package models.user;

import com.mongodb.client.result.InsertOneResult;
import dbconnection.DbConnection;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Driver extends User implements login{
    public Driver(Document document) {
        super(document);
        setFirstName(document.getString("first_name"));
        setTools((ArrayList<ObjectId>) document.get("tools"));
        setRatings((ArrayList<ObjectId>) document.get("ratings"));
        setCurrentPos((JsonObject) document.get("current_pos"));
    }

    public Driver(String username, String password, String name, String email,
                  ArrayList<ObjectId> orders, String firstName, ArrayList<ObjectId> tools,
                  ArrayList<ObjectId> ratings, JsonObject current_pos) {
        super(username, password, name, email, orders);
        setFirstName(firstName);
        setTools(tools);
        setRatings(ratings);
        setCurrentPos(current_pos);
    }

    public boolean write(){
        Document doc = this.toDocument();
        doc.put("current_pos", getCurrentPos());
        doc.put("ratings", getRatings());
        doc.put("tools", getTools());
        doc.put("first_name", getFirstName());
        InsertOneResult written = DbConnection.insertOne("drivers",doc);
        BsonObjectId id = (BsonObjectId) written.getInsertedId();
        this.setId(id.getValue());
        return written.wasAcknowledged();
    }

    @BsonProperty(value = "first_name")
    private String firstName;
    @BsonProperty(value = "tools")
    private ArrayList<ObjectId> tools;
    @BsonProperty(value = "ratings")
    private ArrayList<ObjectId> ratings;
    @BsonProperty(value = "current_pos")
    private JsonObject currentPos;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public ArrayList<ObjectId> getTools() {
        return tools;
    }

    public void setTools(ArrayList<ObjectId> tools) {
        this.tools = tools;
    }

    public ArrayList<ObjectId> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<ObjectId> ratings) {
        this.ratings = ratings;
    }

    public JsonObject getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(JsonObject currentPos) {
        this.currentPos = currentPos;
    }
}
