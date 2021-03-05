package com.template.employee;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradelproject.EmpDetails;
import com.gradelproject.EmpResponse;
import template.TemplateService;


/**
 *
 * @author : Jeyamani A
 */
public class lambdaEmployee implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    TemplateService service=new TemplateService();

    /**
     *
     * @param apiGatewayProxyRequestEvent: Request
     * @param context: context
     * @return : Response
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        String request=apiGatewayProxyRequestEvent.getBody();
        String res;
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent=new APIGatewayProxyResponseEvent();
        ObjectMapper objectMapper=new ObjectMapper();
        try
        {
            EmpDetails empDetails=objectMapper.readValue(request, EmpDetails.class);
            EmpResponse response=service.createUser(empDetails);
            res=objectMapper.writeValueAsString(response);
            apiGatewayProxyResponseEvent.setBody(res);
            return apiGatewayProxyResponseEvent;
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return apiGatewayProxyResponseEvent;
    }
}