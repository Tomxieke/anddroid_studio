package dataparmte.xml;
/**
 * 解析xml的工具接口
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface XmlparmateInter {
	
	/**
	 * 将xml解析成对象集合
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<Book> parmateToBook(InputStream in) throws Exception;
	
	/**
	 * 将集合对象解析成xml字符串
	 * @param books
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 */
	public String pramateToXmlStr(ArrayList<Book> books) throws IllegalArgumentException, IllegalStateException, IOException;
}
