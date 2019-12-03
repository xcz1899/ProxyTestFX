package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class ProxyTest {
    /**
     * 是否为IP地址
     *
     * @param str 参数
     * @return 返回结果
     */
    public static boolean isIPAddressByRegex(String str) {
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 判断ip地址是否与正则表达式匹配
        if (str.matches(regex)) {
            String[] arr = str.split("\\.");
            for (int i = 0; i < 4; i++) {
                int temp = Integer.parseInt(arr[i]);
                //如果某个数字不是0到255之间的数 就返回false
                if (temp < 0 || temp > 255) return false;
            }
            return true;
        } else return false;
    }

    /**
     * 发起代理测试
     *
     * @param proxyIP      代理服务器IP
     * @param proxyPort    代理服务器端口
     * @param targetServer 目标服务器
     * @return 访问结果
     */
    public static String beginTest(String proxyIP, String proxyPort, String targetServer) {
        StringBuilder tempStr = new StringBuilder();
        Proxy proxy = null;
        if (isIPAddressByRegex(proxyIP)) {
            try {
                int port = Integer.parseInt(proxyPort);
                if (port < 65536 && port > 0) {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, port));
                    tempStr.append("使用代理访问！！！\n");
                } else {
                    tempStr.append("未使用代理访问，代理服务器端口错误（端口取值范围1~65535）！！！\n");
                }
            } catch (Exception e) {
                tempStr.append("未使用代理访问，代理服务器端口不是错误（端口需为数字）！！！\n");
            }
        } else {
            tempStr.append("未使用代理访问，代理服务器IP地址错误！！！\n");
        }
        try {
            URL url = new URL(targetServer);
            HttpURLConnection httpURLConnection;
            if (proxy != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            //设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.flush();
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String tempLine = reader.readLine();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = reader.readLine();
            }
            reader.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            tempStr.append(e.getMessage());
        }
        return tempStr.toString();
    }

}
