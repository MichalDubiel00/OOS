package bank;
import bank.exceptions.TransactionAttributeException;
import com.google.gson.*;

import java.lang.reflect.Type;

public class Serializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction>{

    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ){
        JsonObject jo = jsonElement.getAsJsonObject();
        JsonElement instance = jo.get("INSTANCE");
        JsonObject io = instance.getAsJsonObject();
        if (jo.get("CLASSNAME").getAsString().contains("Payment")){
            return new Payment(
                    io.get("date").getAsString(),
                    io.get("amount").getAsDouble(),

                    io.get("description").getAsString(),
                    io.get("outgoingInterest").getAsDouble(),
                    io.get("incomingInterest").getAsDouble()
            );
        }
        else if (jo.get("CLASSNAME").getAsString().contains("OutgoingTransfer")){
            try {
                return new OutgoingTransfer(
                        io.get("date").getAsString(),
                        io.get("amount").getAsDouble(),

                        io.get("description").getAsString(),
                        io.get("recipient").getAsString(),
                        io.get("sender").getAsString()
                );
            } catch (TransactionAttributeException e) {
                throw new RuntimeException(e);
            }
        }
        else if (jo.get("CLASSNAME").getAsString().contains("IncomingTransfer")){
            try {
                return new IncomingTransfer(
                        io.get("date").getAsString(),
                        io.get("amount").getAsDouble(),

                        io.get("description").getAsString(),
                        io.get("recipient").getAsString(),
                        io.get("sender").getAsString()
                );
            } catch (TransactionAttributeException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jo = new JsonObject();
        Gson test = new Gson();
        JsonElement transactionValues=test.toJsonTree(transaction);


        jo.addProperty("CLASSNAME", transaction.getClass().getName());
        jo.add("INSTANCE",transactionValues);

        return jo;
    }

}

