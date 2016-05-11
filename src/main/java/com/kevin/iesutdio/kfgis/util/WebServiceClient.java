package com.kevin.iesutdio.kfgis.util;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class WebServiceClient {
    
    public String obtainData(String url, String operationName, String xmlRequest){
        String result = "";
        String endpoint = url;
        Service service = new Service();

        try {
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endpoint));

            call.setOperationName(operationName);

            String res = (String) call.invoke(new Object[] { xmlRequest });
            result = res;
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        return result;
    }
}
