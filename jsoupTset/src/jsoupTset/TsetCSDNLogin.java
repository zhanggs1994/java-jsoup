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
        loginDemo.login("youraccount", "yourpassword");// 输入CSDN的用户名，和密码
//        loginDemo.getTitle();
    }
    /**
     * 模拟登陆CSDN
     * 
     * @param userName
     *            用户名
     * @param pwd
     *            密码
     * 
     * **/
    public void login(String userName, String pwd) throws Exception {
    	 // 第一次请求
        Connection con = Jsoup
                .connect("https://passport.csdn.net/account/login");// 获取连接
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
        Response rs = con.timeout(15000).execute();// 获取响应
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        List<Element> et = d1.select("#fm1");// 获取form表单，可以通过查看页面源码代码得知
        // 获取，cooking和表单属性，下面map存放post时的数据
        Map<String, String> datas = new HashMap<>();
        for (Element e : et.get(0).getAllElements()) {
            if (e.attr("name").equals("username")) {
                e.attr("value", userName);// 设置用户名
            }
            if (e.attr("name").equals("password")) {
                e.attr("value", pwd); // 设置用户密码
            }
            if (e.attr("name").length() > 0) {// 排除空值表单属性
                datas.put(e.attr("name"), e.attr("value"));
            }
        }
        System.out.println("第一次请求回去的cookies:--->"+rs.cookies());
        /**
         * 第二次请求，post表单数据，以及cookie信息
         * 
         * **/
        
        //设置睡眠时间，给俩次请求留下空间
        try{
            Thread.currentThread().sleep(5000);
         }catch(InterruptedException ie){
             ie.printStackTrace();
         }
        
 
        Connection con2 = Jsoup
                .connect("https://passport.csdn.net/account/login");
        con2.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        // 设置cookie和post上面的map数据
        Response login = con2.ignoreContentType(true).method(Method.POST)
                .data(datas).cookies(rs.cookies()).timeout(15000).execute();
        // 打印，登陆成功后的信息
        //System.out.println(login.body());
        // 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
        Map<String, String> map = login.cookies();
        for (String s : map.keySet()) {
            System.out.println(s + "      " + map.get(s));
        }
        
        TsetCSDNLogin.getMessage( login.cookies());
    }
    
	/**
	 * 抓取数据
	 * @param cookies
	 *   登陆成功返回 的 cookies
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
        System.out.println("用户名："+links.text());
        Elements titles = doc.select(".silder-content > ul > li");

        for(Element link :titles){
       	String href = link.getElementsByTag("a").attr("href");
      	String title =  link.getElementsByTag("a").text();
       	System.out.println("----title-----标题地址为："+href+"--标题名称："+title);
        }
  	
    }
    
	
}
