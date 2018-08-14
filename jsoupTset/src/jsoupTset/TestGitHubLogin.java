package jsoupTset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestGitHubLogin {

	
	  public static String LOGIN_URL = "https://github.com/login";
	    public static String USER_AGENT = "User-Agent";
	    public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
	    
	    public static void main(String[] args) throws Exception {
	 
	        simulateLogin("921364401@qq.com", "zgs921364401"); // ģ���½github���û���������
	 
	    }
	 
	    /**
	     * @param userName �û���
	     * @param pwd ����
	     * @throws Exception
	     */
	    public static void simulateLogin(String userName, String pwd) throws Exception {
	 
	        /* 
	         * ��һ������ 
	         * grab login form page first
	         * ��ȡ��½�ύ�ı���Ϣ�����޸����ύdata���ݣ�login��password��
	         */
	        // get the response, which we will post to the action URL(rs.cookies())
	        Connection con = Jsoup.connect(LOGIN_URL);  // ��ȡconnection
	        con.header(USER_AGENT, USER_AGENT_VALUE);   // ����ģ�������
	        Response rs = con.execute();                // ��ȡ��Ӧ
	        Document d1 = Jsoup.parse(rs.body());       // ת��ΪDom��
	        List<Element> eleList = d1.select("form");  // ��ȡ�ύform��������ͨ���鿴ҳ��Դ������֪
	 
	        // ��ȡcooking�ͱ�����
	        // lets make data map containing all the parameters and its values found in the form
	        Map<String, String> datas = new HashMap<>();
	        for (Element e : eleList.get(0).getAllElements()) {
	            // �����û���
	            if (e.attr("name").equals("login")) {
	                e.attr("value", userName);
	            }
	            // �����û�����
	            if (e.attr("name").equals("password")) {
	                e.attr("value", pwd);
	            }
	            // �ų���ֵ������
	            if (e.attr("name").length() > 0) {
	                datas.put(e.attr("name"), e.attr("value"));
	            }
	        }
	 
	        /*
	         * �ڶ���������post��ʽ�ύ�������Լ�cookie��Ϣ
	         */
	        Connection con2 = Jsoup.connect("https://github.com/session");
	        con2.header(USER_AGENT, USER_AGENT_VALUE);
	        // ����cookie��post�����map����
	        Response login = con2.ignoreContentType(true).followRedirects(true).method(Method.POST)
	                                .data(datas).cookies(rs.cookies()).execute();
	        // ��ӡ����½�ɹ������Ϣ
	        // parse the document from response
	        System.out.println(login.body());
	 
	        // ��½�ɹ����cookie��Ϣ�����Ա��浽���أ��Ժ��½ʱ��ֻ��һ�ε�½����
	        Map<String, String> map = login.cookies();
	        for (String s : map.keySet()) {
	            System.out.println(s + " : " + map.get(s));
	        }
	    }

}
