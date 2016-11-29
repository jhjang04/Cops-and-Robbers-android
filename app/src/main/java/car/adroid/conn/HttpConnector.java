package car.adroid.conn;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import car.adroid.config.AppConfig;

/**
 * Created by jjh on 2016-11-29.
 */

public class HttpConnector extends abstractConnector{

    public static JSONObject SimpleRequest(String src , Map params) throws Exception {
        URL Url = null;
        HttpURLConnection conn = null;
        try {
            StringBuffer urlBuffer = new StringBuffer()
                    .append(AppConfig.HTTP_URL)
                    .append("?src=")
                    .append(src);

            if (params != null) {
                Set<String> keyset = (Set<String>) params.keySet();
                Object[] strKeys = keyset.toArray();
                for (int i = 0; i < strKeys.length; i++) {
                    urlBuffer.append("&")
                            .append(strKeys[i].toString())
                            .append("=")
                            .append(params.get(strKeys[i].toString()))
                    ;
                }
            }

            Url = new URL(urlBuffer.toString());  // URL화 한다.
            conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestMethod("GET"); // get방식 통신
            conn.setDoOutput(true);       // 쓰기모드 지정
            conn.setDoInput(true);        // 읽기모드 지정
            conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

            String strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

            InputStream is = conn.getInputStream();        //input스트림 개방

            StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  //문자열 셋 세팅
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            String result = builder.toString();
            JSONObject json = new JSONObject(result);

            return json;
        }
        catch(Exception e){
            throw e;
        }
        finally {
            conn.disconnect();
        }
    }

    @Override
    public void makeRoom(String strPwd , String strNick) {
        Map<String , String> mapParam = new HashMap<String , String>();
        mapParam.put("pwd" , strPwd);
        mapParam.put("nick" , strNick);
        try {
            JSONObject json = HttpConnector.SimpleRequest("makeRoom" , mapParam);
            String strRst = json.getString("result");
            int iRoomNo = json.getInt("room_no");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinRoom(String strRoomNo , String strPwd , String strNick) {
        Map<String , String> mapParam = new HashMap<String , String>();
        mapParam.put("room_no" , strRoomNo );
        mapParam.put("pwd" , strPwd );
        mapParam.put("nick" , strNick);
        try {
            JSONObject json = HttpConnector.SimpleRequest("makeRoom" , mapParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
