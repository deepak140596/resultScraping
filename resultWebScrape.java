
/**
 * Write a description of class resultWebScrape here.
 *
 * @Deepak
 * @1
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class resultWebScrape
{
    
    public static void main(String arg[]){
        resultWebScrape scrape=new resultWebScrape();
    
    }
    String RESULT_URL="http://61.12.70.61:8084/heresult18o.aspx";
    String eightDigCode="12615001";
    int semester=5;
    int stopRoll=10;
    int startRoll=1;
    String nonExistentStudent="No such student exists in this database";
    
    public resultWebScrape()
    {
        
        
        int i=0;
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter first 8-digit of roll code: ");
        eightDigCode=scan.next();
        System.out.println("Enter semester: ");
        semester=scan.nextInt();
        System.out.println("Enter starting roll: ");
        startRoll=scan.nextInt();
        System.out.println("Enter ending roll: ");
        stopRoll=scan.nextInt();
        
        
        try{
            
            for(i=startRoll;i<stopRoll;i++){
                String rollNo="";
                if(i<10)
                    rollNo=eightDigCode+"00"+i;
              
               else if(i<100)
                     rollNo=eightDigCode+"0"+i;
                    
               String res=postToSite(rollNo,semester+""); 
               if(res.equalsIgnoreCase(nonExistentStudent)){
                    System.out.println("Does not exist.");
                    continue;
                }
                   
            
                Document doc=Jsoup.parse(res);
                Element studNameTag= doc.getElementById("lblname");
                Element sgpaTag=doc.getElementById("lblbottom1");
            
                String studName=(studNameTag.text()).substring(4,studNameTag.text().length()), 
                sgpa=(sgpaTag.text()).substring(24,sgpaTag.text().length());
            
                System.out.println(studName+" \t"+sgpa);
                
            }
        }catch(Exception e){
        }
    }
    public String postToSite(String roll, String sem) throws Exception{
        URL url=new URL(RESULT_URL);
        Map<String, Object> params= new LinkedHashMap<>();
        params.put("roll",roll);
        params.put("sem",sem);
        
        StringBuilder postData=new StringBuilder();
        for(Map.Entry<String,Object> param : params.entrySet()){
            if(postData.length() != 0) 
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
        }
        
        byte[] postDataBytes=postData.toString().getBytes("UTF-8");
        
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        
        Reader in =new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        StringBuilder getData=new StringBuilder();
        for(int c;(c=in.read()) >=0;)
            getData.append((char)c);
         
        return getData.toString();
        

        
    }
}
