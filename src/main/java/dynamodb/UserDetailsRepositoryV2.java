package dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : Jeyamani A
 */
public class UserDetailsRepositoryV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsRepositoryV2.class);

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    DynamoDB dynamoDb = new DynamoDB(client);
    Map<String,String> map=new HashMap<String, String>();

    /**
     * This method retreives the details stored in dynamo db.
     * @param key : key value
     * @return : Value found in the dynamo db.
     */
    public String getUserDetails(String key) {

        System.out.println("entering dynamodb method");
        Table table = dynamoDb.getTable("Template");
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("Key", key);
        try {
            System.out.println("Attempting to read item");
            Item outcome = table.getItem(spec);
            if (Objects.nonNull(outcome)) {
                map.put(outcome.get("Key").toString(),outcome.get("value").toString());
                System.out.println(map.get("1"));
                return map.get(key);
                
            }
        } catch (RuntimeException e) {
            LOGGER.error("Exception occurred during getUserDetails : ", e);
        }
        return null;
    }

}

