/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nmproto;

import java.util.Dictionary;
import com.ironsource.mediationsdk.utils.IronSourceConstants;
import org.asynchttpclient.*;
import org.asynchttpclient.Dsl.*;
import org.json.JSONObject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;



/**
 *
 * @author faiza
 */
public class WebService {
    
    public static String doPost(String url, Dictionary headers, Object body)
    {
        String result = "";
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder builder = client.preparePost(url);
        
        
        
        return "";
        
    }
    public static String doGet(String url, Dictionary headers)
    {
        String result = "";
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder builder = client.prepareGet(url);
        for(Enumeration i = headers.keys() ; i.hasMoreElements();)
        {
            String key = (String) i.nextElement();
            builder.setHeader(key, headers.get(key));
        }
        Future<Response> future = client.executeRequest(builder.build());
        Response response;
        try{
            response = future.get();
            int status = response.getStatusCode();
            if(status==200)
            {
                result = response.getResponseBody();
            }
        }catch (InterruptedException | ExecutionException ex)
        {           
            
            result = "";
        }
        return result;
    }
}
