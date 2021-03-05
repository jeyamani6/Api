package template;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.gradelproject.EmpDetails;
import com.gradelproject.EmpResponse;
import dynamodb.UserDetailsRepositoryV2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Base64;

/**
 * This class calls the public api and returns api response
 * @author : Jeyamani A
 */
public class TemplateService {

    UserDetailsRepositoryV2 user=new UserDetailsRepositoryV2();
    RestTemplate restTemplate=new RestTemplate();

    /**
     * This method calls the public Api and returns  Api response.
     * @param template:EmpDetails object
     * @return : Api response
     */
    public EmpResponse createUser(EmpDetails template)
    {
        getSecret();
        String resource=user.getUserDetails("url");
        System.out.println("Entering servce class");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<EmpDetails> entity = new HttpEntity<EmpDetails>(template, headers);
        ResponseEntity<EmpResponse> responseEntity =  restTemplate
                .exchange(resource, HttpMethod.POST, entity, EmpResponse.class);
        System.out.println("Response entity-"+responseEntity.getBody());
        return responseEntity.getBody();

    }
    /**
     * This method retrives the stored secrets from the Secrets Manager.
     */
    public static void getSecret() {

        String secretName = "demo";
        String region = "us-east-2";

        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();


        String secret, decodedBinarySecret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException | InternalServiceErrorException | InvalidParameterException | InvalidRequestException | ResourceNotFoundException e) {
            throw e;
        }

        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();

            System.out.println(secret);
        }
        else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());

            System.out.println(decodedBinarySecret);
        }

    }
}