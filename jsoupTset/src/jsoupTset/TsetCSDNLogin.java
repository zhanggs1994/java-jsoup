package jsoupTset;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class TsetCSDNLogin {
	public static void main(String[] args) throws Exception {
		TsetCSDNLogin loginDemo = new TsetCSDNLogin();
        loginDemo.login("youraccount", "yourpassword");// ����CSDN���û�����������
//        loginDemo.getTitle();
    }
    /**
     * ģ���½CSDN
     * 
     * @param userName
     *            �û���
     * @param pwd
     *            ����
     * 
     * **/
    public void login(String userName, String pwd) throws Exception {
    	 // ��һ������
        Connection con = Jsoup
                .connect("https://passport.csdn.net/account/login");// ��ȡ����
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// ����ģ�������
        Response rs = con.timeout(15000).execute();// ��ȡ��Ӧ
        Document d1 = Jsoup.parse(rs.body());// ת��ΪDom��
        List<Element> et = d1.select("#fm1");// ��ȡform��������ͨ���鿴ҳ��Դ������֪
        // ��ȡ��cooking�ͱ����ԣ�����map���postʱ������
        Map<String, String> datas = new HashMap<>();
        for (Element e : et.get(0).getAllElements()) {
            if (e.attr("name").equals("username")) {
                e.attr("value", userName);// �����û���
            }
            if (e.attr("name").equals("password")) {
                e.attr("value", pwd); // �����û�����
            }
            if (e.attr("name").length() > 0) {// �ų���ֵ������
                datas.put(e.attr("name"), e.attr("value"));
            }
        }
        System.out.println("��һ�������ȥ��cookies:--->"+rs.cookies());
        /**
         * �ڶ�������post�����ݣ��Լ�cookie��Ϣ
         * 
         * **/
        
        //����˯��ʱ�䣬�������������¿ռ�
        try{
            Thread.currentThread().sleep(5000);
         }catch(InterruptedException ie){
             ie.printStackTrace();
         }
        
 
        Connection con2 = Jsoup
                .connect("https://passport.csdn.net/account/login");
        con2.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        // ����cookie��post�����map����
        Response login = con2.ignoreContentType(true).method(Method.POST)
                .data(datas).cookies(rs.cookies()).timeout(15000).execute();
        // ��ӡ����½�ɹ������Ϣ
        //System.out.println(login.body());
        // ��½�ɹ����cookie��Ϣ�����Ա��浽���أ��Ժ��½ʱ��ֻ��һ�ε�½����
        Map<String, String> map = login.cookies();
        for (String s : map.keySet()) {
            System.out.println(s + "      " + map.get(s));
        }
        
        TsetCSDNLogin.getMessage( login.cookies());
    }
    
	/**
	 * ץȡ����
	 * @param cookies
	 *   ��½�ɹ����� �� cookies
	 * 
	 * */
	
    public static void getMessage( Map<String, String> cookies){
		Document doc = null;
		String listurl ="https://my.csdn.net/";
        try {
            Response re = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.104 Safari/537.36").cookies(cookies).execute();
            //System.out.println(re.body());
            doc = Jsoup.parse(re.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        //System.out.println("----doc-----"+doc);
        
        Elements links = doc.getElementsByClass("person-nick-name");
        System.out.println("�û�����"+links.text());
        Elements titles = doc.select(".silder-content > ul > li");

        for(Element link :titles){
       	String href = link.getElementsByTag("a").attr("href");
      	String title =  link.getElementsByTag("a").text();
       	System.out.println("----title-----�����ַΪ��"+href+"--�������ƣ�"+title);
        }
  	
    }
    
	
}
