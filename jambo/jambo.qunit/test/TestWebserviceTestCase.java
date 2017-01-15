import com.jambo.ws.test.ws.WebserviceTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: 彭宙硕
 * Date: 13-8-29
 * Time: 下午6:07
 */
public class TestWebserviceTestCase extends WebserviceTestCase {

    public void testPost(){
        super.assertEqual("http://localhost:8070/webservicedemo/services/Demo","D:\\testXml\\input\\message.xml","D:\\testXml\\output\\message.xml");
    }
}
